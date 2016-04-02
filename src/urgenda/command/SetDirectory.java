package urgenda.command;

import urgenda.logic.LogicData;
import urgenda.util.StorageException;

public class SetDirectory extends Command {
	
	private static final String MESSAGE_CHANGED_DIRECTORY = "Your tasks will now be saved in \"%1$s\"";
	private static final String MESSAGE_INVALID_DIRECTORY = "Invalid file directory";
	
	private String _newPath;
	
	public SetDirectory(String path) {
		_newPath = path;
	}

	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		if (_newPath == null || _newPath.equals("")) {
			return MESSAGE_INVALID_DIRECTORY;
		}
		try {
			data.changeDirectory(_newPath);
		} catch (StorageException e) {
			data.reinitialiseStorage();
			return e.getMessage();
		}
		return String.format(MESSAGE_CHANGED_DIRECTORY, _newPath.toUpperCase());
	}
	
	public void setPath(String path) {
		_newPath = path;
	}

}
