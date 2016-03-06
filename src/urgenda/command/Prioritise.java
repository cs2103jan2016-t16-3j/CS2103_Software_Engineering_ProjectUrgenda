package urgenda.command;

import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class Prioritise implements Undoable {
	
	private static final String MESSAGE_IMPORTANT = " marked as important!";
	private static final String MESSAGE_UNIMPORTANT = " unmarked as important";
	private static final String MESSAGE_EVENT = "\"%1$s\" on %2$d/%3$d, %4$d:%5$d - %6$d:%7$d";
	private static final String MESSAGE_FLOAT = "\"%1$s\"";
	private static final String MESSAGE_DEADLINE = "\"%1$s\" by %2$d/%3$d, %4$d:%5$d";
	private static final String MESSAGE_UNDO = "Undo: ";
	private static final String MESSAGE_REDO = "Redo: ";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	private static final String MESSAGE_NO_MATCH = "No matches found to prioritise";
	
	private String _desc;
	private Integer _id;
	
	private Task _task;
	private LogicData _data;
	
	@Override
	public String execute(LogicData data) throws Exception {
		_data = data;
		ArrayList<Task> matches;
		if (_id != null || _id != 0) {
			_task = _data.findMatchingPosition(_id.intValue());			
		} else if (_desc != null) {
				matches = _data.findMatchingTasks(_desc);
				if (matches.size() == 1) {
					_task = matches.get(0);
				} else if (matches.size() > 1) {
					_data.clearDisplays();
					_data.setDisplays(matches);
					_data.setCurrState(LogicData.DisplayState.MULTIPLE_PRIORITISE);
					throw new Exception(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
				} // else matches has no match hence _task remains null
		}
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		if (_task == null) {
			throw new Exception(MESSAGE_NO_MATCH);
		}
		String feedback = toggleTaskImportance();
		return feedback;
	}
	
	private String taskMessage() {
		Task.Type taskType = _task.getTaskType();
		String feedback = null;
		switch (taskType) {
			case EVENT :
				feedback = String.format(MESSAGE_EVENT, _task.getDesc(), _task.getStartTime().getDayOfMonth(),
						_task.getStartTime().getMonthValue(), _task.getStartTime().getHour(), 
						_task.getStartTime().getMinute(), _task.getEndTime().getHour(), 
						_task.getEndTime().getMinute());
				break;
		
			case FLOATING :
				feedback = String.format(MESSAGE_FLOAT, _task.getDesc());
				break;
		
			case DEADLINE :
				feedback = String.format(MESSAGE_DEADLINE, _task.getDesc(), _task.getStartTime().getDayOfMonth(),
						_task.getStartTime().getMonthValue(), _task.getEndTime().getHour(), 
						_task.getEndTime().getMinute());
				break;

		}
		return feedback;
	}
	@Override
	public String undo() {
		String feedback = toggleTaskImportance();
		return MESSAGE_UNDO + feedback;
	}

	@Override
	public String redo() {
		String feedback = toggleTaskImportance();
		return MESSAGE_REDO + feedback;
	}

	public String toggleTaskImportance() {
		String feedback;
		if (_task.isImportant()) {
			feedback = taskMessage() + MESSAGE_UNIMPORTANT;
		} else {
			feedback = taskMessage() + MESSAGE_IMPORTANT;
		}
		_task.toggleImportant();
		return feedback;
	}

}
