package testStorage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.junit.Test;

import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import urgenda.storage.JsonCipher;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;
import urgenda.util.DateTimePair;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JsonEncryptionDecryptionTest {
	//testing for correct json format strings
	@Test
	public void test006rightJsonString(){
		String phrase = "{\"directory\":\"settings\",\"name\":\"data.txt\"}";
		JsonCipher cipher = new JsonCipher(phrase);
		assertFalse(cipher.getDetailsMap().isEmpty());
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
		for (int i = 0; i < expectedList.size(); i++) {
			LocalDateTime actualStart = actualList.get(i).getEarlierDateTime();
			LocalDateTime actualEnd = actualList.get(i).getLaterDateTime();
			LocalDateTime expectedStart = expectedList.get(i).getEarlierDateTime();
			LocalDateTime expectedEnd = expectedList.get(i).getLaterDateTime();
			assertEquals(expectedStart, actualStart);
			assertEquals(expectedEnd,actualEnd);
		}
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
