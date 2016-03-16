package urgenda.parser.commandParser;

import urgenda.command.*;

public class ShowArchiveCommandParser {
	private static String _argsString;
	private static int _index;
	
	public ShowArchiveCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		if (_argsString.equals("")) {
			ShowArchive showArchiveCommand = new ShowArchive();
			return showArchiveCommand;
		} else {
			return new Invalid();
		}
	}
}
