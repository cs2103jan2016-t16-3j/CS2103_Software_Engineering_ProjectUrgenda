//@@author A0127764X
package urgenda.parser.commandParser;

import urgenda.command.Command;
import urgenda.command.Demo;

public class DemoCommandParser {
	private static String _argsString;
	private static int _index;

	public DemoCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		return new Demo();
	}
}
