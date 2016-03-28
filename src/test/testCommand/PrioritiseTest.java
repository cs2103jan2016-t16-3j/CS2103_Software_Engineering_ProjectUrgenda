package testCommand;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;


import org.junit.Test;

import urgenda.command.Prioritise;
import urgenda.logic.LogicData;
import urgenda.util.Task;

public class PrioritiseTest {

	@Test
	public void testExecute() throws Exception {
		LogicData _data = LogicData.getInstance();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		Task obj = new Task("Apply Financial Aid", "", notime, notime, _tags, false);
		Task obj2 = new Task("Submit ie2150 draft", "", notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59),
				_tags, false);
		Task obj3 = new Task("Dental Appointment", "", notime,
				notime, _tags, false);
		Task obj4 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), _tags, false);
		
		_tasks.add(obj);
		_tasks.add(obj2);
		_tasks.add(obj3);
		_tasks.add(obj4);
		
		_data.setDisplays(_tasks);
		Prioritise tester = new Prioritise();
		tester.setDesc("Apply Financial Aid");
		assertEquals("\"Apply Financial Aid\" marked as important",tester.execute());
		Prioritise tester2 = new Prioritise();
		ArrayList<Integer> range= new ArrayList<Integer>();
		range.add(2);
		tester2.setPositions(range);
		assertEquals("\"Dental Appointment\" marked as important",tester2.execute());
		Prioritise tester3 = new Prioritise();
		range.clear();
		range.add(0);
		range.add(2);
		tester3.setPositions(range);
		StringBuilder str = new StringBuilder("Priority of 2 tasks have been changed:\n");
		str.append("\"Apply Financial Aid\" unmarked as important\n");
		str.append("\"Dental Appointment\" unmarked as important");
		assertEquals(str.toString(), tester3.execute());
	}
}
