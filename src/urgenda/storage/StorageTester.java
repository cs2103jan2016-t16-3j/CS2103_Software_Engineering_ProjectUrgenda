package urgenda.storage;

public class StorageTester extends Storage{
	private static final String TEST_SETTINGS_DIRECTORY = "testfiles\\testsettings";
	private static final String TEST_SETTINGS_NAME = "settings.txt";
	
	public StorageTester(String filePath, String fileName){
		super(filePath, fileName);
		_settings = new SettingsEditor(TEST_SETTINGS_DIRECTORY, TEST_SETTINGS_NAME);
	}
}
