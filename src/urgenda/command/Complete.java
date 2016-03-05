package urgenda.command;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class Complete implements Undoable {
	
	// for undo of completed task
	private String _desc;
	private Integer _id;
	
	private Task completedTask;
	private LogicData _data;
	
	@Override
	public String execute(LogicData data) {
		_data = data;
		// TODO complete with search algorithm
		return null;
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


	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

}
