//@@author A0131857B
package test.testCommand;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.command.Complete;
import urgenda.logic.LogicData;
import urgenda.util.Task;

/**
 * Test class for commands to mark tasks as completed.
 * 
 * @author KangSoon
 *
 */
public class CompleteTest {

	/**
	 * Tests for the marking as complete command functionality.
	 * 
	 * @throws Exception
	 *             thrown exception when executing command
	 */
	@Test
	public void testExecuteDesc() throws Exception {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());

		Complete tester = new Complete();
		tester.setDesc("Sweden");
		// mark by description
		assertEquals("Done \"Travel to Sweden\" on 26/7 00:00 - 17/8 23:59!", tester.execute());
		assertTrue(_data.getTaskList().get(4).isCompleted());

		Complete tester2 = new Complete();
		tester2.setDesc("mum");
		String feedback;
		try {
			feedback = tester2.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		// tests when no matches are found
		assertEquals("No matches found to complete", feedback);
		Complete tester3 = new Complete();
		tester3.setDesc("Submit");
		try {
			feedback = tester3.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		// tests for multiple matches
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
				LocalDateTime.of(2016, Month.OCTOBER, 4, 11, 00),
				LocalDateTime.of(2016, Month.OCTOBER, 5, 11, 00), LocalDateTime.now(), notime, null);
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

	/**
	 * Tests for returned position indexes when executing marking of tasks as
	 * done.
	 * 
	 * @throws Exception
	 *             thrown exception when executing command
	 */
	@Test
	public void testExecutePositions() throws Exception {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);

		Complete tester = new Complete();
		ArrayList<Integer> range = new ArrayList<Integer>();
		// boundary value for minimum
		range.add(0);
		range.add(2);
		// boundary value for max
		range.add(_data.getDisplays().size() - 1);
		tester.setPositions(range);
		// test marking by positions
		assertEquals("3 tasks have been marked as done: \"Buy milk\", \"Submit ie2100 hw3\", \"Mop floor\"",
				tester.execute());
		range.clear();
		range.add(-6);
		Complete tester2 = new Complete();
		tester2.setPositions(range);
		String feedback;
		try {
			feedback = tester2.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		// test negative values for indexes
		assertEquals("No matches found to complete", feedback);
		range.clear();
		Complete tester3 = new Complete();
		tester3.setPositions(range);
		try {
			feedback = tester3.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		// test for indexes that tasks do not exist at
		assertEquals("No matches found to complete", feedback);
		_data.clearTasks();
	}

	/**
	 * Tests undo of previous marking as complete.
	 * 
	 * @throws Exception
	 *             thrown exception from executing command
	 */
	@Test
	public void testUndo() throws Exception {
		LogicData _data = setUpTestDisplayList();

		_data.setDisplays(_data.getTaskList());
		Complete tester = new Complete();
		tester.setDesc("Sweden");
		tester.execute();
		// test undo of a complete by desc
		assertEquals("\"Travel to Sweden\" on 26/7 00:00 - 17/8 23:59 unmarked from done!", tester.undo());

		ArrayList<Integer> range = new ArrayList<Integer>();
		range.clear();
		range.add(0);
		range.add(1);
		Complete tester2 = new Complete();
		tester2.setPositions(range);
		tester2.execute();
		// test undo of a complete by desc
		assertEquals("2 have been unmarked from done: \"Buy milk\", \"Submit ie2150 draft\"", tester2.undo());
		_data.clearTasks();
	}

	/**
	 * Tests redo of previous marking as complete.
	 * 
	 * @throws Exception
	 *             thrown exception from executing command
	 */
	@Test
	public void testRedo() throws Exception {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		Complete tester = new Complete();
		tester.setDesc("Sweden");
		tester.execute();
		tester.undo();
		assertEquals("Done \"Travel to Sweden\" on 26/7 00:00 - 17/8 23:59!", tester.redo());
		_data.clearTasks();
	}
}
