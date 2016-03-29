package urgenda.command;

import java.time.LocalDateTime;

import urgenda.logic.LogicData;
import urgenda.util.Task;
import urgenda.util.DateTimePair;
import urgenda.util.MultipleSlot;

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

	public String execute() throws Exception {
		if (_confirmed == null) {
			throw new Exception(MESSAGE_INVALID_TIME);
		} else if (_confirmed.getEarlierDateTime() == null || _confirmed.getLaterDateTime() == null) {
			throw new Exception(MESSAGE_INVALID_TIME);
		}
		_data = LogicData.getInstance();
		if (_id == null) {
			throw new Exception(MESSAGE_NO_POSITION);
		} else {
			_prevTask = _data.findMatchingPosition(_id.intValue());
		}
		
		if (_prevTask == null) {
			throw new Exception(MESSAGE_NO_POSITION);
		} else if (_prevTask.getSlot() == null) {
			_data.setTaskPointer(_prevTask);
			throw new Exception(MESSAGE_NO_MULTIPLE);
		}
		
		_confirmedTask = new Task(_prevTask);
		DateTimePair currPair = new DateTimePair(_confirmedTask.getStartTime(), _confirmedTask.getEndTime());
		if (currPair.equals(_confirmed)) {
			_confirmedTask.setSlot(null);
			_data.deleteTask(_prevTask);
			_data.addTask(_confirmedTask);
			_data.setTaskPointer(_confirmedTask);
			_data.clearShowMoreTasks();
			return MESSAGE_CONFIRM + taskMessageWithMulti(_confirmedTask);
		} else {
			MultipleSlot tempSlot = new MultipleSlot(_confirmedTask.getSlot());
			while (tempSlot != null && !(tempSlot.isEmpty())) {
				currPair = tempSlot.getNextSlot();
				tempSlot.removeNextSlot();
				if (currPair.equals(_confirmed)) {
					_confirmedTask.setStartTime(currPair.getEarlierDateTime());
					_confirmedTask.setEndTime(currPair.getLaterDateTime());
					_confirmedTask.setSlot(null);
					updateDateModified();
					_data.deleteTask(_prevTask);
					_data.addTask(_confirmedTask);
					_data.setTaskPointer(_confirmedTask);
					_data.clearShowMoreTasks();
					return MESSAGE_CONFIRM + taskMessageWithMulti(_confirmedTask);
				}
			}
			_data.setTaskPointer(_prevTask);
			throw new Exception(String.format(MESSAGE_NO_MATCH, _confirmed.getEarlierDateTime().getDayOfMonth(),
					_confirmed.getEarlierDateTime().getMonthValue(), _confirmed.getEarlierDateTime().getHour(),
					_confirmed.getEarlierDateTime().getMinute(), _confirmed.getLaterDateTime().getDayOfMonth(),
					_confirmed.getLaterDateTime().getMonthValue(), _confirmed.getLaterDateTime().getHour(),
					_confirmed.getLaterDateTime().getMinute()));
		}
	}

	private void updateDateModified() {
		_prevTask.setDateModified(LocalDateTime.now());
		_confirmedTask.setDateModified(LocalDateTime.now());
		
	}

	public String undo() {
		updateDateModified();
		_data.deleteTask(_confirmedTask);
		_data.addTask(_prevTask);
		_data.setTaskPointer(_prevTask);
		return MESSAGE_BLOCK + taskMessageWithMulti(_prevTask);
	}

	public String redo() {
		updateDateModified();
		_data.deleteTask(_prevTask);
		_data.addTask(_confirmedTask);
		_data.setTaskPointer(_confirmedTask);
		return MESSAGE_CONFIRM + taskMessageWithMulti(_confirmedTask);
	}
	
	public void setId(int id) {
		_id = Integer.valueOf(id);
	}
	
	public void setTimeSlot(LocalDateTime start, LocalDateTime end) {
		_confirmed = new DateTimePair(start, end);
	}
	
}
