package urgenda.command;

import java.time.LocalDateTime;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class Edit implements Undoable {

	private static final String MESSAGE_NO_EDIT_MATCH = "Invalid task number. No matches found to edit";
	private static final String MESSAGE_EDIT = " has been edited to ";
	private static final String MESSAGE_EVENT = "\"%1$s\" on %2$d/%3$d, %4$02d:%5$02d - %6$02d:%7$02d";
	private static final String MESSAGE_DEADLINE = "\"%1$s\" by %2$d/%3$d, %4$02d:%5$02d";
	private static final String MESSAGE_FLOAT = "\"%1$s\"";
	private static final String MESSAGE_UNDO = "Undo: ";
	private static final String MESSAGE_REDO = "Redo: ";
	private static final String MESSAGE_ERROR = "Error: ";
	
	private static final String ERROR_NO_DESC = "Task has no description";
	private static final String ERROR_MISSING_START_TIME = "Task has missing start time";
	private static final String ERROR_MISSING_END_TIME = "Task has missing end time";
	private static final String ERROR_EXTRA_START_TIME = "Task has extra start time";
	private static final String ERROR_EXTRA_END_TIME = "Task has extra end time";
	private static final String ERROR_END_BEFORE_START = "Task start time is after end time";
	private static final String ERROR_SAME_START_END = "Task has same start and end time";

	private Task _newTask;
	private Task _prevTask;
	private Integer _id;
	private LogicData _data;

	// default constructor
	public Edit() {

	}

	public Edit(Integer id, Task newtask) {
		_id = id;
		_newTask = newtask;
	}

	@Override
	public String execute(LogicData data) throws Exception {
		_data = data;
		if (_id != null && _id.intValue() != -1) {
			_prevTask = _data.findMatchingPosition(_id.intValue());
		}
		if (_prevTask == null) {
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			throw new Exception(MESSAGE_NO_EDIT_MATCH);
		} else {
			if(_newTask.getDesc() == null && _prevTask.getDesc() != null || _newTask.getDesc().equals("") && _prevTask.getDesc() != null) {
				_newTask.setDesc(_prevTask.getDesc());
			}
			if(_newTask.getLocation() == null && _prevTask.getLocation() != null) {
				_newTask.setLocation(_prevTask.getLocation());
			}
			if(_newTask.getStartTime() == null && _prevTask.getStartTime() != null) {
				_newTask.setStartTime(_prevTask.getStartTime());
			}
			if(_newTask.getEndTime() == null && _prevTask.getEndTime() != null) {
				_newTask.setEndTime(_prevTask.getEndTime());
			}
			_newTask.setId(_prevTask.getId());
			_newTask.setIsCompleted(_prevTask.isCompleted());
			_newTask.setIsImportant(_prevTask.isImportant());
			_newTask.setDateAdded(_prevTask.getDateAdded());
			_newTask.setDateModified(LocalDateTime.now()); // TODO refactor out to update undo and redo
			_prevTask.setDateModified(LocalDateTime.now());
			_newTask.updateTaskType(_newTask.getStartTime(), _newTask.getEndTime());
			try {
				checkTaskValidity(_newTask);
				_data.deleteTask(_prevTask);
				_data.addTask(_newTask);
				_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			} catch (Exception e) {
				_data.setCurrState(LogicData.DisplayState.INVALID_TASK);
				// throws exception to prevent Edit being added to undo stack
				throw new Exception(MESSAGE_ERROR + e.getMessage());
			}
			return oldTaskMessage() + MESSAGE_EDIT + newTaskMessage();
		}
	}
	
	public void checkTaskValidity(Task task) throws Exception {
		if (task.getDesc() == null || task.getDesc().equals("")) {
			throw new Exception(ERROR_NO_DESC);
		}
		LocalDateTime start = task.getStartTime();
		LocalDateTime end = task.getEndTime();
		if (task.getTaskType() == Task.Type.DEADLINE) {
			if (start != null) {
				throw new Exception(ERROR_EXTRA_START_TIME);
			}
			if (end == null) {
				throw new Exception(ERROR_MISSING_END_TIME);
			}
		} else if (task.getTaskType() == Task.Type.EVENT) {
			if (start == null) {
				throw new Exception(ERROR_MISSING_START_TIME);
			} else if (end == null) {
				throw new Exception(ERROR_MISSING_END_TIME);
			} else if (end.isBefore(start)) {
				throw new Exception(ERROR_END_BEFORE_START);
			} else if (end.equals(start)) {
				throw new Exception(ERROR_SAME_START_END);
			}
		} else { // floating type
			if (start != null) {
				throw new Exception(ERROR_EXTRA_START_TIME);
			}
			if (end != null) {
				throw new Exception(ERROR_EXTRA_END_TIME);
			}
		}
		
	}

	private String oldTaskMessage() {
		Task.Type taskType = _prevTask.getTaskType();
		String feedback = null;
		switch (taskType) {
		case EVENT:
			feedback = String.format(MESSAGE_EVENT, _prevTask.getDesc(), _prevTask.getStartTime().getDayOfMonth(),
					_prevTask.getStartTime().getMonthValue(), _prevTask.getStartTime().getHour(),
					_prevTask.getStartTime().getMinute(), _prevTask.getEndTime().getHour(),
					_prevTask.getEndTime().getMinute());
			break;

		case DEADLINE:
			feedback = String.format(MESSAGE_DEADLINE, _prevTask.getDesc(), _prevTask.getEndTime().getDayOfMonth(),
					_prevTask.getEndTime().getMonthValue(), _prevTask.getEndTime().getHour(),
					_prevTask.getEndTime().getMinute());
			break;

		case FLOATING:
			feedback = String.format(MESSAGE_FLOAT, _prevTask.getDesc());
			break;
		}
		return feedback;
	}
	
	private String newTaskMessage() {
		Task.Type taskType = _newTask.getTaskType();
		String feedback = null;
		switch (taskType) {
		case EVENT:
			feedback = String.format(MESSAGE_EVENT, _newTask.getDesc(), _newTask.getStartTime().getDayOfMonth(),
					_newTask.getStartTime().getMonthValue(), _newTask.getStartTime().getHour(),
					_newTask.getStartTime().getMinute(), _newTask.getEndTime().getHour(),
					_newTask.getEndTime().getMinute());
			break;

		case DEADLINE:
			feedback = String.format(MESSAGE_DEADLINE, _newTask.getDesc(), _newTask.getEndTime().getDayOfMonth(),
					_newTask.getEndTime().getMonthValue(), _newTask.getEndTime().getHour(),
					_newTask.getEndTime().getMinute());
			break;

		case FLOATING:
			feedback = String.format(MESSAGE_FLOAT, _newTask.getDesc());
			break;
		}
		return feedback;
	}

	@Override
	public String undo() {
		_data.deleteTask(_newTask);
		_data.addTask(_prevTask);
		return MESSAGE_UNDO + oldTaskMessage() + MESSAGE_EDIT + newTaskMessage();
	}

	@Override
	public String redo() {
		_data.deleteTask(_prevTask);
		_data.addTask(_newTask);
		return MESSAGE_REDO + oldTaskMessage() + MESSAGE_EDIT + newTaskMessage();
	}

	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}

}
