package urgenda.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import urgenda.logic.LogicData;
import urgenda.util.UrgendaLogger;
import urgenda.util.Task;

public class Prioritise extends TaskCommand {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String MESSAGE_IMPORTANT = " marked as important!";
	private static final String MESSAGE_UNIMPORTANT = " unmarked as important";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	private static final String MESSAGE_NO_MATCH = "No matches found to prioritise";
	private static final String MESSAGE_NUM = "%1$s tasks have been marked as important:\n";
	private static final String MESSAGE_INVALID_RANGE = "Invalid task range";

	private String _desc;
	private Integer _id;
	private ArrayList<Integer> _multiId;

	private Task _task;
	private LogicData _data;
	

	public String execute() throws Exception {
		_data = LogicData.getInstance();
		ArrayList<Task> matches;
		if (_multiId == null || _multiId.isEmpty()) {
			if (_desc != null) {
				matches = _data.findMatchingDesc(_desc);
				if (matches.size() == 1) {
					_task = matches.get(0);
				} else if (matches.size() > 1) {
					_data.clearDisplays();
					_data.setDisplays(matches);
					_data.setCurrState(LogicData.DisplayState.MULTIPLE_PRIORITISE);
					logger.getLogger().severe("Exception(Multi-pri) thrown");
					throw new Exception(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
				} // else matches has no match hence _task remains null
			} else if (_id != null && _id.intValue() != -1) {
				_task = _data.findMatchingPosition(_id.intValue());
			}
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			if (_task == null) {
				logger.getLogger().severe("Exception(Pri no match) thrown");
				throw new Exception(MESSAGE_NO_MATCH);
			} else {
				_data.setTaskPointer(_task);
			}
			String feedback = toggleTaskImportance();
			return feedback;
		} else {
			Collections.sort(_multiId);
			if (_multiId.get(0) >= 0 && _multiId.get((_multiId.size()-1)) <= _data.getDisplays().size()) {
				ArrayList<Task> _priTaskList = new ArrayList<Task>();
				StringBuilder feedback = new StringBuilder();
				Iterator<Integer> i = _multiId.iterator();
				while (i.hasNext()) {
					Task temp = _data.findMatchingPosition(i.next().intValue());
					_priTaskList.add(temp);
					feedback = feedback.append(taskMessage(temp)).append("\n");
				}
				_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				for (int j = 0; j < _priTaskList.size(); j++) {
					_priTaskList.get(j).toggleImportant();
				}
				return String.format(MESSAGE_NUM, _priTaskList.size()) + feedback;
			} else {
				_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				throw new Exception(MESSAGE_INVALID_RANGE);
			}
		}
	}

	public String undo() {
		String feedback = toggleTaskImportance();
		_data.setTaskPointer(_task);
		return feedback;
	}

	public String redo() {
		String feedback = toggleTaskImportance();
		_data.setTaskPointer(_task);
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

	public void setMultiId(ArrayList<Integer> id) {
		_multiId = id;
	}

}
