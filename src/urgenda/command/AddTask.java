//@@author A0080436J
package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.LogicException;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

/**
 * AddTask is the command object used for adding of tasks to Urgenda.
 *
 */
public class AddTask extends TaskCommand {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_ERROR = "Error: ";
	private static final String MESSAGE_OVERLAP = "\nWarning: Overlaps with ";
	private static final String MESSAGE_EVENT_PASSED = "\nWarning: Event added has already passed";
	private static final String MESSAGE_DEADLINE_PASSED = "\nWarning: Deadline added has already passed";

	private Task _newTask;
	private LogicData _data;

	/**
	 * Default constructor for creating a new AddTask command object.
	 */
	public AddTask() {
		_newTask = new Task();
	}

	/**
	 * Alternative constructor for AddTask command object with the new task to
	 * be added.
	 * 
	 * @param newTask
	 *            New Task object that is to be added.
	 */
	public AddTask(Task newTask) {
		_newTask = newTask;
	}

	/**
	 * Execute command of AddTask which adds newTask to Urgenda.
	 * 
	 * @throws LogicException
	 *             When the task added is invalid.
	 */
	public String execute() throws LogicException {
		logger.getLogger().warning("Can cause exception");
		_data = LogicData.getInstance();
		updateNewTask();
		return addNewTask();
	}

	/*
	 * Adds newTask to LogicData of Urgenda. Checks if the task is valid.
	 */
	private String addNewTask() throws LogicException {
		String feedback;
		try {
			checkTaskValidity(_newTask);
			_data.addTask(_newTask);
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			_data.setTaskPointer(_newTask);
			_data.clearShowMoreTasks();
			feedback = checkPassed();
			feedback += findOverlaps();
		} catch (LogicException e) {
			logger.getLogger().severe("Exception occurred" + e);
			_data.setCurrState(LogicData.DisplayState.INVALID_TASK);
			// throws exception to prevent AddTask being added to undo stack
			throw new LogicException(MESSAGE_ERROR + e.getMessage());
		}
		return taskMessageWithMulti(_newTask) + MESSAGE_ADDED + feedback;
	}

	/*
	 * Updates all the attributes of newTask according to current time and new
	 * task id.
	 */
	private void updateNewTask() {
		LocalDateTime now = LocalDateTime.now();
		_newTask.setId(_data.getCurrentId());
		_newTask.setSlot(null);
		_newTask.setDateAdded(now);
		_newTask.setDateModified(now);
		_data.updateCurrentId();
	}

	/*
	 * Checks if newTask has already passed based on current time.
	 */
	private String checkPassed() {
		if (_newTask.getTaskType() == Task.Type.EVENT) {
			if (_newTask.getEndTime().isBefore(LocalDateTime.now())) {
				return MESSAGE_EVENT_PASSED;
			}
		} else if (_newTask.getTaskType() == Task.Type.DEADLINE) {
			if (_newTask.getEndTime().isBefore(LocalDateTime.now())) {
				return MESSAGE_DEADLINE_PASSED;
			}
		}
		return "";
	}

	/*
	 * Finds all overlapping task with newTask and returns task message of
	 * overlaps.
	 */
	private String findOverlaps() {
		ArrayList<Task> overlaps;
		overlaps = _data.overlappingTasks(_newTask);
		if (overlaps.size() == 0) {
			return "";
		} else {
			return generateOverlapMessage(overlaps);
		}
	}

	/*
	 * Generates all the message of tasks that overlap with the newTask.
	 */
	private String generateOverlapMessage(ArrayList<Task> overlaps) {
		String feedback = MESSAGE_OVERLAP + taskMessage(overlaps.get(0));
		overlaps.remove(0);
		for (Task task : overlaps) {
			feedback += ", " + taskMessage(task);
		}
		return feedback;
	}

	/**
	 * Undo method of AddTask which undos the adding of the task into task list.
	 */
	public String undo() {
		_data.deleteTask(_newTask);
		return taskMessageWithMulti(_newTask) + MESSAGE_REMOVE;
	}

	/**
	 * Redo method of AddTask which redo the action and adds the task back.
	 */
	public String redo() {
		_newTask.setDateModified(LocalDateTime.now());
		_data.addTask(_newTask);
		_data.setTaskPointer(_newTask);
		return taskMessageWithMulti(_newTask) + MESSAGE_ADDED + findOverlaps();
	}

	/**
	 * Setter for the newTask.
	 * 
	 * @param newTask
	 *            New Task that is to be added to Urgenda.
	 */
	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}

}
