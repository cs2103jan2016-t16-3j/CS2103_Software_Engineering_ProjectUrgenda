//@@author A0127764X
package urgenda.parser.commandParser;

import urgenda.command.*;

public class ShowArchiveCommandParser {
	private static String _argsString;
	private static int _index;

	/**
	 * public constructor of ShowArchiveCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public ShowArchiveCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate ShowArchive object
	 * 
	 * @return ShowArchive object with parsed details stored in its attributes
	 */
	public static Command generateAndReturn() {
		if (_argsString == null) {
			ShowArchive showArchiveCommand = new ShowArchive();
			return showArchiveCommand;
		} else {
			return new Invalid();
		}
	}
}
