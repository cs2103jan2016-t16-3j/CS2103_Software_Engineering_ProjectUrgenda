package urgenda.parser.commandParser;

import urgenda.command.*;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;

public class ShowDetailsCommandParser {
	private static String _argsString;
	private static int _index;
	
	public ShowDetailsCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		if (_argsString == null) {
			ShowDetails showDetailCommand = new ShowDetails();
			showDetailCommand.setPosition(_index);
			return showDetailCommand;
		} else {
			String reducedArgsString = TaskDetailsParser.searchTaskIndex(_argsString);
			if (reducedArgsString != null) {
				return new Invalid();
			} else {
				ShowDetails showDetailCommand = new ShowDetails();
				showDetailCommand.setPosition(PublicVariables.taskIndex);
				return showDetailCommand;
			}
		}
	}
}

