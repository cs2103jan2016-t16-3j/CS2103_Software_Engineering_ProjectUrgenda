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

	public AddCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			descPlaceHolder = null;
			ArrayList<String> reservedWords = getReservedWords();
			String reformattedString = checkSpecialDesc();
			reformattedString = PublicFunctions.reformatArgsString(_argsString);
			String reducedArgsString = DateTimeParser.searchTaskTimes(reformattedString);
			reducedArgsString = undoReserveWords(reservedWords, reducedArgsString);
			// System.out.print(reducedArgsString + "\n");
//			reducedArgsString = TaskDetailsParser.searchTaskHashtags(reducedArgsString);
			reducedArgsString = TaskDetailsParser.searchTaskLocation(reducedArgsString);
			// System.out.print(reducedArgsString + "\n");
			TaskDetailsParser.searchTaskDescription(reducedArgsString);
			TaskDetailsParser.searchTaskType();
			return generateAddCommandAndReturn();
		}
	}

	private static Command generateAddCommandAndReturn() {
		Task newTask = new Task();
		if (descPlaceHolder != null && !descPlaceHolder.equals("\"\"")) {
			newTask.setDesc(descPlaceHolder.substring(1,descPlaceHolder.length()));
		} else {
			if (!PublicVariables.taskDescription.equals("")) {
				newTask.setDesc(PublicVariables.taskDescription);
			}
		}
		if (!PublicVariables.taskLocation.equals("")) {
			newTask.setLocation(PublicVariables.taskLocation);
		}
		if (PublicVariables.taskStartTime != null) {
			newTask.setStartTime(PublicVariables.taskStartTime);
		}
		if (PublicVariables.taskEndTime != null) {
			newTask.setEndTime(PublicVariables.taskEndTime);
		}
		if (!PublicVariables.taskSlots.isEmpty()) {
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
	public static String checkSpecialDesc() {
		int counter = 0;
		for( int i=0; i<_argsString.length(); i++ ) {
		    if( _argsString.charAt(i) == '\"' ) {
		        counter++;
		    } 
		}
		if (counter!= 2) {
			return _argsString;
		} else {
			int firstOccurence = _argsString.indexOf('\"');
			int secondOccurence = _argsString.indexOf('\"', firstOccurence + 1);
			descPlaceHolder = _argsString.substring(firstOccurence,secondOccurence);
			return _argsString.replace(descPlaceHolder, "");
		}
	}
	
	private static ArrayList<String> getReservedWords() {
		ArrayList<String> array = new ArrayList<String>();
		Matcher matcher = Pattern.compile("([^\\d+\\s+/-:]+)(\\d+)").matcher(_argsString);
		while (matcher.find()) {
			_argsString = _argsString.replace(matcher.group(), "<" + matcher.group() + ">");
			array.add("<" + matcher.group() + ">");
		}
		
		return array;
	}
	
	private static String undoReserveWords(ArrayList<String> array, String string) {
		for (String arrayString:array) {
			string = string.replace(arrayString, arrayString.substring(1,arrayString.length()-1));
		}
		return string;
	}
}
