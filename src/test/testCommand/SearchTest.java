package test.testCommand;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Test;

import urgenda.command.Search;
import urgenda.logic.LogicData;
import urgenda.util.Task;

public class SearchTest {

	//This method is for testing whether correct feedbck msg was returned when searching for tasks.
	@Test
	public void testExecute() {
		LogicData _data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		Search tester = new Search();
		tester.setSearchInput("Sweden");
		// test search by desc
		assertEquals("Showing: all task(s) found containing \"Sweden\"", tester.execute());

		_data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		Search tester2 = new Search();
		tester2.setSearchDate(LocalDate.of(2016, Month.FEBRUARY, 24));
		// test search by date
		assertEquals("These are all the task(s) falling on \"2016-02-24\"", tester2.execute());

		_data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		Search tester3 = new Search();
		tester3.setSearchDateTime(LocalDateTime.of(2016, Month.JULY, 26, 00, 00));
		// test search by datetime
		assertEquals("These are all the task(s) falling on \"2016-07-26, 00:00\"", tester3.execute());

		_data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		Search tester4 = new Search();
		tester4.setSearchMonth(Month.AUGUST);
		// test search by month
		assertEquals("These are all the task(s) falling on \"AUGUST\"", tester4.execute());

		_data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		Search tester5 = new Search();
		tester5.setSearchId(0);
		assertEquals("Search Result: Showing detailed info of task no. 1", tester5.execute());

		_data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		Search tester6 = new Search();
		tester6.setSearchInput("floating");
		// test search tasktype
		assertEquals("Showing: all task(s) found of type \"floating\"", tester6.execute());

		Search tester7 = new Search();
		tester7.setSearchInput("milk");
		// test progressive search
		assertEquals(
				"PROGRESSIVE SEARCH: all task(s) found containing \"milk\" based on the current view. Enter home to show all tasks",
				tester7.execute());
		
		_data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		Search tester8 = new Search();
		tester8.setSearchInput("hello");
		//test no match found
		assertEquals("There is no match found for \"hello\"", tester8.execute());
		
		_data = setUpTestDisplayList();
		_data.setDisplays(_data.getTaskList());
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		Search tester9 = new Search("Buy egg");
		//test near match
		assertEquals("No exact match found for \"Buy egg\". Showing: 1 near match(es)", tester9.execute());
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
}
