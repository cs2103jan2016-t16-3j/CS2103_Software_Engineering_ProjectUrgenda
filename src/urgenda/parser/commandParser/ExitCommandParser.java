package urgenda.parser.commandParser;

import urgenda.command.*;

public class ExitCommandParser {
	private static String _argsString;
	private static int _index;
	
	public ExitCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		if (_argsString.equals("")) {
			Exit exitCommand = new Exit();
			return exitCommand;
		} else {
			return new Invalid();
		}
	}
}
