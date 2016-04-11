//@@author A0127764X
package urgenda.parser.commandParser;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

import urgenda.command.*;
import urgenda.parser.DateTimeParser;
import urgenda.parser.PublicFunctions;
import urgenda.util.Task;

public class NewEditCommandParser {
	private static String _argsString;
	private static int _index;

	private static enum TIME_TYPE {
		START_TIME, END_TIME
	}

	private static String startFlagRegex = "(-s)|(-s:)|(from)";
	private static String endFlagRegex = "(-e)|(-e:)|(to)|(by)";
	private static String removeFlagRegex = "((-r)|(-rm))";
	private static String locationRegex = "(@)";
	private static String combinedRegex = "((\\A|\\D)(" + startFlagRegex + "|" + endFlagRegex + ")(\\Z|\\D))|"
			+ locationRegex;

	private static LocalDateTime startTime;
	private static LocalDateTime endTime;
	private static LocalDateTime unknownTime;
	private static String descString;
	private static String location;
	private static Integer index;
	private static int numberOfRemoveFlag = 0;

	/**
	 * public constructor of NewEditCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public NewEditCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate Edit object
	 * 
	 * @return Edit object with parsed details stored in its attributes
	 */
	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			reinitializeVariables();

			_argsString = PublicFunctions.reformatArgsString(_argsString).trim();
			String reducedString = searchIndex();
			reducedString = countRmFlag(reducedString);
			searchDetails(reducedString);

			Edit editCommand = new Edit();
			Task newTask = new Task();
			if (startTime != null) {
				newTask.setStartTime(startTime);
			}
			if (endTime != null) {
				newTask.setEndTime(endTime);
			}
			if (descString != null) {
				newTask.setDesc(descString);
			}
			if (location != null) {
				newTask.setLocation(location);
			}
			if (unknownTime != null) {
				editCommand.setUnknown(unknownTime);
			}
			if (index != null) {
				editCommand.setId(index);
			} else {
				editCommand.setId(_index);
			}
			editCommand.setNewTask(newTask);

