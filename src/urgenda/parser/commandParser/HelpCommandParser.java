package urgenda.parser.commandParser;

import urgenda.command.Command;
import urgenda.command.Help;
import urgenda.command.Invalid;

public class HelpCommandParser {
	private static String _argsString;
	private static int _index;

	public HelpCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString != null) {
			return new Invalid();
		} else {
			return new Help();
		}
	}
}
