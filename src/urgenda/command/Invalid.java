//@@author A0080436J
package urgenda.command;

import urgenda.logic.LogicData;

/**
 * Invalid command class is the object returned when the command is not valid
 * for the current state.
 * 
 */
public class Invalid extends Command {

	private static final String MESSAGE_INVALID_COMMAND = "\"%1$s\" is not a valid command";
	private static final String MESSAGE_INVALID_FINDFREE = "Invalid command for available time";
	private static final String MESSAGE_INVALID_ARCHIVE = "Invalid command for archived task";

	private String _command;
	private LogicData.DisplayState _state;

	/**
	 * Default constructor for an empty invalid command.
	 */
	public Invalid() {

	}

	/**
	 * Constructor for invalid command with a given string input.
	 * 
	 * @param command
	 *            Invalid command given by the user.
	 */
	public Invalid(String command) {
		_command = command;
	}

	/**
	 * Constructor for invalid command when a command is done on the incorrect
	 * state Urgenda is in.
	 * 
	 * @param state
	 *            Invalid state that Urgenda is in.
	 */
	public Invalid(LogicData.DisplayState state) {
		_state = state;
	}

	/**
	 * Execute method for Invalid command.
	 */
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.INVALID_COMMAND);
		return generateFeedbackMessage();
	}

	private String generateFeedbackMessage() {
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

	/**
	 * Setter method for setting of the invalid command string.
	 * 
	 * @param command
	 *            Invalid command given by the user.
	 */
	public void setCommand(String command) {
		_command = command;
	}

}
