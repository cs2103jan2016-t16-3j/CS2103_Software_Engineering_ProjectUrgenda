package urgenda.command;

import urgenda.logic.LogicData;

public class Help implements Command {
	
	private static final String MESSAGE_HELP = "You have requested for help. The userguide is currently being displayed";

	@Override
	public String execute(LogicData data) {
		// data.setCurrState(LogicData.DisplayState.SHOW_HELP);  new displaystate show_help to be added in future
		return MESSAGE_HELP;
	}

}
