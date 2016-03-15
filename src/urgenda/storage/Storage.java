package urgenda.storage;

import java.nio.file.Path;
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
	
	public Storage(String path, String name){
		_help = new FileEditor(SETTINGS_DIRECTORY, SETTINGS_HELP);
		_file = new FileEditor(path, name);
		_file.retrieveFromFile(_fileDataStringArr, _archiveStringArr);
	}
	
	public ArrayList<Task> updateCurrentTaskList(){
		ArrayList<Task> tasks = new ArrayList<Task>();
		_decryptor.decryptTaskList(tasks, _fileDataStringArr);
		return tasks;
	}
	
	public ArrayList<Task> updateArchiveTaskList(){
		ArrayList<Task> archives = new ArrayList<Task>();
		_decryptor.decryptArchiveList(archives, _archiveStringArr);
		return archives;
	}

	public void save(ArrayList<Task> tasks, ArrayList<Task> archive) {
		_encryptor.encrypt(tasks, _fileDataStringArr);
		_encryptor.encrypt(archive, _archiveStringArr);
		_file.writeToFile(_fileDataStringArr, _archiveStringArr);
	}

	public void changeFilePath(String path) {
		_settings.setFileDir(path);
		_settings.saveSettings();
		_file.relocate(path);
	}

	public void changeFileName(String name) {
		_settings.setFileName(name);
		_settings.saveSettings();
		_file.rename(name);
	}

	public void changeFileSettings(String path, String name) {
		_settings.setFileDir(path);
		_settings.setFileName(name);
		_settings.saveSettings();
		_file.rename(name);
		_file.relocate(path);
	}
	
	public String retrieveHelp(){
		String help;
		help = _help.retrieveFromFile();
		return help;
	}
	
	public String getFilePath(){
		return _file.getAbsolutePath();
	}
}
