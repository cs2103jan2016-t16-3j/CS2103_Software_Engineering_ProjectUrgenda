package urgenda.parser.commandParser;

import urgenda.command.*;

public class InvalidCommandParser {
	private String _argsString;
	private int _index;
	
	public InvalidCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		return new Invalid();
	}
}
