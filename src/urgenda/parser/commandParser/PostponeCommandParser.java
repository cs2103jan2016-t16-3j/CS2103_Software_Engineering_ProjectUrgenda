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
	
	private static String secondRegex = "((\\d+)( )?([(second(s)?)|s|(sec(s)?)]))";
	private static String minuteRegex = "((\\d+)( )?([(minute(s)?)|(min(s)?)|m]))";
	private static String hourRegex = "((\\d+)( )?([(hour(s)?)|(hr(s)?)|h]))";
	private static String dayRegex = "((\\d+)( )?([(day(s)?)|d]))";
	private static String monthRegex = "((\\d+)( )?([(month(s)?)|(mth(s)?)]))";
	private static String yearRegex = "((\\d+)( )?([(year(s)?)|(yr(s)?)|y]))";

	private static String secondString;
	private static String minuteString;
	private static String hourString;
	private static String dayString;
	private static String monthString;
	private static String yearString;
	private static String reducedArgsString = _argsString;
	
	private static int second;
	private static int minute;
	private static int hour;
	private static int day;
	private static int month;
	private static int year;
	
	public PostponeCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}
	
	public static Command generateAndReturn() {
		Matcher matcher = Pattern.compile(secondRegex).matcher(reducedArgsString);
		if (matcher.find()) {
			secondString = matcher.group();
			reducedArgsString = reducedArgsString.replace(matcher.group(), "");
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
		matcher = Pattern.compile(monthRegex).matcher(reducedArgsString);
		if (matcher.find()) {
			monthString = matcher.group();
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
					postponeCommand.setSecond(getIntPart(minuteString));
				}
				if (hourString != null) {
					postponeCommand.setSecond(getIntPart(hourString));
				}
				if (dayString != null) {
					postponeCommand.setSecond(getIntPart(dayString));
				}
				if (monthString != null) {
					postponeCommand.setSecond(getIntPart(monthString));
				}
				if (yearString != null) {
					postponeCommand.setSecond(getIntPart(yearString));
				}
				return postponeCommand;
			}
			catch (Exception e) {
				return new Invalid();
			}
		}
	}
	
	private static int getIntPart(String string) {
		string = string.replaceAll("\\D+", "");
		return Integer.parseInt(string);
	}
}
