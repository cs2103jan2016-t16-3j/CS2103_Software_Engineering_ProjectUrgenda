package urgenda.parser.commandParser;

import urgenda.command.Command;
import urgenda.command.FindFree;
import urgenda.command.Invalid;
import urgenda.parser.DateTimeParser;
import urgenda.parser.PublicFunctions;
import urgenda.parser.PublicVariables;

public class FindFreeCommandParser {
	private static String _argsString;
	private static int _index;

	public FindFreeCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			_argsString = PublicFunctions.reformatArgsString(_argsString);
			DateTimeParser.searchTaskTimes(_argsString);
			if (PublicVariables.taskStartTime != null && PublicVariables.taskEndTime != null) {
				FindFree findFreeCommand = new FindFree(PublicVariables.taskStartTime, PublicVariables.taskEndTime);
				return findFreeCommand;
			} else {
				return new Invalid();
			}
		}
	}
}
