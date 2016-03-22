package urgenda.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import urgenda.logic.LogicData;
import urgenda.util.MyLogger;
import urgenda.util.Task;

public class DeleteTask extends TaskCommand {

	private static MyLogger logger = MyLogger.getInstance();
	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_NO_DELETE_MATCH = "No matches found to delete";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	private static final String MESSAGE_INVALID_RANGE = "Invalid task range";
	private static final String MESSAGE_NUM = "%1$s tasks have been removed:\n";

	// one of these 3 properties can be filled for identification of deleted
	// task
	private String _desc;
	private Integer _id;
	private ArrayList<Integer> _multiId;

	// to store from deletion, so that undo can be done
	private Task _deletedTask;
	private LogicData _data;
	
	public DeleteTask() {
		_desc = null;
		_id = null;
		_multiId = null;
	}

	public String execute() throws Exception {
		logger.getLogger().warning("Can cause exception");

		_data = LogicData.getInstance();
		ArrayList<Task> matches;
		if (_multiId.isEmpty()) {
			if (_desc != null) {
				matches = _data.findMatchingDesc(_desc);
				if (matches.size() == 1) {
					_deletedTask = matches.get(0);
				} else if (matches.size() > 1) {
					_data.clearDisplays();
					_data.setDisplays(matches);
					_data.setCurrState(LogicData.DisplayState.MULTIPLE_DELETE);
					logger.getLogger().severe("Exception(Multiple delete) thrown");
					throw new Exception(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
				} // else matches has no match hence _deletedTask remains null
			} else if (_id != null && _id.intValue() != -1) {
				_deletedTask = _data.findMatchingPosition(_id.intValue());
			}
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			if (_deletedTask == null) {
				logger.getLogger().severe("Exception(No del match) thrown");
				throw new Exception(MESSAGE_NO_DELETE_MATCH);
			}
			_data.deleteTask(_deletedTask);
			return taskMessage(_deletedTask) + MESSAGE_REMOVE;
		} else {
			Collections.sort(_multiId);
			if (_multiId.get(0) >= 0 && _multiId.get((_multiId.size()-1)) <= _data.getDisplays().size()) {
				ArrayList<Task> _delTaskList = new ArrayList<Task>();
				StringBuilder feedback = new StringBuilder();
				Iterator<Integer> i = _multiId.iterator();
				while (i.hasNext()) {
					Task temp = _data.findMatchingPosition(i.next().intValue());
					_delTaskList.add(temp);
					feedback = feedback.append(taskMessage(temp)).append("\n");
				}
				_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				for (int j = 0; j < _delTaskList.size(); j++) {
					_data.deleteTask(_delTaskList.get(j));
				}
				return String.format(MESSAGE_NUM, _delTaskList.size()) + feedback; 
			} else {
				_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				throw new Exception(MESSAGE_INVALID_RANGE);
			}
		}
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

	public void setMultiId(ArrayList<Integer> id) {
		_multiId = id;
	}

}
