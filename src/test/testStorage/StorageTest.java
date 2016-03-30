package testStorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.storage.StorageTester;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;
import urgenda.util.UrgendaException;

public class StorageTest {
	private static final String TEST_FILE_LOCATION = "testfiles";
	private static final String TEST_FILE_NAME = "test.txt";

	private static final String TEST_FILE_NEW_LOCATION = "testfiles\\test3\\test2.txt";

	private static final String TEST_FILE_LOCATION_2 = "testfiles\\test3";
	private static final String TEST_FILE_NAME_2 = "test2.txt";

	/*
	 * Tests file manipulation given a string with file name specified, and exists in new location.
	 */
	@Test
	public void test003FileManipulationExist(){
		StorageTester store = new StorageTester();
		
		File existingFile = new File(TEST_FILE_NEW_LOCATION);
		existingFile.createNewFile();
	}
	
	/*
	 * Tests file manipulation given a string with file name specified, and
	 * doesn't exist in new location
	 */
	@Test
	public void test002FileManipulation() {
		StorageTester store = new StorageTester();

		try {
			store.changeFileSettings(TEST_FILE_NEW_LOCATION);
		} catch (UrgendaException e) {

		}

		File file = new File(TEST_FILE_LOCATION_2, TEST_FILE_NAME_2);
		assertTrue(file.exists());
		assertEquals(TEST_FILE_LOCATION_2, file.getParent());
		assertEquals(TEST_FILE_NAME_2, file.getName());

		file.delete();
	}

	/*
	 * Tests the saving of tasks into a txt file, and retrieving the tasks from
	 * the txt file back into an array list of task
	 */
	@Test
	public void test001Full() {
		StorageTester store = new StorageTester(TEST_FILE_LOCATION, TEST_FILE_NAME);
		ArrayList<Task> tasks = new ArrayList<Task>();
		ArrayList<Task> archives = new ArrayList<Task>();

		ArrayList<String> _tags = new ArrayList<String>();
		MultipleSlot slot = null;
		LocalDateTime notime = null;
		Task obj = new Task(1, "Buy milk", "FLOATING", "", false, false, false, notime, notime, LocalDateTime.now(),
				notime, _tags, slot);
		Task obj2 = new Task(2, "Submit ie2150 draft", "DEADLINE", "", true, false, true, notime,
				LocalDateTime.of(2016, Month.FEBRUARY, 24, 23, 59), LocalDateTime.now(), notime, _tags, slot);
		Task obj3 = new Task(3, "Submit ie2100 hw3", "DEADLINE", "", false, false, false, notime, LocalDateTime.now(),
				LocalDateTime.now(), notime, _tags, slot);
		Task obj4 = new Task(4, "Dental Appointment", "EVENT", " ", true, false, false,
				LocalDateTime.now().minusHours(3), LocalDateTime.now().plusHours(1), LocalDateTime.now(), notime, _tags,
				slot);
		Task obj5 = new Task(5, "Travel to Sweden", "EVENT", " ", false, false, false,
				LocalDateTime.of(2016, Month.JULY, 26, 00, 00), LocalDateTime.of(2016, Month.AUGUST, 17, 23, 59),
				LocalDateTime.now(), notime, _tags, slot);
		Task obj6 = new Task(1, "Mop floor", "FLOATING", "", true, false, false, notime, notime, LocalDateTime.now(),
				notime, _tags, slot);

		tasks.add(obj);
		tasks.add(obj2);
		tasks.add(obj3);
		archives.add(obj4);
		archives.add(obj5);
		archives.add(obj6);
		store.save(tasks, archives);

		store = new StorageTester();
		ArrayList<Task> actlTasks = store.updateCurrentTaskList();
		ArrayList<Task> actlArchives = store.updateArchiveTaskList();

		for (int i = 0; i < actlTasks.size(); i++) {
			Task exTask = tasks.get(i);
			Task actlTask = tasks.get(i);
			assertEquals(exTask.getDesc(), actlTask.getDesc());
			assertEquals(exTask.getLocation(), actlTask.getLocation());
			assertEquals(exTask.isCompleted(), actlTask.isCompleted());
			assertEquals(exTask.isImportant(), actlTask.isImportant());
			assertEquals(exTask.isOverdue(), actlTask.isOverdue());
			assertEquals(exTask.getStartTime(), actlTask.getStartTime());
			assertEquals(exTask.getEndTime(), actlTask.getEndTime());
			assertEquals(exTask.getDateAdded(), actlTask.getDateAdded());
			assertEquals(exTask.getDateModified(), actlTask.getDateModified());
			assertEquals(exTask.getTaskType(), actlTask.getTaskType());
		}

		for (int i = 0; i < actlArchives.size(); i++) {
			Task exTask = archives.get(i);
			Task actlTask = actlArchives.get(i);
			assertEquals(exTask.getDesc(), actlTask.getDesc());
			assertEquals(exTask.getLocation(), actlTask.getLocation());
			assertEquals(exTask.isCompleted(), actlTask.isCompleted());
			assertEquals(exTask.isImportant(), actlTask.isImportant());
			assertEquals(exTask.isOverdue(), actlTask.isOverdue());
			assertEquals(exTask.getStartTime(), actlTask.getStartTime());
			assertEquals(exTask.getEndTime(), actlTask.getEndTime());
			assertEquals(exTask.getDateAdded(), actlTask.getDateAdded());
			assertEquals(exTask.getDateModified(), actlTask.getDateModified());
			assertEquals(exTask.getTaskType(), actlTask.getTaskType());
		}

		store.delete();

	}

}
