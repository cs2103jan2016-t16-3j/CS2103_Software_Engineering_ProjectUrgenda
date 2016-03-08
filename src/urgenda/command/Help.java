package urgenda.command;

import urgenda.logic.LogicData;

public class Help implements Command {	

	@Override
	public String execute(LogicData data) {
		data.setCurrState(LogicData.DisplayState.HELP);
		return "";
	}

}
