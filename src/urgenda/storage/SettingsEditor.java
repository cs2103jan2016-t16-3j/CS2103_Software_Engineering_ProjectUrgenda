package urgenda.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;

public class SettingsEditor {

	private static final String SETTINGS_DIRECTORY = "settings";
	private static final String SETTINGS_FILENAME = "settings.txt";

	private File _file;
	private File _parentDir;
	private JsonCipher _cipher;
	private String _settingsString;

	public SettingsEditor() {
		_parentDir = new File(SETTINGS_DIRECTORY);
		_parentDir.mkdir();
		_file = new File(_parentDir, SETTINGS_FILENAME);
		checkIfFileExist();
		retrieveSettings();
		_cipher = new JsonCipher(_settingsString);
		_cipher.convertToMap();
	}

	private void checkIfFileExist() {
		if (_file.exists() == false) {
			try {
				_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void retrieveSettings() {
		try {
			FileReader reader = new FileReader(_file);
			BufferedReader breader = new BufferedReader(reader);
			_settingsString = breader.readLine();
			breader.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveSettings() {
		_cipher.convertToString();
		_settingsString = _cipher.getDetailsString();
		try {
			PrintWriter writer = new PrintWriter(_file);
			writer.println(_settingsString);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
