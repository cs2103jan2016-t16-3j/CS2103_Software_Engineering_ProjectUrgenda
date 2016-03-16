package urgenda.command;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class ShowDetails extends TaskCommand {
	
	private static final String MESSAGE_NO_SHOW_MATCH = "Unable to show more details at position %1$d";
	private static final String MESSAGE_SHOWING_MORE = "Showing more details for \"%1$s\"";
	private static final String MESSAGE_SHOWING_LESS = "Showing less details for \"%1$s\"";

	private Integer _position;
	
	private Task _task;
	private LogicData _data;

	public String execute() throws Exception {
		_data = LogicData.getInstance();
		if (_position != null && _position.intValue() != -1) {
			_task = _data.findMatchingPosition(_position.intValue());
		}
		// Does not change the previous state
		if (_task == null) {
			throw new Exception(String.format(MESSAGE_NO_SHOW_MATCH, _position));
		}
		_data.toggleShowMoreTasks(_task);
		return showMoreFeedback();
	}

	public String showMoreFeedback() {
		if (_data.isShowingMore(_task)) {
			return String.format(MESSAGE_SHOWING_MORE, _task.getDesc());
		} else {
			return String.format(MESSAGE_SHOWING_LESS, _task.getDesc());
		}
	}

	public String undo() {
		_data.toggleShowMoreTasks(_task);
		return showMoreFeedback();
	}

	public String redo() {
		_data.toggleShowMoreTasks(_task);
		return showMoreFeedback();
	}

	public Integer getPosition() {
		return _position;
	}

	public void setPosition(int position) {
		_position = Integer.valueOf(position);
	}

}
