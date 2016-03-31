package urgenda.util;

import java.util.ArrayList;

public class SuggestCommand {

	public enum Command {
		ADD, BLOCK, DONE, CONFIRM, DELETE, EXIT, FIND_FERE, HELP, HOME, EDIT,
		POSTPONE, PRIORITISE, REDO, SEARCH, SAVETO, ARCHIVE, SHOWMORE, UNDO
	}
	// AL of possible FULL versions of command IN STRING (since each type has diff variations)
	private ArrayList<String> _possibleCommands;
	
	// command type if confirmed (return null if AL is used) / both null if is add task without 'add' word
	private Command _confirmedCommand;
	
	// String of current command (use only if command is confirmed)
	private String _currCmd;
	
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
	
	public Command getConfirmedCommand() {
		return _confirmedCommand;
	}
	
	public ArrayList<String> getPossibleCommands() {
		return _possibleCommands;
	}
	
	public String getCurrCmd() {
		return _currCmd;
	}
	
}
