package urgenda.command;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class ShowDetails extends TaskCommand {
	
	private static final String MESSAGE_NO_SHOW_MATCH = "Unable to show more details at position %1$d";

	private Integer _position;
	
	private Task _task;
	private LogicData _data;

	public String execute(LogicData data) throws Exception {
		_data = data;
		if (_position != null && _position.intValue() != -1) {
			_task = _data.findMatchingPosition(_position.intValue());
		}
		// Does not change the previous state
		if (_task == null) {
			throw new Exception(String.format(MESSAGE_NO_SHOW_MATCH, _position));
		}
		_data.toggleShowMoreTasks(_task);
		return null;
	}

	public String undo() {
		_data.toggleShowMoreTasks(_task);
		return null;
	}

	public String redo() {
		_data.toggleShowMoreTasks(_task);
		return null;
	}

	public Integer getPosition() {
		return _position;
	}

	public void setPosition(int position) {
		_position = Integer.valueOf(position);
	}

}
