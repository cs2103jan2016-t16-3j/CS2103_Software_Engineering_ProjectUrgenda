//@@author A0127764X
package urgenda.parser.commandParser;

import java.util.ArrayList;

import urgenda.command.*;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;

public class PrioritiseCommandParser {
	private static String _argsString;
	private static int _passedInIndex;

	/**
	 * public constructor of PrioritiseCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public PrioritiseCommandParser(String argsString, int index) {
		_argsString = argsString;
		_passedInIndex = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate Prioritise object
	 * 
	 * @return Prioritise object with parsed details stored in its attributes
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

		Prioritise priCommand = new Prioritise();
		if (!PublicVariables.positions.isEmpty()) {
			priCommand.setPositions(PublicVariables.positions);
		} else {
			priCommand.setDesc(PublicVariables.taskDescription);
		}

		return priCommand;
	}
}
