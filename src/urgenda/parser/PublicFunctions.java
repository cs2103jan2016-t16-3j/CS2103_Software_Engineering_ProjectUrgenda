package urgenda.parser;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import urgenda.parser.PublicVariables.*;
import urgenda.util.MultipleSlot;

public class PublicFunctions {
	public static String getFirstWord(String commandString) {
		return commandString.split("\\s+")[0].toLowerCase();
	}

	public static String removeFirstWord(String commandString) {
		try {
			String removedFirstWord = commandString.split("\\s+", 2)[1];
			Matcher matcher = Pattern.compile("\\s+").matcher(removedFirstWord);
			while (matcher.find()) {
				removedFirstWord = removedFirstWord.replace(matcher.group(), " ");
			}
			return removedFirstWord.trim().toLowerCase();
		} catch (Exception e) {
			return null;
		}
	}

	public static void reinitializePublicVariables() {
		PublicVariables.commandType = COMMAND_TYPE.INVALID;
		PublicVariables.taskIndex = -10;
		PublicVariables.positions = new ArrayList<Integer>();
		PublicVariables.taskDescription = "";
		PublicVariables.taskLocation = "";
		PublicVariables.taskStartTime = null;
		PublicVariables.taskEndTime = null;
		PublicVariables.taskHashtags = new ArrayList<String>();
		PublicVariables.taskSlots = new MultipleSlot();
		PublicVariables.taskType = TASK_TYPE.INVALID;
		PublicVariables.taskDateTime = new ArrayList<LocalDateTime>();
		PublicVariables.taskTimeType = new ArrayList<String>();
	}

	public static String getPreceedingWord(int position, String argsString) {
		if (position == 0) {
			return "";
		} else {
			argsString = argsString.substring(0, position - 1);
			String[] splittedString = argsString.split("\\s+");
			return splittedString[splittedString.length - 1];
		}
	}

	public static String reselectString(String argsString, String removedString) {
		String temp = argsString;
		Matcher matcher = Pattern.compile(removedString).matcher(temp);
		while (matcher.find()) {
			temp = temp.replace(matcher.group(), "");
		}
		matcher = Pattern.compile("\\s+").matcher(temp);
		while (matcher.find()) {
			temp = temp.replace(matcher.group(), " ");
		}
		return temp.trim();
	}
	
	public static String reformatArgsString(String argsString) {
		String reverseDateRegex = "(((\\A|\\D)([1-9]|0[1-9]|[12][0-9]|3[01])([-/.])([1-9]|0[1-9]|1[012])(([-/.])([(19)|(20)])?\\d\\d)?(\\D|\\Z)))";
		Matcher matcher = Pattern.compile(reverseDateRegex).matcher(argsString);
		while (matcher.find()) {
			argsString = argsString.replace(matcher.group(), " " + reverseDateMonth(matcher.group()) + " ");
		}
		return argsString;
	}

	private static String reverseDateMonth(String string) {
		String[] stringArray = string.split("([-/.])");
		if (stringArray.length == 2) {
			return stringArray[1].replaceAll("\\D+", "") + "/" + stringArray[0].replaceAll("\\D+", "");
		} else if (stringArray.length == 3) {
			return stringArray[1] + "/" + stringArray[0].replaceAll("\\D+", "") + "/"
					+ stringArray[2].replaceAll("\\D+", "");
		} else {
			return string;
		}
	}
	
	public static LocalDateTime getLocalDateTimeFromDate(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}
	
	public static LocalDateTime minTime(LocalDateTime time1, LocalDateTime time2) {
		if (time1.isAfter(time2)) {
			return time2;
		} else {
			return time1;
		}
	}
	
	public static LocalDateTime maxTime(LocalDateTime time1, LocalDateTime time2) {
		if (time1.isAfter(time2)) {
			return time1;
		} else {
			return time2;
		}
	}
	
	public static int getNumberOfWords(String string) {
		if (string == null) {
			return 0;
		} else {
			String[] array = string.split("\\s+");
			return array.length;
		}
	}
	
	public static String getLastWord(String string) {
		if (string == null) {
			return null;
		} else {
			String[] array = string.split("\\s+");
			return array[array.length-1].trim();
		}
	}
}
