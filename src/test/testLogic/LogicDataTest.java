//@@author A0127358Y
package test.testLogic;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.logic.LogicData;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;

public class LogicDataTest {

	/*
	 * this method is to test if sort fn returns correct sorted order. test
	 * cases uses diff range of task type (floating, event, deadline, isimpt and
	 * non-impt) to ensure sufficient combination coverage. Also tests that
	 * originally sorted list will still remain sorted after sort process. In
	 * addition, test that sorting of empty arraylist will return empty
	 * arraylist.
	 */
	@Test
	public void testSortList() {
		LogicData _test = LogicData.getInstance(true);
		ArrayList<Task> _testInput = new ArrayList<Task>();
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime, false);
		Task obj2 = new Task("Submit ie2150 draft", "", notime,
				LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), true);
		Task obj3 = new Task("Dental Appointment", "", LocalDateTime.of(2016, Month.APRIL, 4, 10, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 12, 00), false);
		Task obj4 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), false);
		Task obj5 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.of(2016, Month.MARCH, 3, 17, 00),
				true);
		Task obj6 = new Task("Housekeeping", "", notime, notime, false);
		Task obj7 = new Task("Dinner w mum", "", LocalDateTime.of(2016, Month.JANUARY, 8, 19, 00),
				LocalDateTime.of(2016, Month.JANUARY, 8, 20, 00), false);
		Task obj8 = new Task("Project Presentation", "", LocalDateTime.of(2016, Month.MAY, 8, 10, 00),
				LocalDateTime.of(2016, Month.MAY, 8, 12, 00), false);
		Task obj9 = new Task("Bro Birthday Dinner", "", LocalDateTime.of(2016, Month.AUGUST, 21, 20, 00),
				LocalDateTime.of(2016, Month.AUGUST, 21, 21, 00), false);
		Task obj10 = new Task("CS2103 V0.1", "", notime, LocalDateTime.of(2016, Month.MARCH, 10, 17, 00),
				true);

		_testInput.add(obj);
		_testInput.add(obj2);
		_testInput.add(obj3);
		_testInput.add(obj4);
		_testInput.add(obj5);
		_testInput.add(obj6);
		_testInput.add(obj7);
		_testInput.add(obj8);
		_testInput.add(obj9);
		_testInput.add(obj10);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj2);
		_output.add(obj5);
		_output.add(obj10);
		_output.add(obj7);
		_output.add(obj3);
		_output.add(obj8);
		_output.add(obj4);
		_output.add(obj9);
		_output.add(obj);
		_output.add(obj6);

		ArrayList<Task> _testInput2 = new ArrayList<Task>();
		_testInput2.add(obj2);
		_testInput2.add(obj5);
		_testInput2.add(obj10);
		_testInput2.add(obj7);
		_testInput2.add(obj3);
		_testInput2.add(obj8);
		_testInput2.add(obj4);
		_testInput2.add(obj9);
		_testInput2.add(obj);
		_testInput2.add(obj6);

		ArrayList<Task> _testInput3 = new ArrayList<Task>();

		assertEquals(_output, _test.sortList(_testInput));
		assertEquals(_output, _test.sortList(_testInput2)); // test original
															// sorted list
		assertEquals(_testInput3, _test.sortList(_testInput3)); // test empty
																// arraylist
	}

	/*
	 * this method is to test if findmatchingTasks returns right arraylist
	 * containing all required searched description. Test case input uses diff
	 * combination of task type. Test cases test for both single and multiple
	 * matches. Test cases also test for desc containing numbers. Test cases
	 * also test for case sensitivity. Test cases also test for substring. Test
	 * cases also test for empty string.
	 */
	@Test
	public void testFindMatchingDesc() {
		LogicData _test = LogicData.getInstance(true);
		ArrayList<Task> _testInput = new ArrayList<Task>();
		_test.setDisplays(_testInput);
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime);
		Task obj2 = new Task("Buy Book", "", notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59));
		Task obj3 = new Task("Dental Appointment", "", LocalDateTime.of(2016, Month.APRIL, 4, 10, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 12, 00));
		Task obj4 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59));
		Task obj5 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.of(2016, Month.MARCH, 3, 17, 00));
		Task obj6 = new Task("Housekeeping", "", notime, notime);
		Task obj7 = new Task("Submit ie2150 project draft", "", notime,
				LocalDateTime.of(2016, Month.JANUARY, 8, 20, 00));
		Task obj8 = new Task("Project Presentation", "", LocalDateTime.of(2016, Month.MAY, 8, 10, 00),
				LocalDateTime.of(2016, Month.MAY, 8, 12, 00));

		_testInput.add(obj);
		_testInput.add(obj2);
		_testInput.add(obj3);
		_testInput.add(obj4);
		_testInput.add(obj5);
		_testInput.add(obj6);
		_testInput.add(obj7);
		_testInput.add(obj8);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj4);
		// test single match
		assertEquals(_output, _test.findMatchingDesc("Sweden"));

		_output.clear();
		_output.add(obj);
		_output.add(obj2);
		// test multiple matches
		assertEquals(_output, _test.findMatchingDesc("Buy"));

		_output.clear();
		_output.add(obj5);
		// test desc containing numbers
		assertEquals(_output, _test.findMatchingDesc("ie2100"));

		_output.clear();
		_output.add(obj5);
		_output.add(obj7);
		// test case sensitivity and substring
		assertEquals(_output, _test.findMatchingDesc("submit IE"));

		_output.clear();
		// test empty string
		assertEquals(_output, _test.findMatchingDesc(""));
	}

	/*
	 * this method is for updating and checking whether the task falls on today.
	 * test cases partition into 5 types: floating no; event yes,no; deadline
	 * yes,no
	 */
	@Test
	public void testIsTaskToday() {
		LogicData _test = LogicData.getInstance(true);
		ArrayList<Task> _tasks = new ArrayList<Task>();
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime);
		Task obj2 = new Task("Submit ie2150 draft", "", notime,
				LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59));
		Task obj3 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.now());
		Task obj4 = new Task("Dental Appointment", "", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		Task obj5 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59));

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);

		assertFalse(_test.isTaskToday(_tasks.get(0))); // floating
		assertFalse(_test.isTaskToday(_tasks.get(1))); // deadline false
		assertTrue(_test.isTaskToday(_tasks.get(2))); // deadline true
		assertTrue(_test.isTaskToday(_tasks.get(3))); // event true
		assertFalse(_test.isTaskToday(_tasks.get(4))); // event false

	}

	/*
	 * This method is to find task based on given arraylist of id. Equivalence
	 * partition, boundaries values are : MIN_INT, MAX_INT, 0, -1,
	 * _tasks.size(), _tasks.size -1, a random value in btwn 0 and display size
	 * 
	 */
	@Test
	public void testFindMatchingPosition() {
		LogicData _test = LogicData.getInstance(true);
		ArrayList<Task> _testInput = new ArrayList<Task>();
		_test.setDisplays(_testInput);
		LocalDateTime notime = null;
		Task obj = new Task("Cut hair", "", notime, notime);
		Task obj2 = new Task("Bro's Birthday Dinner", "", LocalDateTime.of(2016, Month.AUGUST, 31, 19, 00),
				LocalDateTime.of(2016, Month.AUGUST, 31, 21, 00));
		Task obj3 = new Task("Dental Appointment", "", LocalDateTime.of(2016, Month.APRIL, 4, 10, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 12, 00));
		Task obj4 = new Task("IE2100 Midterm", "", LocalDateTime.of(2016, Month.MARCH, 2, 13, 00),
				LocalDateTime.of(2016, Month.MARCH, 2, 13, 45));
		Task obj5 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.of(2016, Month.MARCH, 9, 17, 00));
		Task obj6 = new Task("Housekeeping", "", notime, notime);

		_testInput.add(obj);
		_testInput.add(obj2);
		_testInput.add(obj3);
		_testInput.add(obj4);
		_testInput.add(obj5);
		_testInput.add(obj6);

		assertEquals(null, _test.findMatchingPosition(Integer.MIN_VALUE)); // test
																			// MIN_INT
		assertEquals(null, _test.findMatchingPosition(Integer.MAX_VALUE)); // test
																			// MAX_INT
		assertEquals(obj, _test.findMatchingPosition(0)); // test 0
		assertEquals(null, _test.findMatchingPosition(-1)); // test -1
		assertEquals(null, _test.findMatchingPosition(6)); // test task.size
		assertEquals(obj6, _test.findMatchingPosition(5)); // test boundary of
															// task.size
		assertEquals(obj4, _test.findMatchingPosition(3)); // any random value
															// in btwn
	}

	/*
	 * This method is to find task based on input date regardless of time.
	 * Equivalence partition, Boundary values: no match, single match, multiple
	 * matches.
	 */
	@Test
	public void testFindMatchingDate() {
		LogicData _test = LogicData.getInstance(true);
		ArrayList<Task> _tasks = new ArrayList<Task>();
		_test.setDisplays(_tasks);
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

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj3);
		_output.add(obj4);
		assertEquals(_output, _test.findMatchingDates(LocalDateTime.now().toLocalDate())); // test
																							// multiple
																							// matches
		_output.clear();
		_output.add(obj2);
		assertEquals(_output, _test.findMatchingDates(LocalDate.of(2016, Month.FEBRUARY, 24))); // test
																								// single
																								// match
		_output.clear();
		assertEquals(_output, _test.findMatchingDates(LocalDate.of(2016, Month.AUGUST, 29))); // test
																								// no
																								// match
	}

	/*
	 * this method is to find task based on input datetime. Equivalence
	 * partition, Boundary values: no match (totally diff), no match(only time
	 * diff), single match, multiple matches.
	 * 
	 */
	@Test
	public void testFindMatchingDateTimes() {
		LogicData _test = LogicData.getInstance(true);
		ArrayList<Task> _tasks = new ArrayList<Task>();
		_test.setDisplays(_tasks);
		LocalDateTime notime = null;
		Task obj = new Task("Internship interview", "", LocalDateTime.of(2016, Month.APRIL, 4, 13, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 15, 00));
		Task obj2 = new Task("Bae's 21st", "", LocalDateTime.of(2016, Month.JANUARY, 15, 19, 00),
				LocalDateTime.of(2016, Month.JANUARY, 15, 21, 00));
		Task obj3 = new Task("Valentine's dinner", "", LocalDateTime.of(2016, Month.FEBRUARY, 14, 10, 00),
				LocalDateTime.of(2016, Month.FEBRUARY, 14, 12, 00));
		Task obj4 = new Task("IE2100 Midterm", "", LocalDateTime.of(2016, Month.MARCH, 2, 13, 00),
				LocalDateTime.of(2016, Month.MARCH, 2, 13, 45));
		Task obj5 = new Task("Renew pass port", "", notime, LocalDateTime.of(2016, Month.MARCH, 2, 13, 00));
		Task obj6 = new Task("Level up pokemon", "", notime, notime);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj3);
		// test single match
		assertEquals(_output,
				_test.findMatchingDateTimes(LocalDateTime.of(2016, Month.FEBRUARY, 14, 10, 00)));
		_output.clear();
		_output.add(obj4);
		_output.add(obj5);
		// test multiple matches
		assertEquals(_output, _test.findMatchingDateTimes(LocalDateTime.of(2016, Month.MARCH, 2, 13, 00)));
		_output.clear();
		// test no match totally different
		assertEquals(_output,
				_test.findMatchingDateTimes(LocalDateTime.of(2016, Month.SEPTEMBER, 2, 13, 00)));
		// test no match (only time diff)
		assertEquals(_output, _test.findMatchingDateTimes(LocalDateTime.of(2016, Month.APRIL, 4, 18, 00)));

	}

	/*
	 * This method is to find task based on input month. Equivalence partition,
	 * Boundary values: no match, single match, multiple matches
	 * 
	 */
	@Test
	public void testFindMatchingMonths() {
		LogicData _test = LogicData.getInstance(true);
		ArrayList<Task> _testInput = new ArrayList<Task>();
		_test.setDisplays(_testInput);
		LocalDateTime notime = null;
		Task obj = new Task("Internship interview", "", LocalDateTime.of(2016, Month.APRIL, 4, 13, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 15, 00));
		Task obj2 = new Task("Bae's 21st", "", LocalDateTime.of(2016, Month.JANUARY, 15, 19, 00),
				LocalDateTime.of(2016, Month.JANUARY, 15, 21, 00));
		Task obj3 = new Task("Good Friday Holi", "", LocalDateTime.of(2016, Month.MARCH, 18, 00, 00),
				LocalDateTime.of(2016, Month.MARCH, 18, 23, 59));
		Task obj4 = new Task("IE2100 Midterm", "", LocalDateTime.of(2016, Month.MARCH, 2, 13, 00),
				LocalDateTime.of(2016, Month.MARCH, 2, 13, 45));
		Task obj5 = new Task("Pay bills", "", notime, LocalDateTime.of(2016, Month.MARCH, 16, 17, 00));
		Task obj6 = new Task("Housewarming", "", notime, notime);

		_testInput.add(obj);
		_testInput.add(obj2);
		_testInput.add(obj3);
		_testInput.add(obj4);
		_testInput.add(obj5);
		_testInput.add(obj6);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj3);
		_output.add(obj4);
		_output.add(obj5);
		// test multiple matches
		assertEquals(_output, _test.findMatchingMonths(Month.MARCH));
		_output.clear();
		_output.add(obj2);
		// test single match
		assertEquals(_output, _test.findMatchingMonths(Month.JANUARY));
		_output.clear();
		// test no match
		assertEquals(_output, _test.findMatchingMonths(Month.DECEMBER));
	}

	/*
	 * This method is to find task that has a specified blockslot. Equivalence
	 * partition, Boundary values: no match, single match, multiple matches.
	 * 
	 */
	@Test
	public void testFindBlocks() {
		LogicData _test = LogicData.getInstance(true);
		MultipleSlot block = new MultipleSlot();
		block.addTimeSlot(LocalDateTime.of(2016, Month.MAY, 15, 19, 00),
				LocalDateTime.of(2016, Month.MAY, 15, 21, 00));
		MultipleSlot block2 = new MultipleSlot();
		block2.addTimeSlot(LocalDateTime.of(2016, Month.JUNE, 4, 19, 00),
				LocalDateTime.of(2016, Month.JUNE, 4, 21, 00));
		MultipleSlot block3 = new MultipleSlot();
		block3.addTimeSlot(LocalDateTime.of(2016, Month.JULY, 4, 19, 00),
				LocalDateTime.of(2016, Month.JULY, 4, 21, 00));
		Task obj = new Task("Internship interview", "", LocalDateTime.of(2016, Month.APRIL, 4, 13, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 15, 00));
		Task obj2 = new Task("Bae's 21st", "", LocalDateTime.of(2016, Month.JANUARY, 15, 19, 00),
				LocalDateTime.of(2016, Month.JANUARY, 15, 21, 00));
		Task obj3 = new Task("Good Friday Holi", "", LocalDateTime.of(2016, Month.MARCH, 18, 00, 00),
				LocalDateTime.of(2016, Month.MARCH, 18, 23, 59));
		Task obj4 = new Task("IE2100 Midterm", "", LocalDateTime.of(2016, Month.MARCH, 2, 13, 00),
				LocalDateTime.of(2016, Month.MARCH, 2, 13, 45));
		obj.setSlot(block);
		obj2.setSlot(block2);
		obj4.setSlot(block);
		_test.addTask(obj);
		_test.addTask(obj2);
		_test.addTask(obj3);
		_test.addTask(obj4);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj2);
		assertEquals(_output, _test.findBlocks(block2)); // test single match
		_output.clear();
		_output.add(obj);
		_output.add(obj4);
		assertEquals(_output, _test.findBlocks(block)); // test multiple matches
		_output.clear();
		assertEquals(_output, _test.findBlocks(block3)); // test no match
	}

	/*
	 * this method is for checking whether the user is showing more on a task.
	 * Test cases partition into 2 main types: yes or no, 6 subtypes: floating
	 * yes,no; event yes,no; deadline yes,no
	 * 
	 */
	@Test
	public void testIsShowingMore() {
		LogicData _test = LogicData.getInstance(true);
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime);
		Task obj2 = new Task("Submit ie2150 draft", "", notime,
				LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59));
		Task obj3 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.now());
		Task obj4 = new Task("Dental Appointment", "", LocalDateTime.now(), LocalDateTime.now().plusHours(2));
		Task obj5 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59));
		Task obj6 = new Task("Housekeeping", "", notime, notime);
		_test.toggleShowMoreTasks(obj);
		_test.toggleShowMoreTasks(obj2);
		_test.toggleShowMoreTasks(obj4);
		assertTrue(_test.isShowingMore(obj)); // test floating yes
		assertTrue(_test.isShowingMore(obj2)); // test deadline yes
		assertFalse(_test.isShowingMore(obj3)); // test deadline no
		assertTrue(_test.isShowingMore(obj4)); // test event yes
		assertFalse(_test.isShowingMore(obj5)); // test event no
		assertFalse(_test.isShowingMore(obj6)); // test floating no
	}

}
