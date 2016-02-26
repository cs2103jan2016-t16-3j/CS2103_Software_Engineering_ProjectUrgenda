package urgenda.command;

import urgenda.logic.LogicData;

public class Undo implements Command {

	@Override
	public String execute(LogicData data) {
		return data.undoCommand();
	}

}
