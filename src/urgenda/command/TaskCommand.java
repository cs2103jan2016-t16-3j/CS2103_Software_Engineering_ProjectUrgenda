//@@author A0080436J
package urgenda.command;

import java.time.LocalDateTime;

import urgenda.util.LogicException;
import urgenda.util.Task;

/**
 * TaskCommand class for commands dealing with tasks and are able to be
 * undone/redone. Consists of a checker method to ensure that tasks are valid.
 *
 */
public abstract class TaskCommand extends Command {

	private static final String ERROR_NO_DESC = "Task has no description";
	private static final String ERROR_MISSING_START_TIME = "Task has missing start time";
	private static final String ERROR_MISSING_END_TIME = "Task has missing end time";
	private static final String ERROR_EXTRA_START_TIME = "Task has extra start time";
	private static final String ERROR_EXTRA_END_TIME = "Task has extra end time";
	private static final String ERROR_END_BEFORE_START = "Task start time is after end time";
	private static final String ERROR_SAME_START_END = "Task has same start and end time";
	private static final String ERROR_INVALID_TASK_TYPE = "Invalid task entered";

	/**
	 * Undo abstract method for individual subcommands to implement their undo.
	 * 
	 * @return String feedback of the undo method.
	 */
	public abstract String undo();

	/**
	 * Redo abstract method for individual subcommands to implement their redo.
	 * 
	 * @return String feedback of the redo method.
	 */
	public abstract String redo();

	/**
	 * Method for checking if the given task is valid.
	 * 
	 * @param task
	 *            Task for checking.
	 * @throws LogicException
	 *             Throws exception if the task does not contain valid details.
	 */
	public void checkTaskValidity(Task task) throws LogicException {
		checkForEmptyDesc(task);
		checkTaskType(task);
	}

	/*
	 * Checks according to the task type.
	 */
	private void checkTaskType(Task task) throws LogicException {
		LocalDateTime start = task.getStartTime();
		LocalDateTime end = task.getEndTime();
		if (task.getTaskType() == Task.Type.DEADLINE) {
			checkDeadlineTime(start, end);
		} else if (task.getTaskType() == Task.Type.EVENT) {
			checkEventTime(start, end);
		} else if (task.getTaskType() == Task.Type.FLOATING) {
			checkFloatingTime(start, end);
		} else { // Invalid task type
			throw new LogicException(ERROR_INVALID_TASK_TYPE);
		}
	}

	/*
	 * Checks if details for a floating task is valid.
	 */
	private void checkFloatingTime(LocalDateTime start, LocalDateTime end) throws LogicException {
		if (start != null) {
			throw new LogicException(ERROR_EXTRA_START_TIME);
		}
		if (end != null) {
			throw new LogicException(ERROR_EXTRA_END_TIME);
		}
	}

	/*
	 * Checks if the details for an event task is valid.
	 */
	private void checkEventTime(LocalDateTime start, LocalDateTime end) throws LogicException {
		if (start == null) {
			throw new LogicException(ERROR_MISSING_START_TIME);
		} else if (end == null) {
			throw new LogicException(ERROR_MISSING_END_TIME);
		} else if (end.isBefore(start)) {
			throw new LogicException(ERROR_END_BEFORE_START);
		} else if (end.equals(start)) {
			throw new LogicException(ERROR_SAME_START_END);
		}
	}

	/*
	 * Checks if the details for a floating task is valid.
	 */
	private void checkDeadlineTime(LocalDateTime start, LocalDateTime end) throws LogicException {
		if (start != null) {
			throw new LogicException(ERROR_EXTRA_START_TIME);
		}
		if (end == null) {
			throw new LogicException(ERROR_MISSING_END_TIME);
		}
	}

	/*
	 * Checks if the description for the task is null or empty.
	 */
	private void checkForEmptyDesc(Task task) throws LogicException {
		if (task.getDesc() == null || task.getDesc().equals("")) {
			throw new LogicException(ERROR_NO_DESC);
		}
	}
}
