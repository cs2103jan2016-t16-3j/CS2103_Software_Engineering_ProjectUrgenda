//@@author A0127764X
package urgenda.parser.commandParser;

import urgenda.command.*;

public class InvalidCommandParser {
	private String _argsString;
	private int _index;

	/**
	 * public constructor of InvalidCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public InvalidCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	/**
	 * function that returns an Invalid object
	 * 
	 * @return Invalid object
	 */
	public static Command generateAndReturn() {
		return new Invalid();
	}
}
