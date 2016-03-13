package urgenda.command;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class DeleteTask extends TaskCommand {
	
	private static Logger theLogger = Logger.getLogger(DeleteTask.class.getName());

	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_NO_DELETE_MATCH = "No matches found to delete";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	
	// one of these 3 properties can be filled for identification of deleted task
	private String _desc;
	private Integer _id;
	
	// to store from deletion, so that undo can be done
	private Task _deletedTask;
	private LogicData _data;
	
	public String execute(LogicData data) throws Exception {
		theLogger.warning("Can cause exception");
		_data = data;
		ArrayList<Task> matches;
		if (_desc != null) {
			matches = _data.findMatchingDesc(_desc);
			if (matches.size() == 1) {
				_deletedTask = matches.get(0);
			} else if (matches.size() > 1) {
				_data.clearDisplays();
				_data.setDisplays(matches);
				_data.setCurrState(LogicData.DisplayState.MULTIPLE_DELETE);
				throw new Exception(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
			} // else matches has no match hence _deletedTask remains null
		} else if (_id != null && _id.intValue() != -1) {
			_deletedTask = _data.findMatchingPosition(_id.intValue());
		}
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		if (_deletedTask == null) {
			theLogger.log(Level.SEVERE, "Exception occur, invalid deletion format");
			
			throw new Exception(MESSAGE_NO_DELETE_MATCH);
		}
		_data.deleteTask(_deletedTask);
		return taskMessage(_deletedTask) + MESSAGE_REMOVE;
	}

	public String undo() {
		_data.addTask(_deletedTask);
		_data.setTaskPointer(_deletedTask);
		return taskMessage(_deletedTask) + MESSAGE_ADDED;
	}

	public String redo() {
		_data.deleteTask(_deletedTask);
		return taskMessage(_deletedTask) + MESSAGE_REMOVE;
	}

	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

}
