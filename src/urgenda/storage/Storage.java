package urgenda.storage;

import java.util.ArrayList;

import urgenda.util.*;

public class Storage {
	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String SETTINGS_HELP = "help.txt";
	private static final String SETTINGS_DIRECTORY = "settings";

	private static final String DELIMITER_FILE_TYPE = "\\";

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

	public Storage(String path, String name) {
		logger.getLogger().info("constructing Storage Object");
		_settings = new SettingsEditor();
		_help = new FileEditor(SETTINGS_DIRECTORY, SETTINGS_HELP);
		_file = new FileEditor(path, name);
		_file.retrieveFromFile(_fileDataStringArr, _archiveStringArr);
	}

	public ArrayList<Task> updateCurrentTaskList() {
		ArrayList<Task> tasks = _decryptor.decryptTaskList(_fileDataStringArr);
		return tasks;
	}

	public ArrayList<Task> updateArchiveTaskList() {
		ArrayList<Task> archives = _decryptor.decryptArchiveList(_archiveStringArr);
		return archives;
	}

	public void save(ArrayList<Task> tasks, ArrayList<Task> archive) {
		_fileDataStringArr = _encryptor.encrypt(tasks);
		_archiveStringArr = _encryptor.encrypt(archive);
		_file.writeToFile(_fileDataStringArr, _archiveStringArr);
	}

	private void changeFilePath(String path) {
		_settings.setFileDir(path);
		_settings.saveSettings();
		_file.relocate(path);
	}

	public void changeFileSettings(String path) throws UrgendaException {
		String fileType = path.trim().substring(path.length() - 4);
		if (fileType.equals(".txt")) {
			String dir = path.trim().substring(0, path.lastIndexOf(DELIMITER_FILE_TYPE));
			String name = path.trim().substring(path.lastIndexOf(DELIMITER_FILE_TYPE) + 1, path.length());
			if (!FileEditor.isExistingFile(dir, name)) {
				_settings.setFileDir(dir);
				_settings.setFileName(name);
				_settings.saveSettings();
				_file.relocate(dir);
				_file.rename(name);
			} else {
				_settings.setFileDir(dir);
				_settings.setFileName(name);
				_settings.saveSettings();
				throw new UrgendaException(dir, name); 
			}
		} else {
			changeFilePath(path);
		}

	}

	public String retrieveHelp() {
		String help;
		help = _help.retrieveFromFile();
		return help;
	}

	public String getDirPath() {
		return _file.getDirAbsolutePath();
	}

	/**
	 * For testing purposes, to eliminate all additional files used for testing.
	 */
	public void delete() {
		_file.delete();
		_settings.delete();
	}
}
