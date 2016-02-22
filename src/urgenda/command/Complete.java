package urgenda.command;

import urgenda.util.Task;

public class Complete implements CommandInterface {
	
	// for undo of completed task
	private Task completedTask;

	@Override
	public String execute() {
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
