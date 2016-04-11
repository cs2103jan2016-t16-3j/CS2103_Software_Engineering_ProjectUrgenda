//@@author A0127358Y
package urgenda.command;

import urgenda.logic.LogicData;

/**
 * Home is the command object used for returning display screen to displaying
 * all tasks.
 *
 */
public class Home extends Command {

	private static final String MESSAGE_HOME = "Showing all tasks";

	/**
	 * Execute command of Home which return user's display screen to showing all
	 * tasks.
	 */
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		data.clearShowMoreTasks();
		return MESSAGE_HOME;
	}

}
