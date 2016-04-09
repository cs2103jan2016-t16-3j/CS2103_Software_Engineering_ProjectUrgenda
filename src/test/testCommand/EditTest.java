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
		checkTask(testTask, _data.getTaskList().get(0));

		_data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		Task testTask2 = new Task(2, null, "EVENT", "", false, false, false,
				LocalDateTime.of(2016, Month.APRIL, 5, 10, 00),
				LocalDateTime.of(2016, Month.APRIL, 5, 12, 00), LocalDateTime.now(), null, null);
		Task exTask = new Task(2, "Buy milk", "EVENT", "", false, false, false,
				LocalDateTime.of(2016, Month.APRIL, 5, 10, 00),
				LocalDateTime.of(2016, Month.APRIL, 5, 12, 00), LocalDateTime.now(), null, null);
		Edit tester2 = new Edit();
		tester2.setNewTask(testTask2);
		tester2.setId(0);
		assertEquals(
				"\"Buy milk\" has been edited to \"Buy milk\" on 5/4 10:00 - 5/4 12:00 Warning: Event added has already passed",
				tester2.execute());
		checkTask(exTask, _data.getTaskList().get(0));
		_data.clearTasks();

	}

	private LogicData setUpTestDisplayList() {
		LogicData _data = LogicData.getInstance(true);
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "FLOATING", "", false, false, false, notime, notime,
				LocalDateTime.now(), notime, null);

		_data.clearTasks();
		_data.addTask(obj);
		return _data;
	}
	
	private void checkTask(Task exTask,Task actlTask) {
			assertEquals(exTask.getDesc(), actlTask.getDesc());
			assertEquals(exTask.getLocation(), actlTask.getLocation());
			assertEquals(exTask.isCompleted(), actlTask.isCompleted());
			assertEquals(exTask.isImportant(), actlTask.isImportant());
			assertEquals(exTask.isOverdue(), actlTask.isOverdue());
			assertEquals(exTask.getStartTime(), actlTask.getStartTime());
			assertEquals(exTask.getEndTime(), actlTask.getEndTime());
			assertEquals(exTask.getTaskType(), actlTask.getTaskType());
		}

	@Test
	public void testUndo() throws Exception {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		Task testTask = new Task(1, "Buy milk and eggs", "FLOATING", "", false, false, false, null, null,
				LocalDateTime.now(), null, null);
		Task exTask = new Task(1, "Buy milk", "FLOATING", "", false, false, false, null, null,
				LocalDateTime.now(), null, null);
		Edit tester = new Edit();
		tester.setId(0);
		tester.setNewTask(testTask);
		tester.execute();
		assertEquals("\"Buy milk and eggs\" has been reverted to \"Buy milk\"", tester.undo());
		checkTask(exTask, _data.getTaskList().get(0));
		_data.clearTasks();
	}

	@Test
	public void testRedo() throws Exception {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());

		Task testTask = new Task(1, "Buy milk and eggs", "FLOATING", "", false, false, false, null, null,
				LocalDateTime.now(), null, null);
		Task exTask = new Task(1, "Buy milk and eggs", "FLOATING", "", false, false, false, null, null,
				LocalDateTime.now(), null, null);
		Edit tester = new Edit();
		tester.setId(0);
		tester.setNewTask(testTask);
		tester.execute();
		tester.undo();
		assertEquals("\"Buy milk\" has been edited to \"Buy milk and eggs\"", tester.redo());
		checkTask(exTask, _data.getTaskList().get(0));
		_data.clearTasks();
	}

}
