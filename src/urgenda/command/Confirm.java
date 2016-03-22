package urgenda.command;

import java.time.LocalDateTime;

import urgenda.logic.LogicData;
import urgenda.util.Task;
import urgenda.util.TimePair;

public class Confirm extends TaskCommand {
	
	private static final String MESSAGE_NO_POSITION = "Invalid position to confirm";
	private static final String MESSAGE_INVALID_TIME = "Invalid confirm time";
	private static final String MESSAGE_NO_MULTIPLE = "Task does not have multiple slots";
	private static final String MESSAGE_NO_MATCH = "No matches found for %1$02d:%2$02d - %3$02d:%4$02d";
	private static final String MESSAGE_CONFIRM = "Confirmed ";
	private static final String MESSAGE_BLOCK = "Blocked ";
	
	private Integer _id;
	private TimePair _confirmed;
	private LogicData _data;
	private Task _prevTask;
	private Task _confirmedTask;

	public String execute() throws Exception {
		if (_confirmed == null) {
			throw new Exception(MESSAGE_INVALID_TIME);
		} else if (_confirmed.getStart() == null || _confirmed.getEnd() == null) {
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
			throw new Exception(MESSAGE_NO_MULTIPLE);
		}
		
		_confirmedTask = new Task(_prevTask);
		TimePair currPair = new TimePair(_confirmedTask.getStartTime(), _confirmedTask.getEndTime());
		if (currPair.isEqual(_confirmed)) {
			_confirmedTask.setSlot(null);
			_data.deleteTask(_prevTask);
			_data.addTask(_confirmedTask);
			_data.setTaskPointer(_confirmedTask);
			return MESSAGE_CONFIRM + taskMessage(_confirmedTask);
		} else {
			while (_confirmedTask.getSlot() != null && !(_confirmedTask.getSlot().isEmpty())) {
				currPair = _confirmedTask.getSlot().getNextSlot();
				_confirmedTask.getSlot().removeNextSlot();
				if (currPair.isEqual(_confirmed)) {
					_confirmedTask.setStartTime(currPair.getStart());
					_confirmedTask.setEndTime(currPair.getEnd());
					_confirmedTask.setSlot(null);
					updateDateModified();
					_data.deleteTask(_prevTask);
					_data.addTask(_confirmedTask);
					_data.setTaskPointer(_confirmedTask);
					return MESSAGE_CONFIRM + taskMessage(_confirmedTask);
				}
			}
			throw new Exception(String.format(MESSAGE_NO_MATCH, _confirmed.getStart().getDayOfMonth(),
					_confirmed.getStart().getMonthValue(), _confirmed.getEnd().getDayOfMonth(),
					_confirmed.getEnd().getMonthValue()));
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
		return MESSAGE_BLOCK + taskMessage(_prevTask);
	}

	public String redo() {
		updateDateModified();
		_data.deleteTask(_prevTask);
		_data.addTask(_confirmedTask);
		_data.setTaskPointer(_confirmedTask);
		return MESSAGE_CONFIRM + taskMessage(_confirmedTask);
	}
	
	public void setId(int id) {
		_id = Integer.valueOf(id);
	}
	
	public void setTimeSlot(LocalDateTime start, LocalDateTime end) {
		_confirmed = new TimePair(start, end);
	}
	
}
