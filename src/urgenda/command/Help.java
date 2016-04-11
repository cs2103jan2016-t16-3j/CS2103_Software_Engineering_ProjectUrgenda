//@@author A0127358Y
package urgenda.command;

import urgenda.logic.LogicData;

/**
 * Help is the command object used for generating help (user manual) of Urgenda.
 *
 */
public class Help extends Command {

	/**
	 * Execute command of Help which set current state of system to HELP.state
	 * and hence indicates to display help guide to user.
	 */
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.HELP);
		return "";
	}

}
