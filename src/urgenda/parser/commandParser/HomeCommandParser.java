package urgenda.parser.commandParser;

import urgenda.command.Command;
import urgenda.command.Home;
import urgenda.command.Invalid;

public class HomeCommandParser {
	private static String _argsString;
	private static int _index;

	public HomeCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		if (_argsString != null) {
			return new Invalid();
		} else {
			return new Home();
		}
	}
}
