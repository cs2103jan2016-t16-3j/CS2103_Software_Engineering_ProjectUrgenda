package urgenda.parser.commandParser;

import urgenda.command.*;
import urgenda.parser.DateTimeParser;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;

public class SearchCommandParser {
	private static String _argsString;
	private static int _index;

	public SearchCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString.equals(null)) {
			return new Invalid();
		} else {
			Search searchCommand = new Search();
			if (DateTimeParser.tryParseMonth(_argsString).equals(null)) {
				searchCommand.setSearchMonth(DateTimeParser.tryParseMonth(_argsString));
			}else if (DateTimeParser.tryParseTime(_argsString).equals(null)) {
				searchCommand.setSearchDateTime(DateTimeParser.tryParseTime(_argsString));
			} else if (DateTimeParser.tryParseDate(_argsString).equals(null)) {
				searchCommand.setSearchDate(DateTimeParser.tryParseDate(_argsString));
			} else {
				TaskDetailsParser.searchTaskDescription(_argsString);
				searchCommand.setSearchInput(PublicVariables.taskDescription);
			}
			return searchCommand;
		}
	}
}
