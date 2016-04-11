//@@author A0127764X
package urgenda.parser.commandParser;

import java.util.ArrayList;

import urgenda.command.*;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;

public class CompleteCommandParser {
	private static String _argsString;
	private static int _passedInIndex;

	/**
	 * public constructor of CompleteCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public CompleteCommandParser(String argsString, int index) {
		_argsString = argsString;
		_passedInIndex = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate Complete object
	 * 
	 * @return Complete object with parsed details stored in its attributes
	 */
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

		Complete completeCommand = new Complete();
		if (!PublicVariables.positions.isEmpty()) {
			completeCommand.setPositions(PublicVariables.positions);
		} else {
			completeCommand.setDesc(PublicVariables.taskDescription);
		}

		return completeCommand;
	}
}
