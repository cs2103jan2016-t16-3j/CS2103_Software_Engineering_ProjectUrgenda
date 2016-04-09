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
		LogicData _data = LogicData.getInstance(true);
		_data.clearTasks();
		LocalDateTime notime = null;
		ArrayList<Task> expectedTasks = new ArrayList<Task>();
		Task newinput = new Task(4, "Do survey", "floating", "", true, false, false, notime, notime,
				LocalDateTime.now(), notime, null);
		expectedTasks.add(newinput);
		AddTask tester = new AddTask(newinput);
		// test normal add
		assertEquals("\"Do survey\" added", tester.execute());

		AddTask tester2 = new AddTask();
		String feedback;
		try {
			feedback = tester2.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		// test invalid task
		assertEquals("Error: Task has no description", feedback);

		Task newinput2 = new Task(5, "lunch w boss", "event", "", true, false, false,
				LocalDateTime.of(2016, Month.MARCH, 30, 00, 00),
				LocalDateTime.of(2016, Month.MARCH, 30, 23, 59), LocalDateTime.now(), notime, null);
		expectedTasks.add(newinput2);
		AddTask tester3 = new AddTask(newinput2);
		// test warning
		assertEquals("\"lunch w boss\" on 30/3 00:00 - 30/3 23:59 added"
				+ "\nWarning: Event added has already passed", tester3.execute());
		checkArrayList(expectedTasks, _data.getTaskList());
		_data.clearTasks();

	}

	private void checkArrayList(ArrayList<Task> expectedTasks, ArrayList<Task> actualTasks) {
		for (int i = 0; i < actualTasks.size(); i++) {
			Task exTask = expectedTasks.get(i);
			Task actlTask = actualTasks.get(i);
			assertEquals(exTask.getDesc(), actlTask.getDesc());
			assertEquals(exTask.getLocation(), actlTask.getLocation());
			assertEquals(exTask.isCompleted(), actlTask.isCompleted());
			assertEquals(exTask.isImportant(), actlTask.isImportant());
			assertEquals(exTask.isOverdue(), actlTask.isOverdue());
			assertEquals(exTask.getStartTime(), actlTask.getStartTime());
			assertEquals(exTask.getEndTime(), actlTask.getEndTime());
			assertEquals(exTask.getTaskType(), actlTask.getTaskType());
		}
	}

	@Test
	public void testUndo() throws LogicException {
		LogicData _data = LogicData.getInstance(true);
		ArrayList<Task> _tasks = new ArrayList<Task>();
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime,
				LocalDateTime.now(), notime, null);
		_tasks.add(obj);
		_data.setDisplays(_tasks);
		AddTask tester = new AddTask(obj);
		tester.execute();
		assertEquals("\"Buy milk\" removed", tester.undo());
		_data.clearTasks();
	}

	@Test
	public void testRedo() throws LogicException {
		LogicData _data = LogicData.getInstance(true);
		ArrayList<Task> _tasks = new ArrayList<Task>();
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime,
				LocalDateTime.now(), notime, null);
		_tasks.add(obj);
		_data.setDisplays(_tasks);
		AddTask tester = new AddTask(obj);
		tester.execute();
		tester.undo();
		assertEquals("\"Buy milk\" added", tester.redo());
		_data.clearTasks();
	}

}
