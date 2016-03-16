package urgenda.command;

import urgenda.logic.LogicData;

public class Redo extends Command {
	
	private static final String MESSAGE_REDO = "Redo: ";

	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		return MESSAGE_REDO;
	}

}
