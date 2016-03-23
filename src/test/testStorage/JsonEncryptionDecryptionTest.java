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
public class JsonEncryptionDecryptionTest {
	//testing 1 TimePair in MultipleSlot
	@Test
	public void test001Multiple(){
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
		String actual = cipher.getMultipleString();
		
	}
}
