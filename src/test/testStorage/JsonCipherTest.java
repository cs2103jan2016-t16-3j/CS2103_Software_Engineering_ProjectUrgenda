package testStorage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Test;

import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import urgenda.storage.JsonCipher;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;
import urgenda.util.DateTimePair;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JsonCipherTest {
	
	@Test
	public void test008GetterSetters(){
		//checking in normal conditions
		JsonCipher cipher = new JsonCipher();
		Task task = new Task();
		String desc = "test desc";
		String location = "test loc";
		boolean isCompleted = true;
		boolean isImportant = true;
		boolean isOverdue = true;
		LocalDateTime startTime = LocalDateTime.of(2016, 8, 17, 17, 0);
		LocalDateTime endTime = LocalDateTime.of(2016, 8, 17, 20, 0);
		LocalDateTime dateAdded = LocalDateTime.of(2016, 8, 9, 7, 0);
		LocalDateTime dateModified = LocalDateTime.of(2016, 8, 9, 11, 0);
		
		task.setDesc(desc);
		task.setLocation(location);
		task.setIsCompleted(isCompleted);
		task.setIsImportant(isImportant);
		task.setIsOverdue(isOverdue);
		task.setStartTime(startTime);
		task.setEndTime(endTime);
		task.setDateAdded(dateAdded);
		task.setDateModified(dateModified);
		task.updateTaskType();
		
		cipher.setDesc(task);
		cipher.setLocation(task);
		cipher.setCompleted(task);
		cipher.setImportant(task);
		cipher.setOverdue(task);
		cipher.setStartTime(task);
		cipher.setEndTime(task);
		cipher.setDateAdded(task);
		cipher.setDateModified(task);
		cipher.setType(task);
		
		assertEquals(desc, cipher.getDesc());
		assertEquals(location, cipher.getLocation());
		assertEquals(isCompleted, cipher.checkCompleted());
		assertEquals(isImportant, cipher.checkImportant());
		assertEquals(isOverdue, cipher.checkOverdue());
		assertEquals(startTime, cipher.getStartTime());
		assertEquals(endTime, cipher.getEndTime());
		assertEquals(dateAdded, cipher.getDateAdded());
		assertEquals(dateModified, cipher.getDateModified());
		assertEquals("EVENT", cipher.getType());
		
		
		//testing .reset() method to clear LinkedHashMap and String.
		cipher.reset();
		assertTrue(cipher.isEmptyMap());
		assertTrue(cipher.getDetailsString().isEmpty());
		
		//testing null values for all attributes of Task except for boolean attributes.
		task.setDesc(null);
		task.setLocation(null);
		task.setStartTime(null);
		task.setEndTime(null);
		task.setDateAdded(null);
		task.setDateModified(null);
		task.updateTaskType();
		
		cipher.setDesc(task);
		cipher.setLocation(task);
		cipher.setStartTime(task);
		cipher.setEndTime(task);
		cipher.setDateAdded(task);
		cipher.setDateModified(task);
		cipher.setType(task);
		
		assertEquals(null, cipher.getDesc());
		assertEquals(null, cipher.getLocation());
		assertEquals(null, cipher.getStartTime());
		assertEquals(null, cipher.getEndTime());
		assertEquals(null, cipher.getDateAdded());
		assertEquals(null, cipher.getDateModified());
		assertEquals("FLOATING", cipher.getType());
		
		cipher.reset();
		assertTrue(cipher.isEmptyMap());
		assertTrue(cipher.getDetailsString().isEmpty());
		
		//test the remaining deadline type
		task.setStartTime(null);
		task.setEndTime(endTime);
		task.updateTaskType();
		cipher.setStartTime(task);
		cipher.setEndTime(task);
		cipher.setType(task);
		assertEquals(null, cipher.getStartTime());
		assertEquals(endTime, cipher.getEndTime());
		assertEquals("DEADLINE", cipher.getType());
		
		//test getter/setters for file name and file directory
		cipher.setDirectory(desc);
		cipher.setFileName(location);
		assertEquals(desc, cipher.getDirectory());
		assertEquals(location, cipher.getFileName());
		
		//test null for getter/setters for filename and file directory
		cipher.setDirectory(null);
		cipher.setFileName(null);
		assertEquals("settings", cipher.getDirectory());
		assertEquals("data.txt", cipher.getFileName());
	}
	
	@Test
	public void test007rightJsonString(){
		String phrase = "";
		String expectedString = "{}";
		JsonCipher cipher = new JsonCipher(phrase);
		assertTrue(cipher.isEmptyMap());
		cipher.convertToString();
		String actualString = cipher.getDetailsString();
		assertEquals(expectedString, actualString);
	}
	
	//testing for correct json format strings
	@Test
	public void test006rightJsonString(){
		String phrase = "{\"directory\":\"settings\",\"name\":\"data.txt\"}";
		JsonCipher cipher = new JsonCipher(phrase);
		assertFalse(cipher.isEmptyMap());
	}
	
	//testing for wrong json format strings
	@Test
	public void test005wrongJsonString(){
		JsonCipher cipher = new JsonCipher("wrongJsonFormat");
		assertTrue(cipher.getDetailsMap().isEmpty());
	}
	
	//testing null for multipleslot
	@Test
	public void test004MultipleNull(){
		JsonCipher cipher = new JsonCipher();
		Task task = new Task();
		task.setSlot(null);
		cipher.setMultiple(task);
		
		assertEquals(task.getSlot(), cipher.getMultiple());
	}
	
	//Testing no timepairs in MultipleSlot
	@Test
	public void test003MultipleNoPairs(){
		JsonCipher cipher = new JsonCipher();
		MultipleSlot slot = new MultipleSlot();
		ArrayList<DateTimePair> expectedList = slot.getSlots();
		Task task = new Task();
		task.setSlot(slot);
		cipher.setMultiple(task);
		
		MultipleSlot actualSlot = cipher.getMultiple();
		ArrayList<DateTimePair> actualList = actualSlot.getSlots();
		assertEquals(expectedList.size(), actualList.size());
	}
	
	//testing multiple TimePairs in MultipleSlot
	@Test
	public void test002MultiplePairs(){
		JsonCipher cipher = new JsonCipher();
		MultipleSlot slot = new MultipleSlot();
		for (int i = 1; i < 10; i++){
			LocalDateTime start = LocalDateTime.of(2016,  3, i, 0, 0);
			LocalDateTime end = LocalDateTime.of(2016, 3, i+1, 0, 0);
			slot.addTimeSlot(start, end);
		}
		ArrayList<DateTimePair> expectedList = slot.getSlots();
		Task task = new Task();
		task.setSlot(slot);
		cipher.setMultiple(task);
		
		MultipleSlot actualSlot = cipher.getMultiple();
		ArrayList<DateTimePair> actualList = actualSlot.getSlots();
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			LocalDateTime actualStart = actualList.get(i).getEarlierDateTime();
			LocalDateTime actualEnd = actualList.get(i).getLaterDateTime();
			LocalDateTime expectedStart = expectedList.get(i).getEarlierDateTime();
			LocalDateTime expectedEnd = expectedList.get(i).getLaterDateTime();
			assertEquals(expectedStart, actualStart);
			assertEquals(expectedEnd,actualEnd);
		}
	}
	
	//testing 1 TimePair in MultipleSlot
	@Test
	public void test001MultipleOnePair(){
		JsonCipher cipher = new JsonCipher();
		MultipleSlot slot = new MultipleSlot();
		LocalDateTime start = LocalDateTime.of(2016, 3, 24, 0, 0);
		LocalDateTime end = LocalDateTime.of(2016, 3, 25, 0, 0);
		slot.addTimeSlot(start, end);
		Task task = new Task();
		task.setSlot(slot);
		cipher.setMultiple(task);
		
		MultipleSlot expectedSlot = cipher.getMultiple();
		ArrayList<DateTimePair> list = expectedSlot.getSlots();
		for (DateTimePair pair: list){
			LocalDateTime actualStart = pair.getEarlierDateTime();
			LocalDateTime actualEnd = pair.getLaterDateTime();
			assertEquals(start, actualStart);
			assertEquals(end, actualEnd);
		}
		
	}
}
