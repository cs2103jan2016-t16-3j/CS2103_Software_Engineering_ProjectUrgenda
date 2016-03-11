package urgenda.command;

import urgenda.logic.LogicData;

public class Redo implements Command {
	
	private static final String MESSAGE_REDO = "Redo: ";

	@Override
	public String execute(LogicData data) {
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		return MESSAGE_REDO;
	}

}
