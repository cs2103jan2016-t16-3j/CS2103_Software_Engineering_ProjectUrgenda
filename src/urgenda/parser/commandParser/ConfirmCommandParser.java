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
		Confirm confirmCommand = new Confirm();
		String firstWord = PublicFunctions.getFirstWord(_argsString);
		String remainingPhrase = PublicFunctions.removeFirstWord(_argsString);

		PrettyTimeParser parser = new PrettyTimeParser();
		List<Date> dateTimes = parser.parse(remainingPhrase);

		if (dateTimes.size() == 2) {
			try {
				int index = Integer.parseInt(firstWord);
				confirmCommand.setId(index - 1);
				
				LocalDateTime time1 = DateTimeParser.getLocalDateTimeFromDate(dateTimes.get(0));
				LocalDateTime time2 = DateTimeParser.getLocalDateTimeFromDate(dateTimes.get(1));
				confirmCommand.setTimeSlot(PublicFunctions.minTime(time1, time2), PublicFunctions.maxTime(time1, time2));				
				return confirmCommand;
			} catch (Exception e) {
				return new Invalid();
			}
		} else {
			dateTimes = parser.parse(_argsString);
			if (dateTimes.size() == 2) {
				confirmCommand.setId(_index);
				
				LocalDateTime time1 = DateTimeParser.getLocalDateTimeFromDate(dateTimes.get(0));
				LocalDateTime time2 = DateTimeParser.getLocalDateTimeFromDate(dateTimes.get(1));
				confirmCommand.setTimeSlot(PublicFunctions.minTime(time1, time2), PublicFunctions.maxTime(time1, time2));				
				return confirmCommand;
			} else {
				return new Invalid();
			}
		}
	}
}
