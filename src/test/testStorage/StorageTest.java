package testStorage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.junit.Test;

import urgenda.storage.Storage;
import urgenda.storage.StorageTester;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;
import urgenda.util.StorageException;

public class StorageTest {
	private static final String TEST_FILE_LOCATION = "testfiles";
	private static final String TEST_FILE_NAME = "test.txt";

	private static final String TEST_FILE_NEW_LOCATION = "testfiles\\test3\\test2.txt";

	private static final String TEST_FILE_LOCATION_2 = "testfiles\\test3";
	private static final String TEST_FILE_NAME_2 = "test2.txt";

	private static final String EXCEPTION_ERROR_MESSAGE_2 = "test2.txt already exist in TESTFILES\\TEST3. \nLoading tasks from existing file";

	/*
	 * Tests file manipulation given a string with file name specified, and
	 * exists in new location. Tests UrgendaException for existing file.
	 */
	@Test
	public void test004FileManipulationExist() throws IOException {
		StorageTester store = new StorageTester();

		File existingFile = new File(TEST_FILE_NEW_LOCATION);
		File dir = new File(TEST_FILE_LOCATION_2);
		dir.mkdir();
		existingFile.createNewFile();

		try {
			store.changeFileSettings(TEST_FILE_LOCATION_2);
		} catch (StorageException e) {
			
		}

		File file = new File(TEST_FILE_LOCATION_2, TEST_FILE_NAME);
		assertTrue(file.exists());
		assertEquals(TEST_FILE_LOCATION_2, file.getParent());
		assertEquals(TEST_FILE_NAME, file.getName());

		file.delete();
		existingFile.delete();
		dir.delete();
		store.delete();
	}
	
	/*
	 * Tests file manipulation given a string with file name specified, and
	 * exists in new location. Tests UrgendaException for existing file.
	 */
	@Test
	public void test003FileManipulationExist() throws IOException {
		StorageTester store = new StorageTester();

		File existingFile = new File(TEST_FILE_NEW_LOCATION);
		File dir = new File(TEST_FILE_LOCATION_2);
		dir.mkdir();
		existingFile.createNewFile();

		try {
			store.changeFileSettings(TEST_FILE_NEW_LOCATION);
		} catch (StorageException e) {
			assertEquals(TEST_FILE_LOCATION_2, e.getDir());
			assertEquals(TEST_FILE_NAME_2, e.getName());
			assertEquals(EXCEPTION_ERROR_MESSAGE_2, e.getMessage());
		}

		File file = new File(TEST_FILE_LOCATION_2, TEST_FILE_NAME_2);
		assertTrue(file.exists());
		assertEquals(TEST_FILE_LOCATION_2, file.getParent());
		assertEquals(TEST_FILE_NAME_2, file.getName());

		file.delete();
		existingFile.delete();
		dir.delete();
		store.delete();
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
		} catch (StorageException e) {

		}

		File file = new File(TEST_FILE_LOCATION_2, TEST_FILE_NAME_2);
		assertTrue(file.exists());
		assertEquals(TEST_FILE_LOCATION_2, file.getParent());
		assertEquals(TEST_FILE_NAME_2, file.getName());

		file.delete();
		store.delete();
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
			Task actlTask = actlTasks.get(i);
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
	
	@Test
	public void test00Constructing(){
		Storage store = new Storage();
		String fullDir = store.getDirPath();
		String dir = fullDir.trim().substring(fullDir.lastIndexOf("\\") + 1, fullDir.length());
		assertEquals("settings", dir);
	}

}
