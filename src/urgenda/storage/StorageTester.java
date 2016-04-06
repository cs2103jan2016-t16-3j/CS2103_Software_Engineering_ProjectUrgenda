//@@author A0126888L
package urgenda.storage;

public class StorageTester extends Storage{
	private static final String TEST_SETTINGS_DIRECTORY = "testfiles";
	private static final String TEST_SETTINGS_NAME = "settings.txt";
	private static final String TEST_FILE_LOCATION = "testfiles";
	private static final String TEST_FILE_NAME = "test.txt";
	
	public StorageTester(String filePath, String fileName){
		super(filePath, fileName);
		_settings = new SettingsEditor(TEST_SETTINGS_DIRECTORY, TEST_SETTINGS_NAME);
		_file.deleteOnExit();
		_settings.deleteOnExit();
	}
	
	public StorageTester(){
		super(TEST_FILE_LOCATION, TEST_FILE_NAME);
		_settings = new SettingsEditor(TEST_SETTINGS_DIRECTORY, TEST_SETTINGS_NAME);
		_file.deleteOnExit();
		_settings.deleteOnExit();
	}
}
