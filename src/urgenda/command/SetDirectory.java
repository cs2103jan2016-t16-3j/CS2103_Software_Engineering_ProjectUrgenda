//@@author A0080436J
package urgenda.command;

import urgenda.logic.LogicData;
import urgenda.util.InvalidFolderException;
import urgenda.util.StorageException;

/**
 * SetDirectory Command for changing of saving directory in Urgenda.
 *
 */
public class SetDirectory extends Command {

	private static final String MESSAGE_CHANGED_DIRECTORY = "Your tasks will now be saved in \"%1$s\"";
	private static final String MESSAGE_INVALID_DIRECTORY = "Invalid file directory";

	private String _newPath;

	/**
	 * Constructor which has the new path for saving of Urgenda data files.
	 * 
	 * @param path
	 *            New path directory for saving.
	 */
	public SetDirectory(String path) {
		_newPath = path;
	}

	/**
	 * Execute method for changing of saving path directory.
	 */
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		return changeUrgendaDirectory(data);
	}

	/*
	 * Changes the saving directory.
	 */
	private String changeUrgendaDirectory(LogicData data) {
		if (_newPath == null || _newPath.equals("")) {
			return MESSAGE_INVALID_DIRECTORY;
		}
		try {
			data.changeDirectory(_newPath);
		} catch (StorageException e) {
			data.reinitialiseStorage();
			return e.getMessage();
		} catch (InvalidFolderException e) {
			return e.getMessage();
		}
		return String.format(MESSAGE_CHANGED_DIRECTORY, _newPath.toUpperCase());
	}

}
