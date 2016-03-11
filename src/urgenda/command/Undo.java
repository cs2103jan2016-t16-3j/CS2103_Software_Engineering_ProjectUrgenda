package urgenda.command;

import urgenda.logic.LogicData;

public class Undo implements Command {
	
	private static final String MESSAGE_UNDO = "Undo: ";

	@Override
	public String execute(LogicData data) {
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		return MESSAGE_UNDO;
	}

}
