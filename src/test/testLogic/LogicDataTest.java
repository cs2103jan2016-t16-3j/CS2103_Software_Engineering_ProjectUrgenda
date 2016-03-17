package test.testLogic;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class LogicDataTest {

	@Test
	public void testSortList() { // test if sort fn returns correct sorted order
		LogicData _test = LogicData.getInstance();;
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime, _tags);
		Task obj2 = new Task("Submit ie2150 draft", "", notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59),
				_tags);
		Task obj3 = new Task("Dental Appointment", "", LocalDateTime.of(2016, Month.APRIL, 4, 10, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 12, 00), _tags);
		Task obj4 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), _tags);
		Task obj5 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.of(2016, Month.MARCH, 3, 17, 00), _tags);
		Task obj6 = new Task("Housekeeping", "", notime, notime, _tags);
		Task obj7 = new Task("Dinner w mum", "", LocalDateTime.of(2016, Month.JANUARY, 8, 19, 00),
				LocalDateTime.of(2016, Month.JANUARY, 8, 20, 00), _tags);
		Task obj8 = new Task("Project Presentation", "", LocalDateTime.of(2016, Month.MAY, 8, 10, 00),
				LocalDateTime.of(2016, Month.MAY, 8, 12, 00), _tags);
		Task obj9 = new Task("Bro Birthday Dinner", "", LocalDateTime.of(2016, Month.AUGUST, 21, 20, 00),
				LocalDateTime.of(2016, Month.AUGUST, 21, 21, 00), _tags);
		Task obj10 = new Task("CS2103 V0.1", "", notime, LocalDateTime.of(2016, Month.MARCH, 10, 17, 00), _tags);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);
		_tasks.add(obj7);
		_tasks.add(obj8);
		_tasks.add(obj9);
		_tasks.add(obj10);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj7);
		_output.add(obj2);
		_output.add(obj5);
		_output.add(obj10);
		_output.add(obj3);
		_output.add(obj8);
		_output.add(obj4);
		_output.add(obj9);
		_output.add(obj);
		_output.add(obj6);

		assertEquals(_output, _test.sortList(_tasks));
	}

	@Test
	public void testFindMatchingTasks() { // test if findmatchingTasks returns
											// right arraylist containing all
											// required searched description
		LogicData _test = LogicData.getInstance();;
		ArrayList<Task> _tasks = new ArrayList<Task>();
		_test.setDisplays(_tasks);
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime, _tags);
		Task obj2 = new Task("Buy Book", "", notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), _tags);
		Task obj3 = new Task("Dental Appointment", "", LocalDateTime.of(2016, Month.APRIL, 4, 10, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 12, 00), _tags);
		Task obj4 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), _tags);
		Task obj5 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.of(2016, Month.MARCH, 3, 17, 00), _tags);
		Task obj6 = new Task("Housekeeping", "", notime, notime, _tags);
		Task obj7 = new Task("Submit ie2150 project draft", "", notime,
				LocalDateTime.of(2016, Month.JANUARY, 8, 20, 00), _tags);
		Task obj8 = new Task("Project Presentation", "", LocalDateTime.of(2016, Month.MAY, 8, 10, 00),
				LocalDateTime.of(2016, Month.MAY, 8, 12, 00), _tags);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);
		_tasks.add(obj7);
		_tasks.add(obj8);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj);
		_output.add(obj2);

		assertEquals(_output, _test.findMatchingDesc("Buy"));

		_output.clear();
		_output.add(obj7);
		_output.add(obj8);

		assertEquals(_output, _test.findMatchingDesc("project"));

		_output.clear();
		_output.add(obj5);
		_output.add(obj7);

		assertEquals(_output, _test.findMatchingDesc("ie"));
		assertEquals(_output, _test.findMatchingDesc("submit ie"));
	}

	@Test
	public void testIsTaskToday() {
		LogicData _test = LogicData.getInstance();;
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime, _tags);
		Task obj2 = new Task("Submit ie2150 draft", "", notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59),
				_tags);
		Task obj3 = new Task("Dental Appointment", "", LocalDateTime.now(), LocalDateTime.now().plusHours(2), _tags);
		Task obj4 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), _tags);
		Task obj5 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.now(), _tags);
		Task obj6 = new Task("Housekeeping", "", notime, notime, _tags);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);

		assertFalse(_test.isTaskToday(_tasks.get(0)));
		assertFalse(_test.isTaskToday(_tasks.get(1)));
		assertTrue(_test.isTaskToday(_tasks.get(2)));
		assertFalse(_test.isTaskToday(_tasks.get(3)));
		assertTrue(_test.isTaskToday(_tasks.get(4)));
		assertFalse(_test.isTaskToday(_tasks.get(5)));
	}

	@Test
	public void testFindMatchingPosition() {
		LogicData _test = LogicData.getInstance();;
		ArrayList<Task> _tasks = new ArrayList<Task>();
		_test.setDisplays(_tasks);
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Cut hair", "", notime, notime, _tags);
		Task obj2 = new Task("Bro's Birthday Dinner", "", LocalDateTime.of(2016, Month.AUGUST, 31, 19, 00),
				LocalDateTime.of(2016, Month.AUGUST, 31, 21, 00), _tags);
		Task obj3 = new Task("Dental Appointment", "", LocalDateTime.of(2016, Month.APRIL, 4, 10, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 12, 00), _tags);
		Task obj4 = new Task("IE2100 Midterm", "", LocalDateTime.of(2016, Month.MARCH, 2, 13, 00),
				LocalDateTime.of(2016, Month.MARCH, 2, 13, 45), _tags);
		Task obj5 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.of(2016, Month.MARCH, 9, 17, 00), _tags);
		Task obj6 = new Task("Housekeeping", "", notime, notime, _tags);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);

		assertEquals(obj4, _test.findMatchingPosition(3));
		assertEquals(obj, _test.findMatchingPosition(0));
		assertEquals(obj2, _test.findMatchingPosition(1));
		assertEquals(obj6, _test.findMatchingPosition(5));
		assertEquals(obj3, _test.findMatchingPosition(2));
		assertEquals(obj5, _test.findMatchingPosition(4));
	}

	@Test
	public void testFindMatchingDate() {
		LogicData _test = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		_test.setDisplays(_tasks);
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Internship interview", "", LocalDateTime.of(2016, Month.APRIL, 4, 13, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 15, 00), _tags);
		Task obj2 = new Task("Jojo 21st", "", LocalDateTime.of(2016, Month.JANUARY, 15, 19, 00),
				LocalDateTime.of(2016, Month.JANUARY, 15, 21, 00), _tags);
		Task obj3 = new Task("Dental Appointment", "", LocalDateTime.of(2016, Month.APRIL, 4, 10, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 12, 00), _tags);
		Task obj4 = new Task("IE2100 Midterm", "", LocalDateTime.of(2016, Month.MARCH, 2, 13, 00),
				LocalDateTime.of(2016, Month.MARCH, 2, 13, 45), _tags);
		Task obj5 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.of(2016, Month.MARCH, 2, 17, 00), _tags);
		Task obj6 = new Task("Housekeeping", "", notime, notime, _tags);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj);
		_output.add(obj3);

		assertEquals(_output, _test.findMatchingDates(LocalDate.of(2016, Month.APRIL, 4)));
	}

	@Test
	public void testFindMatchingDateTimes() {
		LogicData _test = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		_test.setDisplays(_tasks);
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Internship interview", "", LocalDateTime.of(2016, Month.APRIL, 4, 13, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 15, 00), _tags);
		Task obj2 = new Task("Bae's 21st", "", LocalDateTime.of(2016, Month.JANUARY, 15, 19, 00),
				LocalDateTime.of(2016, Month.JANUARY, 15, 21, 00), _tags);
		Task obj3 = new Task("Valentine's dinner", "", LocalDateTime.of(2016, Month.FEBRUARY, 14, 10, 00),
				LocalDateTime.of(2016, Month.FEBRUARY, 14, 12, 00), _tags);
		Task obj4 = new Task("IE2100 Midterm", "", LocalDateTime.of(2016, Month.MARCH, 2, 13, 00),
				LocalDateTime.of(2016, Month.MARCH, 2, 13, 45), _tags);
		Task obj5 = new Task("Renew pass port", "", notime, LocalDateTime.of(2016, Month.MARCH, 2, 17, 00), _tags);
		Task obj6 = new Task("Level up pokemon", "", notime, notime, _tags);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj4);

		assertEquals(_output, _test.findMatchingDateTimes(LocalDateTime.of(2016, Month.MARCH, 2, 13, 00)));
	}

	@Test
	public void testFindMatchingMonths() {
		LogicData _test = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		_test.setDisplays(_tasks);
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Internship interview", "", LocalDateTime.of(2016, Month.APRIL, 4, 13, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 15, 00), _tags);
		Task obj2 = new Task("Bae's 21st", "", LocalDateTime.of(2016, Month.JANUARY, 15, 19, 00),
				LocalDateTime.of(2016, Month.JANUARY, 15, 21, 00), _tags);
		Task obj3 = new Task("Good Friday Holi", "", LocalDateTime.of(2016, Month.MARCH, 18, 00, 00),
				LocalDateTime.of(2016, Month.MARCH, 18, 23, 59), _tags);
		Task obj4 = new Task("IE2100 Midterm", "", LocalDateTime.of(2016, Month.MARCH, 2, 13, 00),
				LocalDateTime.of(2016, Month.MARCH, 2, 13, 45), _tags);
		Task obj5 = new Task("Pay bills", "", notime, LocalDateTime.of(2016, Month.MARCH, 16, 17, 00), _tags);
		Task obj6 = new Task("Housewarming", "", notime, notime, _tags);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj3);
		_output.add(obj4);
		_output.add(obj5);

		assertEquals(_output, _test.findMatchingMonths(Month.MARCH));

	}

}
