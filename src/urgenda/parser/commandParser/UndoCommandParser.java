//@@author A0127764X
package urgenda.parser.commandParser;

import urgenda.command.*;

public class UndoCommandParser {
	private static String _argsString;
	private static int _index;

	/**
	 * public constructor of UndoCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public UndoCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate Undo object
	 * 
	 * @return Undo object with parsed details stored in its attributes
	 */
	public static Command generateAndReturn() {
		if (_argsString == null) {
			Undo undoCommand = new Undo();
			return undoCommand;
		} else {
			return new Invalid();
		}
	}
}
