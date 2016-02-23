package urgenda.command;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class AddTask implements Command {
	
	private Task newTask;
	
	public AddTask() {
		newTask = new Task();
	}
	
	@Override
	public String execute(LogicData data) {
		// TODO Auto-generated method stub
		return null;
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
