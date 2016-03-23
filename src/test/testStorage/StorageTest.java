package testStorage;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

import urgenda.storage.Storage;
import urgenda.storage.StorageTester;
import urgenda.util.Task;

public class StorageTest {
	private static final String TEST_FILE_LOCATION = "testfiles";
	private static final String TEST_FILE_NAME = "test.txt";
	
	private static final String TEST_FILE_LOCATION_2 = "testfiles\\test3";
	private static final String TEST_FILE_NAME_2 = "test2.txt";
	
	
	@Test
	public void test1Save() {
		StorageTester store = new StorageTester(TEST_FILE_LOCATION, TEST_FILE_NAME);
		// store.changeFileName(TEST_FILE_NAME_2);
		// store.changeFilePath(TEST_FILE_LOCATION_2);
		store.changeFileSettings(TEST_FILE_LOCATION_2, TEST_FILE_NAME_2);
		Task task1 = new Task("test1");
		Task task2 = new Task("test2");
		Task task3 = new Task("test3");
		Task task4 = new Task("test4");
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<Task> _archive = new ArrayList<Task>();
		_tasks.add(task1);
		_tasks.add(task2);
		_tasks.add(task3);
		_tasks.add(task4);
		_archive.add(task1);
		_archive.add(task3);
		store.save(_tasks, _archive);
	}

	@Test
	public void test2UpdateArrayLists() {
		Storage store = new Storage(TEST_FILE_LOCATION, TEST_FILE_NAME);
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<Task> _archive = new ArrayList<Task>();
		// ArrayList<MultipleSlot> _blocks = new ArrayList<MultipleSlot>();
		_tasks = store.updateCurrentTaskList();
		_archive = store.updateArchiveTaskList();
		int i = 1;
		for (Task print : _tasks) {
			System.out.printf("task %d details:", i);
			System.out.println("");
			System.out.println("id: " + print.getId());
			System.out.println("desc: " + print.getDesc());
			System.out.println("type: " + print.getTaskType());
			System.out.println("location: " + print.getLocation());
			System.out.println("starttime: " + print.getStartTime());
			System.out.println("endtime: " + print.getEndTime());
			System.out.println("tags: " + print.getHashtags());
			System.out.println("date added: " + print.getDateAdded());
			System.out.println("date modified: " + print.getDateModified());
			System.out.println("completed: " + print.isCompleted());
			System.out.println("urgent: " + print.isImportant());
			System.out.println("overdue: " + print.isOverdue());
			i++;
		}
		for (Task print : _archive) {
			System.out.printf("archive %d details:", i);
			System.out.println("");
			System.out.println("id: " + print.getId());
			System.out.println("desc: " + print.getDesc());
			System.out.println("type: " + print.getTaskType());
			System.out.println("location: " + print.getLocation());
			System.out.println("starttime: " + print.getStartTime());
			System.out.println("endtime: " + print.getEndTime());
			System.out.println("tags: " + print.getHashtags());
			System.out.println("date added: " + print.getDateAdded());
			System.out.println("date modified: " + print.getDateModified());
			System.out.println("completed: " + print.isCompleted());
			System.out.println("urgent: " + print.isImportant());
			System.out.println("overdue: " + print.isOverdue());
			i++;
		}
	}
}
