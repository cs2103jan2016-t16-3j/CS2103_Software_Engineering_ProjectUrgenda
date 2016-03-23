package test.testUtils;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.util.MultipleSlot;
import urgenda.util.Task;
import urgenda.util.Task.Type;

public class TaskTest {

	@Test
	public void testIsCompleted() {
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		MultipleSlot slot = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, slot);
		Task obj2 = new Task(2, "Submit ie2150 draft", "event", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, slot);
		Task obj3 = new Task(3, "Submit ie2100 hw3", "", notime, LocalDateTime.now(), _tags);
		Task obj4 = new Task("Dental Appointment", "", LocalDateTime.now(), LocalDateTime.now().plusHours(2), _tags);
		Task obj5 = new Task("Travel to Sweden", "", LocalDateTime.of(2016, Month.JULY, 26, 00, 00),
				LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), _tags);
		
		
		
	}

}
