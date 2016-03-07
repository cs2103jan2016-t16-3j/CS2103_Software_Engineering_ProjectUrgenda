package urgenda.command;

import urgenda.logic.LogicData;

public class Exit implements Command {

	@Override
	public String execute(LogicData data) {
		data.setCurrState(LogicData.DisplayState.EXIT);
		return null;
	}

}
