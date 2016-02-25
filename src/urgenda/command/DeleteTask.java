package urgenda.command;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class DeleteTask implements Command {

	private static final String MESSAGE_EVENT = "\"$1%s\" on $2%d/$3%d, $4%d:$5%d - $6%d:$7%d";
	private static final String MESSAGE_FLOAT = "\"$1%s\"";
	private static final String MESSAGE_DEADLINE = "\"$1%s\" by $2%d/$3%d, $4%d:$5%d";
	private static final String MESSAGE_START = "\"$1%s\" at $2%d/$3%d, $4%d:$5%d";
	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_UNDO = "Undo: ";
	private static final String MESSAGE_REDO = "Redo: ";
	private static final String MESSAGE_INVALID_DELETE = "Invalid delete command";
	
	// one of these 3 properties can be filled for identification of deleted task
	private String _desc;
	private Integer _id;
	private Integer _position;
	
	// to store from deletion, so that undo can be done
	private Task _deletedTask;
	private LogicData _data;
	
	@Override
	public String execute(LogicData data) {
		_data = data;
		if (_desc != null) {
			_deletedTask = _data.findMatchingDesc(_desc);
		} else if (_id != null) {
			_deletedTask = _data.findMatchingId(_id.intValue());
		} else if (_position != null) {
			_deletedTask = _data.findTaskPosition(_position.intValue());
		}
		
		if (_deletedTask == null) {
			// TODO throw exception for invalid delete position
			return MESSAGE_INVALID_DELETE;
		}
		_data.deleteTask(_deletedTask);
		// TODO allow exception for multiple matching descriptions (throw exception and unique statefeedback)
		return taskMessage() + MESSAGE_REMOVE;
	}
	
	private String taskMessage() {
		Task.Type taskType = _deletedTask.getTaskType();
		String feedback = null;
		switch (taskType) {
			case EVENT :
				feedback = String.format(MESSAGE_EVENT, _deletedTask.getDesc(), _deletedTask.getStartTime().getDayOfMonth(),
						_deletedTask.getStartTime().getMonthValue(), _deletedTask.getStartTime().getHour(), 
						_deletedTask.getStartTime().getMinute(), _deletedTask.getEndTime().getHour(), 
						_deletedTask.getEndTime().getMinute());
				break;
		
			case FLOATING :
				feedback = String.format(MESSAGE_FLOAT, _deletedTask.getDesc());
				break;
		
			case DEADLINE :
				feedback = String.format(MESSAGE_DEADLINE, _deletedTask.getDesc(), _deletedTask.getStartTime().getDayOfMonth(),
						_deletedTask.getStartTime().getMonthValue(), _deletedTask.getEndTime().getHour(), 
						_deletedTask.getEndTime().getMinute());
				break;
			
			case START :
				feedback = String.format(MESSAGE_START, _deletedTask.getDesc(), _deletedTask.getStartTime().getDayOfMonth(),
						_deletedTask.getStartTime().getMonthValue(), _deletedTask.getStartTime().getHour(), 
						_deletedTask.getStartTime().getMinute());
				break;

		}
		return feedback;
	}

	@Override
	public void getDetails(String[] details) {
		// TODO Auto-generated method stub

	}

	@Override
	public String undo() {
		_data.addTask(_deletedTask);
		return MESSAGE_UNDO + taskMessage() + MESSAGE_ADDED;
	}

	@Override
	public String redo() {
		_data.deleteTask(_deletedTask);
		return MESSAGE_REDO + taskMessage() + MESSAGE_REMOVE;
	}

	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setId(int id) {
		_id = id;
	}

	public void setPosition(int position) {
		_position = position;
	}

}
