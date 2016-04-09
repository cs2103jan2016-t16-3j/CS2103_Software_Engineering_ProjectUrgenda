//@@author A0127764X
package urgenda.parser.commandParser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import urgenda.command.*;
import urgenda.parser.DateTimeParser;
import urgenda.parser.PublicFunctions;
import urgenda.parser.PublicVariables;
import urgenda.parser.PublicVariables.*;
import urgenda.util.Task;
import urgenda.parser.TaskDetailsParser;

public class AddCommandParser {
	private static String _argsString;
	private static int _index;
	private static String descPlaceHolder;
	
	private static String emptyString = "";
	private static String doubleQuotes = "\"\"";
	private static char singleQuote ='\"';

	public AddCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
		descPlaceHolder = null;
	}

	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			String reformattedString = checkSpecialDesc();
			ArrayList<String> reservedWords = getReservedWords();
			reformattedString = PublicFunctions.reformatArgsString(_argsString).trim();
			String reducedArgsString = DateTimeParser.searchTaskTimes(reformattedString);
			reducedArgsString = undoReserveWords(reservedWords, reducedArgsString);
			reducedArgsString = TaskDetailsParser.searchTaskLocation(reducedArgsString);
			TaskDetailsParser.searchTaskDescription(reducedArgsString);
			TaskDetailsParser.searchTaskType();
			return generateAddCommandAndReturn();
		}
	}

	private static Command generateAddCommandAndReturn() {
		Task newTask = new Task();
		if (hasSpecialDesc()) {
			newTask.setDesc(descPlaceHolder.substring(1, descPlaceHolder.length()));
		} else {
			if (hasValidDesc()) {
				newTask.setDesc(PublicVariables.taskDescription);
			}
		}
		if (hasValidLocation()) {
			newTask.setLocation(PublicVariables.taskLocation);
		}
		if (hasValidStartTime()) {
			newTask.setStartTime(PublicVariables.taskStartTime);
		}
		if (hasValidEndTime()) {
			newTask.setEndTime(PublicVariables.taskEndTime);
		}
		if (hasValidSlot()) {
			newTask.setSlot(PublicVariables.taskSlots);
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
		return new AddTask(newTask);
	}

	private static boolean hasValidSlot() {
		return !PublicVariables.taskSlots.isEmpty();
	}

	private static boolean hasValidEndTime() {
		return PublicVariables.taskEndTime != null;
	}

	private static boolean hasValidStartTime() {
		return PublicVariables.taskStartTime != null;
	}

	private static boolean hasValidLocation() {
		return !PublicVariables.taskLocation.equals(emptyString);
	}

	private static boolean hasValidDesc() {
		return !PublicVariables.taskDescription.equals(emptyString);
	}

	private static boolean hasSpecialDesc() {
		return descPlaceHolder != null && !descPlaceHolder.equals(doubleQuotes);
	}

	private static String checkSpecialDesc() {
		int counter = 0;
		for (int i = 0; i < _argsString.length(); i++) {
			if (_argsString.charAt(i) == singleQuote) {
				counter++;
			}
		}
		if (counter != 2) {
			return _argsString;
		} else {
			int firstOccurence = _argsString.indexOf(singleQuote);
			int secondOccurence = _argsString.indexOf(singleQuote, firstOccurence + 1);
			descPlaceHolder = _argsString.substring(firstOccurence, secondOccurence);
			return _argsString.replace(descPlaceHolder, emptyString);
		}
	}

	private static ArrayList<String> getReservedWords() {
		String specialDescRegex = "([^\\d+\\s+/-:]+)(\\d+)";
		String leftDelimiter = "<";
		String rightDelimiter = ">";
		ArrayList<String> array = new ArrayList<String>();
		
		Matcher matcher = Pattern.compile(specialDescRegex).matcher(_argsString);
		while (matcher.find()) {
			_argsString = _argsString.replace(matcher.group(), leftDelimiter + matcher.group() + rightDelimiter);
			array.add(leftDelimiter + matcher.group() + rightDelimiter);
		}
		return array;
	}

	private static String undoReserveWords(ArrayList<String> array, String string) {
		for (String arrayString : array) {
			string = string.replace(arrayString, arrayString.substring(1, arrayString.length() - 1));
		}
		return string;
	}
}
