package urgenda.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.parser.PublicVariables.*;
import urgenda.util.MultipleSlot;

public class PublicFunctions {
	public static String getFirstWord(String commandString) {
		return commandString.split("\\s+")[0];
	}
	
	public static String removeFirstWord(String commandString) {
		try {
			return commandString.split("\\s+", 2)[1];
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
}
