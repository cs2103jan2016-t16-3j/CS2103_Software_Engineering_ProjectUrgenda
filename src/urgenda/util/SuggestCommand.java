//@@author A0080436
package urgenda.util;

import java.util.ArrayList;

/**
 * SuggestCommand contains all the parsed details for suggestion to the user
 * input
 * 
 */
public class SuggestCommand {

	/**
	 * Command type that parser parses based on the different commands in
	 * Urgenda
	 *
	 */
	public enum Command {
		ADD, ARCHIVE, BLOCK, CONFIRM, DELETE, DEMO, DONE, EDIT, EXIT, FIND_FREE, 
		HELP, HIDE, HOME, POSTPONE, PRIORITISE, REDO, SAVETO, SEARCH, SHOWMORE, UNDO
	}

	private ArrayList<String> _possibleCommands;
	private Command _confirmedCommand;
	private String _currCmd;
	private boolean _isDeadline = false;
	private boolean _isEvent = false;

	/**
	 * Constructor of SuggestCommand that contains the command as well as
	 * possible commands if there are not confirmed commands
	 * 
	 * @param confirmed
	 *            enum type of the detected command
	 * @param possibleCommands
	 *            lists of possible command if no full command detected
	 * @param currCmd
	 *            the current version of the command used
	 */
	public SuggestCommand(Command confirmed, ArrayList<String> possibleCommands, String currCmd) {
		_confirmedCommand = confirmed;
		_possibleCommands = possibleCommands;
		_currCmd = currCmd;
	}

	/**
	 * Sets if the input given is a deadline
	 * 
	 * @param isDeadline
	 *            boolean of whether the input is a deadline
	 */
	public void setDeadline(boolean isDeadline) {
		_isDeadline = isDeadline;
	}

	/**
	 * Sets if the input given is an event
	 * 
	 * @param isEvent
	 *            boolean of whether the input is an event
	 */
	public void setEvent(boolean isEvent) {
		_isEvent = isEvent;
	}

	/**
	 * Retrieves the current confirmed command type
	 * @return Command enum of SuggestCommand
	 */
	public Command getConfirmedCommand() {
		return _confirmedCommand;
	}

	/**
	 * Retrieves the possible commands
	 * @return ArrayList of strings that are possible commands for current input
	 */
	public ArrayList<String> getPossibleCommands() {
		return _possibleCommands;
	}

	/**
	 * Retrieves the current command input
	 * @return String of the current command input by user
	 */
	public String getCurrCmd() {
		return _currCmd;
	}

	/**
	 * Gets if the suggestion has a deadline
	 * @return boolean of if it is a deadline
	 */
	public boolean isDeadline() {
		return _isDeadline;
	}

	/**
	 * Gets if the suggestion is an event
	 * @return boolean of if it is an event
	 */
	public boolean isEvent() {
		return _isEvent;
	}

}
