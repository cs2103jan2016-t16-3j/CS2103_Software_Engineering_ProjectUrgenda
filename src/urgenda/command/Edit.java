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
			_data.deleteTask(_prevTask);
			_data.addTask(_newTask);
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			return oldTaskMessage() + MESSAGE_EDIT + newTaskMessage();
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
