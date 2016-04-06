//@@author A0127358Y
package urgenda.command;

import urgenda.logic.LogicData;

public class Exit extends Command {

	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.EXIT);
		return null;
	}

}
