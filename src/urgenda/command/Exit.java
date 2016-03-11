package urgenda.command;

import urgenda.logic.LogicData;

public class Exit extends Command {

	public String execute(LogicData data) {
		data.setCurrState(LogicData.DisplayState.EXIT);
		return null;
	}

}
