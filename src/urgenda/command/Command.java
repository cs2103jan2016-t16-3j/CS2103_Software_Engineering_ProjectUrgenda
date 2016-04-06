//@@author A0080436J
package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.util.DateTimePair;
import urgenda.util.LogicException;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;

/**
 * Command abstract class for implementation of subsequent command classes. Used
 * for the command pattern. Includes all different versions of generating task
 * messages which can be used across all inherited classes.
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

	/**
	 * Abstract method for execution of specific command.
	 * 
	 * @return String of the feedback to the user.
	 * @throws LogicException
	 *             When the command on the task is invalid.
	 */
	public abstract String execute() throws LogicException;

	/**
	 * Generates a task message for the task with multiple slots.
	 * 
	 * @param task
	 *            Task object for generating of message.
	 * @return Feedback for given task.
	 */
	public String taskMessageWithMulti(Task task) {
		String feedback = taskMessage(task);
		if (task.getSlot() != null) {
			feedback += additionalTimings(task.getSlot());
		}
		return feedback;
	}

	/**
	 * Generate task message for the task with location. Used when location may
	 * be edited.
	 * 
	 * @param task
	 *            Task object for generating of message.
	 * @return Feedback for given task.
	 */
	public String taskMessageWithLocation(Task task) {
		Task.Type taskType = task.getTaskType();
		String feedback = null;
		if (task.getLocation() == null || task.getLocation().isEmpty()) {
			feedback = taskMessage(task);
		} else {
			feedback = generateTaskMessageWithVenue(task, taskType, feedback);
		}
		if (task.getSlot() != null) {
			feedback += additionalTimings(task.getSlot());
		}
		return feedback;
	}

	/*
	 * Returns the task message with location placed within.
	 */
	private String generateTaskMessageWithVenue(Task task, Task.Type taskType, String feedback) {
		switch (taskType) {
		case EVENT :
			feedback = String.format(MESSAGE_EVENT_VENUE, task.getDesc(), task.getLocation(),
					task.getStartTime().getDayOfMonth(), task.getStartTime().getMonthValue(),
					task.getStartTime().getHour(), task.getStartTime().getMinute(), task.getEndTime().getHour(),
					task.getEndTime().getMinute());
			break;
		case FLOATING :
			feedback = String.format(MESSAGE_FLOAT_VENUE, task.getDesc(), task.getLocation());
			break;
		case DEADLINE :
			feedback = String.format(MESSAGE_DEADLINE_VENUE, task.getDesc(), task.getLocation(),
					task.getEndTime().getDayOfMonth(), task.getEndTime().getMonthValue(),
					task.getEndTime().getHour(), task.getEndTime().getMinute());
			break;
		}
		return feedback;
	}

	// for generation of messages of task given as input, excludes multiple
	// tasks
	/**
	 * For generating of generic task message, regardless of location and
	 * multipleslots.
	 * 
	 * @param task
	 *            Task object for generating of message.
	 * @return Feedback for given task.
	 */
	public String taskMessage(Task task) {
		Task.Type taskType = task.getTaskType();
		String feedback = null;
		switch (taskType) {
		case EVENT :
			feedback = String.format(MESSAGE_EVENT, task.getDesc(), task.getStartTime().getDayOfMonth(),
					task.getStartTime().getMonthValue(), task.getStartTime().getHour(), task.getStartTime().getMinute(),
					task.getEndTime().getDayOfMonth(), task.getEndTime().getMonthValue(), task.getEndTime().getHour(),
					task.getEndTime().getMinute());
			break;
		case FLOATING :
			feedback = String.format(MESSAGE_FLOAT, task.getDesc());
			break;
		case DEADLINE :
			feedback = String.format(MESSAGE_DEADLINE, task.getDesc(), task.getEndTime().getDayOfMonth(),
					task.getEndTime().getMonthValue(), task.getEndTime().getHour(), task.getEndTime().getMinute());
			break;
		}
		return feedback;
	}

	/*
	 * Generates the feedback for the block within the given block from the task.
	 */
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
