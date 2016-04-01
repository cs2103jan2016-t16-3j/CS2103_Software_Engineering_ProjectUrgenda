package test.testCommand;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.command.NewEdit;
import urgenda.logic.LogicData;
import urgenda.util.Task;

public class NewEditTest {
	
	@Test
	public void testExecute() throws Exception {
		LogicData _data = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "FLOATING", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, null);
		Task obj2 = new Task(2, "Submit ie2150 draft", "DEADLINE", "", true, false, true, notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), LocalDateTime.now(), notime, _tags, null);
		Task obj3 = new Task(3, "Submit ie2100 hw3", "DEADLINE", "", false, false, false, notime, LocalDateTime.now(), LocalDateTime.now(), notime, _tags, null);
		Task obj4 = new Task(4, "Dental Appointment", "EVENT", " ", true, false, false, LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(1), LocalDateTime.now(), notime,  _tags, null);
		Task obj5 = new Task(5, "Travel to Sweden", "EVENT", " ", false, false, false, LocalDateTime.of(2016, Month.JULY, 26, 00, 00), LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), LocalDateTime.now(), notime,  _tags, null);
		Task obj6 = new Task(1, "Mop floor", "FLOATING", "", true, false, false, notime, notime, LocalDateTime.now(), notime, _tags, null);
	
		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		_tasks.add(obj5);
		_tasks.add(obj6);
		
		_data.setDisplays(_tasks);
		Task testTask = new Task(1, "Buy milk and eggs", "FLOATING", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, null);
		NewEdit tester = new NewEdit();
		tester.setId(0);
		tester.setNewTask(testTask);
		assertEquals("\"Buy milk\" has been edited to \"Buy milk and eggs\"",tester.execute());
		Task testTask2 = new Task(2, null, "EVENT", "", true, false, true, LocalDateTime.of(2016, Month.APRIL, 5, 10, 00), LocalDateTime.of(2016, Month.APRIL, 5, 12, 00), LocalDateTime.now(), notime, _tags, null);
		NewEdit tester2 = new NewEdit();
		tester2.setNewTask(testTask2);
		tester2.setId(1);
		assertEquals("\"Submit ie2150 draft\" by 24/2, 23:59 has been edited to \"Submit ie2150 draft\" on 5/4, 10:00 - 12:00",tester2.execute());
		/*Task testTask3 = new Task();
		NewEdit tester3 = new NewEdit();
		tester3.setNewTask(testTask3);
		tester3.setId(3);
		tester3.setIsRemovedOnce();
		tester3.setIsRemovedTwice();
		assertEquals("\"Travel to Sweden\" on 26/7, 00:00 - 23:59 has been edited to \"Travel to Sweden\"",tester3.execute());
		NewEdit tester4 = new NewEdit();
		tester4.setUnknown(LocalDateTime.of(2016, Month.MARCH, 17, 23, 59));
		tester4.setId(9);
		assertEquals("\"CS2103 V0.1\" by 10/3, 17:00 has been edited to \"CS2103 V0.1\" by 17/3, 23:59",tester4.execute());	
		*/
	}
	
	
	@Test
	public void testUndo() throws Exception {
		LogicData _data = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "FLOATING", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, null);
		Task obj2 = new Task(2, "Submit ie2150 draft", "DEADLINE", "", true, false, true, notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), LocalDateTime.now(), notime, _tags, null);
		_tasks.add(obj);
		_tasks.add(obj2);
		_data.setDisplays(_tasks);
		Task testTask = new Task(1, "Buy milk and eggs", "FLOATING", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, null);
		NewEdit tester = new NewEdit();
		tester.setId(0);
		tester.setNewTask(testTask);
		tester.execute();
		assertEquals("\"Buy milk and eggs\" has been reverted to \"Buy milk\"",tester.undo());
	}
	
	@Test 
	public void testRedo() throws Exception {
		LogicData _data = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "FLOATING", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, null);
		_tasks.add(obj);
		_data.setDisplays(_tasks);
		Task testTask = new Task(1, "Buy milk and eggs", "FLOATING", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, null);
		NewEdit tester = new NewEdit();
		tester.setId(0);
		tester.setNewTask(testTask);
		tester.execute();
		tester.undo();
		assertEquals("\"Buy milk\" has been edited to \"Buy milk and eggs\"",tester.redo());
		
	}

}
