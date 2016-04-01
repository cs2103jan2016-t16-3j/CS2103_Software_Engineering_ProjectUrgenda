package test.testStorage;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.Test;

import urgenda.storage.FileEditor;

import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileEditorTest {
	private static final String TEST_FILE_LOCATION = "testfiles";
	private static final String TEST_FILE_NAME = "test.txt";
	
	private static final String TEST_FILE_LOCATION_2 = "testfiles\\test3";
	private static final String TEST_FILE_NAME_2 = "test2.txt";

	/*
	 * testing file not found exception
	 */
	@Test
	public void test008Exception(){
		FileEditor file = new FileEditor(TEST_FILE_LOCATION, TEST_FILE_NAME);
		
		assertTrue(FileEditor.isExistingFile(TEST_FILE_LOCATION, TEST_FILE_NAME));
		
		file.delete();
		
		String phrase = "test";
		file.writeToFile(phrase);
		String actual = file.retrieveFromFile();
		assertNull(actual);
		
		ArrayList<String> expected = new ArrayList<String>();
		file.writeToFile(expected, expected);
		ArrayList<String> actlData = new ArrayList<String>();
		ArrayList<String> actlArchive = new ArrayList<String>();
		file.retrieveFromFile(actlData, actlArchive);
		assertEquals(expected, actlData);
		assertEquals(expected, actlArchive);
		
		file.clearFile();
		file.relocate(TEST_FILE_LOCATION_2);
		assertFalse(FileEditor.isExistingFile(TEST_FILE_LOCATION, TEST_FILE_NAME));
	}
	
	/*
	 * Testing empty lines in text file
	 */
	@Test
	public void test007EmptyLines(){
		FileEditor file = new FileEditor(TEST_FILE_LOCATION, TEST_FILE_NAME);
		file.deleteOnExit();
		ArrayList<String> newLines = new ArrayList<String>();
		newLines.add("\n");
		file.writeToFile(newLines, newLines);
		
		ArrayList<String> expected = new ArrayList<String>();
		ArrayList<String> actlTasks = new ArrayList<String>();
		ArrayList<String> actlArchives = new ArrayList<String>();
		file.retrieveFromFile(actlTasks, actlArchives);
		assertEquals(expected, actlTasks);
		assertEquals(expected, actlArchives);
		
	}
	
	@Test
	public void test001Rename() {
		FileEditor file = new FileEditor(TEST_FILE_LOCATION, TEST_FILE_NAME);
		file.rename(TEST_FILE_NAME_2); 
		file.paths();
		file.delete();
	}

	@Test
	public void test002Relocate() {
		FileEditor file = new FileEditor(TEST_FILE_LOCATION, TEST_FILE_NAME);
		file.relocate(TEST_FILE_LOCATION_2)	;
		file.paths();
		file.delete();
	}

	@Test
	public void test003ReadWriteSingleLine() {
		FileEditor file = new FileEditor(TEST_FILE_LOCATION, TEST_FILE_NAME);
		String inputPhrase = "test phrase";
		file.writeToFile(inputPhrase);
		String retrievedPhrase = file.retrieveFromFile();
		assertEquals("not the same", inputPhrase, retrievedPhrase);

		inputPhrase = "";
		file.writeToFile(inputPhrase);
		retrievedPhrase = file.retrieveFromFile();
		assertEquals("not the same", inputPhrase, retrievedPhrase);
		file.delete();
	}

	@Test
	public void test004ReadWriteMultipleLinesOneArrayList() {
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
		assertEquals(inputArrayOne.size(), retrievedArrayOne.size());

		for (int i = 0; i < 1000; i++) {
			inputArrayOne.add("string" + i);
		}
		file.writeToFile(inputArrayOne, inputArrayTwo);
		file.retrieveFromFile(retrievedArrayOne, retrievedArrayTwo);
		for (int i = 0; i < retrievedArrayOne.size(); i++) {
			assertEquals(i + "diff", inputArrayOne.get(i), retrievedArrayOne.get(i));
		}
		file.delete();
	}

	@Test
	public void test005ReadWriteMultipleLinesTwoArrayList() {
		FileEditor file = new FileEditor(TEST_FILE_LOCATION, TEST_FILE_NAME);
		ArrayList<String> inputArrayOne = new ArrayList<String>();
		ArrayList<String> inputArrayTwo = new ArrayList<String>();
		//normal test scenario
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
		
		//testing empty arraylists to write and retrieve
		inputArrayOne.clear();
		inputArrayTwo.clear();
		retrievedArrayOne.clear();
		retrievedArrayTwo.clear();
		file.writeToFile(inputArrayOne, inputArrayTwo);
		file.retrieveFromFile(retrievedArrayOne, retrievedArrayTwo);
		assertEquals(inputArrayOne.size(), retrievedArrayOne.size());
		
		//testing large quantities to write and retrieve
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
		for (int i = 0; i < retrievedArrayTwo.size(); i++) {
			assertEquals(i + "diff", inputArrayTwo.get(i), retrievedArrayTwo.get(i));
		}

		inputArrayOne.clear();
		inputArrayTwo.clear();
		retrievedArrayOne.clear();
		retrievedArrayTwo.clear();
		for (int i = 0; i < 100; i++) {
			inputArrayOne.add("String " + i
					+ " is a very very long string used to test the robustness of the method in execution speed and smoothness");
		}
		for (int i = 0; i < 100; i++) {
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
		file.clearFile();
		file.delete();
	}
	
	@Test
	public void test006getfileNamePath(){
		FileEditor file = new FileEditor(TEST_FILE_LOCATION, TEST_FILE_NAME);
		String expectedPath = "C:\\Users\\User\\workspace\\Urgenda\\testfiles";
		String expectedName = "C:\\Users\\User\\workspace\\Urgenda\\testfiles\\test.txt";
		assertEquals("wrong path name", expectedPath, file.getDirAbsolutePath());
		assertEquals("wrong file name", expectedName, file.getAbsolutePath());
		file.delete();
	}
	
	
	

}
