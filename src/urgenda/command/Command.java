package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.util.MultipleSlot;
import urgenda.util.Task;
import urgenda.util.DateTimePair;

/**
 * Command interface for implementation of subsequent command classes
 *
 */
public abstract class Command {
	
	private static final String MESSAGE_EVENT = "\"%1$s\" on %2$d/%3$d, %4$02d:%5$02d - %6$02d:%7$02d";
	private static final String MESSAGE_EVENT_DATETIME = ", %1$d/%2$d, %3$02d:%4$02d - %5$02d:%6$02d";
	private static final String MESSAGE_FLOAT = "\"%1$s\"";
	private static final String MESSAGE_DEADLINE = "\"%1$s\" by %2$d/%3$d, %4$02d:%5$02d";

	// for execution of specific command
	public abstract String execute() throws Exception;

	// for generation of messages of the task given as input, includes multiple tasks
	public String taskMessageWithMulti(Task task) {
		String feedback = taskMessage(task);
		if (task.getSlot() != null) {
			feedback += additionalTimings(task.getSlot());
		}
		return feedback;
	}

	// for generation of messages of task given as input, excludes multiple tasks
	public String taskMessage(Task task) {
		Task.Type taskType = task.getTaskType();
		String feedback = null;
		switch (taskType) {
			case EVENT :
				feedback = String.format(MESSAGE_EVENT, task.getDesc(), task.getStartTime().getDayOfMonth(),
						task.getStartTime().getMonthValue(), task.getStartTime().getHour(), 
						task.getStartTime().getMinute(), task.getEndTime().getHour(), 
						task.getEndTime().getMinute());
				break;
		
			case FLOATING :
				feedback = String.format(MESSAGE_FLOAT, task.getDesc());
				break;
		
			case DEADLINE :
				feedback = String.format(MESSAGE_DEADLINE, task.getDesc(), task.getEndTime().getDayOfMonth(),
						task.getEndTime().getMonthValue(), task.getEndTime().getHour(), 
						task.getEndTime().getMinute());
				break;

		}
		return feedback;
	}

	private String additionalTimings(MultipleSlot block) {
		String feedback = "";
		ArrayList<DateTimePair> slots = block.getSlots();
		for (DateTimePair pair : slots) {
			LocalDateTime start = pair.getEarlierDateTime();
			LocalDateTime end = pair.getLaterDateTime();
			feedback += String.format(MESSAGE_EVENT_DATETIME, start.getDayOfMonth(), start.getMonthValue(),
					start.getHour(), start.getMinute(), end.getHour(), end.getMinute());
		}
		return feedback;
	}
}

