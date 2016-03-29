package urgenda.parser.commandParser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import urgenda.command.*;
import urgenda.parser.DateTimeParser;
import urgenda.parser.PublicVariables;
import urgenda.parser.PublicVariables.*;
import urgenda.util.Task;
import urgenda.parser.TaskDetailsParser;

public class PostponeCommandParser {
	private static String _argsString;
	private static int _index;

	private static String secondRegex = "((\\d+)( )?((second(s)?)|s|(sec(s)?)))";
	private static String minuteRegex = "((\\d+)( )?((minute(s)?)|(min(s)?)|m))";
	private static String hourRegex = "((\\d+)( )?((hour(s)?)|(hr(s)?)|h))";
	private static String dayRegex = "((\\d+)( )?([(day(s)?)|d]))";
	private static String monthRegex = "((\\d+)( )?((month(s)?)|(mth(s)?)))";
	private static String yearRegex = "((\\d+)( )?((year(s)?)|(yr(s)?)|y))";

	private static String secondString;
	private static String minuteString;
	private static String hourString;
	private static String dayString;
	private static String monthString;
	private static String yearString;
	private static String reducedArgsString;

	private static Integer second;
	private static Integer minute;
	private static Integer hour;
	private static Integer day;
	private static Integer month;
	private static Integer year;

	public PostponeCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			reinitializeVariables();
			
			reducedArgsString = _argsString;
			Matcher matcher2 = Pattern.compile(monthRegex).matcher(reducedArgsString);
			if (matcher2.find()) {
				monthString = matcher2.group();
				reducedArgsString = reducedArgsString.replace(matcher2.group(), "");
			}
			Matcher matcher = Pattern.compile(secondRegex).matcher(reducedArgsString);
			if (matcher.find()) {
				secondString = matcher.group();
				reducedArgsString = _argsString.replace(matcher.group(), "");
			}
			matcher = Pattern.compile(minuteRegex).matcher(reducedArgsString);
			if (matcher.find()) {
				minuteString = matcher.group();
				reducedArgsString = reducedArgsString.replace(matcher.group(), "");
			}
			matcher = Pattern.compile(hourRegex).matcher(reducedArgsString);
			if (matcher.find()) {
				hourString = matcher.group();
				reducedArgsString = reducedArgsString.replace(matcher.group(), "");
			}
			matcher = Pattern.compile(dayRegex).matcher(reducedArgsString);
			if (matcher.find()) {
				dayString = matcher.group();
				reducedArgsString = reducedArgsString.replace(matcher.group(), "");
			}
			matcher = Pattern.compile(yearRegex).matcher(reducedArgsString);
			if (matcher.find()) {
				yearString = matcher.group();
				reducedArgsString = reducedArgsString.replace(matcher.group(), "");
			}

			if (reducedArgsString.trim().equals("")) {
				return new Invalid();
			} else {
				Postpone postponeCommand = new Postpone();
				try {
					if (secondString != null) {
						postponeCommand.setSecond(getIntPart(secondString));
					}
					if (minuteString != null) {
						postponeCommand.setMinute(getIntPart(minuteString));
					}
					if (hourString != null) {
						postponeCommand.setHour(getIntPart(hourString));
					}
					if (dayString != null) {
						postponeCommand.setDay(getIntPart(dayString));
					}
					if (monthString != null) {
						postponeCommand.setMonth(getIntPart(monthString));
					}
					if (yearString != null) {
						postponeCommand.setYear(getIntPart(yearString));
					}
					postponeCommand.setId(_index);
					System.out.print("sencond: " + secondString + "\n");
					System.out.print("minute: " + minuteString + "\n");
					System.out.print("hour: " + hourString + "\n");
					System.out.print("day: " + dayString + "\n");
					System.out.print("month: " + monthString + "\n");
					System.out.print("year: " + yearString + "\n");
					return postponeCommand;
				} catch (Exception e) {
					return new Invalid();
				}
			}
		}
	}

	private static int getIntPart(String string) {
		string = string.replaceAll("\\D+", "");
		return Integer.parseInt(string);
	}

	private static void reinitializeVariables() {
		second = null;
		minute = null;
		hour = null;
		day = null;
		month = null;
		year = null;
		secondString = null;
		minuteString = null;
		hourString = null;
		dayString = null;
		monthString = null;
		yearString = null;
		reducedArgsString = null;
	}
}
