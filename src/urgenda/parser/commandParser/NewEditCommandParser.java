package urgenda.parser.commandParser;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import org.ocpsoft.prettytime.nlp.parse.DateGroup;

import urgenda.command.*;
import urgenda.parser.DateTimeParser;
import urgenda.parser.PublicFunctions;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;
import urgenda.parser.PublicVariables.COMMAND_TYPE;
import urgenda.util.Task;

public class NewEditCommandParser {
	private static String _argsString;
	private static int _index;

	private static String startFlagRegex = "(-s)|(-s:)|(from)";
	private static String endFlagRegex = "(-e)|(-e:)|(to)|(by)";
	private static String removeFlagRegex = "(-r)|(-rm)";
	private static String combinedRegex = startFlagRegex + "|" + endFlagRegex;

	private static LocalDateTime startTime;
	private static LocalDateTime endTime;
	private static LocalDateTime unknownTime;
	private static String removeString1;
	private static String removeString2;
	private static Integer index;

	public NewEditCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			_argsString = PublicFunctions.reformatArgsString(_argsString);
			String reducedString = searchIndex();
			searchDetails(reducedString);
			int numberOfRmFlag = countRmFlag(reducedString);
			NewEdit editCommand = new NewEdit();
			Task newTask = new Task();
			if (startTime != null) {
				newTask.setStartTime(startTime);
			}
			if (endTime != null) {
				newTask.setEndTime(endTime);
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

			switch (numberOfRmFlag) {
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

	private static int countRmFlag(String reducedString) {
		int index = reducedString.indexOf("-r");
		int count = 0;
		while (index != -1) {
		    count++;
		    reducedString = reducedString.substring(index + 1);
		    index = reducedString.indexOf("-r");
		}
		index = reducedString.indexOf("-rm");
		while (index != -1) {
		    count++;
		    reducedString = reducedString.substring(index + 1);
		    index = reducedString.indexOf("-rm");
		}
		return count;
	}

	private static String searchIndex() {
		String firstWord = PublicFunctions.getFirstWord(_argsString);
		try {
			index = Integer.parseInt(firstWord);
			return PublicFunctions.removeFirstWord(_argsString);
		} catch (Exception e) {
			return _argsString;
		}
	}

	private static void searchDetails(String argsString) {
		String[] stringArray = argsString.split(combinedRegex);
		if (stringArray.length == 0) {
			unknownTime = parseUnknownTime(argsString);
		} else {
			for (int i = 0; i < stringArray.length; i++) {
				int position = argsString.indexOf(stringArray[i]);
				String preceedingWord = PublicFunctions.getPreceedingWord(position, argsString);
				if (preceedingWord.equals("-s") || preceedingWord.equals("-s:") || preceedingWord.equals("from")) {
					startTime = parseStartTime(stringArray[i]);
				} else if (preceedingWord.equals("-e") || preceedingWord.equals("-e:") || preceedingWord.equals("to")
						|| preceedingWord.equals("by")) {
					endTime = parseEndTime(stringArray[i]);
				} else {
					unknownTime = parseUnknownTime(stringArray[i]);
				}
			}
		}
	}

	// taken from DateTimeParser, refractor later
	private static LocalDateTime parseStartTime(String argsString) {
		List<DateGroup> dateGroups = new PrettyTimeParser().parseSyntax(argsString);
		if (dateGroups.size() == 1) {
			String parsedString = dateGroups.get(0).getText();
			if (dateGroups.get(0).getDates().size() == 1 && parsedString.trim().equals(argsString)) {
				List<DateGroup> dateGroups2 = new PrettyTimeParser().parseSyntax(argsString);
				Date firstParseDate = dateGroups.get(0).getDates().get(0);
				Date secondParseDate = dateGroups2.get(0).getDates().get(0);
				LocalDateTime localDateTime = PublicFunctions.getLocalDateTimeFromDate(firstParseDate);
				if (firstParseDate.equals(secondParseDate)) {
					return localDateTime;
				} else {
					return DateTimeParser.adjustedDateEvent(localDateTime);
				}
			}
		}
		return null;
	}

	private static LocalDateTime parseEndTime(String argsString) {
		List<DateGroup> dateGroups = new PrettyTimeParser().parseSyntax(argsString);
		if (dateGroups.size() == 1) {
			String parsedString = dateGroups.get(0).getText();
			if (dateGroups.get(0).getDates().size() == 1 && parsedString.trim().equals(argsString)) {
				List<DateGroup> dateGroups2 = new PrettyTimeParser().parseSyntax(argsString);
				Date firstParseDate = dateGroups.get(0).getDates().get(0);
				Date secondParseDate = dateGroups2.get(0).getDates().get(0);
				LocalDateTime localDateTime = PublicFunctions.getLocalDateTimeFromDate(firstParseDate);
				if (firstParseDate.equals(secondParseDate)) {
					return localDateTime;
				} else {
					return DateTimeParser.adjustedDateDeadline(localDateTime);
				}
			}
		}
		return null;
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

	private static Command generateEditCommandAndReturn() {
		if (PublicVariables.commandType == COMMAND_TYPE.INVALID) {
			return new Invalid();
		} else {
			Task newTask = new Task();
			if (!PublicVariables.taskLocation.equals("")) {
				newTask.setLocation(PublicVariables.taskLocation);
			}
			if (PublicVariables.taskStartTime != null) {
				newTask.setStartTime(PublicVariables.taskStartTime);
			}
			if (PublicVariables.taskEndTime != null) {
				newTask.setEndTime(PublicVariables.taskEndTime);
			}
			if (!PublicVariables.taskHashtags.isEmpty()) {
				newTask.setHashtags(PublicVariables.taskHashtags);
			}
			if (PublicVariables.taskSlots != null) {
				newTask.setSlot(PublicVariables.taskSlots);
			}
			if (!PublicVariables.taskDescription.equals("")) {
				newTask.setDesc(PublicVariables.taskDescription);
			}
			switch (PublicVariables.taskType) {
			case EVENT:
				newTask.setTaskType(Task.Type.EVENT);
				break;
			case DEADLINE:
				newTask.setTaskType(Task.Type.DEADLINE);
				break;
			case FLOATING:
				newTask.setTaskType(Task.Type.FLOATING);
				break;
			default:
				return new Invalid();
			}
			if (PublicVariables.taskIndex != -10) {
				return new Edit(PublicVariables.taskIndex, newTask);
			} else {
				return new Edit(_index, newTask);
			}
		}
	}
}
