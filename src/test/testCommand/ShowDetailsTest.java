package testCommand;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.command.ShowDetails;
import urgenda.logic.LogicData;
import urgenda.util.Task;

public class ShowDetailsTest {

	@Test
	public void testExecute() throws Exception {
		LogicData _data = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task(1, "But new specs", "floating", "", false, false, false, notime, notime,
				LocalDateTime.now(), notime, _tags, null);
		Task obj2 = new Task(2, "Submit cs2103 v0.1", "deadline", "", true, false, true, notime,
				LocalDateTime.of(2016, Month.FEBRUARY, 14, 23, 59), LocalDateTime.now(), notime, _tags, null);
		Task obj3 = new Task(3, "Submit admission application", "deadline", "", false, false, false, notime,
				LocalDateTime.of(2016, Month.AUGUST, 31, 23, 59), LocalDateTime.now(), notime, _tags, null);
		Task obj4 = new Task(4, "Dinner w relatives", "event", " ", false, false, false,
				LocalDateTime.of(2016, Month.JUNE, 14, 18, 00), LocalDateTime.of(2016, Month.JUNE, 14, 20, 30),
				LocalDateTime.now(), notime, _tags, null);
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
		ShowDetails test = new ShowDetails();
		ArrayList<Integer> range = new ArrayList<Integer>();
		range.add(3);
		test.setPosition(range);
		assertEquals("Showing more details for \"Dinner w relatives\"", test.execute()); //test single match
		ShowDetails test2 = new ShowDetails();
		range.clear();
		range.add(0); //test min acceptable boundary
		range.add(_tasks.size()-1); //test max acceptable boundary
		test2.setPosition(range);
		assertEquals("Changed showing details for 2 tasks\n" +
				"Showing more details for \"But new specs\", \"Mop floor\"", test2.execute());
	}
}
