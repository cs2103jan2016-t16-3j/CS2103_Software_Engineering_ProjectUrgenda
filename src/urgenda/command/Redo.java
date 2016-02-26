package urgenda.command;

import urgenda.logic.LogicData;

public class Redo implements Command {

	@Override
	public String execute(LogicData data) {
		return data.redoCommand();
	}

}
