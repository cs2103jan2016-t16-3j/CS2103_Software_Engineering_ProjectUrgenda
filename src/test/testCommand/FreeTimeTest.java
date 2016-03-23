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
		LocalDateTime rangeStart = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2016, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 12.01pm to 17.59pm starttime and endtime both infront
		// of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2016, 3, 23, 11, 59);
		LocalDateTime end1 = LocalDateTime.of(2016, 3, 23, 18, 01);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "There are no available time between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2016, 3, 23, 18, 0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yy");
		exTask1.setDesc(exStart1.format(formatter));
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase = test.execute();
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
	 * 1 task comparing with range timing is from 12.00pm to 18.00pm. start time
	 * and end time are the same as the range
	 */
	@Test
	public void test006NoFreeTimeOneTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2016, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 12.01pm to 17.59pm starttime and endtime both infront
		// of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime end1 = LocalDateTime.of(2016, 3, 23, 18, 0);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "There are no available time between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2016, 3, 23, 18, 0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yy");
		exTask1.setDesc(exStart1.format(formatter));
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase = test.execute();
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
		LocalDateTime rangeStart = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2016, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 6.01pm to 7pm starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2016, 3, 23, 18, 01);
		LocalDateTime end1 = LocalDateTime.of(2016, 3, 23, 19, 00);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2016, 3, 23, 18, 0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yy");
		exTask1.setDesc(exStart1.format(formatter));
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase = test.execute();
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
		LocalDateTime rangeStart = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2016, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 12.01 to 7pm starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2016, 3, 23, 12, 01);
		LocalDateTime end1 = LocalDateTime.of(2016, 3, 23, 19, 00);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2016, 3, 23, 12, 1);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yy");
		exTask1.setDesc(exStart1.format(formatter));
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase = test.execute();
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
		LocalDateTime rangeStart = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2016, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 10.00 to 11.59 starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2016, 3, 23, 17, 59);
		LocalDateTime end1 = LocalDateTime.of(2016, 3, 23, 19, 00);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2016, 3, 23, 17, 59);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yy");
		exTask1.setDesc(exStart1.format(formatter));
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase = test.execute();
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

	// 1 task comparing with range
	// timing is from 10.00 to 11.59 start time and end time both in front of
	// range
	@Test
	public void test002FreeTimeOneTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2016, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 10.00 to 11.59 starttime and endtime both infront of
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2016, 3, 23, 10, 0);
		LocalDateTime end1 = LocalDateTime.of(2016, 3, 23, 11, 59);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2016, 3, 23, 18, 0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yy");
		exTask1.setDesc(exStart1.format(formatter));
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase = test.execute();
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

	// 1 task comparing with range
	// timing is from 11.59 to 14.00 start time outside range, end time inside
	// range
	@Test
	public void test001FreeTimeOneTask() {
		// setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		ArrayList<Task> expectedList = new ArrayList<Task>();
		LocalDateTime rangeStart = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2016, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);

		// timing is from 11.59 to 14.00 starttime outside range, endtime inside
		// range
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2016, 3, 23, 11, 59);
		LocalDateTime end1 = LocalDateTime.of(2016, 3, 23, 14, 0);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);

		// configuring expected outputs
		String expectedPhrase = "Showing available time slots between 23/3, 12:00 to 23/3, 18:00";
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2016, 3, 23, 14, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2016, 3, 23, 18, 0);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yy");
		exTask1.setDesc(exStart1.format(formatter));
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);

		// getting actual outputs
		String actualPhrase = test.execute();
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
