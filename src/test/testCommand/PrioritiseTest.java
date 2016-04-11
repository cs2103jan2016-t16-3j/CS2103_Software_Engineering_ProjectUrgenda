//@@author A0127358Y
package test.testCommand;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.command.Prioritise;
import urgenda.logic.LogicData;
import urgenda.util.Task;

public class PrioritiseTest {

	// this method is to test Prioritise by specifying task desc
	@Test
	public void testExecuteDesc() throws Exception {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());

		Prioritise tester = new Prioritise();
		tester.setDesc("Sweden");
		// test pri by desc
		assertEquals("\"Travel to Sweden\" marked as important", tester.execute());
		assertTrue(_data.getTaskList().get(4).isImportant());

		Prioritise tester2 = new Prioritise();
		tester2.setDesc("mum");
		String feedback;
		try {
			feedback = tester2.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		// test pri by desc no match
		assertEquals("No matches found to prioritise", feedback);

		Prioritise tester3 = new Prioritise();
		tester3.setDesc("Submit");
		try {
			feedback = tester3.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		// test pri by desc multi matches
		assertEquals("Multiple tasks with description \"Submit\" found", feedback);
		_data.clearTasks();
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
		Task obj6 = new Task(6, "Mop floor", "floating", "", true, false, false, notime, notime,
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

	// this method is to test Prioritise by specifying task positions.
	@Test
	public void testExecutePositions() throws Exception {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());

		Prioritise tester = new Prioritise();
		ArrayList<Integer> range = new ArrayList<Integer>();
		range.add(0); // test min acceptable boundary
		range.add(3);
		range.add(_data.getDisplays().size() - 1); // test max acceptable
													// boundary
		tester.setPositions(range);
		assertEquals(
				"Priority of 3 tasks have been changed:\n"
						+ "\"Buy milk\", \"Dental Appointment\", \"Mop floor\" marked as important",
				tester.execute()); // test pri by positions
		assertTrue(_data.getTaskList().get(0).isImportant());
		assertTrue(_data.getTaskList().get(3).isImportant());
		assertTrue(_data.getTaskList().get(5).isImportant());

		range.clear();
		range.add(-6);
		Prioritise tester2 = new Prioritise();
		tester2.setPositions(range);
		String feedback;
		try {
			feedback = tester2.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		assertEquals("No matches found to prioritise", feedback);

		range.clear();
		range.add(0);
		range.add(4);
		Prioritise tester3 = new Prioritise();
		tester3.setPositions(range);
		assertEquals("Priority of 1 tasks have been changed:\n" + "\"Travel to Sweden\" marked as important",
				tester3.execute()); // test pri range consisting of pri and
									// unpri task
		assertTrue(_data.getTaskList().get(0).isImportant());
		assertTrue(_data.getTaskList().get(4).isImportant());

		range.clear();
		Prioritise tester4 = new Prioritise();
		tester4.setPositions(range);
		try {
			feedback = tester4.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		assertEquals("No matches found to prioritise", feedback);
		_data.clearTasks();
	}

	// This method is to test undo of Prioritise.
	@Test
	public void testUndo() throws Exception {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());

		Prioritise tester = new Prioritise();
		tester.setDesc("Sweden");
		tester.execute();
		assertEquals("\"Travel to Sweden\" unmarked from important", tester.undo());
		assertFalse(_data.getTaskList().get(4).isImportant());

		ArrayList<Integer> range = new ArrayList<Integer>();
		range.clear();
		range.add(0);
		range.add(1);
		Prioritise tester2 = new Prioritise();
		tester2.setPositions(range);
		tester2.execute();
		assertEquals("Priority of 2 tasks have been changed:\n"
				+ "\"Buy milk\", \"Submit ie2150 draft\" unmarked from important", tester2.undo());
		assertFalse(_data.getTaskList().get(0).isImportant());
		assertFalse(_data.getTaskList().get(1).isImportant());
	}

	// This method is to test redo of prioritise.
	@Test
	public void testRedo() throws Exception {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		Prioritise tester = new Prioritise();
		tester.setDesc("Sweden");
		tester.execute();
		tester.undo();
		assertEquals("\"Travel to Sweden\" marked as important", tester.redo());
		assertTrue(_data.getTaskList().get(4).isImportant());
		_data.clearTasks();
	}

}
