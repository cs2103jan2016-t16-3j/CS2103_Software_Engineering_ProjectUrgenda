package urgenda.command;

import java.util.ArrayList;
import java.util.Collections;

import urgenda.logic.LogicData;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

public class Prioritise extends TaskCommand {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String MESSAGE_IMPORTANT = " marked as important";
	private static final String MESSAGE_UNIMPORTANT = " unmarked as important";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	private static final String MESSAGE_NO_MATCH = "No matches found to prioritise";
	private static final String MESSAGE_NUM = "Priority of %1$s tasks have been changed:";

	private String _desc;
	private ArrayList<Integer> _positions;

	private ArrayList<Task> _tasks;
	private LogicData _data;

	public String execute() throws Exception {
		_data = LogicData.getInstance();
		ArrayList<Task> matches;
		if (_desc != null) {
			matches = _data.findMatchingDesc(_desc);
			if (matches.size() == 1) {
				_tasks = matches;
			} else if (matches.size() > 1) {
				_data.clearDisplays();
				_data.setDisplays(matches);
				_data.setCurrState(LogicData.DisplayState.MULTIPLE_PRIORITISE);
				logger.getLogger().severe("Exception(Multi-pri) thrown");
				throw new Exception(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
			} // else matches has no match hence _tasks remains null
		} else if (_positions != null && _positions.size() != 0) {
			Collections.sort(_positions);
			_tasks = _data.findMatchingPosition(_positions);
		}
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		if (_tasks == null || _tasks.isEmpty()) {
			logger.getLogger().severe("Exception(Pri no match) thrown");
			throw new Exception(MESSAGE_NO_MATCH);
		} else {
			String feedback = toggleTasks();
			_data.setTaskPointer(_tasks.get(0));
			return feedback;
		}
	}

	private String toggleTasks() {
		if (_tasks.size() == 1) {
			return toggleTaskImportance(_tasks.get(0));
		} else {
			String feedback = String.format(MESSAGE_NUM, _tasks.size());
			for (Task task : _tasks) {
				feedback += "\n" + toggleTaskImportance(task);
			}
			return feedback;
		}
	}

	public String undo() {
		String feedback = toggleTasks();
		_data.setTaskPointer(_tasks.get(0));
		return feedback;
	}

	public String redo() {
		String feedback = toggleTasks();
		_data.setTaskPointer(_tasks.get(0));
		return feedback;
	}

	public String toggleTaskImportance(Task task) {
		String feedback;
		if (task.isImportant()) {
			feedback = taskMessage(task) + MESSAGE_UNIMPORTANT;
		} else {
			feedback = taskMessage(task) + MESSAGE_IMPORTANT;
		}
		task.toggleImportant();
		return feedback;
	}

	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setPositions(ArrayList<Integer> positions) {
		_positions = positions;
	}

}
