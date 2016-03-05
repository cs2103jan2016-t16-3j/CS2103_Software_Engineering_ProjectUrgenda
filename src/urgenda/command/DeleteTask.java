package urgenda.command;

import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class DeleteTask implements Undoable {

	private static final String MESSAGE_EVENT = "\"%1$s\" on %2$d/%3$d, %4$d:%5$d - %6$d:%7$d";
	private static final String MESSAGE_FLOAT = "\"%1$s\"";
	private static final String MESSAGE_DEADLINE = "\"%1$s\" by %2$d/%3$d, %4$d:%5$d";
	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_UNDO = "Undo: ";
	private static final String MESSAGE_REDO = "Redo: ";
	private static final String MESSAGE_NO_DELETE_MATCH = "No matches found to delete";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	
	// one of these 3 properties can be filled for identification of deleted task
	private String _desc;
	private Integer _id;
	
	// to store from deletion, so that undo can be done
	private Task _deletedTask;
	private LogicData _data;
	
	@Override
	public String execute(LogicData data) throws Exception {
		_data = data;
		ArrayList<Task> matches;
		if (_id != null) {
			_deletedTask = _data.findMatchingId(_id.intValue());			
		} else if (_desc != null) {
				matches = _data.findMatchingTasks(_desc);
				if (matches.size() == 1) {
					_deletedTask = matches.get(0);
				} else if (matches.size() > 1) {
					_data.clearDisplays();
					_data.setDisplays(matches);
					_data.setCurrState(LogicData.DisplayState.MULTIPLE_DELETE);
					throw new Exception(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
				} // else matches has no match hence _deletedTask remains null
		}
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		if (_deletedTask == null) {
			throw new Exception(MESSAGE_NO_DELETE_MATCH);
		}
		_data.deleteTask(_deletedTask);
		return taskMessage() + MESSAGE_REMOVE;
	}
	
	private String taskMessage() {
		Task.Type taskType = _deletedTask.getTaskType();
		String feedback = null;
		switch (taskType) {
			case EVENT :
				feedback = String.format(MESSAGE_EVENT, _deletedTask.getDesc(), _deletedTask.getStartTime().getDayOfMonth(),
						_deletedTask.getStartTime().getMonthValue(), _deletedTask.getStartTime().getHour(), 
						_deletedTask.getStartTime().getMinute(), _deletedTask.getEndTime().getHour(), 
						_deletedTask.getEndTime().getMinute());
				break;
		
			case FLOATING :
				feedback = String.format(MESSAGE_FLOAT, _deletedTask.getDesc());
				break;
		
			case DEADLINE :
				feedback = String.format(MESSAGE_DEADLINE, _deletedTask.getDesc(), _deletedTask.getStartTime().getDayOfMonth(),
						_deletedTask.getStartTime().getMonthValue(), _deletedTask.getEndTime().getHour(), 
						_deletedTask.getEndTime().getMinute());
				break;

		}
		return feedback;
	}

	@Override
	public String undo() {
		_data.addTask(_deletedTask);
		return MESSAGE_UNDO + taskMessage() + MESSAGE_ADDED;
	}

	@Override
	public String redo() {
		_data.deleteTask(_deletedTask);
		return MESSAGE_REDO + taskMessage() + MESSAGE_REMOVE;
	}

	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

}
