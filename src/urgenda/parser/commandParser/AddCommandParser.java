package urgenda.parser.commandParser;

import urgenda.command.*;
import urgenda.parser.DateTimeParser;
import urgenda.parser.PublicVariables;
import urgenda.parser.PublicVariables.*;
import urgenda.util.Task;
import urgenda.parser.TaskDetailsParser;

public class AddCommandParser {
	private static String _argsString;
	private static int _index;

	public AddCommandParser(String argsString, int index) {
		_argsString = argsString;
		_index = index;
	}

	public static Command generateAndReturn() {
		if (_argsString == null) {
			return new Invalid();
		} else {
			String reducedArgsString = DateTimeParser.searchTaskTimes(_argsString);
			// System.out.print(reducedArgsString + "\n");
			reducedArgsString = TaskDetailsParser.searchTaskHashtags(reducedArgsString);
			reducedArgsString = TaskDetailsParser.searchTaskLocation(reducedArgsString);
			// System.out.print(reducedArgsString + "\n");
			TaskDetailsParser.searchTaskDescription(reducedArgsString);
			TaskDetailsParser.searchTaskType();
			return generateAddCommandAndReturn();
		}
	}

	private static Command generateAddCommandAndReturn() {
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
		if (!PublicVariables.taskHashtags.isEmpty()) {
			newTask.setHashtags(PublicVariables.taskHashtags);
		}
		if (PublicVariables.taskSlots != null) {
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
}
