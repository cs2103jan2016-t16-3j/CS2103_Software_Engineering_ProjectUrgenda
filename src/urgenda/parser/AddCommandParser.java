package urgenda.parser;

import urgenda.command.*;

public class AddCommandParser {
	private String _argsString;
	private int _index;
	
	public AddCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		return new Invalid();
	}
}

