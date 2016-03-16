package urgenda.parser.commandParser;

import urgenda.command.*;

public class RedoCommandParser {
	private static String _argsString;
	private static int _index;
	
	public RedoCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		if (_argsString == null) {
			Redo redoCommand = new Redo();
			return redoCommand;
		} else {
			return new Invalid();
		}
	}
}
