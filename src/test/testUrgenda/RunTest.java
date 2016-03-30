package testUrgenda;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import urgenda.logic.Logic;
import urgenda.util.StateFeedback;
import urgenda.util.Task;

public class RunTest {
	
	private static final String ADD_COMMAND = "add ";
	private static final int ADD_INDEX = 0;

	@Test
	public void testAdd() {
		String desc = "floating task";
		
		Logic logic = Logic.getInstance(true);
		StateFeedback actualOutput = logic.executeCommand(ADD_COMMAND + desc, ADD_INDEX);
		Task newTask = new Task(desc);
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(newTask);
		StateFeedback expectedOutput = new StateFeedback(tasks, 0, 0, 1);
		System.out.println(actualOutput.getAllTasks().getTasks());
		System.out.println(tasks);
		assertTrue(actualOutput.getAllTasks().getTasks().equals(tasks));
		
		
	}

}
