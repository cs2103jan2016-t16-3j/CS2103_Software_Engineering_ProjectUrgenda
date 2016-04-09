package test.testCommand;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.command.AddTask;
import urgenda.logic.LogicData;
import urgenda.util.LogicException;
import urgenda.util.Task;

public class AddTaskTest {

	@Test
	public void testExecute() throws LogicException {
		LocalDateTime notime = null;
		Task newinput = new Task(4, "Do survey", "floating", "", true, false, false, notime, notime, LocalDateTime.now(),
				notime, null);
		AddTask tester = new AddTask(newinput);
		assertEquals("\"Do survey\" added", tester.execute()); //test normal add
		AddTask tester2 = new AddTask();
		String feedback;
		try {
			feedback = tester2.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		assertEquals("Error: Task has no description", feedback); // test invalid task
		Task newinput2 = new Task(5, "lunch w boss", "event", "", true, false, false, LocalDateTime.of(2016, Month.MARCH, 30, 00, 00), LocalDateTime.of(2016, Month.MARCH, 30, 23, 59), LocalDateTime.now(),
				notime, null);
		AddTask tester3 = new AddTask(newinput2);
		assertEquals("\"lunch w boss\" on 30/3 00:00 - 30/3 23:59 added" +
				"\nWarning: Event added has already passed", tester3.execute()); //test warning
		
	}
	
	@Test
	public void testUndo() throws LogicException {
		LogicData _data = LogicData.getInstance(true);
		ArrayList<Task> _tasks = new ArrayList<Task>();
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime, LocalDateTime.now(),
				notime, null);
		_tasks.add(obj);
		_data.setDisplays(_tasks);
		AddTask tester = new AddTask(obj);
		tester.execute();
		assertEquals("\"Buy milk\" removed", tester.undo());
		
	}
	
	@Test
	public void testRedo() throws LogicException {
		LogicData _data = LogicData.getInstance(true);
		ArrayList<Task> _tasks = new ArrayList<Task>();
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime, LocalDateTime.now(),
				notime, null);
		_tasks.add(obj);
		_data.setDisplays(_tasks);
		AddTask tester = new AddTask(obj);
		tester.execute();
		tester.undo();
		assertEquals("\"Buy milk\" added", tester.redo());
	}
	
	

}
