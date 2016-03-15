package urgenda.command;

import java.time.LocalDateTime;
// import java.util.logging.Level;
// import java.util.logging.Logger;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class AddTask extends TaskCommand {
	
	// private static Logger theLogger = Logger.getLogger(AddTask.class.getName());
	
	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_ERROR = "Error: ";
	
	private Task _newTask;
	private LogicData _data;
	
	public AddTask() {
		_newTask = new Task();
	}
	
	public AddTask(Task newTask) {
		_newTask = newTask;
	}
	
	public String execute(LogicData dataStorage) throws Exception {
		// theLogger.warning("Can cause exception");
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
			_data.setTaskPointer(_newTask);
		} catch (Exception e) {
			// theLogger.log(Level.SEVERE, "Exception occur", e);
			
			_data.setCurrState(LogicData.DisplayState.INVALID_TASK);
			// throws exception to prevent AddTask being added to undo stack
			throw new Exception(MESSAGE_ERROR + e.getMessage());
		}
		return taskMessage(_newTask) + MESSAGE_ADDED;
	}

	public String undo() {
		_data.deleteTask(_newTask);
		return taskMessage(_newTask) + MESSAGE_REMOVE;
	}

	public String redo() {
		_data.addTask(_newTask);
		_data.setTaskPointer(_newTask);
		return taskMessage(_newTask) + MESSAGE_ADDED;
	}
	
	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}

}
