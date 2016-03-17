package urgenda.command;

import java.time.LocalDateTime;

import urgenda.util.Task;

/**
 * TaskCommand class for commands dealing with tasks and are able to be undone/redone
 *
 */
public abstract class TaskCommand extends Command{
	
	private static final String ERROR_NO_DESC = "Task has no description";
	private static final String ERROR_MISSING_START_TIME = "Task has missing start time";
	private static final String ERROR_MISSING_END_TIME = "Task has missing end time";
	private static final String ERROR_EXTRA_START_TIME = "Task has extra start time";
	private static final String ERROR_EXTRA_END_TIME = "Task has extra end time";
	private static final String ERROR_END_BEFORE_START = "Task start time is after end time";
	private static final String ERROR_SAME_START_END = "Task has same start and end time";
	private static final String ERROR_INVALID_TASK_TYPE = "Invalid task entered";

	// abstract functions for implementation by subcommands
	public abstract String undo();
	public abstract String redo();
	
	// function to check validity of task object being passed, throws exception if task is invalid
	public void checkTaskValidity(Task task) throws Exception {
		if (task.getDesc() == null || task.getDesc().equals("")) {
			throw new Exception(ERROR_NO_DESC);
		}
		LocalDateTime start = task.getStartTime();
		LocalDateTime end = task.getEndTime();
		if (task.getTaskType() == Task.Type.DEADLINE) {
			if (start != null) {
				throw new Exception(ERROR_EXTRA_START_TIME);
			}
			if (end == null) {
				throw new Exception(ERROR_MISSING_END_TIME);
			}
		} else if (task.getTaskType() == Task.Type.EVENT) {
			if (start == null) {
				throw new Exception(ERROR_MISSING_START_TIME);
			} else if (end == null) {
				throw new Exception(ERROR_MISSING_END_TIME);
			} else if (end.isBefore(start)) {
				throw new Exception(ERROR_END_BEFORE_START);
			} else if (end.equals(start)) {
				throw new Exception(ERROR_SAME_START_END);
			}
		} else if (task.getTaskType() == Task.Type.FLOATING){
			if (start != null) {
				throw new Exception(ERROR_EXTRA_START_TIME);
			}
			if (end != null) {
				throw new Exception(ERROR_EXTRA_END_TIME);
			}
		} else { // Invalid task type
			throw new Exception(ERROR_INVALID_TASK_TYPE);
		}
	}
}
