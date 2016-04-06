//@@author A0127358Y
package urgenda.command;

import urgenda.logic.LogicData;

/**
 * 
 * Exit is the command object used for exiting and closing of Urgenda.
 *
 */

public class Exit extends Command {

	/**
	 * Execute command of Exit which closes Urgenda.
	 */
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.EXIT);
		return null;
	}

}
