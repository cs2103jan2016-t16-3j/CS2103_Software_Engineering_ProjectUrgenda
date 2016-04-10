//@@author A0127764X
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
	private static String whiteSpaceRegex = "\\s+";
	private static String emptyString = "";
	private static String singleSpace = " ";
	private static int dummyIndex = -10;

	/**
	 * get the first word of the passed in string
	 * @param commandString the passed in string
	 * @return the passed in string's first word
	 */
	public static String getFirstWord(String commandString) {
		return commandString.split(whiteSpaceRegex)[0].toLowerCase();
	}

	/**
	 * Removes the first word from passed in string and return the rest of the string
	 * @param commandString passed in string
	 * @return passed in string without first word
	 */
	public static String removeFirstWord(String commandString) {
		try {
			String removedFirstWord = commandString.split(whiteSpaceRegex, 2)[1];
			Matcher matcher = Pattern.compile(whiteSpaceRegex).matcher(removedFirstWord);
			while (matcher.find()) {
				removedFirstWord = removedFirstWord.replace(matcher.group(), singleSpace);
			}
			return removedFirstWord.trim().toLowerCase();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Function to reinitialize the global variables stored in PublicVariables for every new parse
	 */
	public static void reinitializePublicVariables() {
		PublicVariables.commandType = COMMAND_TYPE.INVALID;
		PublicVariables.taskIndex = dummyIndex;
		PublicVariables.positions = new ArrayList<Integer>();
		PublicVariables.taskDescription = emptyString;
		PublicVariables.taskLocation = emptyString;
		PublicVariables.taskStartTime = null;
		PublicVariables.taskEndTime = null;
		PublicVariables.taskHashtags = new ArrayList<String>();
		PublicVariables.taskSlots = new MultipleSlot();
		PublicVariables.taskType = TASK_TYPE.INVALID;
		PublicVariables.taskDateTime = new ArrayList<LocalDateTime>();
		PublicVariables.taskTimeType = new ArrayList<String>();
	}

	/**
	 * Function to get the preceeding word from a certain index in a passed in string
	 * @param position the index
	 * @param argsString the passed in string
	 * @return the word right in front of the index position in the passed in string
	 */
	public static String getPreceedingWord(int position, String argsString) {
		if (position == 0 || position == -1) {
			return emptyString;
		} else {
			argsString = argsString.substring(0, position);
			String[] splittedString = argsString.split(whiteSpaceRegex);
			return splittedString[splittedString.length - 1];
		}
	}

	/**
	 * function that removes a substring from the passed in string and reformatted the string to contain only single whitespace
	 * @param argsString the passed in string
	 * @param removedString the sub string to be removed
	 * @return the reformatted string with the substring removed
	 */
	public static String reselectString(String argsString, String removedString) {
		String temp = argsString;
		Matcher matcher = Pattern.compile(removedString).matcher(temp);
		while (matcher.find()) {
			temp = temp.replace(matcher.group(), emptyString);
		}
		matcher = Pattern.compile(whiteSpaceRegex).matcher(temp);
		while (matcher.find()) {
			temp = temp.replace(matcher.group(), singleSpace);
		}
		return temp.trim();
	}

	/**
	 * function that performs a reformatting of the passed in string to switch any MMDDYYYY format substring to DDMMYYY
	 * this is needed as pretty time parse use the MMDDYYYY format, while the Singapore culture use DDMMYYYY
	 * @param argsString passed in string
	 * @return formatted string with DDMMYYYY format
	 */
	public static String reformatArgsString(String argsString) {
		String reverseDateRegexWithoutYear = "(((\\A|\\D)([1-9]|0[1-9]|[12][0-9]|3[01])([-/.])([1-9]|0[1-9]|1[012])(\\D|\\Z)))";
		String reverseDateRegexWithYear = "(((\\A|\\D)([1-9]|0[1-9]|[12][0-9]|3[01])([-/.])([1-9]|0[1-9]|1[012])(([-/.])((19)|(20))?\\d\\d)(\\D|\\Z)))";
		String reverseDateRegex = "(" + reverseDateRegexWithYear + "|" + reverseDateRegexWithoutYear + ")";
		Matcher matcher = Pattern.compile(reverseDateRegex).matcher(argsString);
		while (matcher.find()) {
			argsString = argsString.replace(matcher.group(),
					singleSpace + reverseDateMonth(matcher.group()) + singleSpace);
		}
		return argsString;
	}

	private static String reverseDateMonth(String string) {
		String dateMonthDelimiterRegex = "([-/.])";
		String nonDigitRegex = "\\D+";
		String standardDateMonthDelimiter = "/";

		String[] stringArray = string.split(dateMonthDelimiterRegex);
		if (stringArray.length == 2) {
			return stringArray[1].replaceAll(nonDigitRegex, emptyString) + standardDateMonthDelimiter
					+ stringArray[0].replaceAll(nonDigitRegex, emptyString);
		} else if (stringArray.length == 3) {
			return stringArray[1] + standardDateMonthDelimiter + stringArray[0].replaceAll(nonDigitRegex, emptyString)
					+ standardDateMonthDelimiter + stringArray[2].replaceAll(nonDigitRegex, emptyString);
		} else {
			return string;
		}
	}

	/**
	 * Convert a date of type Jave.util.Date to java.time.LocalDateTime
	 * @param date the date of type Jave.util.Date
	 * @return the date of type java.time.LocalDateTime
	 */
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

	/**
	 * Function that get the number of words separated by white space within a string
	 * @param string the passed in string
	 * @return the number of words in the passed in string
	 */
	public static int getNumberOfWords(String string) {
		if (string == null) {
			return 0;
		} else {
			String[] array = string.split(whiteSpaceRegex);
			return array.length;
		}
	}

	/**
	 * function that returns the last word of the passed in string
	 * @param string the passed in string
	 * @return the last word of the passed in string
	 */
	public static String getLastWord(String string) {
		if (string == null) {
			return null;
		} else {
			String[] array = string.split(whiteSpaceRegex);
			return array[array.length - 1].trim();
		}
	}

	/**
	 * function that returns the second last word of the passed in string
	 * @param string the passed in string
	 * @return second last word of the passed in string
	 */
	public static String getSecondLastWord(String string) {
		if (string == null) {
			return null;
		} else {
			String[] array = string.split(whiteSpaceRegex);
			if (array.length >= 2) {
				return array[array.length - 2].trim();
			} else {
				return null;
			}
		}
	}
}
