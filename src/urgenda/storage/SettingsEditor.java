package urgenda.storage;

import java.util.LinkedHashMap;

public class SettingsEditor {

	private static final String SETTINGS_DIRECTORY = "settings";
	private static final String SETTINGS_FILENAME = "settings.txt";

	private JsonCipher _cipher;
	private FileEditor _settings;
	private String _settingsString;
	
	public SettingsEditor(){
		_settings = new FileEditor(SETTINGS_DIRECTORY, SETTINGS_FILENAME);
		_settingsString = _settings.retrieveFromFile();
		_cipher = new JsonCipher(_settingsString);
		_cipher.convertToMap();
	}

	public void saveSettings() {
		_cipher.convertToString();
		_settingsString = _cipher.getDetailsString();
		_settings.writeToFile(_settingsString);
	}

	public String getFileDir() {
		return _cipher.getDirectory();
	}

	public String getFileName() {
		return _cipher.getFileName();
	}

	public void setFileDir(String path) {
		_cipher.setDirectory(path);
	}

	public void setFileName(String name) {
		_cipher.setFileName(name);
	}
	
	public LinkedHashMap<String, String> getMap(){
		return _cipher.getDetailsMap();
	}

}
