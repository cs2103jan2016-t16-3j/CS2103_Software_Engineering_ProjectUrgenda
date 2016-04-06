//@@author A0080436J
package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import urgenda.logic.LogicData;
import urgenda.util.LogicException;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

/**
 * Prioritise command for marking a task as important in Urgenda.
 *
 */
public class Prioritise extends TaskCommand {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	private static final String MESSAGE_IMPORTANT = " marked as important";
	private static final String MESSAGE_UNIMPORTANT = " unmarked from important";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	private static final String MESSAGE_NO_MATCH = "No matches found to prioritise";
	private static final String MESSAGE_NUM = "Priority of %1$s tasks have been changed:\n";
	private static final String MESSAGE_TASK_DESC = "\"%1$s\", ";
	private static final String COMMA_DELIMITER = ",";

	private String _desc;
	private ArrayList<Integer> _positions;
	private ArrayList<Task> _tasks;
	private LogicData _data;

	/**
	 * Execute method of Prioritise for marking the task as important.
	 */
	public String execute() throws LogicException {
		_data = LogicData.getInstance();
		findMatchingTask();
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		checkValidTask();
		return togglePriority();
	}

	/*
	 * Toggles the priority of the Tasks that matches.
	 */
	private String togglePriority() {
		String feedback = toggleTasks();
		updateDateModified(_tasks);
		_data.setTaskPointer(_tasks.get(0));
		return feedback;
	}

	/*
	 * Checks if there are any matches for the tasks found based on the input.
	 */
	private void checkValidTask() throws LogicException {
		if (_tasks == null || _tasks.isEmpty()) {
			logger.getLogger().severe("Exception(Pri no match) thrown");
			throw new LogicException(MESSAGE_NO_MATCH);
		}
	}

	/*
	 * Finds all matching tasks based on the description or position.
	 */
	private void findMatchingTask() throws LogicException {
		if (_desc != null) {
			matchGivenDesc();
		} else if (_positions != null && _positions.size() != 0) {
			Collections.sort(_positions);
			_tasks = _data.findMatchingPosition(_positions);
		}
	}

	/*
	 * Finds all matching tasks based on the description.
	 */
	private void matchGivenDesc() throws LogicException {
		ArrayList<Task> matches;
		matches = _data.findMatchingDesc(_desc);
		if (matches.size() == 1) {
			_tasks = matches;
		} else if (matches.size() > 1) {
			setExceptionState(matches);
			throw new LogicException(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
		}
	}

	/*
	 * Sets up the exception state for the multiple matches found based on desc.
	 */
	private void setExceptionState(ArrayList<Task> matches) {
		_data.clearDisplays();
		_data.setDisplays(matches);
		_data.setCurrState(LogicData.DisplayState.MULTIPLE_PRIORITISE);
		logger.getLogger().severe("Exception(Multi-pri) thrown");
	}

	/*
	 * Toggles the importance of tasks. If multiple tasks were selected, unless
	 * all of them were marked as important, only unimportant ones will be
	 * selected to mark as important.
	 */
	private String toggleTasks() {
		if (_tasks.size() == 1) {
			return toggleSingleTask();
		} else if (!isAllImportant()) {
			filterImportantTasks();
		}
		return toggleMultipleTasks();
	}

	/*
	 * Toggles the importance status of multiple tasks.
	 */
	private String toggleMultipleTasks() {
		String feedback = String.format(MESSAGE_NUM, _tasks.size());
		for (Task task : _tasks) {
			feedback += String.format(MESSAGE_TASK_DESC, task.getDesc());
			task.toggleImportant();
		}
		feedback = formatFeedbackWithImportance(feedback);
		return feedback;
	}

	/*
	 * Toggles the importance status of a single task.
	 */
	private String toggleSingleTask() {
		String feedback = String.format(MESSAGE_TASK_DESC, _tasks.get(0).getDesc());
		_tasks.get(0).toggleImportant();
		return formatFeedbackWithImportance(feedback);
	}

	/*
	 * Formats the feedback based on the importance of the tasks.
	 */
	private String formatFeedbackWithImportance(String feedback) {
		feedback = feedback.substring(0, feedback.lastIndexOf(COMMA_DELIMITER));
		feedback += getTaskImportance(_tasks.get(0));
		return feedback;
	}

	/*
	 * Removes all important tasks from the list.
	 */
	private void filterImportantTasks() {
		ArrayList<Task> removeTasks = new ArrayList<Task>();
		for (Task task : _tasks) {
			if (task.isImportant()) {
				removeTasks.add(task);
			}
		}
		_tasks.removeAll(removeTasks);
	}

	/*
	 * Checks for the importance of all the tasks in _tasks.
	 */
	private boolean isAllImportant() {
		for (Task task : _tasks) {
			if (!task.isImportant()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Undo method for undoing the prioritising of tasks.
	 */
	public String undo() {
		return togglePriority();
	}

	/**
	 * Redo method for redoing the prioritising of tasks.
	 */
	public String redo() {
		return togglePriority();
	}

	/*
	 * Generates the importance feedback for the task.
	 */
	private String getTaskImportance(Task task) {
		String feedback;
		if (task.isImportant()) {
			feedback = MESSAGE_IMPORTANT;
		} else {
			feedback = MESSAGE_UNIMPORTANT;
		}
		return feedback;
	}

	private void updateDateModified(ArrayList<Task> tasks) {
		LocalDateTime now = LocalDateTime.now();
		for (Task task : tasks) {
			task.setDateModified(now);
		}
	}

	/**
	 * Setter method for setting of the task to change its priority.
	 * 
	 * @param desc
	 *            Description of task to change priority.
	 */
	public void setDesc(String desc) {
		_desc = desc;
	}

	/**
	 * Setter method for setting of indexes of tasks to change their priority.
	 * 
	 * @param positions
	 *            ArrayList of Integer representing the location of tasks to
	 *            change priority.
	 */
	public void setPositions(ArrayList<Integer> positions) {
		_positions = positions;
	}

}
