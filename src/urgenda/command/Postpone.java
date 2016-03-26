package urgenda.command;

import java.time.LocalDateTime;

import urgenda.logic.LogicData;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

public class Postpone extends TaskCommand {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String MESSAGE_NO_MATCH = "Invalid task number. No matches found to postpone";
	private static final String MESSAGE_POSTPONE_FLOATING = "Task has no time to postpone";
	private static final String MESSAGE_POSTPONED_TASK = "\"%1$s\" postponed by ";
	private static final String MESSAGE_YEARS = " year(s) ";
	private static final String MESSAGE_MONTHS = " month(s) ";
	private static final String MESSAGE_DAYS = " day(s) ";
	private static final String MESSAGE_HOURS = " hour(s) ";
	private static final String MESSAGE_MINUTES = " minute(s) ";
	private static final String MESSAGE_SECONDS = " second(s) ";
	
	private Task _task;
	private Integer _id;
	private LogicData _data;
	
	private int _year;
	private int _month;
	private int _day;
	private int _hour;
	private int _minute;
	private int _second;

	public Postpone() {
		_year = 0;
		_month = 0;
		_day = 0;
		_hour = 0;
		_minute = 0;
		_second = 0;
	}

	public Postpone(int year, int month, int day, int hour, int minute, int second) {
		_year = year;
		_month = month;
		_day = day;
		_hour = hour;
		_minute = minute;
		_second = second;
	}

	public String execute() throws Exception {
		_data = LogicData.getInstance();
		if (_id != null && _id.intValue() != -1) {
			_task = _data.findMatchingPosition(_id.intValue());
		}
		if (_task == null) {
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			logger.getLogger().severe("Exception(No postpone match) thrown");
			throw new Exception(MESSAGE_NO_MATCH);
		} else if (_task.getTaskType() == Task.Type.FLOATING){
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			logger.getLogger().severe("Exception postpone of floating thrown");
			throw new Exception(MESSAGE_POSTPONE_FLOATING);
		} else {
			addTime();
			_task.setDateModified(LocalDateTime.now());
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			_data.setTaskPointer(_task);
		}
		return generateFeedback();
	}

	private String generateFeedback() {
		String feedback = String.format(MESSAGE_POSTPONED_TASK, _task.getDesc());
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

	private void addTime() {
		if (_task.getTaskType() == Task.Type.EVENT) {
			LocalDateTime start = _task.getStartTime();
			start.plusYears(_year);
			start.plusMonths(_month);
			start.plusDays(_day);
			start.plusHours(_hour);
			start.plusMinutes(_minute);
			start.plusSeconds(_second);
			_task.setStartTime(start);
		}		

		LocalDateTime end = _task.getEndTime();
		end.plusYears(_year);
		end.plusMonths(_month);
		end.plusDays(_day);
		end.plusHours(_hour);
		end.plusMinutes(_minute);
		end.plusSeconds(_second);
		_task.setEndTime(end);
	}

	public String undo() {
		minusTime();
		_task.setDateModified(LocalDateTime.now());
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		_data.setTaskPointer(_task);
		return generateFeedback();
	}

	private void minusTime() {
		if (_task.getTaskType() == Task.Type.EVENT) {
			LocalDateTime start = _task.getStartTime();
			start.minusYears(_year);
			start.minusMonths(_month);
			start.minusDays(_day);
			start.minusHours(_hour);
			start.minusMinutes(_minute);
			start.minusSeconds(_second);
			_task.setStartTime(start);
		}		

		LocalDateTime end = _task.getEndTime();
		end.minusYears(_year);
		end.minusMonths(_month);
		end.minusDays(_day);
		end.minusHours(_hour);
		end.minusMinutes(_minute);
		end.minusSeconds(_second);
		_task.setEndTime(end);
	}

	public String redo() {
		addTime();
		_task.setDateModified(LocalDateTime.now());
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		_data.setTaskPointer(_task);
		return generateFeedback();
	}

	public void setYear(int year) {
		if (year >= 0) {
			_year = year;			
		}
	}

	public void setMonth(int month) {
		if (month >= 0) {
			_month = month;			
		}
	}

	public void setDay(int day) {
		if (day >= 0) {
			_day = day;			
		}
	}

	public void setHour(int hour) {
		if (hour >= 0) {
			_hour = hour;			
		}
	}

	public void setMinute(int minute) {
		if (minute >= 0) {
			_minute = minute;			
		}
	}

	public void setSecond(int second) {
		if (second >= 0) {
			_second = second;			
		}
	}
	
	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

}
