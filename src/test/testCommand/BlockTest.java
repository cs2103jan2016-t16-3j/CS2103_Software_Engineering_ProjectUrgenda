package test.testCommand;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import urgenda.command.BlockSlots;
import urgenda.logic.LogicData;
import urgenda.util.DateTimePair;
import urgenda.util.LogicException;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlockTest {
	
	/*
	 * test invalid blocks, when MultipleSlot is null or empty.
	 */
	@Test
	public void test005InvalidBlocks() {
		LogicData _data = LogicData.getInstance(true);
		_data.clearTasks();
		LocalDateTime now = LocalDateTime.of(2017, 4, 10, 0, 0);
		Task task1 = new Task(1, "Buy milk", "FLOATING", "", false, false, false, null, null, now, null,
				null);
		BlockSlots block = new BlockSlots(task1);
		String expectedMsg = "Error: Insufficient slots entered for blocking of timeslots";
		try {
			block.execute();
		} catch (LogicException e) {
			assertEquals(expectedMsg, e.getMessage());
		}
		
		MultipleSlot slot1 = new MultipleSlot();
		task1.setSlot(slot1);
		block = new BlockSlots(task1);
		expectedMsg = "Error: Insufficient slots entered for blocking of timeslots";
		try {
			block.execute();
		} catch (LogicException e) {
			assertEquals(expectedMsg, e.getMessage());
		}
		
	}
	
	/*
	 * Testing undo/redo of blockslots.
	 */
	@Test
	public void test004UndoRedo() throws LogicException {
		LogicData _data = LogicData.getInstance(true);
		_data.clearTasks();

		LocalDateTime now = LocalDateTime.of(2017, 4, 10, 0, 0);
		Task task1 = new Task(1, "Buy milk", "FLOATING", "", false, false, false, null, null, now, null,
				null);
		MultipleSlot slot1 = new MultipleSlot();
		LocalDateTime start1 = LocalDateTime.of(2017, 4, 10, 10, 0);
		LocalDateTime end1 = LocalDateTime.of(2017, 4, 10, 11, 0);
		LocalDateTime start2 = LocalDateTime.of(2017, 4, 10, 8, 0);
		LocalDateTime end2 = LocalDateTime.of(2017, 4, 10, 9, 0);
		slot1.addTimeSlot(start1, end1);
		slot1.addTimeSlot(start2, end2);
		task1.setSlot(slot1);

		ArrayList<Task> exTaskList = new ArrayList<Task>();
		exTaskList.add(task1);

		BlockSlots block = new BlockSlots(task1);
		String expectedMsg = "Blocked \"Buy milk\" on 10/4 08:00 - 10/4 09:00, 10/4, 10:00 - 11:00 added";
		String actualMsg = block.execute();
		assertEquals(expectedMsg, actualMsg);

		checkArrayList(exTaskList, _data.getTaskList());
		
		expectedMsg = "\"Buy milk\" on 10/4 08:00 - 10/4 09:00, 10/4, 10:00 - 11:00 removed";
		actualMsg = block.undo();
		assertEquals(expectedMsg, actualMsg);
		checkArrayList(exTaskList, _data.getTaskList());
		
		expectedMsg = "Blocked \"Buy milk\" on 10/4 08:00 - 10/4 09:00, 10/4, 10:00 - 11:00 added";
		actualMsg = block.redo();
		assertEquals(expectedMsg, actualMsg);
		checkArrayList(exTaskList, _data.getTaskList());
		
		_data.clearTasks();
	}
	
	/*
	 * Testing addition of 3 slots for 2 different tasks
	 */
	@Test
	public void test003MultipleTasksMultipleSlots() throws LogicException {
		LogicData _data = LogicData.getInstance(true);
		_data.clearTasks();

		LocalDateTime now = LocalDateTime.of(2017, 4, 10, 0, 0);
		Task task1 = new Task(1, "Buy milk", "FLOATING", "", false, false, false, null, null, now, null,
				null);
		MultipleSlot slot1 = new MultipleSlot();
		LocalDateTime start1 = LocalDateTime.of(2017, 4, 10, 10, 0);
		LocalDateTime end1 = LocalDateTime.of(2017, 4, 10, 11, 0);
		LocalDateTime start2 = LocalDateTime.of(2017, 4, 10, 8, 0);
		LocalDateTime end2 = LocalDateTime.of(2017, 4, 10, 9, 0);
		slot1.addTimeSlot(start1, end1);
		slot1.addTimeSlot(start2, end2);
		task1.setSlot(slot1);

		Task task2 = new Task(2, "Buy rice", "FLOATING", "", false, false, false, null, null, now, null,
				null);
		MultipleSlot slot2 = new MultipleSlot();
		LocalDateTime start3 = LocalDateTime.of(2017, 4, 11, 13, 0);
		LocalDateTime end3 = LocalDateTime.of(2017, 4, 11, 15, 0);
		LocalDateTime start4 = LocalDateTime.of(2017, 4, 11, 9, 0);
		LocalDateTime end4 = LocalDateTime.of(2017, 4, 11, 10, 0);
		LocalDateTime start5 = LocalDateTime.of(2017, 4, 11, 17, 0);
		LocalDateTime end5 = LocalDateTime.of(2017, 4, 11, 19, 0);
		slot2.addTimeSlot(start3, end3);
		slot2.addTimeSlot(start4, end4);
		slot2.addTimeSlot(start5, end5);
		task2.setSlot(slot2);
		ArrayList<Task> exTaskList = new ArrayList<Task>();
		exTaskList.add(task2);
		exTaskList.add(task1);

		BlockSlots block = new BlockSlots(task2);
		String expectedMsg = "Blocked \"Buy rice\" on 11/4 09:00 - 11/4 10:00, 11/4, 13:00 - 15:00, 11/4, 17:00 - 19:00 added";
		String actualMsg = block.execute();
		assertEquals(expectedMsg, actualMsg);
		
		block = new BlockSlots(task1);
		expectedMsg = "Blocked \"Buy milk\" on 10/4 08:00 - 10/4 09:00, 10/4, 10:00 - 11:00 added";
		actualMsg = block.execute();
		assertEquals(expectedMsg, actualMsg);

		checkArrayList(exTaskList, _data.getTaskList());
		
		_data.clearTasks();
	}
	
	/*
	 * Testing sorting of slots when given non-arranged slots
	 */
	@Test
	public void test002TwoUnarrangedSlots() throws LogicException {
		LogicData _data = LogicData.getInstance(true);
		_data.clearTasks();

		LocalDateTime now = LocalDateTime.of(2017, 4, 10, 0, 0);
		Task task1 = new Task(1, "Buy milk", "FLOATING", "", false, false, false, null, null, now, null,
				null);
		MultipleSlot slot1 = new MultipleSlot();
		LocalDateTime start1 = LocalDateTime.of(2017, 4, 10, 10, 0);
		LocalDateTime end1 = LocalDateTime.of(2017, 4, 10, 11, 0);
		LocalDateTime start2 = LocalDateTime.of(2017, 4, 10, 8, 0);
		LocalDateTime end2 = LocalDateTime.of(2017, 4, 10, 9, 0);
		slot1.addTimeSlot(start1, end1);
		slot1.addTimeSlot(start2, end2);
		task1.setSlot(slot1);

		ArrayList<Task> exTaskList = new ArrayList<Task>();
		exTaskList.add(task1);

		BlockSlots block = new BlockSlots(task1);
		String expectedMsg = "Blocked \"Buy milk\" on 10/4 08:00 - 10/4 09:00, 10/4, 10:00 - 11:00 added";
		String actualMsg = block.execute();
		assertEquals(expectedMsg, actualMsg);

		checkArrayList(exTaskList, _data.getTaskList());

		_data.clearTasks();
	}
	
	/*
	 * Test partition case when only 1 slot is given, ie not enough slots are given.
	 */
	@Test
	public void test001NotEnoughSlots() {
		LogicData _data = LogicData.getInstance(true);
		_data.clearTasks();

		LocalDateTime now = LocalDateTime.of(2017, 4, 10, 0, 0);
		Task task1 = new Task(1, "Buy milk", "FLOATING", "", false, false, false, null, null, now, null,
				null);
		MultipleSlot slot1 = new MultipleSlot();
		LocalDateTime start1 = LocalDateTime.of(2017, 4, 10, 8, 0);
		LocalDateTime end1 = LocalDateTime.of(2017, 4, 10, 9, 0);
		slot1.addTimeSlot(start1, end1);
		task1.setSlot(slot1);

		BlockSlots block = new BlockSlots(task1);
		String expectedMsg = "Error: Insufficient slots entered for blocking of timeslots";

		try {
			block.execute();
		} catch (LogicException e) {
			assertEquals(expectedMsg, e.getMessage());
		}
		_data.clearTasks();
	}

	private void checkArrayList(ArrayList<Task> expectedTasks, ArrayList<Task> actualTasks) {
		for (int i = 0; i < actualTasks.size(); i++) {
			Task exTask = expectedTasks.get(i);
			Task actlTask = actualTasks.get(i);
			assertEquals(exTask.getDesc(), actlTask.getDesc());
			assertEquals(exTask.getLocation(), actlTask.getLocation());
			assertEquals(exTask.isCompleted(), actlTask.isCompleted());
			assertEquals(exTask.isImportant(), actlTask.isImportant());
			assertEquals(exTask.isOverdue(), actlTask.isOverdue());
			assertEquals(exTask.getStartTime(), actlTask.getStartTime());
			assertEquals(exTask.getEndTime(), actlTask.getEndTime());
			assertEquals(exTask.getTaskType(), actlTask.getTaskType());
			ArrayList<DateTimePair> exPairList = exTask.getSlot().getSlots();
			ArrayList<DateTimePair> actlPairList = exTask.getSlot().getSlots();
			assertEquals(exPairList.size(), actlPairList.size());
			for (int j = 0; j < exPairList.size(); j++) {
				DateTimePair exPair = exPairList.get(j);
				DateTimePair actlPair = actlPairList.get(j);
				assertEquals(exPair.getEarlierDateTime(), actlPair.getEarlierDateTime());
				assertEquals(exPair.getLaterDateTime(), actlPair.getLaterDateTime());
			}
		}
	}
}
