//@@author A0080436J
package test.testUrgenda;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.logic.Logic;
import urgenda.util.DateTimePair;
import urgenda.util.MultipleSlot;
import urgenda.util.StateFeedback;
import urgenda.util.Task;
import urgenda.util.TaskList;

// test for integration across logic and parser.
public class UrgendaTest {
	
	private static final String ADD_COMMAND = "add ";
	private static final String DELETE_COMMAND = "delete ";
	private static final String UNDO_COMMAND = "undo";
	private static final String REDO_COMMAND = "redo";
	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_UNDO = "Undo: ";
	private static final String MESSAGE_REDO = "Redo: ";
	private static final String MESSAGE_EVENT = "\"%1$s\" on %2$d/%3$d %4$02d:%5$02d - "
			+ "%6$d/%7$d %8$02d:%9$02d";
	private static final String MESSAGE_EVENT_DATETIME = ", %1$d/%2$d, %3$02d:%4$02d - %5$02d:%6$02d";
	private static final String MESSAGE_FLOAT = "\"%1$s\"";
	private static final String MESSAGE_DEADLINE = "\"%1$s\" by %2$d/%3$d, %4$02d:%5$02d";

	private static final int ADD_INDEX = 0;

	// Test case of adding a floating task across logic and parser, using a stubbed location for storage.
	@Test
	public void testAddFloating() {
		String desc = "floating task";
		String timing = " ";
		LocalDateTime start = null;
		LocalDateTime end = null;
		
		Logic logic = Logic.getInstance(true);
		StateFeedback actualOutput = logic.executeCommand(ADD_COMMAND + desc + timing, ADD_INDEX);
		Task newTask = new Task(desc, null, start, end);
		ArrayList<Task> expectedTasks = new ArrayList<Task>();
		expectedTasks.add(newTask);
		ArrayList<Task> expectedArchives = new ArrayList<Task>();
		TaskList expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 1, 0);
		TaskList actualList = actualOutput.getAllTasks();
		ArrayList<Integer> expectedIndexes = new ArrayList<Integer>();
		String expectedFeedback = taskMessageWithMulti(newTask) + MESSAGE_ADDED;
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		assertEquals(actualOutput.getDisplayPosition(), 0);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		logic.clearStorageTester();
	}

	// test case for adding an event
	@Test
	public void testAddEvent() {
		String desc = "event task";
		String timing = " 31/5 10am to 1/6 9pm";
		LocalDateTime start = LocalDateTime.of(2016, 5, 31, 10, 0);
		LocalDateTime end = LocalDateTime.of(2016, 6, 1, 21, 0);
		
		Logic logic = Logic.getInstance(true);
		StateFeedback actualOutput = logic.executeCommand(ADD_COMMAND + desc + timing, ADD_INDEX);
		Task newTask = new Task(desc, null, start, end);
		ArrayList<Task> expectedTasks = new ArrayList<Task>();
		expectedTasks.add(newTask);
		ArrayList<Task> expectedArchives = new ArrayList<Task>();
		TaskList expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 1, 0);
		TaskList actualList = actualOutput.getAllTasks();
		ArrayList<Integer> expectedIndexes = new ArrayList<Integer>();
		String expectedFeedback = taskMessageWithMulti(newTask) + MESSAGE_ADDED;
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		assertEquals(actualOutput.getDisplayPosition(), 0);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		logic.clearStorageTester();
	}

	// test case for adding of deadline task.
	@Test
	public void testAddDeadline() {
		String desc = "deadline task";
		String timing = " by 1/6 9pm";
		LocalDateTime start = null;
		LocalDateTime end = LocalDateTime.of(2016, 6, 1, 21, 0);
		
		Logic logic = Logic.getInstance(true);
		StateFeedback actualOutput = logic.executeCommand(ADD_COMMAND + desc + timing, ADD_INDEX);
		Task newTask = new Task(desc, null, start, end);
		ArrayList<Task> expectedTasks = new ArrayList<Task>();
		expectedTasks.add(newTask);
		ArrayList<Task> expectedArchives = new ArrayList<Task>();
		TaskList expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 1, 0);
		TaskList actualList = actualOutput.getAllTasks();
		ArrayList<Integer> expectedIndexes = new ArrayList<Integer>();
		String expectedFeedback = taskMessageWithMulti(newTask) + MESSAGE_ADDED;
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		assertEquals(actualOutput.getDisplayPosition(), 0);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		logic.clearStorageTester();
	}

	// test the adding of multiple tasks of different type across logic and parser.
	// checks for the positioning of the tasks being inserted as well.
	@Test
	public void testAddMulitple() {
		String desc1 = "deadline task";
		String timing1 = " by 1/6 9pm";
		LocalDateTime start1 = null;
		LocalDateTime end1 = LocalDateTime.of(2016, 6, 1, 21, 0);
		String desc2 = "event task";
		String timing2 = " 31/5 10am to 1/6 9pm";
		LocalDateTime start2 = LocalDateTime.of(2016, 5, 31, 10, 0);
		LocalDateTime end2 = LocalDateTime.of(2016, 6, 1, 21, 0);
		String desc3 = "floating task";
		String timing3 = " ";
		LocalDateTime start3 = null;
		LocalDateTime end3 = null;
		String desc4 = "abc task";
		String timing4 = " ";
		LocalDateTime start4 = null;
		LocalDateTime end4 = null;
		
		Logic logic = Logic.getInstance(true);
		
		StateFeedback actualOutput = logic.executeCommand(ADD_COMMAND + desc1 + timing1, ADD_INDEX);
		Task newTask = new Task(desc1, null, start1, end1);
		ArrayList<Task> expectedTasks = new ArrayList<Task>();
		expectedTasks.add(newTask);
		ArrayList<Task> expectedArchives = new ArrayList<Task>();
		TaskList expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 1, 0);
		TaskList actualList = actualOutput.getAllTasks();
		ArrayList<Integer> expectedIndexes = new ArrayList<Integer>();
		String expectedFeedback = taskMessageWithMulti(newTask) + MESSAGE_ADDED;
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		assertEquals(actualOutput.getDisplayPosition(), 0);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		actualOutput = logic.executeCommand(ADD_COMMAND + desc2 + timing2, ADD_INDEX);
		newTask = new Task(desc2, null, start2, end2);
		// inserts at the front due to sorting
		expectedTasks.add(0, newTask);
		expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 2, 0);
		actualList = actualOutput.getAllTasks();
		expectedIndexes = new ArrayList<Integer>();
		expectedFeedback = taskMessageWithMulti(newTask) + MESSAGE_ADDED;
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		assertEquals(actualOutput.getDisplayPosition(), 0);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		actualOutput = logic.executeCommand(ADD_COMMAND + desc3 + timing3, ADD_INDEX);
		newTask = new Task(desc3, null, start3, end3);
		// inserts at the front due to sorting
		expectedTasks.add(newTask);
		expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 3, 0);
		actualList = actualOutput.getAllTasks();
		expectedIndexes = new ArrayList<Integer>();
		expectedFeedback = taskMessageWithMulti(newTask) + MESSAGE_ADDED;
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		// position shifted to 2 due to being inserted as last
		assertEquals(actualOutput.getDisplayPosition(), 2);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		actualOutput = logic.executeCommand(ADD_COMMAND + desc4 + timing4, ADD_INDEX);
		newTask = new Task(desc4, null, start4, end4);
		// inserts at the front due to sorting
		expectedTasks.add(2, newTask);
		expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 4, 0);
		actualList = actualOutput.getAllTasks();
		expectedIndexes = new ArrayList<Integer>();
		expectedFeedback = taskMessageWithMulti(newTask) + MESSAGE_ADDED;
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		// position shifted to 2 due to being inserted at third position by alphabetical sort
		assertEquals(actualOutput.getDisplayPosition(), 2);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		logic.clearStorageTester();
	}

	// tests the undo and redo functionality of adding a task.
	@Test
	public void testUndoAndRedo() {
		String desc = "event task";
		String timing = " 31/5 10am to 1/6 9pm";
		LocalDateTime start = LocalDateTime.of(2016, 5, 31, 10, 0);
		LocalDateTime end = LocalDateTime.of(2016, 6, 1, 21, 0);
		
		Logic logic = Logic.getInstance(true);
		StateFeedback actualOutput = logic.executeCommand(ADD_COMMAND + desc + timing, ADD_INDEX);
		Task newTask = new Task(desc, null, start, end);
		ArrayList<Task> expectedTasks = new ArrayList<Task>();
		expectedTasks.add(newTask);
		ArrayList<Task> expectedArchives = new ArrayList<Task>();
		TaskList expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 1, 0);
		TaskList actualList = actualOutput.getAllTasks();
		ArrayList<Integer> expectedIndexes = new ArrayList<Integer>();
		String expectedFeedback = taskMessageWithMulti(newTask) + MESSAGE_ADDED;
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		assertEquals(actualOutput.getDisplayPosition(), 0);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		actualOutput = logic.executeCommand(UNDO_COMMAND, 0);
		expectedTasks.remove(newTask);
		expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 0, 0);
		actualList = actualOutput.getAllTasks();
		expectedFeedback = MESSAGE_UNDO + taskMessageWithMulti(newTask) + MESSAGE_REMOVE;
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		assertEquals(actualOutput.getDisplayPosition(), 0);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		actualOutput = logic.executeCommand(REDO_COMMAND, 0);
		expectedTasks.add(newTask);
		expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 1, 0);
		actualList = actualOutput.getAllTasks();
		expectedFeedback = MESSAGE_REDO + taskMessageWithMulti(newTask) + MESSAGE_ADDED;
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		assertEquals(actualOutput.getDisplayPosition(), 0);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		logic.clearStorageTester();
	}

	// tests for deletion of a task by the position of the task.
	@Test
	public void testDelete() {
		String desc = "event task";
		String timing = " 31/5 10am to 1/6 9pm";
		LocalDateTime start = LocalDateTime.of(2016, 5, 31, 10, 0);
		LocalDateTime end = LocalDateTime.of(2016, 6, 1, 21, 0);
		
		Logic logic = Logic.getInstance(true);
		StateFeedback actualOutput = logic.executeCommand(ADD_COMMAND + desc + timing, ADD_INDEX);
		Task newTask = new Task(desc, null, start, end);
		ArrayList<Task> expectedTasks = new ArrayList<Task>();
		expectedTasks.add(newTask);
		ArrayList<Task> expectedArchives = new ArrayList<Task>();
		TaskList expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 1, 0);
		TaskList actualList = actualOutput.getAllTasks();
		ArrayList<Integer> expectedIndexes = new ArrayList<Integer>();
		String expectedFeedback = taskMessageWithMulti(newTask) + MESSAGE_ADDED;
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		assertEquals(actualOutput.getDisplayPosition(), 0);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		actualOutput = logic.executeCommand(DELETE_COMMAND + "1", 0);
		expectedTasks.remove(0);
		expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 0, 0);
		actualList = actualOutput.getAllTasks();
		expectedFeedback = taskMessageWithMulti(newTask) + MESSAGE_REMOVE;
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		assertEquals(actualOutput.getDisplayPosition(), 0);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		logic.clearStorageTester();
	}

	// tests the deletion of multiple tasks through location.
	@Test
	public void testDeleteMultiple() {
		String desc1 = "deadline task";
		String timing1 = " by 1/6 9pm";
		LocalDateTime start1 = null;
		LocalDateTime end1 = LocalDateTime.of(2016, 6, 1, 21, 0);
		String desc2 = "event task";
		String timing2 = " 31/5 10am to 1/6 9pm";
		LocalDateTime start2 = LocalDateTime.of(2016, 5, 31, 10, 0);
		LocalDateTime end2 = LocalDateTime.of(2016, 6, 1, 21, 0);
		String desc3 = "floating task";
		String timing3 = " ";
		LocalDateTime start3 = null;
		LocalDateTime end3 = null;
		String desc4 = "abc task";
		String timing4 = " ";
		LocalDateTime start4 = null;
		LocalDateTime end4 = null;
		
		Logic logic = Logic.getInstance(true);
		
		StateFeedback actualOutput = logic.executeCommand(ADD_COMMAND + desc1 + timing1, ADD_INDEX);
		Task newTask = new Task(desc1, null, start1, end1);
		ArrayList<Task> expectedTasks = new ArrayList<Task>();
		expectedTasks.add(newTask);
		ArrayList<Task> expectedArchives = new ArrayList<Task>();
		ArrayList<Integer> expectedIndexes = new ArrayList<Integer>();
		
		actualOutput = logic.executeCommand(ADD_COMMAND + desc2 + timing2, ADD_INDEX);
		newTask = new Task(desc2, null, start2, end2);
		expectedTasks.add(0, newTask);
		
		actualOutput = logic.executeCommand(ADD_COMMAND + desc3 + timing3, ADD_INDEX);
		newTask = new Task(desc3, null, start3, end3);
		expectedTasks.add(newTask);
		
		actualOutput = logic.executeCommand(ADD_COMMAND + desc4 + timing4, ADD_INDEX);
		newTask = new Task(desc4, null, start4, end4);
		expectedTasks.add(2, newTask);
		TaskList expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 4, 0);
		TaskList actualList = actualOutput.getAllTasks();
		expectedIndexes = new ArrayList<Integer>();
		String expectedFeedback = taskMessageWithMulti(newTask) + MESSAGE_ADDED;
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		// position shifted to 2 due to being inserted at third position by alphabetical sort
		assertEquals(actualOutput.getDisplayPosition(), 2);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		assertEquals(actualOutput.getFeedback(), expectedFeedback);
		
		actualOutput = logic.executeCommand(DELETE_COMMAND + "1-4", 0);
		expectedTasks.clear();
		expectedList = new TaskList(expectedTasks, expectedArchives, 0, 0, 0, 0);
		actualList = actualOutput.getAllTasks();
		
		compareTaskList(expectedList, actualList);
		assertEquals(actualOutput.getState(), StateFeedback.State.ALL_TASKS);
		assertEquals(actualOutput.getDisplayPosition(), 0);
		assertEquals(actualOutput.getDetailedIndexes(), expectedIndexes);
		
		logic.clearStorageTester();
	}
	
	// comparator function for tasklists.
	private void compareTaskList(TaskList expectedList, TaskList actualList) {
		// tests the TaskList object in state feedback
		ArrayList<Task> actualTasks = actualList.getTasks();
		ArrayList<Task> expectedTasks = expectedList.getTasks();
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
		ArrayList<Task> actualArchives = actualList.getArchives();
		ArrayList<Task> expectedArchives = expectedList.getTasks();
		for (int i = 0; i < actualArchives.size(); i++) {
			Task exTask = expectedArchives.get(i);
			Task actlTask = actualArchives.get(i);
			assertEquals(exTask.getDesc(), actlTask.getDesc());
			assertEquals(exTask.getLocation(), actlTask.getLocation());
			assertEquals(exTask.isCompleted(), actlTask.isCompleted());
			assertEquals(exTask.isImportant(), actlTask.isImportant());
			assertEquals(exTask.isOverdue(), actlTask.isOverdue());
			assertEquals(exTask.getStartTime(), actlTask.getStartTime());
			assertEquals(exTask.getEndTime(), actlTask.getEndTime());
			assertEquals(exTask.getTaskType(), actlTask.getTaskType());			
		}
		
		assertEquals(expectedList.getOverdueCount(), actualList.getOverdueCount());
		assertEquals(expectedList.getTodayCount(), actualList.getTodayCount());
		assertEquals(expectedList.getRemainingCount(), actualList.getRemainingCount());
		assertEquals(expectedList.getArchiveCount(), actualList.getArchiveCount());
		assertEquals(expectedList.getUncompletedCount(), actualList.getUncompletedCount());
	}

	// for generation of messages of the task given as input, includes multiple tasks
	private String taskMessageWithMulti(Task task) {
		String feedback = taskMessage(task);
		if (task.getSlot() != null) {
			feedback += additionalTimings(task.getSlot());
		}
		return feedback;
	}

	// for generation of messages of task given as input, excludes multiple tasks
	private String taskMessage(Task task) {
		Task.Type taskType = task.getTaskType();
		String feedback = null;
		switch (taskType) {
		case EVENT :
			feedback = String.format(MESSAGE_EVENT, task.getDesc(), task.getStartTime().getDayOfMonth(),
					task.getStartTime().getMonthValue(), task.getStartTime().getHour(), 
					task.getStartTime().getMinute(), task.getEndTime().getDayOfMonth(), 
					task.getEndTime().getMonthValue(), task.getEndTime().getHour(),
					task.getEndTime().getMinute());
			break;
		case FLOATING :
			feedback = String.format(MESSAGE_FLOAT, task.getDesc());
			break;
		case DEADLINE :
			feedback = String.format(MESSAGE_DEADLINE, task.getDesc(), task.getEndTime().getDayOfMonth(),
					task.getEndTime().getMonthValue(), task.getEndTime().getHour(),
					task.getEndTime().getMinute());
			break;
		}
		return feedback;
	}

	// generator for feedback message for slots
	private String additionalTimings(MultipleSlot block) {
		String feedback = "";
		ArrayList<DateTimePair> slots = block.getSlots();
		for (DateTimePair pair : slots) {
			LocalDateTime start = pair.getEarlierDateTime();
			LocalDateTime end = pair.getLaterDateTime();
			feedback += String.format(MESSAGE_EVENT_DATETIME, start.getDayOfMonth(), start.getMonthValue(),
					start.getHour(), start.getMinute(), end.getHour(), end.getMinute());
		}
		return feedback;
	}
}
