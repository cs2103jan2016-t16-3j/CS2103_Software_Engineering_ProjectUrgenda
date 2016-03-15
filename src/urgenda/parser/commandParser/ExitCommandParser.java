package urgenda.parser.commandParser;

import urgenda.command.*;

public class ExitCommandParser {
	private String _argsString;
	private int _index;
	
	public ExitCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		return new Invalid();
	}
}
