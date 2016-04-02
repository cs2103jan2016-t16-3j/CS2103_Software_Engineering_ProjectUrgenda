package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

public class Edit extends TaskCommand {

	private static final String MESSAGE_NO_EDIT_MATCH = "Invalid task number. No matches found to edit";
	private static final String MESSAGE_EDIT = " has been edited to ";
	private static final String MESSAGE_REVERTED = " has been reverted to ";
	private static final String MESSAGE_ERROR = "Error: ";
	private static final String MESSAGE_OVERLAP = " Warning: Overlaps with ";
	private static final String MESSAGE_EVENT_PASSED = " Warning: Event added has already passed";
	private static final String MESSAGE_DEADLINE_PASSED = " Warning: Deadline added has already passed";

	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private LogicData _data;
	private Integer _id;
	private Task _prevTask;
	private Task _temp; // a copy of prevTask for edition and comparison of time
						// so that time in prevTask will not get modified during
						// execution
	private Task _newTask = new Task();
	LocalDateTime _unknown;
	boolean isRemoveOnce = false;
	boolean isRemoveTwice = false;

	public String execute() throws Exception {
		_data = LogicData.getInstance();
		if (_id != null && _id.intValue() > -1) {
			_prevTask = _data.findMatchingPosition(_id.intValue());
			_temp = new Task(_prevTask);
		}
		if (_prevTask == null) {
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			logger.getLogger().severe("Exception(No edit match) thrown");
			throw new Exception(MESSAGE_NO_EDIT_MATCH);
		} else {
			if (_newTask.getSlot() == null || _newTask.getSlot().isEmpty()) {
				_newTask.setSlot(null);
			}
			if (isRemoveOnce) {
				if (_temp.getStartTime() != null) {
					_temp.setStartTime(null);
				} else {
					_temp.setEndTime(null);
				}
			}
			if (isRemoveTwice) {
				_temp.setEndTime(null);
			}
			_temp.updateTaskType();
			if (_newTask.getDesc() == null || _newTask.getDesc().equals("") && _prevTask.getDesc() != null) {
				_newTask.setDesc(_prevTask.getDesc());
			}
			if (_newTask.getLocation() == null
					|| _newTask.getLocation().equals("") && _prevTask.getLocation() != null) {
				_newTask.setLocation(_prevTask.getLocation());
			}
			if (_unknown == null) {
				if (_newTask.getStartTime() == null) {
					_newTask.setStartTime(_temp.getStartTime());
				}
				if (_newTask.getEndTime() == null) {
					_newTask.setEndTime(_temp.getEndTime());
				}
			} else {
				switch (_temp.getTaskType()) {
				case FLOATING: // Fallthrough
				case DEADLINE:
					if (_newTask.getStartTime() == null && _newTask.getEndTime() == null) {
						_newTask.setEndTime(_unknown);
					} else {
						if (_newTask.getEndTime() == null) {
							_newTask.setEndTime(_unknown);
						} else {
							_newTask.setStartTime(_unknown);
						}
					}
					break;
				default:
					if (_newTask.getStartTime() == null && _newTask.getEndTime() == null) {
						if (_unknown.isBefore(_temp.getStartTime())) {
							_newTask.setStartTime(_unknown);
						} else if (_unknown.isAfter(_temp.getEndTime())) {
							_newTask.setEndTime(_unknown);
						} else {
							_newTask.setStartTime(_unknown);
						}
					} else {
						if (_newTask.getEndTime() == null) {
							_newTask.setEndTime(_unknown);
						} else {
							_newTask.setStartTime(_unknown);
						}
					}
					break;
				}
				if (_newTask.getStartTime() == null) {
					_newTask.setStartTime(_temp.getStartTime());
				}
				if (_newTask.getEndTime() == null) {
					_newTask.setEndTime(_temp.getEndTime());
				}
			}
			_newTask.setId(_prevTask.getId());
			_newTask.setSlot(_prevTask.getSlot());
			_newTask.setIsCompleted(_prevTask.isCompleted());
			_newTask.setIsImportant(_prevTask.isImportant());
			_newTask.setDateAdded(_prevTask.getDateAdded());
			_newTask.updateTaskType();
			updateDateModified();
			String feedback;
			try {
				checkTaskValidity(_newTask);
				_data.deleteTask(_prevTask);
				_data.addTask(_newTask);
				_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				_data.setTaskPointer(_newTask);
				_data.clearShowMoreTasks();
				feedback = checkPassed();
				feedback += findOverlaps();
			} catch (Exception e) {
				logger.getLogger().severe("Exception occured: " + e);
				_data.setCurrState(LogicData.DisplayState.INVALID_TASK);
				// throws exception to prevent Edit being added to undo stack
				throw new Exception(MESSAGE_ERROR + e.getMessage());
			}
			return taskMessageWithMulti(_prevTask) + MESSAGE_EDIT + taskMessageWithMulti(_newTask) + feedback;
		}
	}

	private void updateDateModified() {
		_newTask.setDateModified(LocalDateTime.now());
		_prevTask.setDateModified(LocalDateTime.now());
	}
	
	private String checkPassed() {
		if (_newTask.getTaskType() == Task.Type.EVENT) {
			if (_newTask.getEndTime().isBefore(LocalDateTime.now())) {
				return MESSAGE_EVENT_PASSED;				
			}
		} else if (_newTask.getTaskType() == Task.Type.DEADLINE) {
			if (_newTask.getEndTime().isBefore(LocalDateTime.now())) {
				return MESSAGE_DEADLINE_PASSED;
			}
		}
		return "";
	}

	private String findOverlaps() {
		ArrayList<Task> overlaps;
		overlaps = _data.overlappingTasks(_newTask);
		
		if (overlaps.size() == 0) {
			return "";
		} else {
			String feedback = MESSAGE_OVERLAP + taskMessage(overlaps.get(0));
			overlaps.remove(0);
			for (Task task : overlaps) {
				feedback += ", " + taskMessage(task);
			}
			return feedback;
		}
	}

	public String undo() {
		updateDateModified();
		_data.deleteTask(_newTask);
		_data.addTask(_prevTask);
		_data.setTaskPointer(_prevTask);
		return taskMessageWithMulti(_newTask) + MESSAGE_REVERTED + taskMessageWithMulti(_prevTask);
	}

	public String redo() {
		updateDateModified();
		_data.deleteTask(_prevTask);
		_data.addTask(_newTask);
		_data.setTaskPointer(_newTask);
		return taskMessageWithMulti(_prevTask) + MESSAGE_EDIT + taskMessageWithMulti(_newTask);
	}

	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}

	public void setUnknown(LocalDateTime unknown) {
		_unknown = unknown;
	}

	public void setIsRemovedOnce() {
		isRemoveOnce = true;
	}

	public void setIsRemovedTwice() {
		isRemoveTwice = true;
	}
}
