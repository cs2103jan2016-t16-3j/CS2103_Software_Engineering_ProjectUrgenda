package testStorage;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.Test;

import urgenda.storage.Storage;
import urgenda.storage.FileEditor;
import urgenda.storage.JsonCipher;
import urgenda.storage.SettingsEditor;
import urgenda.util.Task;

import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileEditorTest {
	private static final String TEST_FILE_LOCATION = "testfiles";
	private static final String TEST_FILE_NAME = "test.txt";
	private static final String TEST_SETTINGS_FILE_NAME = "testsettings.txt";

	@Test
	public void test01ReadWriteSingleLine() {
		FileEditor file = new FileEditor(TEST_FILE_LOCATION, TEST_FILE_NAME);
		String inputPhrase = "test phrase";
		file.writeToFile(inputPhrase);
		String retrievedPhrase = file.retrieveFromFile();
		assertEquals("not the same", inputPhrase, retrievedPhrase);

		inputPhrase = "";
		file.writeToFile(inputPhrase);
		retrievedPhrase = file.retrieveFromFile();
		assertEquals("not the same", inputPhrase, retrievedPhrase);
	}

	@Test
	public void test02ReadWriteMultipleLinesOneArrayList() {
		FileEditor file = new FileEditor(TEST_FILE_LOCATION, TEST_FILE_NAME);
		ArrayList<String> inputArrayOne = new ArrayList<String>();
		ArrayList<String> inputArrayTwo = new ArrayList<String>();
		inputArrayOne.add("string 1");
		inputArrayOne.add("string 2");
		inputArrayOne.add("string 3");
		inputArrayOne.add("string 4");
		inputArrayOne.add("string 5");
		inputArrayOne.add("string 6");
		file.writeToFile(inputArrayOne, inputArrayTwo);
		ArrayList<String> retrievedArrayOne = new ArrayList<String>();
		ArrayList<String> retrievedArrayTwo = new ArrayList<String>();
		file.retrieveFromFile(retrievedArrayOne, retrievedArrayTwo);
		for (int i = 0; i < retrievedArrayOne.size(); i++) {
			assertEquals(i + "diff", inputArrayOne.get(i), retrievedArrayOne.get(i));
		}

		inputArrayOne.clear();
		retrievedArrayOne.clear();
		file.writeToFile(inputArrayOne, inputArrayTwo);
		file.retrieveFromFile(retrievedArrayOne, retrievedArrayTwo);
		for (int i = 0; i < retrievedArrayOne.size(); i++) {
			assertEquals(i + "diff", inputArrayOne.get(i), retrievedArrayOne.get(i));
		}

		for (int i = 0; i < 1000; i++) {
			inputArrayOne.add("string" + i);
		}
		file.writeToFile(inputArrayOne, inputArrayTwo);
		file.retrieveFromFile(retrievedArrayOne, retrievedArrayTwo);
		for (int i = 0; i < retrievedArrayOne.size(); i++) {
			assertEquals(i + "diff", inputArrayOne.get(i), retrievedArrayOne.get(i));
		}
	}

	@Test
	public void test03ReadWriteMultipleLinesTwoArrayList() {
		FileEditor file = new FileEditor(TEST_FILE_LOCATION, TEST_FILE_NAME);
		ArrayList<String> inputArrayOne = new ArrayList<String>();
		ArrayList<String> inputArrayTwo = new ArrayList<String>();
		for (int i = 0; i < 3; i++) {
			inputArrayOne.add("String" + i);
		}
		for (int i = 0; i < 3; i++) {
			inputArrayTwo.add("String" + i);
		}
		file.writeToFile(inputArrayOne, inputArrayTwo);
		ArrayList<String> retrievedArrayOne = new ArrayList<String>();
		ArrayList<String> retrievedArrayTwo = new ArrayList<String>();
		file.retrieveFromFile(retrievedArrayOne, retrievedArrayTwo);
		for (int i = 0; i < retrievedArrayOne.size(); i++) {
			assertEquals(i + "diff", inputArrayOne.get(i), retrievedArrayOne.get(i));
		}
		for (int i = 0; i < retrievedArrayTwo.size(); i++) {
			assertEquals(i + "diff", inputArrayTwo.get(i), retrievedArrayTwo.get(i));
		}

		inputArrayOne.clear();
		inputArrayTwo.clear();
		retrievedArrayOne.clear();
		retrievedArrayTwo.clear();
		file.writeToFile(inputArrayOne, inputArrayTwo);
		file.retrieveFromFile(retrievedArrayOne, retrievedArrayTwo);
		for (int i = 0; i < retrievedArrayOne.size(); i++) {
			assertEquals(i + "diff", inputArrayOne.get(i), retrievedArrayOne.get(i));
		}
		for (int i = 0; i < retrievedArrayTwo.size(); i++) {
			assertEquals(i + "diff", inputArrayTwo.get(i), retrievedArrayTwo.get(i));
		}

		for (int i = 0; i < 2000; i++) {
			inputArrayOne.add("String" + i);
		}
		for (int i = 0; i < 3000; i++) {
			inputArrayTwo.add("String" + i);
		}
		file.writeToFile(inputArrayOne, inputArrayTwo);
		file.retrieveFromFile(retrievedArrayOne, retrievedArrayTwo);
		for (int i = 0; i < retrievedArrayOne.size(); i++) {
			assertEquals(i + "diff", inputArrayOne.get(i), retrievedArrayOne.get(i));
		}
		for (int i = 0; i < retrievedArrayTwo.size(); i++) {
			assertEquals(i + "diff", inputArrayTwo.get(i), retrievedArrayTwo.get(i));
		}

		inputArrayOne.clear();
		inputArrayTwo.clear();
		retrievedArrayOne.clear();
		retrievedArrayTwo.clear();
		for (int i = 0; i < 7000; i++) {
			inputArrayTwo.add("String " + i
					+ " is a very very long string used to test the robustness of the method in execution speed and smoothness");
		}
		file.writeToFile(inputArrayOne, inputArrayTwo);
		file.retrieveFromFile(retrievedArrayOne, retrievedArrayTwo);
		for (int i = 0; i < retrievedArrayOne.size(); i++) {
			assertEquals(i + "diff", inputArrayOne.get(i), retrievedArrayOne.get(i));
		}
		for (int i = 0; i < retrievedArrayTwo.size(); i++) {
			assertEquals(i + "diff", inputArrayTwo.get(i), retrievedArrayTwo.get(i));
		}

		inputArrayOne.clear();
		inputArrayTwo.clear();
		retrievedArrayOne.clear();
		retrievedArrayTwo.clear();
		for (int i = 0; i < 100000; i++) {
			inputArrayOne.add("String " + i
					+ " is a very very long string used to test the robustness of the method in execution speed and smoothness");
		}
		for (int i = 0; i < 100000; i++) {
			inputArrayTwo.add("String " + i
					+ " is a very very long string used to test the robustness of the method in execution speed and smoothness");
		}
		file.writeToFile(inputArrayOne, inputArrayTwo);
		file.retrieveFromFile(retrievedArrayOne, retrievedArrayTwo);
		for (int i = 0; i < retrievedArrayOne.size(); i++) {
			assertEquals(i + "diff", inputArrayOne.get(i), retrievedArrayOne.get(i));
		}
		for (int i = 0; i < retrievedArrayTwo.size(); i++) {
			assertEquals(i + "diff", inputArrayTwo.get(i), retrievedArrayTwo.get(i));
		}
		inputArrayOne.clear();
		inputArrayTwo.clear();
		retrievedArrayOne.clear();
		retrievedArrayTwo.clear();
		// file.clearFile();
	}

	@Test
	public void testHelp() {
		Storage store = new Storage(TEST_FILE_LOCATION, TEST_FILE_NAME);
		String phrase = store.retrieveHelp();
		System.out.println(phrase);
	}

	@Test
	public void test1Save() {
		Storage store = new Storage();
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
		Storage store = new Storage();
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<Task> _archive = new ArrayList<Task>();
		// ArrayList<MultipleSlot> _blocks = new ArrayList<MultipleSlot>();
		int id = store.updateArrayLists(_tasks, _archive);
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
		System.out.println(id);
	}

	@Test
	public void testConvertToString() {
		JsonCipher _cipher = new JsonCipher();
		_cipher.setDirectory("settings");
		_cipher.setFileName("settings.txt");
		_cipher.convertToString();
		String test = _cipher.getDetailsString();
		System.out.println(test);
	}

	@Test
	public void testSettingsEditor() {
		SettingsEditor _settings = new SettingsEditor(TEST_FILE_LOCATION, TEST_SETTINGS_FILE_NAME);
		_settings.setFileDir("testfiles");
		_settings.setFileName("test er.txt");
		_settings.saveSettings();
	}

}
