package testCommand;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.command.Edit;
import urgenda.logic.LogicData;
import urgenda.util.Task;

public class EditTest {

	@Test
	public void testExecute() throws Exception {
		LogicData _data = LogicData.getInstance();
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
		
		_data.setDisplays(_tasks);
		Task testTask = new Task("Buy milk and eggs", "", notime, notime, _tags);
		Edit tester = new Edit(0,testTask);
		assertEquals("\"Buy milk\" has been edited to \"Buy milk and eggs\"",tester.execute());
		Task testTask2 = new Task(null, "", LocalDateTime.of(2016, Month.APRIL, 5, 10, 00),
				LocalDateTime.of(2016, Month.APRIL, 5, 12, 00), _tags);
		Edit tester2 = new Edit(1,testTask2);
		assertEquals("\"Submit ie2150 draft\" by 24/2, 23:59 has been edited to \"Submit ie2150 draft\" on 5/4, 10:00 - 12:00",tester2.execute());	
	}
	
	@Test  //TODO: Redo test to match change in fn
	public void testUndo() throws Exception {
		LogicData _data = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime, _tags);
		Task obj2 = new Task("Submit ie2150 draft", "", notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59),
				_tags);
		_tasks.add(obj);
		_tasks.add(obj2);
		_data.setDisplays(_tasks);
		Task testTask = new Task("Buy milk and eggs", "", notime, notime, _tags);
		Edit tester = new Edit(0,testTask);
		tester.execute();
		assertEquals("\"Buy milk and eggs\" has been reverted to \"Buy milk\"",tester.undo());
	}
	
	@Test //TODO: Redo test to match change in fn
	public void testRedo() throws Exception {
		LogicData _data = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Buy milk", "", notime, notime, _tags);
		Task obj2 = new Task("Submit ie2150 draft", "", notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59),
				_tags);
		_tasks.add(obj);
		_tasks.add(obj2);
		_data.setDisplays(_tasks);
		Task testTask = new Task("Buy milk and eggs", "", notime, notime, _tags);
		Edit tester = new Edit(0,testTask);
		tester.execute();
		tester.undo();
		assertEquals("\"Buy milk\" has been edited to \"Buy milk and eggs\"",tester.redo());
		
	}

}
