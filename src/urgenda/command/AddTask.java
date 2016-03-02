package urgenda.command;

import java.time.LocalDateTime;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class AddTask implements Undoable {
	
	private static final String MESSAGE_EVENT = "\"%1$s\" on %2$d/%3$d, %4$d:%5$d - %6$d:%7$d";
	private static final String MESSAGE_FLOAT = "\"%1$s\"";
	private static final String MESSAGE_DEADLINE = "\"%1$s\" by %2$d/%3$d, %4$d:%5$d";
	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_UNDO = "Undo: ";
	private static final String MESSAGE_REDO = "Redo: ";
	
	private Task _newTask;
	private LogicData _data;
	
	public AddTask() {
		_newTask = new Task();
	}
	
	public AddTask(Task newTask) {
		_newTask = newTask;
	}
	
	@Override
	public String execute(LogicData dataStorage) {
		_data = dataStorage;
		LocalDateTime now = LocalDateTime.now();
		_newTask.setId(_data.getCurrentId());
		_newTask.setDateAdded(now);
		_newTask.setDateModified(now);
		_data.updateCurrentId();
		_data.addTask(_newTask);
		return taskMessage() + MESSAGE_ADDED;
	}

	private String taskMessage() {
		Task.Type taskType = _newTask.getTaskType();
		String feedback = null;
		switch (taskType) {
			case EVENT :
				feedback = String.format(MESSAGE_EVENT, _newTask.getDesc(), _newTask.getStartTime().getDayOfMonth(),
						_newTask.getStartTime().getMonthValue(), _newTask.getStartTime().getHour(), 
						_newTask.getStartTime().getMinute(), _newTask.getEndTime().getHour(), 
						_newTask.getEndTime().getMinute());
				break;
		
			case FLOATING :
				feedback = String.format(MESSAGE_FLOAT, _newTask.getDesc());
				break;
		
			case DEADLINE :
				feedback = String.format(MESSAGE_DEADLINE, _newTask.getDesc(), _newTask.getStartTime().getDayOfMonth(),
						_newTask.getStartTime().getMonthValue(), _newTask.getEndTime().getHour(), 
						_newTask.getEndTime().getMinute());
				break;

		}
		return feedback;
	}

	@Override
	public String undo() {
		_data.deleteTask(_newTask);
		return MESSAGE_UNDO + taskMessage() + MESSAGE_REMOVE;
	}

	@Override
	public String redo() {
		_data.addTask(_newTask);
		return MESSAGE_REDO + taskMessage() + MESSAGE_ADDED;
	}
	
	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}

}
