package urgenda.storage;

import java.util.ArrayList;

import urgenda.util.*;

public class Storage implements StorageInterface {
	private static final String SETTINGS_HELP = "help.txt";
	private static final String SETTINGS_DIRECTORY = "settings";
	
	private FileEditor _file;
	private FileEditor _help;
	private SettingsEditor _settings = new SettingsEditor();
	private ArrayList<String> _fileDataStringArr = new ArrayList<String>();
	private ArrayList<String> _archiveStringArr = new ArrayList<String>();
	private Decryptor _decryptor = new Decryptor();
	private Encryptor _encryptor = new Encryptor();

	public Storage() {
		String path = _settings.getFileDir();
		String name = _settings.getFileName();
		_help = new FileEditor(SETTINGS_DIRECTORY, SETTINGS_HELP);
		_file = new FileEditor(path, name);
		_file.retrieveFromFile(_fileDataStringArr, _archiveStringArr);

	}

	public Storage(String test) {
		String path = _settings.getFileDir();
		String name = _settings.getFileName();
		_file = new FileEditor(path, name);
		_file.retrieveFromFile(_fileDataStringArr, _archiveStringArr);
	}

	public int updateArrayLists(ArrayList<Task> tasks, ArrayList<Task> archive) {
		int taskId = _decryptor.decryptStringArr(tasks, _fileDataStringArr, archive, _archiveStringArr);
		return taskId;
	}

	public void save(ArrayList<Task> tasks, ArrayList<Task> archive) {
		_encryptor.encrypt(tasks, _fileDataStringArr);
		_encryptor.encrypt(archive, _archiveStringArr);
		_file.writeToFile(_fileDataStringArr, _archiveStringArr);
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
	
	public String retrieveHelp(){
		String help;
		help = _help.getHelp();
		return help;
	}
}
