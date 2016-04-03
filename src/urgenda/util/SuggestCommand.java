package urgenda.util;

import java.util.ArrayList;

public class SuggestCommand {

	public enum Command {
		ADD, ARCHIVE, BLOCK, CONFIRM, DELETE, DEMO, DONE, EDIT, EXIT, FIND_FREE, 
		HELP, HIDE, HOME, POSTPONE, PRIORITISE, REDO, SAVETO, SEARCH, SHOWMORE, UNDO
	}
	// AL of possible FULL versions of command IN STRING (since each type has diff variations)
	private ArrayList<String> _possibleCommands;
	
	// command type if confirmed (return null if AL is used) / both null if is add task without 'add' word
	private Command _confirmedCommand;
	
	// String of current command (use only if command is confirmed)
	private String _currCmd;
	
	// two booleans for detecting whether the keywords "by" (deadline = true)
	// or if a timing has been detected (event = true)
	private boolean _isDeadline = false;
	private boolean _isEvent = false;
	
	public SuggestCommand(Command confirmed, ArrayList<String> possibleCommands, String currCmd) {
		_confirmedCommand = confirmed;
		_possibleCommands = possibleCommands;
		_currCmd = currCmd;
	}
	
	public SuggestCommand(Command confirmed, ArrayList<String> possibleCommands) {
		_confirmedCommand = confirmed;
		_possibleCommands = possibleCommands;
		_currCmd = null;
	}
	
	public void setIsDeadline(boolean isDeadline) {
		_isDeadline = isDeadline;
	}
	
	public void setIsEvent(boolean isEvent) {
		_isEvent = isEvent;
	}
	
	public Command getConfirmedCommand() {
		return _confirmedCommand;
	}
	
	public ArrayList<String> getPossibleCommands() {
		return _possibleCommands;
	}
	
	public String getCurrCmd() {
		return _currCmd;
	}
	
	public boolean isDeadline() {
		return _isDeadline;
	}
	
	public boolean isEvent() {
		return _isEvent;
	}
	
}
