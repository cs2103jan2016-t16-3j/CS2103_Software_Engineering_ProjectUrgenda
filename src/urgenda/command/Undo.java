//@@author A0080436J
package urgenda.command;

import urgenda.logic.LogicData;

/**
 * Undo command for the undo function of Urgenda.
 *
 */
public class Undo extends Command {

	private static final String MESSAGE_UNDO = "Undo: ";

	/**
	 * Execute method for Undo which sets the state for undoing of command.
	 */
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		return MESSAGE_UNDO;
	}

}
