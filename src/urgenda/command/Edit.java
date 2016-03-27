package urgenda.command;

import java.time.LocalDateTime;

import urgenda.logic.LogicData;
import urgenda.util.UrgendaLogger;
import urgenda.util.Task;

public class Edit extends TaskCommand {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String MESSAGE_NO_EDIT_MATCH = "Invalid task number. No matches found to edit";
	private static final String MESSAGE_EDIT = " has been edited to ";
	private static final String MESSAGE_REVERTED = " has been reverted to ";
	private static final String MESSAGE_ERROR = "Error: ";

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

	
	public String execute() throws Exception {
		_data = LogicData.getInstance();
		if (_id != null && _id.intValue() != -1) {
			_prevTask = _data.findMatchingPosition(_id.intValue());
		}
		if (_prevTask == null) {
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			logger.getLogger().severe("Exception(No edit match) thrown");
			throw new Exception(MESSAGE_NO_EDIT_MATCH);
		} else {
			if (_newTask.getDesc() == null && _prevTask.getDesc() != null || _newTask.getDesc().equals("") && _prevTask.getDesc() != null) {
				_newTask.setDesc(_prevTask.getDesc());
			}
			if (_newTask.getLocation() == null && _prevTask.getLocation() != null) {
				_newTask.setLocation(_prevTask.getLocation());
			}
			if (_newTask.getStartTime() == null && _prevTask.getStartTime() != null) {
				_newTask.setStartTime(_prevTask.getStartTime());
			}
			if (_newTask.getEndTime() == null && _prevTask.getEndTime() != null) {
				_newTask.setEndTime(_prevTask.getEndTime());
			}
			_newTask.setId(_prevTask.getId());
			_newTask.setIsCompleted(_prevTask.isCompleted());
			_newTask.setIsImportant(_prevTask.isImportant());
			_newTask.setDateAdded(_prevTask.getDateAdded());
			_newTask.updateTaskType(_newTask.getStartTime(), _newTask.getEndTime());
			updateDateModified();
			try {
				checkTaskValidity(_newTask);
				_data.deleteTask(_prevTask);
				_data.addTask(_newTask);
				_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				_data.setTaskPointer(_newTask);
				_data.clearShowMoreTasks();
			} catch (Exception e) {
				logger.getLogger().severe("Exception occured: " + e);
				_data.setCurrState(LogicData.DisplayState.INVALID_TASK);
				// throws exception to prevent Edit being added to undo stack
				throw new Exception(MESSAGE_ERROR + e.getMessage());
			}
			return taskMessage(_prevTask) + MESSAGE_EDIT + taskMessage(_newTask) ;
		}
	}

	private void updateDateModified() {
		_newTask.setDateModified(LocalDateTime.now());
		_prevTask.setDateModified(LocalDateTime.now());
	}

	public String undo() {
		updateDateModified();
		_data.deleteTask(_newTask);
		_data.addTask(_prevTask);
		_data.setTaskPointer(_prevTask);
		return taskMessage(_newTask) + MESSAGE_REVERTED  + taskMessage(_prevTask) ;
	}

	public String redo() {
		updateDateModified();
		_data.deleteTask(_prevTask);
		_data.addTask(_newTask);
		_data.setTaskPointer(_newTask);
		return taskMessage(_prevTask) + MESSAGE_EDIT + taskMessage(_newTask);
	}

	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}

}
