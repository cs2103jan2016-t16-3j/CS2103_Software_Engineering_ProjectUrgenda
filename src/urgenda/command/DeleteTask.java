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
 * DeleteTask class for the deletion of tasks in Urgenda.
 *
 */
public class DeleteTask extends TaskCommand {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_NO_DELETE_MATCH = "No matches found to delete";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	private static final String MESSAGE_NUM_REMOVED = "%1$s tasks have been removed: ";
	private static final String MESSAGE_NUM_ADDED = "%1$s tasks have been added: ";
	private static final String MESSAGE_TASK_DESC = "\"%1$s\", ";
	private static final String COMMA_DELIMITER = ",";

	private String _desc;
	private ArrayList<Integer> _positions;
	private ArrayList<Task> _deletedTasks;
	private LogicData _data;

	/**
	 * Execute method for deletion of tasks which removes the task from Urgenda.
	 * 
	 * @throws LogicException
	 *             When there are no matches found or multiple matches found.
	 */
	public String execute() throws LogicException {
		logger.getLogger().warning("Can cause exception");
		_data = LogicData.getInstance();
		findMatchingTask();
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		checkValidTask();
		updateTask();
		return deleteFeedback();
	}

	/*
	 * Updates the task(s) in Urgenda.
	 */
	private void updateTask() {
		updateDateModified(_deletedTasks);
		_data.deleteTasks(_deletedTasks);
		_data.clearShowMoreTasks();
	}

	/*
	 * Checks if the task(s) is not empty and not null.
	 */
	private void checkValidTask() throws LogicException {
		if (_deletedTasks == null || _deletedTasks.isEmpty()) {
			logger.getLogger().severe("Exception(No del match) thrown");
			throw new LogicException(MESSAGE_NO_DELETE_MATCH);
		}
	}

	/*
	 * Finds matching task based on the description or positions given.
	 */
	private void findMatchingTask() throws LogicException {
		if (_desc != null) {
			matchGivenDesc();
		} else if (_positions != null && _positions.size() != 0) {
			Collections.sort(_positions);
			_deletedTasks = _data.findMatchingPosition(_positions);
		}
	}

	/*
	 * Finds matching task based on the desciption given.
	 */
	private void matchGivenDesc() throws LogicException {
		ArrayList<Task> matches;
		matches = _data.findMatchingDesc(_desc);
		if (matches.size() == 1) {
			_deletedTasks = matches;
		} else if (matches.size() > 1) {
			setExceptionState(matches);
			throw new LogicException(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
		}
	}

	/*
	 * Sets up the exception state due to multiple matches.
	 */
	private void setExceptionState(ArrayList<Task> matches) {
		_data.clearDisplays();
		_data.setDisplays(matches);
		_data.setCurrState(LogicData.DisplayState.MULTIPLE_DELETE);
		logger.getLogger().severe("Exception(Multiple delete) thrown");
	}

	/*
	 * Generates the feedback for deletion of task(s).
	 */
	private String deleteFeedback() {
		if (_deletedTasks.size() == 1) {
			return taskMessageWithMulti(_deletedTasks.get(0)) + MESSAGE_REMOVE;
		} else {
			String feedback = String.format(MESSAGE_NUM_REMOVED, _deletedTasks.size());
			feedback += multipleTaskFeedback();
			return feedback;
		}
	}

	/*
	 * Feedback for appending multiple deletion of tasks.
	 */
	private String multipleTaskFeedback() {
		String feedback = "";
		for (Task task : _deletedTasks) {
			feedback += String.format(MESSAGE_TASK_DESC, task.getDesc());
		}
		feedback = feedback.substring(0, feedback.lastIndexOf(COMMA_DELIMITER));
		return feedback;
	}

	/**
	 * Undo method of delete, which restores the deleted task(s) to Urgenda.
	 */
	public String undo() {
		updateDateModified(_deletedTasks);
		_data.addTasks(_deletedTasks);
		_data.setTaskPointer(_deletedTasks.get(0));
		return addFeedback();
	}

	/*
	 * Feedback for re-adding of the task(s) back into Urgenda from an undo.
	 */
	private String addFeedback() {
		if (_deletedTasks.size() == 1) {
			return taskMessageWithMulti(_deletedTasks.get(0)) + MESSAGE_ADDED;
		} else {
			String feedback = String.format(MESSAGE_NUM_ADDED, _deletedTasks.size());
			feedback += multipleTaskFeedback();
			return feedback;
		}
	}

	/**
	 * Redo method for DeleteTask. Deletes the task(s) again.
	 */
	public String redo() {
		updateDateModified(_deletedTasks);
		_data.deleteTasks(_deletedTasks);
		return deleteFeedback();
	}

	private void updateDateModified(ArrayList<Task> tasks) {
		LocalDateTime now = LocalDateTime.now();
		for (Task task : tasks) {
			task.setDateModified(now);
		}
	}

	/**
	 * Setter for the input description of the task to be deleted.
	 * 
	 * @param desc
	 *            String description of the task for deletion.
	 */
	public void setDesc(String desc) {
		_desc = desc;
	}

	/**
	 * Setter for the positions of the task(s) to be deleted.
	 * 
	 * @param positions
	 *            ArrayList of Integers containing all the positions for
	 *            deletion.
	 */
	public void setPositions(ArrayList<Integer> positions) {
		_positions = positions;
	}

}
