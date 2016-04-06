//@@author A0126888L
package urgenda.storage;

import java.util.LinkedHashMap;

import urgenda.util.UrgendaLogger;

/**
 * The SettingsEditor class handles all matters related to the settings of
 * Urgenda. It consists of a FileEditor and JsonCipher object. Since the
 * settings are not a Task object, it does not need to convert from a String to
 * a LinkedHashMap to a Task. Hence it can converted directly through the
 * JsonCipher class alone with the help of the derived classes Encryptor or
 * Decryptor.
 * 
 */
public class SettingsEditor {
	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String SETTINGS_DIRECTORY = "settings";
	private static final String SETTINGS_FILENAME = "settings.txt";

	private static final String DEFAULT_FILE_LOCATION = "settings";
	private static final String DEFAULT_FILE_NAME = "data.txt";
	private static final boolean DEFAULT_NOVICE_SETTINGS = true;

	private JsonCipher _cipher;
	private FileEditor _settings;
	private String _settingsString;

	/**
	 * Default constructor for SettingsEditor. Uses the default directory where
	 * settings should be stored.
	 */
	public SettingsEditor() {
		logger.getLogger().info("constructing SettingsEditor Object");
		_settings = new FileEditor(SETTINGS_DIRECTORY, SETTINGS_FILENAME);
		_settingsString = _settings.retrieveFromFile();
		_cipher = new JsonCipher(_settingsString);
		checkIfEmptyMap();
	}

	/**
	 * Constructor used for testing, to provide a testing bed for file
	 * manipulation, and saving the settings.
	 * 
	 * @param path
	 *            the file directory where the settings file is stored.
	 * @param name
	 *            the file name of the settings file.
	 */
	public SettingsEditor(String path, String name) {
		_settings = new FileEditor(path, name);
		_settingsString = _settings.retrieveFromFile();
		_cipher = new JsonCipher(_settingsString);
		checkIfEmptyMap();
	}

	/**
	 * Checks if the settings file is empty, adding in the default locations if
	 * so.
	 */
	public void checkIfEmptyMap() {
		if (_cipher.isEmptyMap()) {
			logger.getLogger().info("Empty cipher map. adding default locations.");
			setAllFieldsAsDefaultSettings();
			saveSettings();
		}
	}

	/**
	 * saves the settings by writing to the file after conversion to JSON
	 * format.
	 */
	public void saveSettings() {
		logger.getLogger().info("Saving settings.");
		_cipher.convertToString();
		_settingsString = _cipher.getDetailsString();
		_settings.writeToFile(_settingsString);
	}

	/**
	 * Resets all settings to default. Used for testing purposes to ensure less
	 * messy file directories when testing.
	 */
	public void resetDefault() {
		_cipher.reset();
		setAllFieldsAsDefaultSettings();
		saveSettings();
	}

	private void setAllFieldsAsDefaultSettings() {
		_cipher.setDirectory(DEFAULT_FILE_LOCATION);
		_cipher.setFileName(DEFAULT_FILE_NAME);
		_cipher.setNoviceSettings(DEFAULT_NOVICE_SETTINGS);
	}

	public void setNoviceSettings(boolean isNovice) {
		_cipher.setNoviceSettings(isNovice);
		saveSettings();
	}

	public boolean getNoviceSettings() {
		return _cipher.getNoviceSettings();
	}

	/**
	 * Returns the directory where the main data file is stored.
	 * 
	 * @return String value of the File Directory.
	 */
	public String getFileDir() {
		return _cipher.getDirectory();
	}

	/**
	 * Returns the name of the main data file.
	 * 
	 * @return String value of the name of the main data file.
	 */
	public String getFileName() {
		return _cipher.getFileName();
	}

	/**
	 * Changes the directory of the main data file in the settings file. This
	 * does not change where the main data file is located, only changes the
	 * value in the LinkedHashMap for the settings file.
	 * 
	 * @param path
	 *            the given file directory that will be changed.
	 */
	public void setFileDir(String path) {
		_cipher.setDirectory(path);
	}

	/**
	 * Changes the name of the main data file in the settings file. This does
	 * not change the name of the main data file, only changes the value in the
	 * LinkedHashMap for the settings file.
	 * 
	 * @param name
	 *            the given file name that will be changed.
	 */
	public void setFileName(String name) {
		_cipher.setFileName(name);
	}

	/*
	 * Used for testing purposes to ensure LinkedHashMap is updated accordingly.
	 */
	public LinkedHashMap<String, String> getMap() {
		return _cipher.getDetailsMap();
	}

	/**
	 * Used for testing purposes to delete additional files and directories
	 * created for testing.
	 */
	public void delete() {
		_settings.delete();
	}

	/**
	 * Used for testing purposes to delete additional files and directories
	 * created for testing, without the need to delete on the last line.
	 */
	public void deleteOnExit() {
		_settings.deleteOnExit();
	
	}

}
