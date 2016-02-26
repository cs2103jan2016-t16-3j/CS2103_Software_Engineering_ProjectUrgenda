package urgenda.parser;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.command.AddTask;
import urgenda.command.Command;
import urgenda.util.Task;

public class Parser {
	
	public static Command parseCommand(String command) {
		Task taskU = new Task("Urgent task", "U location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), true);
		Command cmd = new AddTask(taskU);
		return cmd;
	}
	
}
