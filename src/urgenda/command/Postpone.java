//@@author A008043
package urgenda.command;

import java.time.LocalDateTime;

import urgenda.logic.LogicData;
import urgenda.util.LogicException;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

/**
 * Postpone command for postponing of tasks by the given timing.
 *
 */
public class Postpone extends TaskCommand {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	private static final String MESSAGE_NO_MATCH = "Invalid task number. No matches found to postpone";
	private static final String MESSAGE_NO_TIME = "Invalid time entered";
	private static final String MESSAGE_POSTPONE_FLOATING = "Task has no time to postpone";
	private static final String MESSAGE_POSTPONED_TASK = "\"%1$s\" postponed by ";
	private static final String MESSAGE_YEARS = " year(s) ";
	private static final String MESSAGE_MONTHS = " month(s) ";
	private static final String MESSAGE_DAYS = " day(s) ";
	private static final String MESSAGE_HOURS = " hour(s) ";
	private static final String MESSAGE_MINUTES = " minute(s) ";
	private static final String MESSAGE_SECONDS = " second(s) ";

	private Task _prevTask;
	private Task _newTask;
	private Integer _id;
	private LogicData _data;

	private int _year;
	private int _month;
	private int _day;
	private int _hour;
	private int _minute;
	private int _second;

	/**
	 * Default Constructor for postpone which generates a command object that
	 * postpones by 0.
	 */
	public Postpone() {
		_year = 0;
		_month = 0;
		_day = 0;
		_hour = 0;
		_minute = 0;
		_second = 0;
	}

	/**
	 * Constructor for assigning the timings for postponing based on the units
	 * of time.
	 * 
	 * @param year
	 *            Number of years to postpone.
	 * @param month
	 *            Number of months to postpone.
	 * @param day
	 *            Number of days to postpone.
	 * @param hour
	 *            Number of hours to postpone.
	 * @param minute
	 *            Number of minutes to postpone.
	 * @param second
	 *            Number of seconds to postpone.
	 */
	public Postpone(int year, int month, int day, int hour, int minute, int second) {
		_year = year;
		_month = month;
		_day = day;
		_hour = hour;
		_minute = minute;
		_second = second;
	}

	/**
	 * Execution method of the Postpone command to postpone the task by the
	 * given duration.
	 */
	public String execute() throws LogicException {
		_data = LogicData.getInstance();
		findTaskToPostpone();
		checkValidPostponeTask();
		createPostponedTask();
		replacePostponedTask();
		return generateFeedback();
	}

	/*
	 * Updates the state of Urgenda with the new postponed task.
	 */
	private void replacePostponedTask() {
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		_data.deleteTask(_prevTask);
		_data.addTask(_newTask);
		_data.setTaskPointer(_newTask);
		_data.clearShowMoreTasks();
	}

	/*
	 * Creates a new task based on the previous task and adds the new time.
	 */
	private void createPostponedTask() {
		_prevTask.setDateModified(LocalDateTime.now());
		_newTask = new Task(_prevTask);
		checkSlots();
		addTime(_newTask);
	}

	/*
	 * Checks if the task/postpone timing is valid.
	 */
	private void checkValidPostponeTask() throws LogicException {
		if (!isValidPostpone()) {
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			logger.getLogger().severe("Exception no postpone time thrown");
			throw new LogicException(MESSAGE_NO_TIME);
		} else if (_prevTask == null) {
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			logger.getLogger().severe("Exception(No postpone match) thrown");
			throw new LogicException(MESSAGE_NO_MATCH);
		} else if (_prevTask.getTaskType() == Task.Type.FLOATING) {
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			logger.getLogger().severe("Exception postpone of floating thrown");
			throw new LogicException(MESSAGE_POSTPONE_FLOATING);
		}
	}

	/*
	 * Finds the task to postpone based on the given index.
	 */
	private void findTaskToPostpone() {
		if (_id != null && _id.intValue() != -1) {
			_prevTask = _data.findMatchingPosition(_id.intValue());
		}
	}

