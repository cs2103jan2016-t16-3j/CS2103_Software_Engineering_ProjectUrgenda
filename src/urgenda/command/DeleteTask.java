package urgenda.command;

import urgenda.util.Task;

public class DeleteTask implements CommandInterface {

	// to store from deletion, so that undo can be done
	private Task deletedTask;
	
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
