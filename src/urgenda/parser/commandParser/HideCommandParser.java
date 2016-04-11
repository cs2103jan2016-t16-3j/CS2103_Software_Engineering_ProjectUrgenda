//@@author A0127764X
package urgenda.parser.commandParser;

import urgenda.command.Command;
import urgenda.command.Hide;
import urgenda.command.Invalid;

public class HideCommandParser {
	private static String _argsString;
	private static int _index;

	/**
	 * public constructor of HideCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public HideCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate Hide object
	 * 
	 * @return Hide object with parsed details stored in its attributes
	 */
	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Hide();
		} else {
			return new Invalid();
		}
	}
}
