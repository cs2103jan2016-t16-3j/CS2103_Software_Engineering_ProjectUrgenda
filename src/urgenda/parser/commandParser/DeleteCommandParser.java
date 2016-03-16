package urgenda.parser.commandParser;

import urgenda.command.*;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;

public class DeleteCommandParser {
	private static String _argsString;
	private static int _index;
	
	public DeleteCommandParser(String argsString, int index) {
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
		
		DeleteTask deleteCommand = new DeleteTask();
		if (PublicVariables.taskIndex != -10) {
			deleteCommand.setId(PublicVariables.taskIndex);
		} else if (PublicVariables.taskDescription.equals("")){
			deleteCommand.setId(_index);
		}
		
		if (PublicVariables.taskDescription.equals("")) {
			deleteCommand.setDesc(PublicVariables.taskDescription);
		}
		
		return deleteCommand;
	}
}
