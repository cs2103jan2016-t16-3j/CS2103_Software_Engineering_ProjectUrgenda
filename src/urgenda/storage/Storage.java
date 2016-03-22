package urgenda.storage;

import java.util.ArrayList;

import urgenda.util.*;

public class Storage {
	private static MyLogger logger = MyLogger.getInstance();
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
		logger.getLogger().info("constructing Storage Object");
		String path = _settings.getFileDir();
		String name = _settings.getFileName();
		_help = new FileEditor(SETTINGS_DIRECTORY, SETTINGS_HELP);
		_file = new FileEditor(path, name);
		_file.retrieveFromFile(_fileDataStringArr, _archiveStringArr);

	}
	

	public Storage(String path, String name){
		MyLogger logger = MyLogger.getInstance();
		logger.getLogger().info("constructing Storage Object");
		_help = new FileEditor(SETTINGS_DIRECTORY, SETTINGS_HELP);
		_file = new FileEditor(path, name);
		_file.retrieveFromFile(_fileDataStringArr, _archiveStringArr);
	}
	
	public ArrayList<Task> updateCurrentTaskList(){
		ArrayList<Task> tasks = _decryptor.decryptTaskList(_fileDataStringArr);
		return tasks;
	}
	
	public ArrayList<Task> updateArchiveTaskList(){
		ArrayList<Task> archives = _decryptor.decryptArchiveList(_archiveStringArr);
		return archives;
	}

	public void save(ArrayList<Task> tasks, ArrayList<Task> archive) {
		_fileDataStringArr = _encryptor.encrypt(tasks);
		_archiveStringArr = _encryptor.encrypt(archive);
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
	
	public String getDirPath(){
		return _file.getDirAbsolutePath();
	}
}
