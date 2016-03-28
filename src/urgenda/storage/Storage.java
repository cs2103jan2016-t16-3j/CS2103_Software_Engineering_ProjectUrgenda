package urgenda.storage;

import java.util.ArrayList;

import urgenda.util.*;

public class Storage {
	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String SETTINGS_HELP = "help.txt";
	private static final String SETTINGS_DIRECTORY = "settings";
	
	protected FileEditor _file;
	protected FileEditor _help;
	protected SettingsEditor _settings;
	protected ArrayList<String> _fileDataStringArr = new ArrayList<String>();
	protected ArrayList<String> _archiveStringArr = new ArrayList<String>();
	protected Decryptor _decryptor = new Decryptor();
	protected Encryptor _encryptor = new Encryptor();


	public Storage() {
		logger.getLogger().info("constructing Storage Object");
		_settings = new SettingsEditor();
		String path = _settings.getFileDir();
		String name = _settings.getFileName();
		_help = new FileEditor(SETTINGS_DIRECTORY, SETTINGS_HELP);
		_file = new FileEditor(path, name);
		_file.retrieveFromFile(_fileDataStringArr, _archiveStringArr);

	}

	public Storage(String path, String name){
		logger.getLogger().info("constructing Storage Object");
		_settings = new SettingsEditor();
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
