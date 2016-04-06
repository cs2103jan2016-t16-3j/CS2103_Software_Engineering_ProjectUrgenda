//@@author A0127358Y
package urgenda.command;

import urgenda.logic.LogicData;

public class ShowArchive extends Command {
	
	private static final String MESSAGE_SHOWING_ARCHIVE = "Showing all archived tasks";

	public String execute() {
		LogicData data = LogicData.getInstance();
		data.setCurrState(LogicData.DisplayState.ARCHIVE);
		data.clearShowMoreTasks();
		return MESSAGE_SHOWING_ARCHIVE;
	}

}
