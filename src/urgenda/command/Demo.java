package urgenda.command;

import urgenda.logic.LogicData;

public class Demo extends Command {

	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.DEMO);
		data.clearShowMoreTasks();
		return null;
	}

}
