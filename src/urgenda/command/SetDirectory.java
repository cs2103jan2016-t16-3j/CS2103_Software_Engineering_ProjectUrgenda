package urgenda.command;

import urgenda.logic.LogicData;

public class SetDirectory extends Command {
	
	private static final String MESSAGE_CHANGED_DIRECTORY = "Your tasks will now be saved in \"%1$s\"";
	
	private String _newPath;
	
	public SetDirectory(String path) {
		_newPath = path;
	}

	public String execute() {
		LogicData data = LogicData.getInstance();
		data.changeDirectory(_newPath);
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		return String.format(MESSAGE_CHANGED_DIRECTORY, _newPath);
	}
	
	public void setPath(String path) {
		_newPath = path;
	}

}
