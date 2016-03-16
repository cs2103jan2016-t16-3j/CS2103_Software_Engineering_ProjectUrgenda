package urgenda.command;

import urgenda.logic.LogicData;

public class Undo extends Command {
	
	private static final String MESSAGE_UNDO = "Undo: ";

	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		return MESSAGE_UNDO;
	}

}
