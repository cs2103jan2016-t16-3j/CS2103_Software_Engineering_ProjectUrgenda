package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.LogicException;
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
	// a copy of prevTask for edition and comparison of time so that time in
	// prevTask will not get modified during execution
	private Task _temp;
	// new copy of task for edition w info for edition entered by user
	private Task _newTask = new Task();
	// a timing variable for storing of time that user entered w/o starttime and
	// or endtime flag
	LocalDateTime _unknown;
	// a boolean variable to indicate removal of one timing (change of task type
	// from event to deadline or deadline to floating
	boolean isRemoveOnce = false;
	// a boolean variable to indicate removal of a second timing (only set as
	// true after isRemoveOnce is alr set as true)
	// for change of task type from event to floating
	boolean isRemoveTwice = false;

	/**
	 * Execute command of Edit which edits _prevTask to _newTask.
	 */
	public String execute() throws LogicException {
		_data = LogicData.getInstance();
		if (_id != null && _id.intValue() > -1) {
			_prevTask = _data.findMatchingPosition(_id.intValue());
			_temp = new Task(_prevTask);
		}
		if (_prevTask == null) {
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			logger.getLogger().severe("Exception(No edit match) thrown");
			throw new LogicException(MESSAGE_NO_EDIT_MATCH);
		} else {
			performEdition();
			updateEdition();
			String warning = checkValidity();
			return taskMessageWithLocation(_prevTask) + MESSAGE_EDIT + taskMessageWithLocation(_newTask) + warning;
		}
	}

	/*
	 * check for warnings, start time after end time etc and set pointer to
	 * edited task.
	 */
	private String checkValidity() throws LogicException {
		String warning;
		try {
			checkTaskValidity(_newTask);
			_data.deleteTask(_prevTask);
			_data.addTask(_newTask);
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			_data.setTaskPointer(_newTask);
			_data.clearShowMoreTasks();
			warning = checkPassed();
			warning += findOverlaps();
		} catch (LogicException e) {
			logger.getLogger().severe("Exception occured: " + e);
			_data.setCurrState(LogicData.DisplayState.INVALID_TASK);
			// throws exception to prevent Edit being added to undo stack
			throw new LogicException(MESSAGE_ERROR + e.getMessage());
		}
		return warning;
	}

	/*
	 * update fields that are non-editable through edit command by users e.g.
	 * multislot, priority, datemodified etc.
	 */
	private void updateEdition() {
		_newTask.setId(_prevTask.getId());
		_newTask.setSlot(_prevTask.getSlot());
		_newTask.setIsCompleted(_prevTask.isCompleted());
		_newTask.setIsImportant(_prevTask.isImportant());
		_newTask.setDateAdded(_prevTask.getDateAdded());
		_newTask.updateTaskType();
		updateDateModified();
	}

	/*
	 * perform edition of prev task to new task.
	 */
	private void performEdition() {
		setMultiSlots();
		removeTimings();
		_temp.updateTaskType();
		setDesc();
		setLocation();
		setNewTimings();
	}

	private void setNewTimings() {
		if (_unknown == null) {
			setDefiniteTime();
		} else {
			determineUnclearTimings();
			setDefiniteTime();
		}
	}

	// determine whether timing entered is for edition of start/end time if user
	// input it w/o start &/ end flag.
	private void determineUnclearTimings() {
		switch (_temp.getTaskType()) {
		case FLOATING: // Fallthrough
		case DEADLINE:
			// if prevtask is of type float/event (after removal of time had alr
			// been performed), set unknown as endtime.
			setUnknownAsEndTime();
			break;
		default:
			// if prevTask is of type event, compare unknown w task time
			checkStartEndAndSetUnknown();
			break;
		}
	}

	/*
	 * if user entered new timing w/o start/end time, if task to be
	 * edited(_temp) is of type event. Check if new timing entered is before
	 * _temp start time, set new timing as starttime if new timing entered is
	 * after _temp end time, set new timing as endtime. Else if is in btwn, take
	 * default as changing start, set to start time
	 */
	private void checkStartEndAndSetUnknown() {
		if (_newTask.getStartTime() == null && _newTask.getEndTime() == null) {
			if (_unknown.isBefore(_temp.getStartTime())) {
				_newTask.setStartTime(_unknown);
			} else if (_unknown.isAfter(_temp.getEndTime())) {
				_newTask.setEndTime(_unknown);
			} else {
				_newTask.setStartTime(_unknown);
			}
		} else { // when user enter 2 new time to be edited, one w flag one w/o
					// e.g.
			// 7pm to 10pm instead of from 7pm to 10pm, the unknown non-flag one
			// could be determined
			if (_newTask.getEndTime() == null) {
				_newTask.setEndTime(_unknown);
			} else {
				_newTask.setStartTime(_unknown);
			}
		}
	}

	private void setUnknownAsEndTime() {
		if (_newTask.getStartTime() == null && _newTask.getEndTime() == null) {
			_newTask.setEndTime(_unknown);
		} else {
			if (_newTask.getEndTime() == null) {
				_newTask.setEndTime(_unknown);
			} else {
				_newTask.setStartTime(_unknown);
			}
		}
	}

	// when user enter timing to be edited w flag, set to _newTask accordingly
	private void setDefiniteTime() {
		if (_newTask.getStartTime() == null) {
			_newTask.setStartTime(_temp.getStartTime());
		}
		if (_newTask.getEndTime() == null) {
			_newTask.setEndTime(_temp.getEndTime());
		}
	}

	private void setLocation() {
		if (_newTask.getLocation() == null || _newTask.getLocation().equals("") && _prevTask.getLocation() != null) {
			_newTask.setLocation(_prevTask.getLocation());
		}
	}

	private void setDesc() {
		if (_newTask.getDesc() == null || _newTask.getDesc().equals("") && _prevTask.getDesc() != null) {
			_newTask.setDesc(_prevTask.getDesc());
		}
	}

	private void setMultiSlots() {
		if (_newTask.getSlot() == null || _newTask.getSlot().isEmpty()) {
			_newTask.setSlot(null);
		}
	}

	private void removeTimings() {
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

	/**
	 * Undo method of Edit which undo the revert the task edited back to
	 * original.
	 */
	public String undo() {
		updateDateModified();
		_data.deleteTask(_newTask);
		_data.addTask(_prevTask);
		_data.setTaskPointer(_prevTask);
		return taskMessageWithLocation(_newTask) + MESSAGE_REVERTED + taskMessageWithLocation(_prevTask);
	}

	/**
	 * Redo method of Edit which redo the action and edit the task to the new
	 * version.
	 */
	public String redo() {
		updateDateModified();
		_data.deleteTask(_prevTask);
		_data.addTask(_newTask);
		_data.setTaskPointer(_newTask);
		return taskMessageWithLocation(_prevTask) + MESSAGE_EDIT + taskMessageWithLocation(_newTask) + findOverlaps();
	}

	/**
	 * Setter for the position of the task to be edited.
	 * 
	 * @param id
	 */
	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

	/**
	 * Setter for the new edited task containing info of the fields to be edited
	 * (e.g. new desc, location, timings etc).
	 * 
	 * @param newTask
	 */
	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}

	/**
	 * Setter for unknown, a LocalDateTime variable for storing of timing that
	 * user entered w/o starttime andor endtime flags.
	 * 
	 * @param unknown
	 */
	public void setUnknown(LocalDateTime unknown) {
		_unknown = unknown;
	}

	/**
	 * Setter for boolean variable isRemovedOnce which is used to indicate the
	 * removal of one timing (change of task type).
	 */
	public void setIsRemovedOnce() {
		isRemoveOnce = true;
	}

	/**
	 * Setter for boolean variable isRemovedTwice (only set after isRemovedOnce
	 * had alr been set as true) which is used to indicate the removal of two
	 * timings.
	 */
	public void setIsRemovedTwice() {
		isRemoveTwice = true;
	}
}
