package testCommand;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.command.Complete;
import urgenda.logic.LogicData;
import urgenda.util.Task;

public class CompleteTest {

	// this fn is to test marking of task specified by desc as done
	@Test
	public void testExecuteDesc() throws Exception {
		LogicData _data = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime, LocalDateTime.now(),
				notime, _tags, null);
		Task obj2 = new Task(2, "Submit ie2150 draft", "deadline", "", true, false, true, notime,
				LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), LocalDateTime.now(), notime, _tags, null);
		Task obj3 = new Task(3, "Submit ie2100 hw3", "deadline", "", false, false, false, notime,
				LocalDateTime.of(2016, Month.AUGUST, 4, 23, 59), LocalDateTime.now(), notime, _tags, null);
		Task obj4 = new Task(4, "Dental Appointment", "event", " ", true, false, false,
				LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(1), LocalDateTime.now(), notime, _tags,
				null);
		Task obj5 = new Task(5, "Travel to Sweden", "event", " ", false, false, false,
				LocalDateTime.of(2016, Month.JULY, 26, 00, 00), LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59),
				LocalDateTime.now(), notime, _tags, null);
		Task obj6 = new Task(1, "Mop floor", "floating", "", true, false, false, notime, notime, LocalDateTime.now(),
				notime, _tags, null);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);

		_data.setDisplays(_tasks);
		Complete tester = new Complete();
		tester.setDesc("Sweden");
		assertEquals("Done \"Travel to Sweden\" on 26/7, 00:00 - 23:59!", tester.execute()); // test
																								// done
																								// by
																								// desc
		Complete tester2 = new Complete();
		tester2.setDesc("mum");
		String feedback;
		try {
			feedback = tester2.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		assertEquals("No matches found to complete", feedback); // test done by
																// desc no match
		Complete tester3 = new Complete();
		tester3.setDesc("Submit");
		try {
			feedback = tester3.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		assertEquals("Multiple tasks with description \"Submit\" found", feedback);// test
																					// done
																					// by
																					// desc
																					// multi
																					// match
	}

	// this fn is to test marking of task specified by positions/task num as
	// done
	@Test
	public void testExecutePositions() throws Exception {
		LogicData _data = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime, LocalDateTime.now(),
				notime, _tags, null);
		Task obj2 = new Task(2, "Submit ie2150 draft", "deadline", "", true, false, true, notime,
				LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), LocalDateTime.now(), notime, _tags, null);
		Task obj3 = new Task(3, "Submit ie2100 hw3", "deadline", "", false, false, false, notime,
				LocalDateTime.of(2016, Month.AUGUST, 4, 23, 59), LocalDateTime.now(), notime, _tags, null);
		Task obj4 = new Task(4, "Dental Appointment", "event", " ", false, false, false,
				LocalDateTime.of(2016, Month.SEPTEMBER, 28, 14, 00),
				LocalDateTime.of(2016, Month.SEPTEMBER, 28, 17, 30), LocalDateTime.now(), notime, _tags, null);
		Task obj5 = new Task(5, "Travel to Sweden", "event", " ", false, false, false,
				LocalDateTime.of(2016, Month.JULY, 26, 00, 00), LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59),
				LocalDateTime.now(), notime, _tags, null);
		Task obj6 = new Task(1, "Mop floor", "floating", "", true, false, false, notime, notime, LocalDateTime.now(),
				notime, _tags, null);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);

		_data.setDisplays(_tasks);
		Complete tester = new Complete();
		ArrayList<Integer> range = new ArrayList<Integer>();
		range.add(0); // test min acceptable boundary
		range.add(2);
		range.add(_tasks.size() - 1); // test max acceptable boundary
		tester.setPositions(range);
		assertEquals("3 tasks have been done:\n" + "\"Buy milk\"\n" + "\"Submit ie2100 hw3\" by 4/8, 23:59\n"
				+ "\"Mop floor\"", tester.execute()); // test pri by positions
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
		assertEquals("No matches found to complete", feedback); // test negative
																// boundary
		range.clear();
		Complete tester3 = new Complete();
		tester3.setPositions(range);
		try {
			feedback = tester3.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		assertEquals("No matches found to complete", feedback); // test empty
																// positions
	}

	@Test
	public void testUndo() throws Exception {
		LogicData _data = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime, LocalDateTime.now(),
				notime, _tags, null);
		Task obj2 = new Task(2, "Submit ie2150 draft", "deadline", "", true, false, true, notime,
				LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), LocalDateTime.now(), notime, _tags, null);
		Task obj3 = new Task(3, "Submit ie2100 hw3", "deadline", "", false, false, false, notime,
				LocalDateTime.of(2016, Month.AUGUST, 4, 23, 59), LocalDateTime.now(), notime, _tags, null);
		Task obj4 = new Task(4, "Dental Appointment", "event", " ", true, false, false,
				LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(1), LocalDateTime.now(), notime, _tags,
				null);
		Task obj5 = new Task(5, "Travel to Sweden", "event", " ", false, false, false,
				LocalDateTime.of(2016, Month.JULY, 26, 00, 00), LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59),
				LocalDateTime.now(), notime, _tags, null);
		Task obj6 = new Task(1, "Mop floor", "floating", "", true, false, false, notime, notime, LocalDateTime.now(),
				notime, _tags, null);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);

		_data.setDisplays(_tasks);
		Complete tester = new Complete();
		tester.setDesc("Sweden");
		tester.execute();
		assertEquals("To do \"Travel to Sweden\" on 26/7, 00:00 - 23:59!", tester.undo()); //test undo of a complete by desc
		ArrayList<Integer> range = new ArrayList<Integer>();
		range.clear();
		range.add(0);
		range.add(1);
		Complete tester2 = new Complete();
		tester2.setPositions(range);
		tester2.execute();
		assertEquals("2 tasks to be done:\n" + "\"Buy milk\"\n" + "\"Submit ie2150 draft\" by 24/2, 23:59", tester2.undo()); //test undo of a complete by positions
	}
	
	@Test
	public void testRedo() throws Exception {
		LogicData _data = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, null);
		Task obj2 = new Task(2, "Submit ie2150 draft", "deadline", "", true, false, true, notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), LocalDateTime.now(), notime, _tags, null);
		Task obj3 = new Task(3, "Submit ie2100 hw3", "deadline", "", false, false, false, notime, LocalDateTime.of(2016, Month.AUGUST, 4, 23, 59), LocalDateTime.now(), notime, _tags, null);
		Task obj4 = new Task(4, "Dental Appointment", "event", " ", true, false, false, LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(1), LocalDateTime.now(), notime,  _tags, null);
		Task obj5 = new Task(5, "Travel to Sweden", "event", " ", false, false, false, LocalDateTime.of(2016, Month.JULY, 26, 00, 00), LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), LocalDateTime.now(), notime,  _tags, null);
		Task obj6 = new Task(1, "Mop floor", "floating", "", true, false, false, notime, notime, LocalDateTime.now(), notime, _tags, null);
		
		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);
		
		_data.setDisplays(_tasks);
		Complete tester = new Complete();
		tester.setDesc("Sweden");
		tester.execute();
		tester.undo();
		assertEquals("Done \"Travel to Sweden\" on 26/7, 00:00 - 23:59!", tester.redo());
	}
	
}
