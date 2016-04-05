package test.testCommand;

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
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy new specs", "floating", "", false, false, false, notime, notime,
				LocalDateTime.now(), notime, null);
		Task obj2 = new Task(2, "Submit cs2103 v0.1", "deadline", "", true, false, true, notime,
				LocalDateTime.of(2016, Month.FEBRUARY, 14, 23, 59), LocalDateTime.now(), notime, null);
		Task obj3 = new Task(3, "Submit admission application", "deadline", "", false, false, false, notime,
				LocalDateTime.of(2016, Month.AUGUST, 31, 23, 59), LocalDateTime.now(), notime, null);
		Task obj4 = new Task(4, "Dinner w relatives", "event", " ", false, false, false,
				LocalDateTime.of(2016, Month.JUNE, 14, 18, 00), LocalDateTime.of(2016, Month.JUNE, 14, 20, 30),
				LocalDateTime.now(), notime, null);
		Task obj5 = new Task(5, "Travel to Sweden", "event", " ", false, false, false,
				LocalDateTime.of(2016, Month.JULY, 26, 00, 00), LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59),
				LocalDateTime.now(), notime, null);
		Task obj6 = new Task(1, "Mop floor", "floating", "", true, false, false, notime, notime, LocalDateTime.now(),
				notime, null);
		Task obj7 = new Task(1, "Wash car", "floating", "", true, false, false, notime, notime, LocalDateTime.now(),
				notime, null);
		Task obj8 = new Task(1, "Do survey", "floating", "", true, false, false, notime, notime, LocalDateTime.now(),
				notime, null);


		_data.clearTasks();
		_data.addTask(obj);
		_data.addTask(obj2);
		_data.addTask(obj3);
		_data.addTask(obj4);
		_data.addTask(obj5);
		_data.addTask(obj6);
		_data.addTask(obj7);
		_data.addTask(obj8);

		_data.setDisplays(_data.getTaskList());
		ShowDetails test = new ShowDetails();
		ArrayList<Integer> range = new ArrayList<Integer>();
		range.add(3);
		test.setPosition(range);
		assertEquals("Showing more details for \"Dinner w relatives\"", test.execute()); //test single match
		ShowDetails test2 = new ShowDetails();
		range.clear();
		range.add(0); //test min acceptable boundary
		range.add(_data.getDisplays().size()-1); //test max acceptable boundary
		test2.setPosition(range);
		assertEquals("Changed showing details for 2 tasks\n" +
				"Showing more details for \"Buy new specs\", \"Do survey\"", test2.execute());
		ShowDetails test3 = new ShowDetails();
		range.clear();
		range.add(-6);
		test3.setPosition(range);
		String feedback;
		try {
			feedback = test3.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		assertEquals("Invalid position(s) to showmore details", feedback); // test negative
		ShowDetails test4 = new ShowDetails();																	// boundary
		range.clear();
		test4.setPosition(range);
		try {
			feedback = test4.execute();
		} catch (Exception e) {
			feedback = e.getMessage();
		}
		assertEquals("Invalid position(s) to showmore details", feedback); // test empty positions
		ShowDetails test5 = new ShowDetails();
		range.clear();
		range.add(0);
		range.add(2);
		range.add(4);
		test5.setPosition(range);
		assertEquals("Changed showing details for 2 tasks\n" +
				"Showing more details for \"Submit admission application\", \"Travel to Sweden\"", test5.execute());
		ShowDetails test6 = new ShowDetails();
		range.clear();
		range.add(7);
		range.add(8);
		test6.setPosition(range);
		assertEquals("Showing less details for \"Do survey\"", test6.execute());
		ShowDetails test7 = new ShowDetails();
		range.clear();
		range.add(0);
		range.add(3);
		test7.setPosition(range);
		assertEquals("Changed showing details for 2 tasks\n" +
				"Showing less details for \"Buy new specs\", \"Dinner w relatives\"", test7.execute());
		
	}
}
