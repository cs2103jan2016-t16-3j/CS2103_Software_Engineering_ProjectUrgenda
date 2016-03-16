package urgenda.parser.commandParser;

import urgenda.command.*;

public class UndoCommandParser {
	private static String _argsString;
	private static int _index;
	
	public UndoCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		if (_argsString.equals("")) {
			Undo undoCommand = new Undo();
			return undoCommand;
		} else {
			return new Invalid();
		}
	}
}
