package urgenda.parser.commandParser;

import urgenda.command.Command;
import urgenda.command.SetDirectory;

public class SetDirectoryCommandParser {
	private static String _argsString;
	private static int _index;

	public SetDirectoryCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		SetDirectory setDirectoryCommand = new SetDirectory(_argsString);
		return setDirectoryCommand;
	}
}