			switch (numberOfRemoveFlag) {
			case 0:
				return editCommand;
			case 1:
				editCommand.setIsRemovedOnce();
				return editCommand;
			case 2:
				editCommand.setIsRemovedOnce();
				editCommand.setIsRemovedTwice();
				return editCommand;
			case 3:
				return new Invalid();
			}
		}
		return new Invalid();
	}

	private static String countRmFlag(String reducedString) {
		String removeFlagRegex2 = "-r";
		String removeFlagRegex1 = "-rm";
		String temp = reducedString;
		String combinedRegex = "(" + removeFlagRegex2 + "|" + removeFlagRegex1 + ")";
		int count = 0;

		if (reducedString != null) {
			int index = reducedString.indexOf(removeFlagRegex1);
			while (index != -1) {
				count++;
				if (reducedString.length() - 1 >= index + 3) {
					reducedString = reducedString.substring(0, index) + reducedString.substring(index + 3);
				} else {
					reducedString = reducedString.substring(0, index);
				}
				index = reducedString.indexOf(removeFlagRegex1);
			}

			index = reducedString.indexOf(removeFlagRegex2);
			while (index != -1) {
				count++;
				if (reducedString.length() - 1 >= index + 2) {
					reducedString = reducedString.substring(0, index) + reducedString.substring(index + 2);
				} else {
					reducedString = reducedString.substring(0, index);
				}
				index = reducedString.indexOf(removeFlagRegex2);
			}
			numberOfRemoveFlag = count;
			return reducedString.trim();
		} else {
			return null;
		}
	}

	private static String searchIndex() {
		String firstWord = PublicFunctions.getFirstWord(_argsString);
		try {
			index = Integer.parseInt(firstWord) - 1;
			return PublicFunctions.removeFirstWord(_argsString);
		} catch (Exception e) {
			return _argsString;
		}
	}

	private static void searchDetails(String argsString) {
		if (argsString != null) {
			String temp = argsString;
			String[] stringArray = temp.trim().split(combinedRegex);

			if (stringArray.length == 0) {
				parseTimeWithoutKeyWord(temp);
			} else {
				parseDetailsWithKeyWords(temp, stringArray);
			}
		}
	}

	private static void parseDetailsWithKeyWords(String temp, String[] stringArray) {
		for (int i = 0; i < stringArray.length; i++) {
			parseSeparatedDetailStrings(temp, stringArray, i);
		}
	}

	private static void parseSeparatedDetailStrings(String temp, String[] stringArray, int i) {
		int position = temp.indexOf(stringArray[i].trim());
		String preceedingWord = PublicFunctions.getPreceedingWord(position, temp);

		if (isAfterStartFlag(preceedingWord)) {
			startTime = parseStartTime(stringArray[i].trim());
		} else if (isAfterEndFlag(preceedingWord)) {
			endTime = parseEndTime(stringArray[i].trim());
		} else if (isAfterLocationFlag(preceedingWord)) {
			location = stringArray[i].trim();
		} else {
			parseDetailsWithoutPreceedingKeyWord(stringArray, i);
		}
	}

	private static void parseDetailsWithoutPreceedingKeyWord(String[] stringArray, int i) {
		String removeFlagWithBoundsRegex = "(\\A|\\D)((-rm)|(-r))(\\Z|\\D)";
		String removeFlagRegex = "((-rm)|(-r))";
		String emptyString = "";

		String argsStringWithoutFlags = stringArray[i].replaceAll(removeFlagWithBoundsRegex, emptyString).trim();
		unknownTime = parseUnknownTime(argsStringWithoutFlags);
		if (isFirstGroupOrParseUnknownFail(i)) {
			String[] array = stringArray[i].split(removeFlagWithBoundsRegex);
			if (array.length == 2) {
				unknownTime = parseUnknownTime(array[1].trim());
				if (unknownTime != null) {
					descString = array[0];
				} else {
					descString = stringArray[i].trim();
				}
			} else {
				descString = stringArray[i].replaceAll(removeFlagRegex, emptyString).trim();
			}
		}
	}

	private static boolean isAfterLocationFlag(String preceedingWord) {
		String locationKeyWord = "@";
		return preceedingWord.equals(locationKeyWord);
	}

	private static boolean isAfterEndFlag(String preceedingWord) {
		String endFlag1 = "-e";
		String endFlag2 = "-e:";
		String endFlag3 = "to";
		String endFlag4 = "by";
		return preceedingWord.equals(endFlag1) || preceedingWord.equals(endFlag2) || preceedingWord.equals(endFlag3)
				|| preceedingWord.equals(endFlag4);
	}

	private static boolean isAfterStartFlag(String preceedingWord) {
		String startFlag1 = "-s";
		String startFlag2 = "-s:";
		String startFlag3 = "from";
		return preceedingWord.equals(startFlag1) || preceedingWord.equals(startFlag2)
				|| preceedingWord.equals(startFlag3);
	}

	private static boolean isFirstGroupOrParseUnknownFail(int i) {
		return i == 0 && unknownTime == null;
	}

	private static void parseTimeWithoutKeyWord(String temp) {
		unknownTime = parseUnknownTime(temp);
	}

	private static LocalDateTime parseStartTime(String argsString) {
		return parseTime(argsString, TIME_TYPE.START_TIME);
	}

	private static LocalDateTime parseEndTime(String argsString) {
		return parseTime(argsString, TIME_TYPE.END_TIME);
	}

	private static LocalDateTime parseTime(String argsString, TIME_TYPE timeType) {
		List<DateGroup> dateGroups = new PrettyTimeParser().parseSyntax(argsString);
		if (isValidArgsStringForTimeParse(argsString, dateGroups)) {
			Date firstParseDate = dateGroups.get(0).getDates().get(0);
			LocalDateTime localDateTime = PublicFunctions.getLocalDateTimeFromDate(firstParseDate);
			if (isCompleteDate(argsString, dateGroups)) {
				return localDateTime;
			} else {
				if (timeType == TIME_TYPE.START_TIME) {
					return DateTimeParser.adjustedDateEvent(localDateTime);
				} else {
					return DateTimeParser.adjustedDateDeadline(localDateTime);
				}
			}
		}
		return null;
	}

	private static boolean isCompleteDate(String argsString, List<DateGroup> dateGroups) {
		List<DateGroup> dateGroups2 = new PrettyTimeParser().parseSyntax(argsString);
		Date firstParseDate = dateGroups.get(0).getDates().get(0);
		Date secondParseDate = dateGroups2.get(0).getDates().get(0);
		return firstParseDate.equals(secondParseDate);
	}

	private static LocalDateTime parseUnknownTime(String argsString) {
		List<DateGroup> dateGroups = new PrettyTimeParser().parseSyntax(argsString);
		if (dateGroups.size() == 1) {
			String parsedString = dateGroups.get(0).getText();
			if (dateGroups.get(0).getDates().size() == 1 && parsedString.trim().equals(argsString)) {
				Date parseDate = dateGroups.get(0).getDates().get(0);
				LocalDateTime localDateTime = PublicFunctions.getLocalDateTimeFromDate(parseDate);
				return localDateTime;
			}
		}
		return null;
	}

	private static boolean isValidArgsStringForTimeParse(String argsString, List<DateGroup> dateGroups) {
		if (dateGroups.size() == 1) {
			String parsedString = dateGroups.get(0).getText();
			if (dateGroups.get(0).getDates().size() == 1 && parsedString.trim().equals(argsString)) {
				return true;
			}
		}
		return false;
	}

	private static void reinitializeVariables() {
		startTime = null;
		endTime = null;
		unknownTime = null;
		descString = null;
		location = null;
		index = null;
		numberOfRemoveFlag = 0;
	}
}
