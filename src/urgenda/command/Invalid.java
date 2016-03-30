package urgenda.command;

import urgenda.command.Command;
import urgenda.logic.LogicData;

public class Invalid extends Command {
	
	private static final String MESSAGE_INVALID_COMMAND = "\"%1$s\" is not a valid command";
	private static final String MESSAGE_INVALID_FINDFREE = "Invalid command for available time";
	private static final String MESSAGE_INVALID_ARCHIVE = "Invalid command for archived task";
	
	private String _command;
	private LogicData.DisplayState _state;
	
	public Invalid() {
		
	}
	
	public Invalid(String command) {
		_command = command;
	}
	
	public Invalid(LogicData.DisplayState state) {
		_state = state;
	}

	// TODO set to default pointer at current pointer position
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.INVALID_COMMAND);
		if (_state == LogicData.DisplayState.FIND_FREE) {
			return MESSAGE_INVALID_FINDFREE;
		} else if (_state == LogicData.DisplayState.ARCHIVE) {
			return MESSAGE_INVALID_ARCHIVE;
		} else if (_command.equals("")) {
			return null;
		} else {
			return String.format(MESSAGE_INVALID_COMMAND, _command);			
		}
	}
	
	public void setCommand(String command) {
		_command = command;
	}

}
