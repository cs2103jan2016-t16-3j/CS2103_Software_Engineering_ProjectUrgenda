package urgenda.command;

import urgenda.logic.LogicData;

public class Demo extends Command {

	private static final String MESSAGE_DEMO = "Displaying Demo for Urgenda";
	
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.DEMO);
		data.clearShowMoreTasks();
		return MESSAGE_DEMO;
	}

}
