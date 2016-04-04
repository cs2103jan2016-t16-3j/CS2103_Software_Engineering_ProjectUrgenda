package urgenda.parser.commandParser;

import urgenda.command.*;
import urgenda.parser.DateTimeParser;
import urgenda.parser.PublicVariables;
import urgenda.parser.TaskDetailsParser;
import urgenda.parser.PublicVariables.COMMAND_TYPE;
import urgenda.util.Task;

public class EditCommandParser {
	private static String _argsString;
	private static int _index;

	public EditCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			String reducedArgsString;
			if (TaskDetailsParser.searchTaskIndex(_argsString) == null) {
				PublicVariables.taskIndex = -10;
				reducedArgsString = _argsString;
			} else {
				reducedArgsString = TaskDetailsParser.searchTaskIndex(_argsString);
			}
			reducedArgsString = DateTimeParser.searchTaskTimes(reducedArgsString);
			reducedArgsString = TaskDetailsParser.searchTaskHashtags(reducedArgsString);
			reducedArgsString = TaskDetailsParser.searchTaskLocation(reducedArgsString);
			TaskDetailsParser.searchTaskDescription(reducedArgsString);

			TaskDetailsParser.searchTaskType();
			return generateEditCommandAndReturn();
		}
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
//				return new Edit(PublicVariables.taskIndex, newTask);
				return null;
			} else {
//				return new Edit(_index, newTask);
				return null;
			}
		}
	}
}
