package urgenda.parser;

import urgenda.command.*;
import urgenda.parser.PublicVariables.*;

public class AddCommandParser {
	private static String _argsString;
	private static int _index;
	
	public AddCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			return new Invalid();
		}
	}
}

