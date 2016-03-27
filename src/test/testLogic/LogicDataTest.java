package testLogic;

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

	// this fn is to test if sort fn returns correct sorted order
	//test cases uses diff range of task type (floating, event, deadline, isimpt and non-impt) to ensure sufficient combination coverage
	//also test that originally sorted list will still remain sorted after sort process
	//also test that sorting of empty arraylist will return empty arraylist
	@Test
	public void testSortList() { 
		LogicData _test = LogicData.getInstance();;
		ArrayList<Task> _testinput = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime, _tags, false);
		Task obj2 = new Task("Submit ie2150 draft", "", notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59),
				_tags, true);
		Task obj3 = new Task("Dental Appointment", "", LocalDateTime.of(2016, Month.APRIL, 4, 10, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 12, 00), _tags, false);
		Task obj4 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), _tags, false);
		Task obj5 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.of(2016, Month.MARCH, 3, 17, 00), _tags, true);
		Task obj6 = new Task("Housekeeping", "", notime, notime, _tags, false);
		Task obj7 = new Task("Dinner w mum", "", LocalDateTime.of(2016, Month.JANUARY, 8, 19, 00),
				LocalDateTime.of(2016, Month.JANUARY, 8, 20, 00), _tags, false);
		Task obj8 = new Task("Project Presentation", "", LocalDateTime.of(2016, Month.MAY, 8, 10, 00),
				LocalDateTime.of(2016, Month.MAY, 8, 12, 00), _tags, false);
		Task obj9 = new Task("Bro Birthday Dinner", "", LocalDateTime.of(2016, Month.AUGUST, 21, 20, 00),
				LocalDateTime.of(2016, Month.AUGUST, 21, 21, 00), _tags, false);
		Task obj10 = new Task("CS2103 V0.1", "", notime, LocalDateTime.of(2016, Month.MARCH, 10, 17, 00), _tags, true);

		_testinput.add(obj);
		_testinput.add(obj2);
		_testinput.add(obj3);
		_testinput.add(obj4);
		_testinput.add(obj5);
		_testinput.add(obj6);
		_testinput.add(obj7);
		_testinput.add(obj8);
		_testinput.add(obj9);
		_testinput.add(obj10);

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
		
		ArrayList<Task> _testinput2 = new ArrayList<Task>();
		_testinput2.add(obj2);
		_testinput2.add(obj5);
		_testinput2.add(obj10);
		_testinput2.add(obj7);
		_testinput2.add(obj3);
		_testinput2.add(obj8);
		_testinput2.add(obj4);
		_testinput2.add(obj9);
		_testinput2.add(obj);
		_testinput2.add(obj6);
		
		ArrayList<Task> _testinput3 = new ArrayList<Task>();
		
		assertEquals(_output, _test.sortList(_testinput));
		assertEquals(_output, _test.sortList(_testinput2));  //test original sorted list
		assertEquals(_testinput3, _test.sortList(_testinput3)); //test empty arraylist
	}

	//this fn is to test if findmatchingTasks returns right arraylist containing all required searched description
	//test cases input uses diff combination of task type 
	//test cases test for both single and multiple matches
	//test cases also test for desc containing numbers
	//test cases also test for case sensitivity
	//test cases also test for substring
	//test cases also test for empty string
	@Test
	public void testFindMatchingDesc() { 
		LogicData _test = LogicData.getInstance();;
		ArrayList<Task> _testinput = new ArrayList<Task>();
		_test.setDisplays(_testinput);
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

		_testinput.add(obj);
		_testinput.add(obj2);
		_testinput.add(obj3);
		_testinput.add(obj4);
		_testinput.add(obj5);
		_testinput.add(obj6);
		_testinput.add(obj7);
		_testinput.add(obj8);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj4);
		assertEquals(_output, _test.findMatchingDesc("Sweden")); //test single match

		_output.clear();
		_output.add(obj);
		_output.add(obj2);
		assertEquals(_output, _test.findMatchingDesc("Buy")); //test multi matches

		_output.clear();
		_output.add(obj5);
		assertEquals(_output, _test.findMatchingDesc("ie2100")); //test desc containing num
		
		_output.clear();
		_output.add(obj5);
		_output.add(obj7);
		assertEquals(_output, _test.findMatchingDesc("submit IE")); //test case sensitivity and substring
		
		_output.clear();
		assertEquals(_output, _test.findMatchingDesc("")); //test empty string
	}

	//this fn is for updating and checking whether the task falls on today
	// test cases partition into 5 types: floating no; event yes,no; deadline yes,no
	@Test
	public void testIsTaskToday() {
		LogicData _test = LogicData.getInstance();;
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime, _tags);
		Task obj2 = new Task("Submit ie2150 draft", "", notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59),
				_tags);
		Task obj3 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.now(), _tags);
		Task obj4 = new Task("Dental Appointment", "", LocalDateTime.now(), LocalDateTime.now().plusHours(2), _tags);
		Task obj5 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), _tags);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
	
		assertFalse(_test.isTaskToday(_tasks.get(0))); //floating
		assertFalse(_test.isTaskToday(_tasks.get(1))); //deadline false
		assertTrue(_test.isTaskToday(_tasks.get(2)));  //deadline true
		assertTrue(_test.isTaskToday(_tasks.get(3))); //event true
		assertFalse(_test.isTaskToday(_tasks.get(4)));  //event false
		
	}

	//This fn is to find task based on given arraylist id
	// Equivalence partition, boundaries values are : MIN_INT, MAX_INT, 0, -1,
	//_tasks.size(), _tasks.size -1, a random value in btwn 0 and display size 
	@Test
	public void testFindMatchingPosition() {
		LogicData _test = LogicData.getInstance();;
		ArrayList<Task> _testinput = new ArrayList<Task>();
		_test.setDisplays(_testinput);
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

		_testinput.add(obj);
		_testinput.add(obj2);
		_testinput.add(obj3);
		_testinput.add(obj4);
		_testinput.add(obj5);
		_testinput.add(obj6);

		assertEquals(null, _test.findMatchingPosition(Integer.MIN_VALUE)); //test MIN_INT
		assertEquals(null, _test.findMatchingPosition(Integer.MAX_VALUE)); //test MAX_INT
		assertEquals(obj, _test.findMatchingPosition(0));   //test 0
		assertEquals(null, _test.findMatchingPosition(-1)); // test -1
		assertEquals(null, _test.findMatchingPosition(6));  // test task.size
		assertEquals(obj6, _test.findMatchingPosition(5));  // test boundary of task.size
		assertEquals(obj4, _test.findMatchingPosition(3));   // any random value in btwn
	}

	//this fn is to find task based on input date regardless of time
	//Equivalence partition. Boundary values: no match, single match, multiple matches
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
		assertEquals(_output, _test.findMatchingDates(LocalDate.of(2016, Month.APRIL, 4))); //test multiple matches
		_output.clear();
		_output.add(obj2);
		assertEquals(_output, _test.findMatchingDates(LocalDate.of(2016, Month.JANUARY, 15))); //test single match
		_output.clear();
		assertEquals(_output, _test.findMatchingDates(LocalDate.of(2016, Month.AUGUST, 17))); //test no match
	}

	//this fn is to find task based on input datetime
	//Equivalence partition. Boundary values: no match (totally diff), no match(only time diff),  single match, multiple matches
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
		Task obj5 = new Task("Renew pass port", "", notime, LocalDateTime.of(2016, Month.MARCH, 2, 13, 00), _tags);
		Task obj6 = new Task("Level up pokemon", "", notime, notime, _tags);

		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj3);
		assertEquals(_output, _test.findMatchingDateTimes(LocalDateTime.of(2016, Month.FEBRUARY, 14, 10, 00))); //test single match
		_output.clear();
		_output.add(obj4);
		_output.add(obj5);
		assertEquals(_output, _test.findMatchingDateTimes(LocalDateTime.of(2016, Month.MARCH, 2, 13, 00))); //test multiple matches
		_output.clear();
		assertEquals(_output, _test.findMatchingDateTimes(LocalDateTime.of(2016, Month.SEPTEMBER, 2, 13, 00))); //test no match(total diff)
		assertEquals(_output, _test.findMatchingDateTimes(LocalDateTime.of(2016, Month.APRIL, 4, 18, 00))); //test no match (only time diff)
	}
	
	//this fn is to find task based on input month
	//Equivalence partition. Boundary values: no match, single match, multiple matches
	@Test
	public void testFindMatchingMonths() {
		LogicData _test = LogicData.getInstance();
		ArrayList<Task> _testinput = new ArrayList<Task>();
		_test.setDisplays(_testinput);
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

		_testinput.add(obj);
		_testinput.add(obj2);
		_testinput.add(obj3);
		_testinput.add(obj4);
		_testinput.add(obj5);
		_testinput.add(obj6);

		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj3);
		_output.add(obj4);
		_output.add(obj5);
		assertEquals(_output, _test.findMatchingMonths(Month.MARCH)); //test multiple matches
		_output.clear();
		_output.add(obj2);
		assertEquals(_output, _test.findMatchingMonths(Month.JANUARY)); //test multiple matches
		_output.clear();
		assertEquals(_output, _test.findMatchingMonths(Month.DECEMBER)); //test multiple matches
	}
	
	@Test
	public void testFindMatchingHashtags() {
		LogicData _test = LogicData.getInstance();
		ArrayList<Task> _testinput = new ArrayList<Task>();
		_test.setDisplays(_testinput);
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
		Task obj7 = new Task();

		_testinput.add(obj);
		_testinput.add(obj2);
		_testinput.add(obj3);
		_testinput.add(obj4);
		_testinput.add(obj5);
		_testinput.add(obj6);
		_testinput.add(obj7);

		ArrayList<Task> _output = new ArrayList<Task>();
		assertEquals(_output, _test.findMatchingHashtags("abc"));
		
	}
	
	//this fn is to find task that has a specified blockslot
	//Equivalence partition. Boundary values: no match, single match, multiple matches
	@Test
	public void testFindBlocks() {
		LogicData _test = LogicData.getInstance();
		ArrayList<String> _tags = new ArrayList<String>();
		MultipleSlot block = new MultipleSlot();
		block.addTimeSlot(LocalDateTime.of(2016, Month.MAY, 15, 19, 00), LocalDateTime.of(2016, Month.MAY, 15, 21, 00));
		MultipleSlot block2 = new MultipleSlot();
		block2.addTimeSlot(LocalDateTime.of(2016, Month.JUNE, 4, 19, 00), LocalDateTime.of(2016, Month.JUNE, 4, 21, 00));
		MultipleSlot block3 = new MultipleSlot();
		block3.addTimeSlot(LocalDateTime.of(2016, Month.JULY, 4, 19, 00), LocalDateTime.of(2016, Month.JULY, 4, 21, 00));
		Task obj = new Task("Internship interview", "", LocalDateTime.of(2016, Month.APRIL, 4, 13, 00),
				LocalDateTime.of(2016, Month.APRIL, 4, 15, 00), _tags);
		Task obj2 = new Task("Bae's 21st", "", LocalDateTime.of(2016, Month.JANUARY, 15, 19, 00),
				LocalDateTime.of(2016, Month.JANUARY, 15, 21, 00), _tags);
		Task obj3 = new Task("Good Friday Holi", "", LocalDateTime.of(2016, Month.MARCH, 18, 00, 00),
				LocalDateTime.of(2016, Month.MARCH, 18, 23, 59), _tags);
		Task obj4 = new Task("IE2100 Midterm", "", LocalDateTime.of(2016, Month.MARCH, 2, 13, 00),
				LocalDateTime.of(2016, Month.MARCH, 2, 13, 45), _tags);
		obj.setSlot(block);
		obj2.setSlot(block2);
		obj4.setSlot(block);
		_test.addTask(obj);
		_test.addTask(obj2);
		_test.addTask(obj3);
		_test.addTask(obj4);
		
		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj2);
		assertEquals(_output, _test.findBlocks(block2)); //test single match
		_output.clear();
		_output.add(obj);
		_output.add(obj4);
		assertEquals(_output, _test.findBlocks(block)); //test multiple matches
		_output.clear();
		assertEquals(_output, _test.findBlocks(block3)); //test no match
	}
	
	//this fn is for checking whether the user is showing more a a task
	// test cases partition into 2 main types: yes or no, 6 subtypes: floating yes,no ; event yes,no; deadline yes,no
	@Test
	public void testIsShowingMore() {
		LogicData _test = LogicData.getInstance();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime, _tags);
		Task obj2 = new Task("Submit ie2150 draft", "", notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59),
				_tags);
		Task obj3 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.now(), _tags);
		Task obj4 = new Task("Dental Appointment", "", LocalDateTime.now(), LocalDateTime.now().plusHours(2), _tags);
		Task obj5 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), _tags);
		Task obj6 = new Task("Housekeeping", "", notime, notime, _tags);
		_test.toggleShowMoreTasks(obj);
		_test.toggleShowMoreTasks(obj2);
		_test.toggleShowMoreTasks(obj4);
		assertTrue(_test.isShowingMore(obj)); //test floating yes
		assertTrue(_test.isShowingMore(obj2)); //test deadline yes
		assertFalse(_test.isShowingMore(obj3)); //test deadline no
		assertTrue(_test.isShowingMore(obj4)); //test event yes
		assertFalse(_test.isShowingMore(obj5)); //test event no
		assertFalse(_test.isShowingMore(obj6)); //test floating no
	}

}
