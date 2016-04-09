package test.testCommand;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Test;

import urgenda.command.Edit;
import urgenda.logic.LogicData;
import urgenda.util.Task;

public class EditTest {

	// This method is for testing whether correct feedback msg is return when
	// editing a task.
	@Test
	public void testExecute() throws Exception {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());

		Task testTask = new Task(1, "Buy milk and eggs", "FLOATING", "", false, false, false, null, null,
				LocalDateTime.now(), null, null);
		Edit tester = new Edit();
		tester.setId(0);
		tester.setNewTask(testTask);
		assertEquals("\"Buy milk\" has been edited to \"Buy milk and eggs\"", tester.execute());

		_data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		Task testTask2 = new Task(2, null, "EVENT", "", true, false, true,
				LocalDateTime.of(2016, Month.APRIL, 5, 10, 00),
				LocalDateTime.of(2016, Month.APRIL, 5, 12, 00), LocalDateTime.now(), null, null);
		Edit tester2 = new Edit();
		tester2.setNewTask(testTask2);
		tester2.setId(1);
		assertEquals(
				"\"Submit ie2150 draft\" by 24/2, 23:59 has been edited to \"Submit ie2150 draft\" on 5/4 10:00 - 5/4 12:00 Warning: Event added has already passed",
				tester2.execute());
		_data.clearTasks();

	}

	private LogicData setUpTestDisplayList() {
		LogicData _data = LogicData.getInstance(true);
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "FLOATING", "", false, false, false, notime, notime,
				LocalDateTime.now(), notime, null);
		Task obj2 = new Task(2, "Submit ie2150 draft", "DEADLINE", "", true, false, true, notime,
				LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), LocalDateTime.now(), notime, null);
		Task obj3 = new Task(3, "Submit ie2100 hw3", "DEADLINE", "", false, false, false, notime,
				LocalDateTime.now(), LocalDateTime.now(), notime, null);
		Task obj4 = new Task(4, "Dental Appointment", "EVENT", " ", true, false, false,
				LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(1), LocalDateTime.now(),
				notime, null);
		Task obj5 = new Task(5, "Travel to Sweden", "EVENT", " ", false, false, false,
				LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), LocalDateTime.now(), notime, null);
		Task obj6 = new Task(1, "Mop floor", "FLOATING", "", true, false, false, notime, notime,
				LocalDateTime.now(), notime, null);

		_data.clearTasks();
		_data.addTask(obj);
		_data.addTask(obj2);
		_data.addTask(obj3);
		_data.addTask(obj4);
		_data.addTask(obj5);
		_data.addTask(obj6);
		return _data;
	}

	@Test
	public void testUndo() throws Exception {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		Task testTask = new Task(1, "Buy milk and eggs", "FLOATING", "", false, false, false, null, null,
				LocalDateTime.now(), null, null);
		Edit tester = new Edit();
		tester.setId(0);
		tester.setNewTask(testTask);
		tester.execute();
		assertEquals("\"Buy milk and eggs\" has been reverted to \"Buy milk\"", tester.undo());
		_data.clearTasks();
	}

	@Test
	public void testRedo() throws Exception {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());

		Task testTask = new Task(1, "Buy milk and eggs", "FLOATING", "", false, false, false, null, null,
				LocalDateTime.now(), null, null);
		Edit tester = new Edit();
		tester.setId(0);
		tester.setNewTask(testTask);
		tester.execute();
		tester.undo();
		assertEquals("\"Buy milk\" has been edited to \"Buy milk and eggs\"", tester.redo());
		_data.clearTasks();
	}

}
