package test.testStorage;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;

import org.junit.FixMethodOrder;
import org.junit.Test;


import org.junit.runners.MethodSorters;
import urgenda.storage.SettingsEditor;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SettingsEditorTest {
	private static final String TEST_SETTINGS_DIRECTORY = "testfiles";
	private static final String TEST_SETTINGS_NAME = "settings.txt";
	
	private static final String TEST_FILE_LOCATION = "testfiles";
	private static final String TEST_FILE_NAME = "test.txt";
	
	private static final String HASHMAP_KEY_FILE_DIRECTORY = "directory";
	private static final String HASHMAP_KEY_FILE_NAME = "name";
	private static final String HASHMAP_KEY_NOVICE_SETTINGS = "novice";
	
	private static final String DEFAULT_FILE_LOCATION = "settings";
	private static final String DEFAULT_FILE_NAME = "data.txt";
	private static final String DEFAULT_NOVICE_SETTINGS = "true"; 
	@Test
	public void test001(){
		SettingsEditor settings = new SettingsEditor(TEST_SETTINGS_DIRECTORY, TEST_SETTINGS_NAME);
		settings.setFileDir(TEST_FILE_LOCATION);
		settings.setFileName(TEST_FILE_NAME);
		assertEquals(TEST_FILE_LOCATION, settings.getFileDir());
		assertEquals(TEST_FILE_NAME, settings.getFileName());
		settings.delete();
	}
	
	@Test
	public void test002(){
		SettingsEditor settings = new SettingsEditor(TEST_SETTINGS_DIRECTORY, TEST_SETTINGS_NAME);
		settings.resetDefault();
		LinkedHashMap<String, String> expected = new LinkedHashMap<String, String>();
		expected.put(HASHMAP_KEY_FILE_DIRECTORY, DEFAULT_FILE_LOCATION);
		expected.put(HASHMAP_KEY_FILE_NAME, DEFAULT_FILE_NAME);
		expected.put(HASHMAP_KEY_NOVICE_SETTINGS, DEFAULT_NOVICE_SETTINGS);
		assertEquals(expected, settings.getMap());
		settings.delete();
	}
}
