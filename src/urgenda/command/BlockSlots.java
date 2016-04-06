//@@author A0080436J
package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.DateTimePair;
import urgenda.util.LogicException;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;

/**
 * BlockSlots is the command used for adding tasks with multiple slots.
 *
 */
public class BlockSlots extends TaskCommand {

	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_BLOCK = "Blocked ";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_ERROR = "Error: ";
	private static final String MESSAGE_INVALID_TYPE = "Please input an EVENT for blocking of timeslots";
	private static final String MESSAGE_INSUFFICIENT_SLOTS = "Insufficient slots entered for blocking of timeslots";
	private static final String MESSAGE_INVALID_SLOTS = "Invalid slots entered for blocking of timeslots";

	private MultipleSlot _block;
	private LogicData _data;
	private Task _newTask;

	/**
	 * Default constructor for creating a new BlockSlot command.
	 */
	public BlockSlots() {

	}

	/**
	 * Constructor for adding of a newTask with multiple slots.
	 * 
	 * @param newTask
	 *            new Task object consisting of all details.
	 */
	public BlockSlots(Task newTask) {
		_newTask = newTask;
	}

	/**
	 * Execute method for blocking of slots which adds new Task to Urgenda.
	 */
	public String execute() throws LogicException {
		_data = LogicData.getInstance();
		updateSlot();
		setTaskDetails();
		addBlockTask();
		return MESSAGE_BLOCK + taskMessageWithMulti(_newTask) + MESSAGE_ADDED;
	}

	/*
	 * Adds the block task to Urgenda and sets necessary settings.
	 */
	private void addBlockTask() {
		// TODO exception handling for user manipulation of blocking past tasks
		_data.addTask(_newTask);
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		_data.setTaskPointer(_newTask);
		_data.clearShowMoreTasks();
		_data.toggleShowMoreTasks(_newTask);
	}

	/*
	 * Sets all the details for the new Task. Checks if task is valid type.
	 */
	private void setTaskDetails() throws LogicException {
		checkTaskType();
		_newTask.setId(_data.getCurrentId());
		_data.updateCurrentId();
		LocalDateTime now = LocalDateTime.now();
		_newTask.setDateAdded(now);
		_newTask.setDateModified(now);
		_data.updateMultipleSlot(_newTask);
	}

	/*
	 * Checks if the task for blocking is an event type task. Throws
	 * LogicException if invalid.
	 */
	private void checkTaskType() throws LogicException {
		if (_newTask.getTaskType() != Task.Type.EVENT) {
			throw new LogicException(MESSAGE_ERROR + MESSAGE_INVALID_TYPE);
		}
	}

	/*
	 * Updates the slot given from parser and sorts the slots accordingly.
	 * Throws LogicException if insufficient or invalid slots.
	 */
	private void updateSlot() throws LogicException {
		_block = _newTask.getSlot();
		checkValidBlock();
		_block.sortSlots();
		DateTimePair time = _block.getNextSlot();
		_block.removeNextSlot();
		_newTask.setStartTime(time.getDateTime1());
		_newTask.setEndTime(time.getDateTime2());
		_newTask.updateTaskType();
		checkEmptyBlock();
	}

	/*
	 * Checks for valid blocks given in the task. Throws exception if invalid to
	 * prevent the adding of invalid task.
	 */
	private void checkValidBlock() throws LogicException {
		if (_block == null || _block.isEmpty()) {
			throw new LogicException(MESSAGE_ERROR + MESSAGE_INSUFFICIENT_SLOTS);
		} else if (!isValidBlock(_block)) {
			throw new LogicException(MESSAGE_ERROR + MESSAGE_INVALID_SLOTS);
		}
	}

	/*
	 * Returns if all the blocks within are valid blocks.
	 */
	private boolean isValidBlock(MultipleSlot block) {
		ArrayList<DateTimePair> slots = block.getSlots();
		if (slots == null) {
			return false;
		} else if (slots.isEmpty()) {
			return false;
		} else {
			return hasValidSlots(slots);
		}
	}

	/*
	 * Goes through all slots within. Defensive check to ensure that no blank
	 * slots are included.
	 */
	private boolean hasValidSlots(ArrayList<DateTimePair> slots) {
		for (DateTimePair pair : slots) {
			if (pair.getDateTime1() == null || pair.getDateTime2() == null) {
				return false;
			} else if (pair.getDateTime2().isBefore(pair.getDateTime1())
					|| pair.getDateTime1().equals(pair.getDateTime2())) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Checks if the block is empty. If empty exception is thrown to prevent
	 * code from adding.
	 */
	private void checkEmptyBlock() throws LogicException {
		if (_block.isEmpty()) {
			throw new LogicException(MESSAGE_ERROR + MESSAGE_INSUFFICIENT_SLOTS);
		}
	}

	/**
	 * Setter for the new Block Task to be added.
	 * 
	 * @param newTask
	 *            New Task that is filled with multiple slots.
	 */
	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}

	/**
	 * Undo function for BlockSlots which removes the added task.
	 */
	public String undo() {
		_data.deleteTask(_newTask);
		return taskMessageWithMulti(_newTask) + MESSAGE_REMOVE;
	}

	/**
	 * Redo function for BlockSlots which adds back the new task.
	 */
	public String redo() {
		_newTask.setDateModified(LocalDateTime.now());
		_data.addTask(_newTask);
		_data.setTaskPointer(_newTask);
		return MESSAGE_BLOCK + taskMessageWithMulti(_newTask) + MESSAGE_ADDED;
	}

}