	/*
	 * Checks if postpone timing is valid, if all attributes are empty.
	 */
	private boolean isValidPostpone() {
		if (_year == 0 && _month == 0 && _day == 0 && _hour == 0 && _minute == 0 && _second == 0) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * Creates a new slot if the previous task has a slot.
	 */
	private void checkSlots() {
		if (_prevTask.getSlot() != null) {
			_newTask.setSlot(new MultipleSlot(_prevTask.getSlot()));
		}

	}

	/*
	 * Appends the feedback string of the different durations.
	 */
	private String generateFeedback() {
		String feedback = String.format(MESSAGE_POSTPONED_TASK, _prevTask.getDesc());
		if (_year > 0) {
			feedback += _year + MESSAGE_YEARS;
		}
		if (_month > 0) {
			feedback += _month + MESSAGE_MONTHS;
		}
		if (_day > 0) {
			feedback += _day + MESSAGE_DAYS;
		}
		if (_hour > 0) {
			feedback += _hour + MESSAGE_HOURS;
		}
		if (_minute > 0) {
			feedback += _minute + MESSAGE_MINUTES;
		}
		if (_second > 0) {
			feedback += _second + MESSAGE_SECONDS;
		}
		return feedback;
	}

	/*
	 * Adds timing to the task based on the task type.
	 */
	private void addTime(Task task) {
		if (task.getTaskType() == Task.Type.EVENT) {
			LocalDateTime start = task.getStartTime();
			start = addTime(start);
			task.setStartTime(start);
		}
		LocalDateTime end = task.getEndTime();
		end = addTime(end);
		task.setEndTime(end);
	}

	private LocalDateTime addTime(LocalDateTime time) {
		time = time.plusYears(_year);
		time = time.plusMonths(_month);
		time = time.plusDays(_day);
		time = time.plusHours(_hour);
		time = time.plusMinutes(_minute);
		time = time.plusSeconds(_second);
		return time;
	}

	/**
	 * Undo method for postpone where the postponed task is replaced back with
	 * the original task.
	 */
	public String undo() {
		_prevTask.setDateModified(LocalDateTime.now());
		_newTask.setDateModified(LocalDateTime.now());
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		_data.deleteTask(_newTask);
		_data.addTask(_prevTask);
		_data.setTaskPointer(_prevTask);
		return generateFeedback();
	}

	/**
	 * Redo method for postpone where the postponed task is added back and
	 * replaces the original task.
	 */
	public String redo() {
		_prevTask.setDateModified(LocalDateTime.now());
		_newTask.setDateModified(LocalDateTime.now());
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		_data.deleteTask(_prevTask);
		_data.addTask(_newTask);
		_data.setTaskPointer(_newTask);
		return generateFeedback();
	}

	/**
	 * Setter for the Year duration.
	 * 
	 * @param year
	 *            Number of years to postpone.
	 */
	public void setYear(int year) {
		if (year >= 0) {
			_year = year;
		}
	}

	/**
	 * Setter for the month duration.
	 * 
	 * @param year
	 *            Number of months to postpone.
	 */
	public void setMonth(int month) {
		if (month >= 0) {
			_month = month;
		}
	}

	/**
	 * Setter for the day duration.
	 * 
	 * @param year
	 *            Number of days to postpone.
	 */
	public void setDay(int day) {
		if (day >= 0) {
			_day = day;
		}
	}

	/**
	 * Setter for the hour duration.
	 * 
	 * @param year
	 *            Number of hours to postpone.
	 */
	public void setHour(int hour) {
		if (hour >= 0) {
			_hour = hour;
		}
	}

	/**
	 * Setter for the minute duration.
	 * 
	 * @param year
	 *            Number of minutes to postpone.
	 */
	public void setMinute(int minute) {
		if (minute >= 0) {
			_minute = minute;
		}
	}

	/**
	 * Setter for the second duration.
	 * 
	 * @param year
	 *            Number of seconds to postpone.
	 */
	public void setSecond(int second) {
		if (second >= 0) {
			_second = second;
		}
	}

	/**
	 * Setter for the Id of the task to be postponed.
	 * 
	 * @param id
	 *            task position to be postponed.
	 */
	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

}
