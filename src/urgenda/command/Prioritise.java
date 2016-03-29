package urgenda.command;

import java.time.LocalDateTime;
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
	private static final String MESSAGE_NUM = "Priority of %1$s tasks have been changed:\n";
	private static final String MESSAGE_TASK_DESC = "\"%1$s\", ";
	private static final String COMMA_DELIMITER = ",";

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
			updateDateModified(_tasks);
			_data.setTaskPointer(_tasks.get(0));
			return feedback;
		}
	}

	private String toggleTasks() {
		if (_tasks.size() == 1) {
			String feedback = String.format(MESSAGE_TASK_DESC, _tasks.get(0).getDesc());
			_tasks.get(0).toggleImportant();
			return formatFeedbackWithImportance(feedback);
		} else if (!isAllImportant()) {
			filterImportantTasks();
		}
		String feedback = String.format(MESSAGE_NUM, _tasks.size());
		for (Task task : _tasks) {
			feedback += String.format(MESSAGE_TASK_DESC, task.getDesc());
			task.toggleImportant();
		}
		feedback = formatFeedbackWithImportance(feedback);
		return feedback;

	}

	// pre condition the string contains the extra ", "
	private String formatFeedbackWithImportance(String feedback) {
		feedback = feedback.substring(0, feedback.lastIndexOf(COMMA_DELIMITER));
		feedback += getTaskImportance(_tasks.get(0));
		return feedback;
	}

	private void filterImportantTasks() {
		ArrayList<Task> removeTasks = new ArrayList<Task>();
		for (Task task : _tasks) {
			if (task.isImportant()) {
				removeTasks.add(task);
			}
		}
		_tasks.removeAll(removeTasks);
	}

	private boolean isAllImportant() {
		for (Task task : _tasks) {
			if (!task.isImportant()) {
				return false;
			}
		}
		return true;
	}

	public String undo() {
		String feedback = toggleTasks();
		updateDateModified(_tasks);
		_data.setTaskPointer(_tasks.get(0));
		return feedback;
	}

	public String redo() {
		String feedback = toggleTasks();
		updateDateModified(_tasks);
		_data.setTaskPointer(_tasks.get(0));
		return feedback;
	}

	public String getTaskImportance(Task task) {
		String feedback;
		if (task.isImportant()) {
			feedback = MESSAGE_IMPORTANT;
		} else {
			feedback = MESSAGE_UNIMPORTANT;
		}
		return feedback;
	}

	public void updateDateModified(ArrayList<Task> tasks) {
		LocalDateTime now = LocalDateTime.now();
		for (Task task : tasks) {
			task.setDateModified(now);
		}
	}

	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setPositions(ArrayList<Integer> positions) {
		_positions = positions;
	}

}
