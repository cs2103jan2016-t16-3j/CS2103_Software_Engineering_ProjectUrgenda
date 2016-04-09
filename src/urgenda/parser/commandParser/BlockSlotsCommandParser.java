//@@author A0127764X
package urgenda.parser.commandParser;

import java.util.ArrayList;

import urgenda.command.*;
import urgenda.parser.DateTimeParser;
import urgenda.parser.PublicFunctions;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;
import urgenda.util.Task;

public class BlockSlotsCommandParser {
	private static String _argsString;
	private static int _index;
	private static String taskDetails;
	private static ArrayList<String> taskTimeStrings;

	public BlockSlotsCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			_argsString = PublicFunctions.reformatArgsString(_argsString).trim();
			taskTimeStrings = new ArrayList<String>();
			Boolean isConcatSuccess = concatArgsString();
			if (isConcatSuccess) {
				String reducedArgsString = TaskDetailsParser.searchTaskLocation(_argsString);
				TaskDetailsParser.searchTaskDescription(reducedArgsString);
				TaskDetailsParser.searchTaskType();
				DateTimeParser.searchTaskSlots(taskTimeStrings);
				return generateBlockCommandAndReturn();
			} else {
				return new Invalid();
			}
		}
	}

	private static Boolean concatArgsString() {
		try {
			String[] splitString = _argsString.split(" at ");
			taskDetails = splitString[0];
			String[] timeStrings = splitString[1].split(",");
			for (int i = 0; i < timeStrings.length; i++) {
				taskTimeStrings.add(timeStrings[i]);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private static Command generateBlockCommandAndReturn() {
		Task newTask = new Task();
		if (!PublicVariables.taskDescription.equals("")) {
			newTask.setDesc(PublicVariables.taskDescription);
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
		if (PublicVariables.taskSlots != null) {
			newTask.setSlot(PublicVariables.taskSlots);
		} else {
			return new Invalid();
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
		
		BlockSlots blockCommand = new BlockSlots(newTask);
		return blockCommand;
	}
}
