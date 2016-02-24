package urgenda.command;

import urgenda.logic.LogicData;

public class Undo implements Command {

	@Override
	public String execute(LogicData data) {
		return data.undoCommand();
	}

	@Override
	public void getDetails(String[] details) {
		// TODO Auto-generated method stub

	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String redo() {
		// TODO Auto-generated method stub
		return null;
	}

}
