package test.testCommand;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import urgenda.command.BlockSlots;
import urgenda.logic.LogicData;
import urgenda.util.LogicException;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlockTest {

	@Test
	public void test001NotEnoughSlots() {
		LogicData _data = LogicData.getInstance(true);
		_data.clearTasks();
		
		LocalDateTime now = LocalDateTime.of(2017, 4, 10, 0, 0);
		Task task1 = new Task(1, "Buy milk", "FLOATING", "", false, false, false, null, null,
				now, null, null);
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
	
	@Test
	public void test002TwoUnarrangedSlots() throws LogicException{
		LogicData _data = LogicData.getInstance(true);
		_data.clearTasks();
		
		LocalDateTime now = LocalDateTime.of(2017, 4, 10, 0, 0);
		Task task1 = new Task(1, "Buy milk", "FLOATING", "", false, false, false, null, null,
				now, null, null);
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
		String actualMsg;
		actualMsg = block.execute();
		assertEquals(expectedMsg, actualMsg);
		
		checkArrayList(exTaskList, _data.getTaskList());
		
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
		}
	}
}
