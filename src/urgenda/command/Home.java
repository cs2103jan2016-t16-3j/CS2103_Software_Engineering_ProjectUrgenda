package urgenda.command;

import urgenda.logic.LogicData;

public class Home implements Command {
	
	private static final String MESSAGE_HOME = "Showing all tasks";

	@Override
	public String execute(LogicData data) {
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		return MESSAGE_HOME;
	}

}
