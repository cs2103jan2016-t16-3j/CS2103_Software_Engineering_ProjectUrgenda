package urgenda.parser.commandParser;

import java.util.ArrayList;

import urgenda.command.*;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;

public class DeleteCommandParser {
	private static String _argsString;
	private static int _passedInIndex;

	public DeleteCommandParser(String argsString, int index) {
		_argsString = argsString;
		_passedInIndex = index;
	}

	public static Command generateAndReturn() {
		if (_argsString == null) {
			PublicVariables.positions.add(_passedInIndex);
		} else {
			String reducedArgsString = TaskDetailsParser.searchTaskIndexRange(_argsString);
			if (reducedArgsString != null) {
				PublicVariables.positions = new ArrayList<Integer>();
				TaskDetailsParser.searchTaskDescription(_argsString);
			}
		}
		
		DeleteTask deleteCommand = new DeleteTask();
		if (!PublicVariables.positions.isEmpty()) {
			deleteCommand.setPositions(PublicVariables.positions);
		} else {
			deleteCommand.setDesc(PublicVariables.taskDescription);
		}
		
		return deleteCommand;
	}
}
