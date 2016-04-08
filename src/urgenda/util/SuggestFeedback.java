//@@author A0131857B
package urgenda.util;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class SuggestFeedback {

	private ArrayList<String> _suggestions;
	private String _currCmd;
	private String _userInstructionsPrompt;
	// isCommand is true only if the entire command is detected. else is either
	// partial commands/add
	private boolean _isCommand = false;
	private boolean _isSuggestion = false;

	/**
	 * Creates a SuggestFeedback instance with the suggestions to be shown in
	 * the suggestions popup, for which a complete command is detected.
	 * 
	 * @param suggestions
	 *            array of suggestion text formats
	 * 
	 * @param currCmd
	 *            complete command word detected
	 * 
	 * @param isCommand
	 *            boolean whether a completed command word is detected or not
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
	 * Creates a SuggestFeedback instance with the suggestions to be shown in the suggestions popup, for which a complete command is not yet detected
	 * 
	 * @param suggestions array of suggestion text formats
	 * 
	 * @param isCommand boolean whether a completed command word is detected or not
	 */
	public SuggestFeedback(ArrayList<String> suggestions, boolean isCommand) {
		_suggestions = suggestions;
		_isCommand = isCommand;
		_currCmd = null;
		_userInstructionsPrompt = suggestions.get(suggestions.size() - 1);
		_suggestions.remove(_suggestions.size() - 1);
	}

	/**
	 * Returns the suggestion text array.
	 * 
	 * @return the suggestion text array
	 */
	public ArrayList<String> getSuggestions() {
		return _suggestions;
	}

	/**
	 * Returns the detected complete command word.
	 * 
	 * @return the detected complete command word
	 */
	public String getCurrCmd() {
		return _currCmd;
	}

	/**
	 * Returns the boolean whether a complete command word is detected or not.
	 * 
	 * @return boolean whether a complete command word is detected or not
	 */
	public boolean isCommand() {
		return _isCommand;
	}

	/**
	 * Returns the user prompt message to guide the user on the format.
	 * 
	 * @return the user prompt message to guide the user on the format
	 */
	public String getUserInstructionsPrompt() {
		return _userInstructionsPrompt;
	}

	/**
	 * Sets whether the array of suggestion texts are for complete command words or not.
	 * 
	 * @param isSuggestion boolean whether the array of suggestion texts are for complete command words or not
	 */
	public void setIsSuggestion(boolean isSuggestion) {
		_isSuggestion = isSuggestion;
	}

	/**
	 * Returns whether the suggestion texts are for complete command words or not.
	 * 
	 * @return boolean whether the suggestion texts are for complete command words or not
	 */
	public boolean isSuggestion() {
		return _isSuggestion;
	}
}
