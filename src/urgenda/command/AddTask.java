package urgenda.command;

import java.time.LocalDateTime;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class AddTask extends TaskCommand {
	
	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_ERROR = "Error: ";
	
	private static final String ERROR_NO_DESC = "Task has no description";
	private static final String ERROR_MISSING_START_TIME = "Task has missing start time";
	private static final String ERROR_MISSING_END_TIME = "Task has missing end time";
	private static final String ERROR_EXTRA_START_TIME = "Task has extra start time";
	private static final String ERROR_EXTRA_END_TIME = "Task has extra end time";
	private static final String ERROR_END_BEFORE_START = "Task start time is after end time";
	private static final String ERROR_SAME_START_END = "Task has same start and end time";
	
	private Task _newTask;
	private LogicData _data;
	
	public AddTask() {
		_newTask = new Task();
	}
	
	public AddTask(Task newTask) {
		_newTask = newTask;
	}
	
	public String execute(LogicData dataStorage) throws Exception {
		_data = dataStorage;
		LocalDateTime now = LocalDateTime.now();
		_newTask.setId(_data.getCurrentId());
		_newTask.setDateAdded(now);
		_newTask.setDateModified(now);
		_data.updateCurrentId();
		try {
			checkTaskValidity(_newTask);
			_data.addTask(_newTask);
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		} catch (Exception e) {
			_data.setCurrState(LogicData.DisplayState.INVALID_TASK);
			// throws exception to prevent AddTask being added to undo stack
			throw new Exception(MESSAGE_ERROR + e.getMessage());
		}
		return taskMessage(_newTask) + MESSAGE_ADDED;
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

	public String undo() {
		_data.deleteTask(_newTask);
		return taskMessage(_newTask) + MESSAGE_REMOVE;
	}

	public String redo() {
		_data.addTask(_newTask);
		return taskMessage(_newTask) + MESSAGE_ADDED;
	}
	
	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}

}
