//@@author A0127764X
package urgenda.parser.commandParser;

import urgenda.command.*;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;

public class ShowDetailsCommandParser {
	private static String _argsString;
	private static int _passedInIndex;

	/**
	 * public constructor of ShowDetailsCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public ShowDetailsCommandParser(String argsString, int index) {
		_argsString = argsString;
		_passedInIndex = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate ShowDetails object
	 * 
	 * @return ShowDetails object with parsed details stored in its attributes
	 */
	public static Command generateAndReturn() {
		if (_argsString == null) {
			PublicVariables.positions.add(_passedInIndex);
		} else {
			String reducedArgsString = TaskDetailsParser.searchTaskIndexRange(_argsString);
			if (reducedArgsString != null) {
				return new Invalid();
			}
		}

		ShowDetails showDetailsCommand = new ShowDetails();
		if (!PublicVariables.positions.isEmpty()) {
			showDetailsCommand.setPosition(PublicVariables.positions);
		} else {
			return new Invalid();
		}

		return showDetailsCommand;
	}
}
