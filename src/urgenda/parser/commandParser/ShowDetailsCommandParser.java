package urgenda.parser.commandParser;

import urgenda.command.*;

public class ShowDetailsCommandParser {
	private static String _argsString;
	private static int _index;
	
	public ShowDetailsCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		if (_argsString.equals(null)) {
			ShowDetails showDetailCommand = new ShowDetails();
			showDetailCommand.setPosition(_index);
			return showDetailCommand;
		} else {
			return new Invalid();
		}
	}
}
