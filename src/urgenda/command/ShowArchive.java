//@@author A0127358Y
package urgenda.command;

import urgenda.logic.LogicData;

/**
 * ShowArchive is the command object used for displaying archive (list of completed tasks)
 * to the user.
 *
 */
public class ShowArchive extends Command {
	
	private static final String MESSAGE_SHOWING_ARCHIVE = "Showing all archived tasks";

	/**
	 * Execute command of ShowArchive which sets user's display screen to showing archived tasks.
	 */
	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.ARCHIVE);
		data.clearShowMoreTasks();
		return MESSAGE_SHOWING_ARCHIVE;
	}

}
