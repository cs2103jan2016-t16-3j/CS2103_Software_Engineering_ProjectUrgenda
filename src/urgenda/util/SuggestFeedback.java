//@@author A0131857B
package urgenda.util;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class SuggestFeedback {

	private ArrayList<String> _suggestions;
	private String _currCmd;
	private String _userInstructionsPrompt;
	// isCommand is true only if the entire command is detected. else is either partial commands/add 
	private boolean _isCommand = false;
	private boolean _isSuggestion = false;
	
	/**
	 * TODO
	 * @param suggestions
	 * @param currCmd
	 * @param isCommand
	 */
	public SuggestFeedback(ArrayList<String> suggestions, String currCmd, boolean isCommand) {
		_currCmd = currCmd;
		_isCommand = isCommand;
		_suggestions = new ArrayList<String>();
		if (isCommand) {
			StringTokenizer tk = new StringTokenizer(suggestions.get(0), "|");
			while (tk.hasMoreElements()) {
				String next = tk.nextElement().toString();
				_suggestions.add(next);
			}
			_userInstructionsPrompt = suggestions.get(1);
		}
	}
	
	/**
	 * TODO
	 * @param suggestions
	 * @param isCommand
	 */
	public SuggestFeedback(ArrayList<String> suggestions, boolean isCommand) {
		_suggestions = suggestions;
		_isCommand = isCommand;
		_currCmd = null;
		_userInstructionsPrompt = suggestions.get(suggestions.size() - 1);
		_suggestions.remove(_suggestions.size() - 1);
	}

	/**
	 * TODO
	 * @return
	 */
	public ArrayList<String> getSuggestions() {
		return _suggestions;
	}
	
	/**
	 * TODO
	 * @return
	 */
	public String getCurrCmd() {
		return _currCmd;
	}

	/**
	 * TODO
	 * @return
	 */
	public boolean isCommand() {
		return _isCommand;
	}
	
	/**
	 * TODO
	 * @return
	 */
	public String getUserInstructionsPrompt() {
		return _userInstructionsPrompt;
	}
	
	/**
	 * TODO
	 * @param isSuggestion
	 */
	public void setIsSuggestion(boolean isSuggestion) {
		_isSuggestion = isSuggestion;
	}
	
	/**
	 * TODO
	 * @return
	 */
	public boolean isSuggestion() {
		return _isSuggestion;
	}
}
