//@@author A0127764X
package urgenda.parser.commandParser;

import urgenda.command.Command;
import urgenda.command.Demo;
import urgenda.command.Invalid;

public class DemoCommandParser {
	private static String _argsString;
	private static int _index;

	/**
	 * public constructor of DemoCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public DemoCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate Demo object
	 * 
	 * @return Demo object with parsed details stored in its attributes
	 */
	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Demo();
		} else {
			return new Invalid();
		}

	}
}
