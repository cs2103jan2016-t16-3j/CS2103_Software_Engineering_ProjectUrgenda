//@@author A0127358Y
package urgenda.command;

import urgenda.logic.LogicData;

public class Home extends Command {
	
	private static final String MESSAGE_HOME = "Showing all tasks";

	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		data.clearShowMoreTasks();
		return MESSAGE_HOME;
	}

}
