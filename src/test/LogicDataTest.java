package test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;


import org.junit.Test;

import urgenda.logic.LogicData;
import urgenda.util.Task;


public class LogicDataTest {

	@Test
	public void testSortList() {    //test if sort fn returns correct sorted order
		LogicData _test = new LogicData();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime, _tags);
		Task obj2 = new Task("Submit ie2150 draft", "", notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), _tags);
		Task obj3 = new Task("Dental Appointment", "", LocalDateTime.of(2016, Month.APRIL, 4, 10, 00), LocalDateTime.of(2016, Month.APRIL, 4, 12, 00), _tags);
		Task obj4 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00), LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), _tags);
		Task obj5 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.of(2016, Month.MARCH, 3, 17, 00), _tags);
		Task obj6 = new Task("Housekeeping", "", notime, notime, _tags);
		Task obj7 = new Task("Dinner w mum", "", LocalDateTime.of(2016, Month.JANUARY, 8, 19, 00), LocalDateTime.of(2016, Month.JANUARY, 8, 20, 00), _tags);
		Task obj8 = new Task("Project Presentation", "", LocalDateTime.of(2016, Month.MAY, 8, 10, 00), LocalDateTime.of(2016, Month.MAY, 8, 12, 00), _tags);
		Task obj9 = new Task("Bro Birthday Dinner", "", LocalDateTime.of(2016, Month.AUGUST, 21, 20, 00), LocalDateTime.of(2016, Month.AUGUST, 21, 21, 00), _tags);
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
	public void testFindMatchingTasks() {  //test if findmatchingTasks returns right arraylist containing all required searched description
		LogicData _test = new LogicData();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		_test.setDisplays(_tasks);
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime, _tags);
		Task obj2 = new Task("Buy Book", "", notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), _tags);
		Task obj3 = new Task("Dental Appointment", "", LocalDateTime.of(2016, Month.APRIL, 4, 10, 00), LocalDateTime.of(2016, Month.APRIL, 4, 12, 00), _tags);
		Task obj4 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00), LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), _tags);
		Task obj5 = new Task("Submit ie2100 hw3", "", notime, LocalDateTime.of(2016, Month.MARCH, 3, 17, 00), _tags);
		Task obj6 = new Task("Housekeeping", "", notime, notime, _tags);
		Task obj7 = new Task("Submit ie2150 project draft", "", notime, LocalDateTime.of(2016, Month.JANUARY, 8, 20, 00), _tags);
		Task obj8 = new Task("Project Presentation", "", LocalDateTime.of(2016, Month.MAY, 8, 10, 00), LocalDateTime.of(2016, Month.MAY, 8, 12, 00), _tags);
		
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
		
		assertEquals(_output, _test.findMatchingTasks("Buy"));
		
		_output.clear();
		_output.add(obj7);
		_output.add(obj8);
		
		assertEquals(_output, _test.findMatchingTasks("project"));
		
		_output.clear();
		_output.add(obj5);
		_output.add(obj7);
		
		assertEquals(_output, _test.findMatchingTasks("ie"));
		assertEquals(_output, _test.findMatchingTasks("submit ie"));
	}
	
	
	
}   
