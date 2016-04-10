//@@author A0127764X
package urgenda.parser.commandParser;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

import urgenda.command.Command;
import urgenda.command.Confirm;
import urgenda.command.Invalid;
import urgenda.parser.DateTimeParser;
import urgenda.parser.PublicFunctions;

public class ConfirmCommandParser {
	private static String _argsString;
	private static int _index;

	public ConfirmCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			_argsString = PublicFunctions.reformatArgsString(_argsString).trim();
			Confirm confirmCommand = new Confirm();
			String firstWord = PublicFunctions.getFirstWord(_argsString);
			try {
				tryParsePassedInIndex(confirmCommand, firstWord);
			} catch (Exception e) {
				confirmCommand.setId(_index);
			}
			if (_argsString != null) {
				PrettyTimeParser parser = new PrettyTimeParser();
				List<Date> dateTimes = parser.parse(_argsString);
				if (dateTimes.size() == 2) {
					return setTimeAndReturn(confirmCommand, dateTimes);
				} else {
					return new Invalid();
				}
			} else {
				return new Invalid();
			}

		}
	}

	private static Command setTimeAndReturn(Confirm confirmCommand, List<Date> dateTimes) {
		LocalDateTime time1 = DateTimeParser.getLocalDateTimeFromDate(dateTimes.get(0));
		LocalDateTime time2 = DateTimeParser.getLocalDateTimeFromDate(dateTimes.get(1));
		confirmCommand.setTimeSlot(PublicFunctions.minTime(time1, time2), PublicFunctions.maxTime(time1, time2));
		return confirmCommand;
	}

	private static void tryParsePassedInIndex(Confirm confirmCommand, String firstWord) {
		int index = Integer.parseInt(firstWord);
		confirmCommand.setId(index - 1);
		_argsString = PublicFunctions.removeFirstWord(_argsString);
	}
}
