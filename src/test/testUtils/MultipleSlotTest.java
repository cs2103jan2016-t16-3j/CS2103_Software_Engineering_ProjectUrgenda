//@@author A0080436J
package test.testUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.util.DateTimePair;
import urgenda.util.MultipleSlot;

// test cases for the methods in multipleslot
public class MultipleSlotTest {

	@Test
	public void testConstructor() {
		ArrayList<DateTimePair> empty = new ArrayList<DateTimePair>();
		MultipleSlot test = new MultipleSlot();
		assertEquals(test.getSlots(), empty);
	}

	@Test
	public void testAddTimeSlot() {
		LocalDateTime now = LocalDateTime.now();
		MultipleSlot test = new MultipleSlot();
		test.addTimeSlot(now, now);
		assertTrue(test.getSlots().size() == 1);
		assertFalse(test.getSlots().size() < 1);
		assertFalse(test.getSlots().size() > 1);

		assertEquals(test.getSlots().get(0).getDateTime1(), now);
		assertEquals(test.getSlots().get(0).getDateTime2(), now);

		LocalDateTime newTime = now.plusMonths(1);
		test = new MultipleSlot();
		test.addTimeSlot(now, newTime);
		assertTrue(test.getSlots().size() == 1);
		assertFalse(test.getSlots().size() < 1);
		assertFalse(test.getSlots().size() > 1);

		assertEquals(test.getSlots().get(0).getDateTime1(), now);
		assertEquals(test.getSlots().get(0).getDateTime2(), newTime);
	}

	@Test
	public void testRemoveNextSlot() {
		LocalDateTime now = LocalDateTime.now();
		ArrayList<DateTimePair> slots = new ArrayList<DateTimePair>();
		MultipleSlot test = new MultipleSlot();

		// Boundary case where size is zero and nothing happens
		// test case for empty slot
		assertEquals(test.getSlots(), slots);
		assertTrue(test.isEmpty());
		test.removeNextSlot();
		assertEquals(test.getSlots(), slots);
		assertTrue(test.isEmpty());

		// test case for one slot
		test.addTimeSlot(now, now);
		assertFalse(test.isEmpty());
		test.removeNextSlot();
		assertEquals(test.getSlots(), slots);
		assertTrue(test.isEmpty());

		// test case for multiple slots
		test.addTimeSlot(now, now.plusMonths(1));
		test.addTimeSlot(now.plusMonths(2), now.plusMonths(3));
		assertFalse(test.isEmpty());
		test.removeNextSlot();
		ArrayList<DateTimePair> remains = test.getSlots();
		assertEquals(remains.get(0).getDateTime1(), now.plusMonths(2));
		assertEquals(remains.get(0).getDateTime2(), now.plusMonths(3));

		// test case for multiple slots and the number of calls is more than the
		// slots
		test.addTimeSlot(now, now.plusMonths(1));
		test.addTimeSlot(now.plusMonths(2), now.plusMonths(3));
		assertFalse(test.isEmpty());
		test.removeNextSlot();
		test.removeNextSlot();
		test.removeNextSlot();
		remains = test.getSlots();
		assertEquals(remains, slots);
		assertTrue(remains.isEmpty());
	}

	@Test
	public void testGetNextSlot() {
		LocalDateTime now = LocalDateTime.now();
		MultipleSlot test = new MultipleSlot();
		assertEquals(test.getNextSlot(), null);

		test.addTimeSlot(now, now.plusMonths(1));
		test.addTimeSlot(now.plusMonths(2), now.plusMonths(3));
		assertEquals(test.getNextSlot().getDateTime1(), now);
		assertEquals(test.getNextSlot().getDateTime2(), now.plusMonths(1));

		test.removeNextSlot();
		assertEquals(test.getNextSlot().getDateTime1(), now.plusMonths(2));
		assertEquals(test.getNextSlot().getDateTime2(), now.plusMonths(3));
	}

}
