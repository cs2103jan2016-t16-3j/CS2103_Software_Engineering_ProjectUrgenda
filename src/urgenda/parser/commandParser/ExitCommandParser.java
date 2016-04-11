//@@author A0127764X
package urgenda.parser.commandParser;

import urgenda.command.*;

public class ExitCommandParser {
	private static String _argsString;
	private static int _index;

	/**
	 * public constructor of ExitCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public ExitCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate Exit object
	 * 
	 * @return Exit object with parsed details stored in its attributes
	 */
	public static Command generateAndReturn() {
		if (_argsString == null) {
			Exit exitCommand = new Exit();
			return exitCommand;
		} else {
			return new Invalid();
		}
	}
}
