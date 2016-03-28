package testCommand;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.Test;

import org.junit.runners.MethodSorters;

import urgenda.command.FindFree;
import urgenda.logic.LogicData;
import urgenda.util.Task;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FreeTimeTest {

	private static final String MESSAGE_INVALID_TIME_RANGE = "Invalid time range for finding available time";
	private static final String MESSAGE_FREE_TIME = "Showing available time slots between "
			+ "%1$d/%2$d, %3$02d:%4$02d to %5$d/%6$d, %7$02d:%8$02d";

	/**
	 * start time of range is before current time
	 */
	@Test
	public void test015ModifiedRange() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 6.01pm to 7pm starttime and endtime both infront of
		// range
		// removed all tasks to test modified range. 
//		Task task1 = new Task();
//		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 11, 58);
//		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 12, 01);
//		task1.setStartTime(start1);
//		task1.setEndTime(end1);
//		task1.updateTaskType();
//		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = String.format(MESSAGE_FREE_TIME, LocalDateTime.now().getDayOfMonth(),
				LocalDateTime.now().getMonthValue(), LocalDateTime.now().getHour(), LocalDateTime.now().getMinute(),
				rangeEnd.getDayOfMonth(), rangeEnd.getMonthValue(), rangeEnd.getHour(), rangeEnd.getMinute());
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.now();
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 18, 0);
		exTask1.setDesc("5 hours 49 minutes "); //this desc is obviously wrong. But so is actual desc?
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = e.getMessage();
			assertEquals(MESSAGE_INVALID_TIME_RANGE, actualPhrase);
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		/*
		 * TODO: Why is everything still running fine when test.execute() is
		 * supposed to throw an exception and not have executed everything
		 * below?
		 */
		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * End time of range is before current timing
	 */
	@Test
	public void test014InvalidRange() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2016, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 6.01pm to 7pm starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 11, 58);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 12, 01);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType();
		data.addTask(task1);
		Task task2 = new Task();
		LocalDateTime start2 = LocalDateTime.of(2017, 3, 23, 11, 59);
		LocalDateTime end2 = LocalDateTime.of(2017, 3, 23, 12, 11);
		task2.setStartTime(start2);
		task2.setEndTime(end2);
		task2.updateTaskType(start2, end2);
		data.addTask(task2);

		// configuring expected outputs
		String expectedPhrase = MESSAGE_INVALID_TIME_RANGE;
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2017, 3, 23, 12, 11);
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 18, 0);
		exTask1.setDesc("5 hours 49 minutes ");
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = e.getMessage();
			assertEquals(MESSAGE_INVALID_TIME_RANGE, actualPhrase);
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		/*
		 * TODO: Why is everything still running fine when test.execute() is
		 * supposed to throw an exception and not have executed everything
		 * below?
		 */
		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * Start time of range is after end time of range
	 */
	@Test
	public void test013InvalidRange() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 18, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 12, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 6.01pm to 7pm starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 11, 58);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 12, 01);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType();
		data.addTask(task1);
		Task task2 = new Task();
		LocalDateTime start2 = LocalDateTime.of(2017, 3, 23, 11, 59);
		LocalDateTime end2 = LocalDateTime.of(2017, 3, 23, 12, 11);
		task2.setStartTime(start2);
		task2.setEndTime(end2);
		task2.updateTaskType(start2, end2);
		data.addTask(task2);

		// configuring expected outputs
		String expectedPhrase = MESSAGE_INVALID_TIME_RANGE;
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2017, 3, 23, 12, 11);
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 18, 0);
		exTask1.setDesc("5 hours 49 minutes ");
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = e.getMessage();
			assertEquals(MESSAGE_INVALID_TIME_RANGE, actualPhrase);
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		/*
		 * TODO: Why is everything still running fine when test.execute() is
		 * supposed to throw an exception and not have executed everything
		 * below?
		 */
		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * two tasks 1st has start time in front of range, end time inside range 2nd
	 * has start time in front of range after start time, end time inside range,
	 * after 1st end time
	 */
	@Test
	public void test012FreeTimeMultipleTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 6.01pm to 7pm starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 11, 58);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 12, 01);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType();
		data.addTask(task1);
		Task task2 = new Task();
		LocalDateTime start2 = LocalDateTime.of(2017, 3, 23, 11, 59);
		LocalDateTime end2 = LocalDateTime.of(2017, 3, 23, 12, 11);
		task2.setStartTime(start2);
		task2.setEndTime(end2);
		task2.updateTaskType(start2, end2);
		data.addTask(task2);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2017, 3, 23, 12, 11);
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 18, 0);
		exTask1.setDesc("5 hours 49 minutes ");
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = MESSAGE_INVALID_TIME_RANGE;
			assertEquals(MESSAGE_INVALID_TIME_RANGE, e.getMessage());
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * two tasks 1st has start time in front of range, end time inside range 2nd
	 * has start time in front of range, end time inside range, after 1st end
	 * time
	 */
	@Test
	public void test011FreeTimeMultipleTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 6.01pm to 7pm starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 11, 59);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 12, 01);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType();
		data.addTask(task1);
		Task task2 = new Task();
		LocalDateTime start2 = LocalDateTime.of(2017, 3, 23, 11, 58);
		LocalDateTime end2 = LocalDateTime.of(2017, 3, 23, 12, 11);
		task2.setStartTime(start2);
		task2.setEndTime(end2);
		task2.updateTaskType(start2, end2);
		data.addTask(task2);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2017, 3, 23, 12, 11);
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 18, 0);
		exTask1.setDesc("5 hours 49 minutes ");
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = MESSAGE_INVALID_TIME_RANGE;
			assertEquals(MESSAGE_INVALID_TIME_RANGE, e.getMessage());
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * two tasks 1st has start time in front of range, end time inside range 2nd
	 * has start time inside range, end time after the range
	 */
	@Test
	public void test010FreeTimeMultipleTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 6.01pm to 7pm starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 11, 59);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 12, 01);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);
		Task task2 = new Task();
		LocalDateTime start2 = LocalDateTime.of(2017, 3, 23, 17, 59);
		LocalDateTime end2 = LocalDateTime.of(2017, 3, 23, 18, 01);
		task2.setStartTime(start2);
		task2.setEndTime(end2);
		task2.updateTaskType(start2, end2);
		data.addTask(task2);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2017, 3, 23, 12, 1);
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 17, 59);
		exTask1.setDesc("5 hours 58 minutes ");
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = MESSAGE_INVALID_TIME_RANGE;
			assertEquals(MESSAGE_INVALID_TIME_RANGE, e.getMessage());
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * two tasks, both inside range
	 */
	@Test
	public void test009FreeTimeMultipleTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 6.01pm to 7pm starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 12, 01);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 13, 00);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);
		Task task2 = new Task();
		LocalDateTime start2 = LocalDateTime.of(2017, 3, 23, 17, 00);
		LocalDateTime end2 = LocalDateTime.of(2017, 3, 23, 17, 59);
		task2.setStartTime(start2);
		task2.setEndTime(end2);
		task2.updateTaskType(start2, end2);
		data.addTask(task2);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 12, 1);
		exTask1.setDesc("1 minute ");
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);
		Task exTask2 = new Task();
		LocalDateTime exStart2 = LocalDateTime.of(2017, 3, 23, 13, 0);
		LocalDateTime exEnd2 = LocalDateTime.of(2017, 3, 23, 17, 0);
		exTask2.setDesc("4 hours ");
		exTask2.setStartTime(exStart2);
		exTask2.setEndTime(exEnd2);
		expectedList.add(exTask2);
		Task exTask3 = new Task();
		LocalDateTime exStart3 = LocalDateTime.of(2017, 3, 23, 17, 59);
		LocalDateTime exEnd3 = LocalDateTime.of(2017, 3, 23, 18, 0);
		exTask3.setDesc("1 minute ");
		exTask3.setStartTime(exStart3);
		exTask3.setEndTime(exEnd3);
		expectedList.add(exTask3);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = MESSAGE_INVALID_TIME_RANGE;
			assertEquals(MESSAGE_INVALID_TIME_RANGE, e.getMessage());
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * no tasks for comparison with range
	 */
	@Test
	public void test008FreeTimeNoTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 18, 0);
		exTask1.setDesc("6 hours ");
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = MESSAGE_INVALID_TIME_RANGE;
			assertEquals(MESSAGE_INVALID_TIME_RANGE, e.getMessage());
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * 1 task comparing with range timing is from 11.59pm to 18.01pm. start time
	 * is in front of range, end time is after the range
	 */
	@Test
	public void test007NoFreeTimeOneTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 12.01pm to 17.59pm starttime and endtime both infront
		// of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 11, 59);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 18, 01);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "There are no available time between 23/3, 12:00 to 23/3, 18:00";

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = MESSAGE_INVALID_TIME_RANGE;
			assertEquals(MESSAGE_INVALID_TIME_RANGE, e.getMessage());
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
	}

	/**
	 * 1 task comparing with range timing is from 12.00pm to 18.00pm. start time
	 * and end time are the same as the range
	 */
	@Test
	public void test006NoFreeTimeOneTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 12.01pm to 17.59pm starttime and endtime both infront
		// of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 18, 0);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "There are no available time between 23/3, 12:00 to 23/3, 18:00";

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = MESSAGE_INVALID_TIME_RANGE;
			assertEquals(MESSAGE_INVALID_TIME_RANGE, e.getMessage());
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * 1 task comparing with range timing is from 6.01pm to 7pm. both start time
	 * and end time is outside range
	 */
	@Test
	public void test005FreeTimeOneTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 6.01pm to 7pm starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 18, 01);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 19, 00);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 18, 0);
		exTask1.setDesc("6 hours ");
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = MESSAGE_INVALID_TIME_RANGE;
			assertEquals(MESSAGE_INVALID_TIME_RANGE, e.getMessage());
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * 1 task comparing with range timing is from 12.01pm to 7pm. start time is
	 * inside range next to range start, end time outside range
	 */
	@Test
	public void test004FreeTimeOneTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 12.01 to 7pm starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 12, 01);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 19, 00);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 12, 1);
		exTask1.setDesc("1 minute ");
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = MESSAGE_INVALID_TIME_RANGE;
			assertEquals(MESSAGE_INVALID_TIME_RANGE, e.getMessage());
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * 1 task comparing with range timing is from 5.59pm to 7pm. start time is
	 * inside range next to range end, end time outside range
	 */
	@Test
	public void test003FreeTimeOneTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 10.00 to 11.59 starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 17, 59);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 19, 00);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 17, 59);
		exTask1.setDesc("5 hours 59 minutes ");
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = MESSAGE_INVALID_TIME_RANGE;
			assertEquals(MESSAGE_INVALID_TIME_RANGE, e.getMessage());
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * 1 task comparing with range timing is from 10.00 to 11.59 start time and
	 * end time both in front of range
	 */
	@Test
	public void test002FreeTimeOneTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 10.00 to 11.59 starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 10, 0);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 11, 59);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 18, 0);
		exTask1.setDesc("6 hours ");
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = MESSAGE_INVALID_TIME_RANGE;
			assertEquals(MESSAGE_INVALID_TIME_RANGE, e.getMessage());
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

	/**
	 * 1 task comparing with range timing is from 11.59 to 14.00 start time
	 * outside range, end time inside range
	 */
	@Test
	public void test001FreeTimeOneTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2017, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2017, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 11.59 to 14.00 starttime outside range, endtime inside
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2017, 3, 23, 11, 59);
		LocalDateTime end1 = LocalDateTime.of(2017, 3, 23, 14, 0);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2017, 3, 23, 14, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2017, 3, 23, 18, 0);
		exTask1.setDesc("4 hours ");
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase;
		try {
			actualPhrase = test.execute();
		} catch (Exception e) {
			actualPhrase = MESSAGE_INVALID_TIME_RANGE;
			assertEquals(MESSAGE_INVALID_TIME_RANGE, e.getMessage());
		}
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();

		// comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++) {
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getDesc(), actual.getDesc());
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
	}

}
