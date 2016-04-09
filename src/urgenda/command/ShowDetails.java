//@@author A0080436J
package urgenda.command;

import java.util.ArrayList;
import java.util.Collections;

import urgenda.logic.LogicData;
import urgenda.util.LogicException;
import urgenda.util.Task;

/**
 * ShowDetails command is for showing more of the selected task(s) in Urgenda.
 *
 */
public class ShowDetails extends Command {

	private static final String MESSAGE_NO_SHOW_MATCH = "Invalid position(s) to showmore details";
	private static final String MESSAGE_SHOWING_MORE = "Showing more details for \"%1$s\"";
	private static final String MESSAGE_SHOWING_LESS = "Showing less details for \"%1$s\"";
	private static final String MESSAGE_MORE_TASKS = ", \"%1$s\"";
	private static final String MESSAGE_NUM = "Changed showing details for %1$s tasks";

	private ArrayList<Integer> _positions;
	private ArrayList<Task> _tasks;
	private LogicData _data;

	/**
	 * Execute method for ShowDetails which shows more details of the task(s)
	 * given.
	 */
	public String execute() throws LogicException {
		_data = LogicData.getInstance();
		// Does not change the previous state
		findMatches();
		return toggleShowDetails();
	}

	/*
	 * Toggles the showmore state of each task. Throws exception if there are no
	 * tasks.
	 */
	private String toggleShowDetails() throws LogicException {
		if (_tasks == null || _tasks.isEmpty()) {
			throw new LogicException(MESSAGE_NO_SHOW_MATCH);
		} else {
			_data.setTaskPointer(_tasks.get(0));
			return toggleTasks();
		}
	}

	/*
	 * Find matches for the the task(s) based on the positions given.
	 */
	private void findMatches() {
		if (_positions != null && _positions.size() != 0) {
			Collections.sort(_positions);
			_tasks = _data.findMatchingPosition(_positions);
		}
	}

	/*
	 * Toggles the showmore state of all task(s) given. If multiple tasks were
	 * selected, unless all of them were showing more, only tasks that are not
	 * showing more will be selected to showmore.
	 */
	private String toggleTasks() {
		if (_tasks.size() == 1) {
			_data.toggleShowMoreTasks(_tasks.get(0));
			return showMoreFeedback();
		} else if (!isAllShowMore()) {
			// remove tasks that are already showing more
			removeShowMoreTasks();
		}
		return toggleRemainingTasks();
	}

	private String toggleRemainingTasks() {
		for (Task task : _tasks) {
			_data.toggleShowMoreTasks(task);
		}
		return showMoreFeedback();
	}

	private void removeShowMoreTasks() {
		ArrayList<Task> removeTasks = new ArrayList<Task>();
		for (Task task : _tasks) {
			if (_data.isShowingMore(task)) {
				removeTasks.add(task);
			}
		}
		_tasks.removeAll(removeTasks);
	}

	private boolean isAllShowMore() {
		for (Task task : _tasks) {
			if (!_data.isShowingMore(task)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Generates the feedback for showing more based on the number of tasks.
	 */
	private String showMoreFeedback() {
		if (_tasks.size() == 1) {
			return checkShowMore(_tasks.get(0));
		} else {
			String feedback = String.format(MESSAGE_NUM, _tasks.size()) + "\n" + checkShowMore(_tasks.get(0));
			_tasks.remove(0);
			for (Task task : _tasks) {
				feedback += String.format(MESSAGE_MORE_TASKS, task.getDesc());
			}
			return feedback;
		}
	}

	private String checkShowMore(Task task) {
		if (_data.isShowingMore(task)) {
			return String.format(MESSAGE_SHOWING_MORE, task.getDesc());
		} else {
			return String.format(MESSAGE_SHOWING_LESS, task.getDesc());
		}
	}

	/**
	 * Setter method for adding positions to be selected for showmore.
	 * 
	 * @param positions
	 *            ArrayList of Integer positions representing the location of
	 *            tasks to showmore.
	 */
	public void setPosition(ArrayList<Integer> positions) {
		_positions = positions;
	}

}
