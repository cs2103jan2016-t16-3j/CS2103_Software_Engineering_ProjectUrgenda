//@@author A0127358Y
//Unused feature due to change of additional special feature towards the end. Decided to go for nicer and 
//more rewarding feature Good Gui rather than recurring task. Hence this feature has not been fully tested, implemented
//and refactored. Left it here for the effort spent, probably deleting it in the end.

package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.LogicException;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

/**
 * 
 * Recurrence command object used for performing recurring tasks in Urgenda.
 * This is an unused feature due to change in special feature. Not implemented and called by other components.
 * Tag as unused due to the effort spent ><. 
 *
 */
public class Recurrence extends TaskCommand {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String MESSAGE_NO_RECURR_MATCH = "Invalid task number. No matches found to set as recurring";
	private static final String MESSAGE_ERROR = "ERROR:";
	private static final String MESSAGE_INVALID = "Insufficient reccurrence info entered";
	private static final String MESSAGE_TYPE_ERROR = "ERROR: Invalid Task Type for recurrence";
	private static final String MESSAGE_RECURR = " has been set as recurring task";
	private static final String MESSAGE_RECURR_TASK = "Recurring task ";
	private static final String MESSAGE_REMOVED = " removed";
	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_EVENT_PASSED = "\nWarning: Event added has already passed";
	private static final String MESSAGE_DEADLINE_PASSED = "\nWarning: Deadline added has already passed";
	private static final String MESSAGE_OVERLAP = "\nWarning: Overlaps with ";
	private static final String MESSAGE_NONRECURR = " has been reverted to non-recurring";

	private MultipleSlot _recurr;
	private LogicData _data;
	private Task _newTask;
	private Task _recurrTask;
	private Integer _id;
	private Integer _minutes;
	private Integer _hours;
	private Integer _days;
	private Integer _weeks;
	private Integer _months;
	private Integer _years;
	private Integer _length;

	public Recurrence() {
	}

	public String execute() throws LogicException {
		_data = LogicData.getInstance();
		if (_id != null && _id.intValue() > -1) {
			_recurrTask = _data.findMatchingPosition(_id.intValue());
		}
		if (_recurrTask == null) {
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			logger.getLogger().severe("Exception(No edit match) thrown");
			throw new LogicException(MESSAGE_NO_RECURR_MATCH);
		} else {
			_newTask = new Task(_recurrTask);
		}
		_recurr = _newTask.getSlot();
		if (_length == null) {
			_length = Integer.MAX_VALUE;
		}
		recurrTimeSlot();
		if (_recurr.isEmpty() || _recurr.equals(null)) {
			throw new LogicException(MESSAGE_INVALID);
		}

		// _newTask.toggleRecurring(); attribute to be added to task to indicate
		// task is of type recurring
		// highlight to gui to only show start time, end time and first
		// datetimepair in multiple slot
		_newTask.setSlot(_recurr);
		LocalDateTime now = LocalDateTime.now();
		updateId(now);
		_newTask.setDateModified(now);
		_newTask.updateTaskType();
		String feedback = checkValidity();
		return taskMessage(_newTask) + MESSAGE_RECURR + feedback;
	}

	private String checkValidity() throws LogicException {
		String feedback = "";
		try {
			checkTaskValidity(_newTask);
			_data.addTask(_newTask);
			if (_id != null) {
				_data.deleteTask(_recurrTask);
			}
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			_data.setTaskPointer(_newTask);
			_data.clearShowMoreTasks();
			if (_id == null) {
				feedback = checkPassed();
				feedback += findOverlaps();
			}
		} catch (LogicException e) {
			logger.getLogger().severe("Exception occurred" + e);
			_data.setCurrState(LogicData.DisplayState.INVALID_TASK);
			// throws exception to prevent AddTask being added to undo stack
			throw new LogicException(MESSAGE_ERROR + e.getMessage());
		}
		return feedback;
	}

	private void updateId(LocalDateTime now) {
		if (_id != null) {
			_newTask.setId(_data.getCurrentId());
			_newTask.setDateAdded(now);
			_data.updateCurrentId();
		}
	}

