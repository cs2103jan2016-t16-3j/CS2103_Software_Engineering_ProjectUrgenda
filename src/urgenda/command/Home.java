package urgenda.command;

import urgenda.logic.LogicData;

public class Home extends Command {
	
	private static final String MESSAGE_HOME = "Showing all tasks";

	public String execute(LogicData data) {
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		return MESSAGE_HOME;
	}

}
