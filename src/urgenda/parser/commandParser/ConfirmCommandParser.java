//@@author A0127764X
package urgenda.parser.commandParser;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import urgenda.command.Command;
import urgenda.command.Confirm;
import urgenda.command.Invalid;
import urgenda.parser.DateTimeParser;
import urgenda.parser.PublicFunctions;

public class ConfirmCommandParser {
	private static String _argsString;
	private static int _index;

	/**
	 * public constructor of ConfirmCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public ConfirmCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate Confirm object
	 * 
	 * @return Confirm object with parsed details stored in its attributes
	 */
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
