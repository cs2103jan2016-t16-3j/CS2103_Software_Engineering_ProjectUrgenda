//@@author A0080436J
package urgenda.command;

import java.time.LocalDateTime;

import urgenda.logic.LogicData;
import urgenda.util.DateTimePair;
import urgenda.util.LogicException;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;

/**
 * Confirm class for confirming of a blocked slot previously blocked in Urgenda.
 *
 */
public class Confirm extends TaskCommand {

	private static final String MESSAGE_NO_POSITION = "Invalid position to confirm";
	private static final String MESSAGE_INVALID_TIME = "Invalid confirm time";
	private static final String MESSAGE_NO_MULTIPLE = "Task does not have multiple slots";
	private static final String MESSAGE_NO_MATCH = "No matches found for %1$d/%2$d, %3$02d:%4$02d - "
			+ "%5$d/%6$d, %7$02d:%8$02d";
	private static final String MESSAGE_CONFIRM = "Confirmed ";
	private static final String MESSAGE_BLOCK = "Blocked ";

	private Integer _id;
	private DateTimePair _confirmed;
	private LogicData _data;
	private Task _prevTask;
	private Task _confirmedTask;

	/**
	 * Execute method for confirm. Confirms the blocked slot if valid.
	 * 
	 * @throws LogicException
	 *             When there is no match for the confirmed slot or invalid task
	 *             to confirm.
	 */
	public String execute() throws LogicException {
		_data = LogicData.getInstance();
		checkValidTime();
		findMatchingTask();
		checkValidTask();
		return updateConfirmedTask();
	}

	/*
	 * Updates the task with the confirmed slot.
	 */
	private String updateConfirmedTask() throws LogicException {
		_confirmedTask = new Task(_prevTask);
		DateTimePair currPair = new DateTimePair(_confirmedTask.getStartTime(), _confirmedTask.getEndTime());
		if (currPair.equals(_confirmed)) {
			updateTask();
			return MESSAGE_CONFIRM + taskMessageWithMulti(_confirmedTask);
		} else {
			return checkSlotsForMatch();
		}
	}

	/*
	 * Updates the task in Urgenda to be replaced with the new confirmed task.
	 */
	private void updateTask() {
		_confirmedTask.setSlot(null);
		_data.deleteTask(_prevTask);
		_data.addTask(_confirmedTask);
		_data.setTaskPointer(_confirmedTask);
		_data.clearShowMoreTasks();
	}

	/*
	 * Iterates through all possible slots within the task to check if the slot
	 * is matching given time slot.
	 */
	private String checkSlotsForMatch() throws LogicException {
		DateTimePair currPair;
		MultipleSlot tempSlot = new MultipleSlot(_confirmedTask.getSlot());
		while (tempSlot != null && !(tempSlot.isEmpty())) {
			currPair = tempSlot.getNextSlot();
			tempSlot.removeNextSlot();
			if (currPair.equals(_confirmed)) {
				updateConfirmedSlot(currPair);
				return MESSAGE_CONFIRM + taskMessageWithMulti(_confirmedTask);
			}
		}
		_data.setTaskPointer(_prevTask);
		throw new LogicException(String.format(MESSAGE_NO_MATCH, _confirmed.getEarlierDateTime().getDayOfMonth(),
				_confirmed.getEarlierDateTime().getMonthValue(), _confirmed.getEarlierDateTime().getHour(),
				_confirmed.getEarlierDateTime().getMinute(), _confirmed.getLaterDateTime().getDayOfMonth(),
				_confirmed.getLaterDateTime().getMonthValue(), _confirmed.getLaterDateTime().getHour(),
				_confirmed.getLaterDateTime().getMinute()));
	}

	/*
	 * Updates the confirmed slot details with the new details.
	 */
	private void updateConfirmedSlot(DateTimePair currPair) {
		_confirmedTask.setStartTime(currPair.getEarlierDateTime());
		_confirmedTask.setEndTime(currPair.getLaterDateTime());
		updateDateModified();
		updateTask();
	}

	/*
	 * Checks if the task is a valid task based on the input given.
	 */
	private void checkValidTask() throws LogicException {
		if (_prevTask == null) {
			throw new LogicException(MESSAGE_NO_POSITION);
		} else if (_prevTask.getSlot() == null) {
			_data.setTaskPointer(_prevTask);
			throw new LogicException(MESSAGE_NO_MULTIPLE);
		}
	}

	/*
	 * Sets the task to match the position given.
	 */
	private void findMatchingTask() throws LogicException {
		if (_id == null) {
			throw new LogicException(MESSAGE_NO_POSITION);
		} else {
			_prevTask = _data.findMatchingPosition(_id.intValue());
		}
	}

	/*
	 * Checks if the given time is a valid range.
	 */
	private void checkValidTime() throws LogicException {
		if (_confirmed == null) {
			throw new LogicException(MESSAGE_INVALID_TIME);
		} else if (_confirmed.getEarlierDateTime() == null || _confirmed.getLaterDateTime() == null) {
			throw new LogicException(MESSAGE_INVALID_TIME);
		}
	}

	private void updateDateModified() {
		_prevTask.setDateModified(LocalDateTime.now());
		_confirmedTask.setDateModified(LocalDateTime.now());

	}

	/**
	 * Undo method for confirming of slots. Returns the task to the previous
	 * version of blocked task.
	 */
	public String undo() {
		updateDateModified();
		_data.deleteTask(_confirmedTask);
		_data.addTask(_prevTask);
		_data.setTaskPointer(_prevTask);
		return MESSAGE_BLOCK + taskMessageWithMulti(_prevTask);
	}

	/**
	 * Redo method for confirming of slots. Reconfirms the task to the confirmed
	 * slot.
	 */
	public String redo() {
		updateDateModified();
		_data.deleteTask(_prevTask);
		_data.addTask(_confirmedTask);
		_data.setTaskPointer(_confirmedTask);
		return MESSAGE_CONFIRM + taskMessageWithMulti(_confirmedTask);
	}

	/**
	 * Setter method for setting the location of the task to be confirmed.
	 * 
	 * @param id
	 *            Current location of the task to be confirmed.
	 */
	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

	/**
	 * Sets the time slot to be confirmed.
	 * 
	 * @param start
	 *            LocalDateTime of the start time of confirm.
	 * @param end
	 *            LocalDateTime of the end time of the confirm.
	 */
	public void setTimeSlot(LocalDateTime start, LocalDateTime end) {
		_confirmed = new DateTimePair(start, end);
	}

}
