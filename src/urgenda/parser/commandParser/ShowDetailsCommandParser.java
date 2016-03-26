package urgenda.parser.commandParser;

import java.util.ArrayList;

import urgenda.command.*;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;

public class ShowDetailsCommandParser {
	private static String _argsString;
	private static int _passedInIndex;
	private static ArrayList<Integer> _positions = new ArrayList<Integer>();

	public ShowDetailsCommandParser(String argsString, int index) {
		_argsString = argsString;
		_passedInIndex = index;
	}

	public static Command generateAndReturn() {
		if (_argsString == null) {
			_positions.add(_passedInIndex);
		} else {
			String reducedArgsString = TaskDetailsParser.searchTaskIndexRange(_argsString);
			if (reducedArgsString != null) {
				return new Invalid();
			}
		}
		
		ShowDetails showDetailsCommand = new ShowDetails();
		if (!PublicVariables.positions.isEmpty()) {
			showDetailsCommand.setPosition(PublicVariables.positions);
		} else {
			return new Invalid();
		}
		
		return showDetailsCommand;
	}
}
