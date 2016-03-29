package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;

public class Recurrence extends TaskCommand {
	
	private static final String MESSAGE_INVALID = "Insufficient reccurrence info entered";
	private static final String MESSAGE_ERROR = "ERROR: Invalid Task Type for recurrence";
	private static final String MESSAGE_RECURR = " has been set as recurring task";
	private static final String MESSAGE_RECURR_TASK = "Recurring task ";
	private static final String MESSAGE_REMOVED = " removed";
	private static final String MESSAGE_ADDED = " added";
	
	private MultipleSlot _recurr;
	private LogicData _data;
	private Task _newTask;
	private Integer _minutes;
	private Integer _hours;
	private Integer _days;
	private Integer _weeks;
	private Integer _months;
	private Integer _years;
	private Integer _length;

	public Recurrence() {
	}

	public String execute() throws Exception {
		_recurr = _newTask.getSlot();
		_data = LogicData.getInstance();
		if(_length == null) {
			_length = Integer.MAX_VALUE;
		}
		switch (_newTask.getTaskType()) {
		case DEADLINE:
			LocalDateTime start = null;
			LocalDateTime end = _newTask.getEndTime();
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
			if(_recurr.isEmpty() || _recurr.equals(null)) {
				throw new Exception(MESSAGE_INVALID);
			}
			break;
		case EVENT:
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
			if(_recurr.isEmpty() || _recurr.equals(null)) {
				throw new Exception(MESSAGE_INVALID);
			}
			break;
			default:
				throw new Exception (MESSAGE_ERROR);
		}
		//_newTask.toggleRecurring();   to be added 
		_newTask.updateTaskType();
		_newTask.setId(_data.getCurrentId());
		_data.updateCurrentId();
		LocalDateTime now = LocalDateTime.now();
		_newTask.setDateAdded(now);
		_newTask.setDateModified(now);
		_data.updateMultipleSlot(_newTask);
		_data.addTask(_newTask);
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		_data.setTaskPointer(_newTask);
		return taskMessage(_newTask) + MESSAGE_RECURR;
		
	}


	@Override
	public String undo() {
		_data.deleteTask(_newTask);
		return MESSAGE_RECURR_TASK + taskMessage(_newTask) + MESSAGE_REMOVED;
	}

	@Override
	public String redo() {
		_newTask.setDateModified(LocalDateTime.now());
		_data.addTask(_newTask);
		_data.setTaskPointer(_newTask);
		return MESSAGE_RECURR_TASK + taskMessage(_newTask) + MESSAGE_ADDED;
	}
	
	public void setNewTask(Task newTask) {
		_newTask = newTask;
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
