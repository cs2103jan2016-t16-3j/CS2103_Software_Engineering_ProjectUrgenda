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
	public void testSortList() {
		LogicData _test = new LogicData();
		ArrayList<String> _tags = new ArrayList<String>();
		Task obj = new Task("", "", LocalDateTime.of(2014, Month.JANUARY, 1, 10, 10, 30), LocalDateTime.of(2014, Month.FEBRUARY, 25, 10, 10, 30), _tags);
		Task obj2 = new Task("", "", LocalDateTime.of(2014, Month.JANUARY, 15, 10, 10, 30), LocalDateTime.of(2014, Month.AUGUST, 31, 10, 10, 30), _tags);
		Task obj3 = new Task("", "", LocalDateTime.of(2014, Month.JANUARY, 17, 10, 10, 30), LocalDateTime.of(2014, Month.SEPTEMBER, 28, 12, 10, 45), _tags);
		Task obj4 = new Task("", "", LocalDateTime.of(2014, Month.JANUARY, 26, 10, 10, 30), LocalDateTime.of(2014, Month.JANUARY, 15, 10, 06, 30), _tags);
		Task obj5 = new Task("", "", LocalDateTime.of(2014, Month.JANUARY, 1, 10, 10, 30), LocalDateTime.of(2014, Month.AUGUST, 17, 10, 10, 30), _tags);
		Task obj6 = new Task("", "", LocalDateTime.of(2014, Month.JANUARY, 1, 10, 10, 30), LocalDateTime.of(2014, Month.JANUARY, 31, 10, 10, 30), _tags);
		Task obj7 = new Task("", "", LocalDateTime.of(2014, Month.JANUARY, 1, 10, 10, 30), LocalDateTime.of(2014, Month.JANUARY, 8, 05, 25, 30), _tags);
		Task obj8 = new Task("", "", LocalDateTime.of(2014, Month.JANUARY, 1, 10, 10, 30), LocalDateTime.of(2014, Month.MAY, 8, 10, 10, 30), _tags);
		Task obj9 = new Task("", "", LocalDateTime.of(2014, Month.JANUARY, 1, 10, 10, 30), LocalDateTime.of(2014, Month.AUGUST, 21, 10, 10, 30), _tags);
		Task obj10 = new Task("", "", LocalDateTime.of(2014, Month.JANUARY, 1, 10, 10, 30), LocalDateTime.of(2014, Month.APRIL, 4, 10, 10, 30), _tags);
		
		_test.addTask(obj);
		_test.addTask(obj2);
		_test.addTask(obj3);
		_test.addTask(obj4);
		_test.addTask(obj5);
		_test.addTask(obj6);
		_test.addTask(obj7);
		_test.addTask(obj8);
		_test.addTask(obj9);
		_test.addTask(obj10);
		
		
		ArrayList<Task> _output = new ArrayList<Task>();
		_output.add(obj7);
		_output.add(obj4);
		_output.add(obj6);
		_output.add(obj);
		_output.add(obj10);
		_output.add(obj8);
		_output.add(obj5);
		_output.add(obj9);
		_output.add(obj2);
		_output.add(obj3);
		
		assertEquals(_output, _test.sortList(_test.getTaskList()));	
	}
}   //it doesnt pass the test wohensad and i dunno why it dont pass cos testing it on my own project does sort the list. i think it got to do w the initialising cos of logic data
