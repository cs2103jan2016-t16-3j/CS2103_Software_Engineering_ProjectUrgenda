package urgenda.parser.commandParser;

import urgenda.command.*;

public class DeleteCommandParser {
	private String _argsString;
	private int _index;
	
	public DeleteCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		return new Invalid();
	}
}
