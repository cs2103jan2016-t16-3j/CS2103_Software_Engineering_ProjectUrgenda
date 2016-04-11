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

	private static String timeDelimiter = " at ";
	private static String groupDelimiter = ",";
	private static String emptyString = "";

	/**
	 * public constructor of BlockSlotsCommandParser
	 * 
	 * @param argsString
	 *            argument string to be parsed
	 * @param index
	 *            location of currently highlighted task
	 */
	public BlockSlotsCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	/**
	 * function that parses the passed in argument string to generate an return
	 * an appropriate BlockSlots object
	 * 
	 * @return BlockSlots object with parsed details stored in its attributes
	 */
	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			reformatArgsString();
			taskTimeStrings = new ArrayList<String>();
			Boolean isConcatSuccess = concatArgsString();
			if (isConcatSuccess) {
				String reducedArgsString = TaskDetailsParser.searchTaskLocation(taskDetails);
				TaskDetailsParser.searchTaskDescription(reducedArgsString);
				TaskDetailsParser.searchTaskType();
				DateTimeParser.searchTaskSlots(taskTimeStrings);
				return generateBlockCommandAndReturn();
			} else {
				return new Invalid();
			}
		}
	}

	private static void reformatArgsString() {
		_argsString = PublicFunctions.reformatArgsString(_argsString).trim();
	}

	private static Boolean concatArgsString() {
		String[] splitString = _argsString.split(timeDelimiter);
		if (splitString.length == 2) {
			try {
				taskDetails = splitString[0];
				String[] timeStrings = splitString[1].split(groupDelimiter);
				for (int i = 0; i < timeStrings.length; i++) {
					taskTimeStrings.add(timeStrings[i]);
				}
				return true;
			} catch (Exception e) {
				return false;
			}
		} else if (splitString.length == 3) {
			try {
				taskDetails = splitString[0] + timeDelimiter + splitString[1];
				String[] timeStrings = splitString[2].split(groupDelimiter);
				for (int i = 0; i < timeStrings.length; i++) {
					taskTimeStrings.add(timeStrings[i]);
				}
				return true;
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}

	}

	private static Command generateBlockCommandAndReturn() {
		Task newTask = new Task();
		if (hasValidDesc()) {
			newTask.setDesc(PublicVariables.taskDescription);
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

	private static boolean hasValidSlot() {
		return PublicVariables.taskSlots != null;
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
}
