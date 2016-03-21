package urgenda.command;

import urgenda.util.Task;

/**
 * Command interface for implementation of subsequent command classes
 *
 */
public abstract class Command {
	
	private static final String MESSAGE_EVENT = "\"%1$s\" on %2$d/%3$d, %4$02d:%5$02d - %6$02d:%7$02d";
	private static final String MESSAGE_FLOAT = "\"%1$s\"";
	private static final String MESSAGE_DEADLINE = "\"%1$s\" by %2$d/%3$d, %4$02d:%5$02d";

	// for execution of specific command
	public abstract String execute() throws Exception;

	// for generation of messages of the task given as input
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
}

