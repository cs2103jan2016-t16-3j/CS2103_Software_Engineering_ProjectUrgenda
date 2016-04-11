//@@author A0127358Y
package urgenda.command;

import urgenda.logic.LogicData;

/**
 * 
 * Demo is the command object used for running a short demo in Urgenda.
 *
 */
public class Demo extends Command {

	/**
	 * Execute command of demo which runs a short demo version of Urgenda.
	 */
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.DEMO);
		data.clearShowMoreTasks();
		return null;
	}

}
