package urgenda.command;

import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class Prioritise extends TaskCommand {
	
	private static final String MESSAGE_IMPORTANT = " marked as important!";
	private static final String MESSAGE_UNIMPORTANT = " unmarked as important";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	private static final String MESSAGE_NO_MATCH = "No matches found to prioritise";
	
	private String _desc;
	private Integer _id;
	
	private Task _task;
	private LogicData _data;
	
	public String execute(LogicData data) throws Exception {
		_data = data;
		ArrayList<Task> matches;
		if (_desc != null) {
			matches = _data.findMatchingDesc(_desc);
			if (matches.size() == 1) {
				_task = matches.get(0);
			} else if (matches.size() > 1) {
				_data.clearDisplays();
				_data.setDisplays(matches);
				_data.setCurrState(LogicData.DisplayState.MULTIPLE_PRIORITISE);
				throw new Exception(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
			} // else matches has no match hence _task remains null
		} else if (_id != null && _id.intValue() != -1) {
			_task = _data.findMatchingPosition(_id.intValue());
		}
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		if (_task == null) {
			throw new Exception(MESSAGE_NO_MATCH);
		}
		String feedback = toggleTaskImportance();
		return feedback;
	}
	
	public String undo() {
		String feedback = toggleTaskImportance();
		return feedback;
	}

	public String redo() {
		String feedback = toggleTaskImportance();
		return feedback;
	}

	public String toggleTaskImportance() {
		String feedback;
		if (_task.isImportant()) {
			feedback = taskMessage(_task) + MESSAGE_UNIMPORTANT;
		} else {
			feedback = taskMessage(_task) + MESSAGE_IMPORTANT;
		}
		_task.toggleImportant();
		return feedback;
	}
	
	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setId(Integer id) {
		_id = Integer.valueOf(id);
	}


}
