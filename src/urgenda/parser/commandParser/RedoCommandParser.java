//@@author A0127764X
package urgenda.parser.commandParser;

import urgenda.command.*;

public class RedoCommandParser {
	private static String _argsString;
	private static int _index;

	/**
	 * public constructor of RedoCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public RedoCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate Redo object
	 * 
	 * @return Redo object with parsed details stored in its attributes
	 */
	public static Command generateAndReturn() {
		if (_argsString == null) {
			Redo redoCommand = new Redo();
			return redoCommand;
		} else {
			return new Invalid();
		}
	}
}
