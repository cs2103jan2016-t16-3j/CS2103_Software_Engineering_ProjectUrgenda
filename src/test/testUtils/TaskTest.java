package testUtils;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.util.MultipleSlot;
import urgenda.util.Task;

public class TaskTest {

	//This fn is for checking whether a task has been completed
	//test cases partition into 2 main types: yes or no, 6 subtypes: floating yes,no ; event yes,no; deadline yes,no
	//also test empty task
	@Test
	public void testIsCompleted() {
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		MultipleSlot slot = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, slot);
		Task obj2 = new Task(2, "Submit ie2150 draft", "deadline", "", true, false, true, notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), LocalDateTime.now(), notime, _tags, slot);
		Task obj3 = new Task(3, "Submit ie2100 hw3", "deadline", "", false, false, false, notime, LocalDateTime.now(), LocalDateTime.now(), notime, _tags, slot);
		Task obj4 = new Task(4, "Dental Appointment", "event", " ", true, false, false, LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(1), LocalDateTime.now(), notime,  _tags, slot);
		Task obj5 = new Task(5, "Travel to Sweden", "event", " ", false, false, false, LocalDateTime.of(2016, Month.JULY, 26, 00, 00), LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), LocalDateTime.now(), notime,  _tags, slot);
		Task obj6 = new Task(1, "Mop floor", "floating", "", true, false, false, notime, notime, LocalDateTime.now(), notime, _tags, slot);
		Task obj7 = new Task();
		
		assertFalse(obj.isCompleted()); //test floating no
		assertTrue(obj2.isCompleted()); //test deadline yes
		assertFalse(obj3.isCompleted()); //test deadline no
		assertTrue(obj4.isCompleted()); //test event yes
		assertFalse(obj5.isCompleted()); //test event no
		assertTrue(obj6.isCompleted()); // test floating yes
		assertFalse(obj7.isCompleted()); //test empty task

	}
	
	//This fn is for checking whether a task has been marked as important
	//test cases partition into 2 main types: yes or no, 6 subtypes: floating yes,no ; event yes,no; deadline yes,no
	//also test empty task
	@Test
	public void testIsImportant() {
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		MultipleSlot slot = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, slot);
		Task obj2 = new Task(2, "Submit ie2150 draft", "deadline", "", true, true, true, notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), LocalDateTime.now(), notime, _tags, slot);
		Task obj3 = new Task(3, "Submit ie2100 hw3", "deadline", "", false, false, false, notime, LocalDateTime.now(), LocalDateTime.now(), notime, _tags, slot);
		Task obj4 = new Task(4, "Dental Appointment", "event", " ", true, true, false, LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(1), LocalDateTime.now(), notime,  _tags, slot);
		Task obj5 = new Task(5, "Travel to Sweden", "event", " ", false, false, false, LocalDateTime.of(2016, Month.JULY, 26, 00, 00), LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59), LocalDateTime.now(), notime,  _tags, slot);
		Task obj6 = new Task(1, "Renew pastport", "floating", "", true, true, false, notime, notime, LocalDateTime.now(), notime, _tags, slot);
		Task obj7 = new Task();
		
		assertFalse(obj.isImportant()); //test floating no
		assertTrue(obj2.isImportant()); //test deadline yes
		assertFalse(obj3.isImportant()); //test deadline no
		assertTrue(obj4.isImportant()); //test event yes
		assertFalse(obj5.isImportant()); //test event no
		assertTrue(obj6.isImportant()); // test floating yes
		assertFalse(obj7.isImportant()); //test empty task
	}
	
	//This fn is for checking whether a task is overdue
	//test cases partition into 2 main types: yes or no; deadline can be yes or no, floating, event and empty will always be no
	@Test
	public void testIsOverdue() {
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		MultipleSlot slot = null;
		Task obj = new Task(1, "Buy milk", "floating", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, slot);
		Task obj2 = new Task(2, "Submit ie2150 draft", "deadline", "", true, false, true, notime, LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), LocalDateTime.now(), notime, _tags, slot);
		Task obj3 = new Task(3, "Submit ie2100 hw3", "deadline", "", false, true, false, notime, LocalDateTime.now(), LocalDateTime.now(), notime, _tags, slot);
		Task obj4 = new Task(4, "Dental Appointment", "event", " ", true, false, false, LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(1), LocalDateTime.now(), notime,  _tags, slot);
		Task obj5 = new Task();
		
		assertFalse(obj.isOverdue()); //test floating
		assertTrue(obj2.isOverdue()); //test deadline yes
		assertFalse(obj3.isOverdue()); //test deadline no
		assertFalse(obj4.isOverdue()); //test event
		assertFalse(obj5.isOverdue()); //test empty task
	}
	
	//This fn is for checking whether two task overlaps
	//Boundary Value Analysis: Dont overlap (task tested lies totally to the left of compared task
	// Dont overlap( task tested lies totally to the right of compared task
	// Overlap: End of task tested overlaps w compared task
	//overlap: start of task tested overlaps w compared task
	//overlap: task tested same as compared task
	//overlap: task tested lies within compared task
	//overlap: test compared is subset of test tested
	@Test
	public void testHasOverlap() {
		Task test = new Task();
		LocalDateTime start = LocalDateTime.of(2016, Month.MARCH, 1, 9, 00, 00);
		LocalDateTime end = LocalDateTime.of(2016, Month.MARCH, 1, 22, 00, 00);
		LocalDateTime comparestart1 = LocalDateTime.of(2016, Month.MARCH, 1, 9, 00, 00);
		LocalDateTime compareend1 = LocalDateTime.of(2016, Month.MARCH, 1, 22, 00, 00);
		LocalDateTime comparestart2 = LocalDateTime.of(2016, Month.MARCH, 1, 19, 30, 00);
		LocalDateTime compareend2 = LocalDateTime.of(2016, Month.MARCH, 1, 20, 45, 00);
		LocalDateTime comparestart3 = LocalDateTime.of(2016, Month.MARCH, 1, 7, 15, 00);
		LocalDateTime compareend3 = LocalDateTime.of(2016, Month.MARCH, 1, 11, 17, 00);
		LocalDateTime comparestart4 = LocalDateTime.of(2016, Month.MARCH, 1, 12, 35, 00);
		LocalDateTime compareend4 = LocalDateTime.of(2016, Month.MARCH, 1, 23, 30, 00);
		LocalDateTime comparestart5 = LocalDateTime.of(2016, Month.MARCH, 1, 7, 00, 00);
		LocalDateTime compareend5 = LocalDateTime.of(2016, Month.MARCH, 1, 9, 00, 00);
		LocalDateTime comparestart6 = LocalDateTime.of(2016, Month.MARCH, 1, 22, 00, 00);
		LocalDateTime compareend6 = LocalDateTime.of(2016, Month.MARCH, 1, 23, 00, 00);
		LocalDateTime comparestart7 = LocalDateTime.of(2016, Month.MARCH, 1, 8, 00, 00);
		LocalDateTime compareend7 = LocalDateTime.of(2016, Month.MARCH, 1, 23, 00, 00);
		
		assertTrue(test.hasOverlap(start, end, comparestart1, compareend1)); //overlap: task tested same as compared task
		assertTrue(test.hasOverlap(start, end, comparestart2, compareend2)); //overlap: task tested lies within compared task
		assertTrue(test.hasOverlap(start, end, comparestart3, compareend3)); //overlap: end of task tested overlaps w compared task
		assertTrue(test.hasOverlap(start, end, comparestart4, compareend4)); //overlap: start of task tested overlaps w compared task
		assertFalse(test.hasOverlap(start, end, comparestart5, compareend5)); // Dont overlap( task tested lies totally to the left of compared task
		assertFalse(test.hasOverlap(start, end, comparestart6, compareend6)); // Dont overlap( task tested lies totally to the right of compared task
		assertTrue(test.hasOverlap(start, end, comparestart7, compareend7)); //overlap: test compared is subset of test tested
		
	}
	
	//This fn test whether a task is overlapping w other task
	//Equivalence partition: yes, no, no(not an event)
	@Test
	public void testIsOverlapping() {
		ArrayList<String> _tags = new ArrayList<String>();
		LocalDateTime notime = null;
		MultipleSlot slot = null;
		Task compare = new Task(4, "Dental Appointment", "EVENT", " ", true, false, false, LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(1), LocalDateTime.now(), notime,  _tags, slot);
		Task test1 = new Task(1, "Buy milk", "FLOATING", "", false, false, false, notime, notime, LocalDateTime.now(), notime, _tags, slot);
		Task test2 = new Task(2, "Dental Appointment", "EVENT", " ", true, false, false, LocalDateTime.now().minusHours(2), LocalDateTime.now(), LocalDateTime.now(), notime,  _tags, slot);
		Task test3 = new Task(3, "Dental Appointment", "EVENT", " ", true, false, false, LocalDateTime.now().plusHours(3), LocalDateTime.now().plusHours(5), LocalDateTime.now(), notime,  _tags, slot);
		
		assertFalse(compare.isOverlapping(test1)); //no (non-event)
		assertTrue(compare.isOverlapping(test2)); //yes
		assertFalse(compare.isOverlapping(test3)); //no
	}

}
