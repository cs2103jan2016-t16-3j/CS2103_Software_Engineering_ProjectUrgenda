//@@author A0080436J
package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.util.DateTimePair;
import urgenda.util.LogicException;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;

/**
 * Command interface for implementation of subsequent command classes
 *
 */
public abstract class Command {

	private static final String MESSAGE_EVENT = "\"%1$s\" on %2$d/%3$d %4$02d:%5$02d - %6$d/%7$d %8$02d:%9$02d";
	private static final String MESSAGE_EVENT_VENUE = "\"%1$s\" at %2$s on %3$d/%4$d %5$02d:%6$02d - %7$d/%8$d %9$02d:%10$02d";
	private static final String MESSAGE_EVENT_DATETIME = ", %1$d/%2$d, %3$02d:%4$02d - %5$02d:%6$02d";
	private static final String MESSAGE_FLOAT = "\"%1$s\"";
	private static final String MESSAGE_FLOAT_VENUE = "\"%1$s\" at %2$s";
	private static final String MESSAGE_DEADLINE = "\"%1$s\" by %2$d/%3$d, %4$02d:%5$02d";
	private static final String MESSAGE_DEADLINE_VENUE = "\"%1$s\" at %2$s by %3$d/%4$d, %5$02d:%6$02d";

	// for execution of specific command
	public abstract String execute() throws LogicException;

	// for generation of messages of the task given as input, includes multiple
	// tasks
	public String taskMessageWithMulti(Task task) {
		String feedback = taskMessage(task);
		if (task.getSlot() != null) {
			feedback += additionalTimings(task.getSlot());
		}
		return feedback;
	}

	// generation of messages of task for edit fn whereby location of task may
	// be edited
	public String taskMessageWithLocation(Task task) {
		Task.Type taskType = task.getTaskType();
		String feedback = null;
		if (task.getLocation() == null || task.getLocation().isEmpty()) {
			feedback = taskMessage(task);
		} else {
			switch (taskType) {
			case EVENT:
				feedback = String.format(MESSAGE_EVENT_VENUE, task.getDesc(), task.getLocation(),
						task.getStartTime().getDayOfMonth(), task.getStartTime().getMonthValue(),
						task.getStartTime().getHour(), task.getStartTime().getMinute(), task.getEndTime().getHour(),
						task.getEndTime().getMinute());
				break;

			case FLOATING:
				feedback = String.format(MESSAGE_FLOAT_VENUE, task.getDesc(), task.getLocation());
				break;

			case DEADLINE:
				feedback = String.format(MESSAGE_DEADLINE_VENUE, task.getDesc(), task.getLocation(),
						task.getEndTime().getDayOfMonth(), task.getEndTime().getMonthValue(),
						task.getEndTime().getHour(), task.getEndTime().getMinute());
				break;

			}
		}
		if (task.getSlot() != null) {
			feedback += additionalTimings(task.getSlot());
		}
		return feedback;
	}

	// for generation of messages of task given as input, excludes multiple
	// tasks
	public String taskMessage(Task task) {
		Task.Type taskType = task.getTaskType();
		String feedback = null;
		switch (taskType) {
		case EVENT:
			feedback = String.format(MESSAGE_EVENT, task.getDesc(), task.getStartTime().getDayOfMonth(),
					task.getStartTime().getMonthValue(), task.getStartTime().getHour(), task.getStartTime().getMinute(),
					task.getEndTime().getDayOfMonth(), task.getEndTime().getMonthValue(), task.getEndTime().getHour(),
					task.getEndTime().getMinute());
			break;

		case FLOATING:
			feedback = String.format(MESSAGE_FLOAT, task.getDesc());
			break;

		case DEADLINE:
			feedback = String.format(MESSAGE_DEADLINE, task.getDesc(), task.getEndTime().getDayOfMonth(),
					task.getEndTime().getMonthValue(), task.getEndTime().getHour(), task.getEndTime().getMinute());
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
