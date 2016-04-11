//@@author A0127764X
package urgenda.parser.commandParser;

import urgenda.command.Command;
import urgenda.command.Invalid;
import urgenda.command.SetDirectory;

public class SetDirectoryCommandParser {
	private static String _argsString;
	private static int _index;

	/**
	 * public constructor of SetDirectoryCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public SetDirectoryCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString != null) {
			SetDirectory setDirectoryCommand = new SetDirectory(_argsString);
			return setDirectoryCommand;
		} else {
			return new Invalid();
		}

	}
}
