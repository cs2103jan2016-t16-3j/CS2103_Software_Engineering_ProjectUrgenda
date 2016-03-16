package urgenda.parser.commandParser;

import urgenda.command.*;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;

public class CompleteCommandParser {
	private static String _argsString;
	private static int _index;

	public CompleteCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString.equals("")) {
			PublicVariables.taskIndex = _index;
		} else {
			TaskDetailsParser.searchTaskIndex(_argsString);
			TaskDetailsParser.searchTaskDescription(_argsString);
		}
		
		Complete completeCommand = new Complete();
		if (PublicVariables.taskIndex != -10) {
			completeCommand.setId(PublicVariables.taskIndex);
		} else if (PublicVariables.taskDescription.equals("")){
			completeCommand.setId(_index);
		}
		
		if (PublicVariables.taskDescription.equals("")) {
			completeCommand.setDesc(PublicVariables.taskDescription);
		}
		
		return completeCommand;
	}
}
