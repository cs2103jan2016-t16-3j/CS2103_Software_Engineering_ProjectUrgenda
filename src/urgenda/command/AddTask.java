package urgenda.command;

import java.time.LocalDateTime;
// import java.util.logging.Level;
// import java.util.logging.Logger;
import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.UrgendaLogger;
import urgenda.util.Task;

public class AddTask extends TaskCommand {
	
	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_ERROR = "Error: ";
	private static final String MESSAGE_OVERLAP = "\nWarning: Overlaps with ";
	private static final String MESSAGE_EVENT_PASSED = "\nWarning: Event added has already passed";
	private static final String MESSAGE_DEADLINE_PASSED = "\nWarning: Deadline added has already passed";
	
	private Task _newTask;
	private LogicData _data;
	
	public AddTask() {
		_newTask = new Task();
	}
	
	public AddTask(Task newTask) {
		_newTask = newTask;
	}
	
	
	public String execute() throws Exception {
		logger.getLogger().warning("Can cause exception");
		
		_data = LogicData.getInstance();
		LocalDateTime now = LocalDateTime.now();
		_newTask.setId(_data.getCurrentId());
		_newTask.setDateAdded(now);
		_newTask.setDateModified(now);
		_data.updateCurrentId();
		
		String feedback;
		try {
			checkTaskValidity(_newTask);
			_data.addTask(_newTask);
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			_data.setTaskPointer(_newTask);
			_data.clearShowMoreTasks();
			feedback = checkPassed();
			feedback += findOverlaps();
		} catch (Exception e) {
			logger.getLogger().severe("Exception occurred" + e);			
			_data.setCurrState(LogicData.DisplayState.INVALID_TASK);
			// throws exception to prevent AddTask being added to undo stack
			throw new Exception(MESSAGE_ERROR + e.getMessage());
		}
		return taskMessage(_newTask) + MESSAGE_ADDED + feedback;
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
		_data.deleteTask(_newTask);
		return taskMessage(_newTask) + MESSAGE_REMOVE;
	}

	public String redo() {
		_newTask.setDateModified(LocalDateTime.now());
		_data.addTask(_newTask);
		_data.setTaskPointer(_newTask);
		return taskMessage(_newTask) + MESSAGE_ADDED + findOverlaps();
	}
	
	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}

}
