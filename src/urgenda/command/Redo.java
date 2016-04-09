//@@author A0080436J
package urgenda.command;

import urgenda.logic.LogicData;

/**
 * Redo command for the redo function of Urgenda.
 *
 */
public class Redo extends Command {

	private static final String MESSAGE_REDO = "Redo: ";

	/**
	 * Execute method for Redo which sets the state for redoing of command.
	 */
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		return MESSAGE_REDO;
	}

}
