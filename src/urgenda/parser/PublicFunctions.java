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
		return commandString.split("\\s+")[0];
	}

	public static String removeFirstWord(String commandString) {
		try {
			String removedFirstWord = commandString.split("\\s+", 2)[1];
			Matcher matcher = Pattern.compile("\\s+").matcher(removedFirstWord);
			while (matcher.find()) {
				removedFirstWord = removedFirstWord.replace(matcher.group(), " ");
			}
			return removedFirstWord.trim();
		} catch (Exception e) {
			return null;
		}
	}

	public static void reinitializePublicVariables() {
		PublicVariables.commandType = COMMAND_TYPE.INVALID;
		PublicVariables.passedInCommandString = "";
		PublicVariables.passedInIndex = -1;
		PublicVariables.taskIndex = -10;
		PublicVariables.taskDescription = "";
		PublicVariables.taskLocation = "";
		PublicVariables.taskStartTime = null;
		PublicVariables.taskEndTime = null;
		PublicVariables.taskHashtags = new ArrayList<String>();
		PublicVariables.taskSlots = null;
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
}
