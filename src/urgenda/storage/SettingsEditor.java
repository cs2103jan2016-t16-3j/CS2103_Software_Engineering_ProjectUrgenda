//@@author A0126888L
package urgenda.storage;

import java.util.LinkedHashMap;

import urgenda.util.UrgendaLogger;

public class SettingsEditor {
	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String SETTINGS_DIRECTORY = "settings";
	private static final String SETTINGS_FILENAME = "settings.txt";

	private static final String DEFAULT_FILE_LOCATION = "settings";
	private static final String DEFAULT_FILE_NAME = "data.txt";
	
	private JsonCipher _cipher;
	private FileEditor _settings;
	private String _settingsString;
	
	public SettingsEditor(){
		logger.getLogger().info("constructing SettingsEditor Object");
		_settings = new FileEditor(SETTINGS_DIRECTORY, SETTINGS_FILENAME);
		_settingsString = _settings.retrieveFromFile();
		_cipher = new JsonCipher(_settingsString);
		checkIfEmptyMap();
	}
	
	public SettingsEditor(String path, String name){
		_settings = new FileEditor(path, name);
		_settingsString = _settings.retrieveFromFile();
		_cipher = new JsonCipher(_settingsString);
		checkIfEmptyMap();
	}

	public void checkIfEmptyMap() {
		if (_cipher.isEmptyMap()) {
			logger.getLogger().info("Empty cipher map. adding default locations.");
			_cipher.setDirectory(DEFAULT_FILE_LOCATION);
			_cipher.setFileName(DEFAULT_FILE_NAME);
			saveSettings();
		}
	}
	
	public void saveSettings() {
		logger.getLogger().info("Saving settings.");
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
	
	public void resetDefault(){
		_cipher.reset();
		_cipher.setDirectory(DEFAULT_FILE_LOCATION);
		_cipher.setFileName(DEFAULT_FILE_NAME);
		saveSettings();
	}
	
	public void delete(){
		_settings.delete();
	}

	public void deleteOnExit() {
		_settings.deleteOnExit();
		
	}
	
	
}
