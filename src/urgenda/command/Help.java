package urgenda.command;

import urgenda.logic.LogicData;

public class Help extends Command {	

	public String execute(LogicData data) {
		data.setCurrState(LogicData.DisplayState.HELP);
		return "";
	}

}
