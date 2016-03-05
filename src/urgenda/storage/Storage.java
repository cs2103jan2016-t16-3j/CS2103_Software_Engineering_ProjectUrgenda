package urgenda.storage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import urgenda.util.*;

public class Storage implements StorageInterface {
	private FileEditor _file;
	private SettingsEditor _settings;
	private ArrayList<String> _fileDataStringArr;
	private ArrayList<String> _archiveStringArr;
	private Decryptor _decryptor;
	private Encryptor _encryptor;

	public Storage() {
		_settings = new SettingsEditor();
		String path = _settings.getFileDir();
		String name = _settings.getFileName();
		_file = new FileEditor(path, name);
		_fileDataStringArr = new ArrayList<String>();
		_archiveStringArr = new ArrayList<String>();
		_file.retrieveCurrentTask(_fileDataStringArr);
		_decryptor = new Decryptor();
		_encryptor = new Encryptor();

	}

	public Storage(String test) {
		_settings = new SettingsEditor();
		String path = _settings.getFileDir();
		String name = _settings.getFileName();
		_file = new FileEditor(path, name);
		_fileDataStringArr = new ArrayList<String>();
		_file.retrieveCurrentTask(_fileDataStringArr);
		_decryptor = new Decryptor();
		_encryptor = new Encryptor();
	}

	public int updateArrayLists(ArrayList<Task> tasks, ArrayList<Task> archive) {
		int taskId = _decryptor.decrypt(tasks, _fileDataStringArr);
		// int archiveId = _decryptor.decrypt(archive, _fileDataStringArr);
		return taskId;
	}

	public void save(ArrayList<Task> tasks, ArrayList<Task> archive) {
		_encryptor.encrypt(tasks, _fileDataStringArr);
		// _encryptor.encrypt(archive, _fileDataStringArr);
		_file.writeToFile(_fileDataStringArr);
	}

	public void changeFilePath(String path) {
		_settings.setFileDir(path);
		_settings.saveSettings();
	}

	public void changeFileName(String name) {
		_settings.setFileName(name);
		_settings.saveSettings();
	}

	public void changeFileSettings(String path, String name) {
		_settings.setFileDir(path);
		_settings.setFileName(name);
		_settings.saveSettings();
	}
}
