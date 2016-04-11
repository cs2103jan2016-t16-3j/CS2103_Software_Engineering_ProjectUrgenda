//@@author A0127764X
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

	/**
	 * public constructor of FindFreeCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public FindFreeCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate FindFree object
	 * 
	 * @return FindFree object with parsed details stored in its attributes
	 */
	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			_argsString = PublicFunctions.reformatArgsString(_argsString).trim();
			DateTimeParser.searchTaskTimes(_argsString);
			if (hasValidStartAndEndTime()) {
				FindFree findFreeCommand = new FindFree(PublicVariables.taskStartTime, PublicVariables.taskEndTime);
				return findFreeCommand;
			} else {
				return new Invalid();
			}
		}
	}

	private static boolean hasValidStartAndEndTime() {
		return PublicVariables.taskStartTime != null && PublicVariables.taskEndTime != null;
	}
}
