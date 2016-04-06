//@@author A0127358Y
package urgenda.command;

import urgenda.logic.LogicData;

public class Hide extends Command {
	
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.HIDE);
		return null;
	}

}
