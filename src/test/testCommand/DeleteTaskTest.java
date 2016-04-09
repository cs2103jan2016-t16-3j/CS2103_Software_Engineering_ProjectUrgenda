package test.testCommand;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.command.DeleteTask;
import urgenda.logic.LogicData;
import urgenda.util.LogicException;
import urgenda.util.Task;

public class DeleteTaskTest {

	@Test
	public void testExecute() throws LogicException {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());

		DeleteTask test = new DeleteTask();
		test.setDesc("Mop Floor");
		assertEquals("\"Mop floor\" removed", test.execute()); // test deleting
																// by desc

		DeleteTask test2 = new DeleteTask();
		ArrayList<Integer> range = new ArrayList<Integer>();
		range.add(0); // boundary value
		range.add(_data.getDisplays().size() - 1); // boundary value
		test2.setPositions(range);
		//test delete by position
		assertEquals("2 tasks have been removed: \"Buy milk\", \"Travel to Sweden\"", test2.execute()); 

		DeleteTask test3 = new DeleteTask();
		range.clear();
		range.add(10);
		test3.setPositions(range);
		String feedback;
		try {
			feedback = test3.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		assertEquals("No matches found to delete", feedback); // test no match
	}

	private LogicData setUpTestDisplayList() {
		LogicData _data = LogicData.getInstance(true);
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime,
				LocalDateTime.now(), notime, null);
		Task obj2 = new Task(2, "Submit ie2150 draft", "deadline", "", true, false, true, notime,
				LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), LocalDateTime.now(), notime, null);
		Task obj3 = new Task(3, "Submit ie2100 hw3", "deadline", "", false, false, false, notime,
				LocalDateTime.of(2016, Month.AUGUST, 4, 23, 59), LocalDateTime.now(), notime, null);
		Task obj4 = new Task(4, "Dental Appointment", "event", " ", true, false, false,
				LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(1), LocalDateTime.now(),
				notime, null);
		Task obj5 = new Task(5, "Travel to Sweden", "event", " ", false, false, false,
				LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), LocalDateTime.now(), notime, null);
		Task obj6 = new Task(1, "Mop floor", "floating", "", true, false, false, notime, notime,
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
	public void testUndo() throws LogicException {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		DeleteTask test = new DeleteTask();
		test.setDesc("Mop Floor");
		test.execute();
		assertEquals("\"Mop floor\" added", test.undo()); // test undo
	}

	@Test
	public void testRedo() throws LogicException {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		DeleteTask test = new DeleteTask();
		test.setDesc("Mop Floor");
		test.execute();
		test.undo();
		assertEquals("\"Mop floor\" removed", test.redo()); // test redo
	}

}
