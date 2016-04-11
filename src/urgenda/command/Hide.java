//@@author A0127358Y
package urgenda.command;

import urgenda.logic.LogicData;

/**
 * Hide is the command object used for minimising app window of Urgenda.
 *
 */
public class Hide extends Command {

	/**
	 * Execute command of hide which minimises/hides Urgenda.
	 */
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.HIDE);
		return null;
	}

}