	private void recurrTimeSlot() throws LogicException {
		switch (_newTask.getTaskType()) {
		case DEADLINE:
			LocalDateTime start = null;
			LocalDateTime end = _newTask.getEndTime();
			end = updateDeadLineTimeSlot(start, end);
			break;
		case EVENT:
			upDateEventTimeSlot();
			break;
		default:
			throw new LogicException(MESSAGE_TYPE_ERROR);
		}
	}

	private void upDateEventTimeSlot() {
		LocalDateTime start;
		LocalDateTime end;
		start = _newTask.getStartTime();
		end = _newTask.getEndTime();
		for (int i = 0; i < (_length).intValue(); i++) {
			if (_minutes != null) {
				start = start.plusMinutes(_minutes);
				end = end.plusMinutes(_minutes);
			}
			if (_hours != null) {
				start = start.plusHours(_hours);
				end = end.plusHours(_hours);
			}
			if (_days != null) {
				start = start.plusDays(_days);
				end = end.plusDays(_days);
			}
			if (_weeks != null) {
				start = start.plusWeeks(_weeks);
				end = end.plusWeeks(_weeks);
			}
			if (_months != null) {
				start = start.plusMonths(_months);
				end = end.plusMonths(_months);
			}
			if (_months != null) {
				start = start.plusYears(_years);
				end = end.plusYears(_years);
			}
			_recurr.addTimeSlot(start, end);
		}
	}

	private LocalDateTime updateDeadLineTimeSlot(LocalDateTime start, LocalDateTime end) {
		for (int i = 0; i < (_length).intValue(); i++) {
			if (_minutes != null) {
				end = end.plusMinutes(_minutes);
			}
			if (_hours != null) {
				end = end.plusHours(_hours);
			}
			if (_days != null) {
				end = end.plusDays(_days);
			}
			if (_weeks != null) {
				end = end.plusWeeks(_weeks);
			}
			if (_months != null) {
				end = end.plusMonths(_months);
			}
			if (_months != null) {
				end = end.plusYears(_years);
			}
			_recurr.addTimeSlot(start, end);
		}
		return end;
	}

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

	private String findOverlaps() {
		ArrayList<Task> overlaps;
		overlaps = _data.overlappingTasks(_newTask);

		if (overlaps.size() == 0) {
			return "";
		} else {
			String feedback = MESSAGE_OVERLAP + taskMessage(overlaps.get(0));
			overlaps.remove(0);
			for (Task task : overlaps) {
				feedback += ", " + taskMessage(task);
			}
			return feedback;
		}
	}

	@Override
	public String undo() {
		String feedback;
		if (_id != null) {
			_data.addTask(_recurrTask);
			feedback = taskMessage(_newTask) + MESSAGE_NONRECURR;
		} else {
			feedback = MESSAGE_RECURR_TASK + taskMessage(_newTask) + MESSAGE_REMOVED;
		}
		_data.deleteTask(_newTask);
		return feedback;
	}

	@Override
	public String redo() {
		String feedback;
		if (_id != null) {
			_data.deleteTask(_recurrTask);
			feedback = taskMessage(_recurrTask) + MESSAGE_RECURR;
		} else {
			feedback = MESSAGE_RECURR_TASK + taskMessage(_newTask) + MESSAGE_ADDED;
		}
		_newTask.setDateModified(LocalDateTime.now());
		_data.addTask(_newTask);
		_data.setTaskPointer(_newTask);
		return feedback;
	}

	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}

	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

	public void setMins(int input) {
		_minutes = Integer.valueOf(input);
	}

	public void setHours(int input) {
		_hours = Integer.valueOf(input);
	}

	public void setDays(int input) {
		_days = Integer.valueOf(input);
	}

	public void setWeeks(int input) {
		_weeks = Integer.valueOf(input);
	}

	public void setMonths(int input) {
		_months = Integer.valueOf(input);
	}

	public void setYears(int input) {
		_years = input;
	}

	public void updateDateModified(ArrayList<Task> tasks) {
		LocalDateTime now = LocalDateTime.now();
		for (Task task : tasks) {
			task.setDateModified(now);
		}
	}

}
