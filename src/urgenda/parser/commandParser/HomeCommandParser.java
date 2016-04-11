//@@author A0127764X
package urgenda.parser.commandParser;

import urgenda.command.Command;
import urgenda.command.Home;
import urgenda.command.Invalid;

public class HomeCommandParser {
	private static String _argsString;
	private static int _index;

	/**
	 * public constructor of HomeCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public HomeCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate Home object
	 * 
	 * @return Home object with parsed details stored in its attributes
	 */
	public static Command generateAndReturn() {
		if (_argsString != null) {
			return new Invalid();
		} else {
			return new Home();
		}
	}
}
