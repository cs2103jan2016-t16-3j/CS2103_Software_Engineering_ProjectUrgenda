package urgenda.command;

import urgenda.logic.LogicData;

public class Redo implements Command {

	@Override
	public String execute(LogicData data) {
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		return data.redoCommand();
	}

}
