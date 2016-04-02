package urgenda.util;

import java.util.ArrayList;

public class SuggestFeedback {

	private ArrayList<String> _suggestions;
	private String _currCmd;
	// isCommand is true only if the entire command is detected. else is either partial commands/add 
	private boolean _isCommand;
	
	public SuggestFeedback(ArrayList<String> suggestions, String currCmd, boolean isCommand) {
		_suggestions = suggestions;
		_currCmd = currCmd;
		_isCommand = isCommand;
	}
	
	public SuggestFeedback(ArrayList<String> suggestions, boolean isCommand) {
		_suggestions = suggestions;
		_isCommand = isCommand;
		_currCmd = null;
	}

	public ArrayList<String> getSuggestions() {
		return _suggestions;
	}
	
	public String getCurrCmd() {
		return _currCmd;
	}

	public boolean isCommand() {
		return _isCommand;
	}
	
}
