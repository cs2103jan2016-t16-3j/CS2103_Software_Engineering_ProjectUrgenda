package urgenda.command;

import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class Complete implements Undoable {
	
	private static final String MESSAGE_DONE = "Done ";
	private static final String MESSAGE_UNDONE = "To do ";
	private static final String MESSAGE_EVENT = "\"%1$s\" on %2$d/%3$d, %4$02d:%5$02d - %6$02d:%7$02d";
	private static final String MESSAGE_FLOAT = "\"%1$s\"";
	private static final String MESSAGE_DEADLINE = "\"%1$s\" by %2$d/%3$d, %4$02d:%5$02d";
	private static final String MESSAGE_UNDO = "Undo: ";
	private static final String MESSAGE_REDO = "Redo: ";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	private static final String MESSAGE_NO_COMPLETE_MATCH = "No matches found to complete";
	
	// for undo of completed task
	private String _desc;
	private Integer _id;
	
	private Task _completedTask;
	private LogicData _data;
	
	@Override
	public String execute(LogicData data) throws Exception {
		_data = data;
		ArrayList<Task> matches;
		if (_desc != null) {
			matches = _data.findMatchingDesc(_desc);
			if (matches.size() == 1) {
				_completedTask = matches.get(0);
			} else if (matches.size() > 1) {
				_data.clearDisplays();
				_data.setDisplays(matches);
				_data.setCurrState(LogicData.DisplayState.MULTIPLE_COMPLETE);
				throw new Exception(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
			} // else matches has no match hence _completedTask remains null
		} else if (_id != null && _id.intValue() != -1) {
			_completedTask = _data.findMatchingPosition(_id.intValue());
		}
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		if (_completedTask == null) {
			throw new Exception(MESSAGE_NO_COMPLETE_MATCH);
		}
		_completedTask.setIsCompleted(true);
		_data.deleteTask(_completedTask);
		_data.addArchive(_completedTask);
		return MESSAGE_DONE + taskMessage() + "!";
	}
	
	private String taskMessage() {
		Task.Type taskType = _completedTask.getTaskType();
		String feedback = null;
		switch (taskType) {
			case EVENT :
				feedback = String.format(MESSAGE_EVENT, _completedTask.getDesc(), _completedTask.getStartTime().getDayOfMonth(),
						_completedTask.getStartTime().getMonthValue(), _completedTask.getStartTime().getHour(), 
						_completedTask.getStartTime().getMinute(), _completedTask.getEndTime().getHour(), 
						_completedTask.getEndTime().getMinute());
				break;
		
			case FLOATING :
				feedback = String.format(MESSAGE_FLOAT, _completedTask.getDesc());
				break;
		
			case DEADLINE :
				feedback = String.format(MESSAGE_DEADLINE, _completedTask.getDesc(), _completedTask.getEndTime().getDayOfMonth(),
						_completedTask.getEndTime().getMonthValue(), _completedTask.getEndTime().getHour(), 
						_completedTask.getEndTime().getMinute());
				break;

		}
		return feedback;
	}

	@Override
	public String undo() {
		_completedTask.setIsCompleted(false);
		_data.removeArchive(_completedTask);
		_data.addTask(_completedTask);
		return MESSAGE_UNDO + MESSAGE_UNDONE + taskMessage() + "!";
	}

	@Override
	public String redo() {
		_completedTask.setIsCompleted(true);
		_data.deleteTask(_completedTask);
		_data.addArchive(_completedTask);
		return MESSAGE_REDO + MESSAGE_DONE + taskMessage() + "!";
	}


	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

}
