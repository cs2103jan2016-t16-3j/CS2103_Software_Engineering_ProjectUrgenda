package urgenda.storage;

import java.util.ArrayList;

import urgenda.util.*;

public class Storage {
	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	private static final String DELIMITER_FILE_TYPE = "\\";
	private static final String DESC_INTRO_TASK = "Add your first Task! Press Help or Alt + F1 for guidance";

	protected FileEditor _file;
	protected Help _help;
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
		logger.getLogger().info("retrieved file specs from settings. Creating help");
		_help = new Help(true);
		logger.getLogger().info("help created. creating datafiles");
		_file = new FileEditor(path, name);
		_file.retrieveFromFile(_fileDataStringArr, _archiveStringArr);
		//checkIfEmptyFile();
		logger.getLogger().info("Storage object created.");
	}

	private void checkIfEmptyFile() {
		if (_fileDataStringArr.isEmpty() && _archiveStringArr.isEmpty()) {
			createIntroTask();
		}
		
	}

	private void createIntroTask() {
		Task introTask = new Task();
		introTask.setDesc(DESC_INTRO_TASK);
		introTask.updateTaskType();
		ArrayList<Task> introList = new ArrayList<Task>();
		ArrayList<Task> introArchive = new ArrayList<Task>();
		introList.add(introTask);
		save(introList, introArchive);
		
	}

	public Storage(String path, String name) {
		logger.getLogger().info("constructing Storage Object");
		_settings = new SettingsEditor();
		_help = new Help(true);
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

	public void changeFileSettings(String path) throws StorageException {
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
				throw new StorageException(dir, name); 
			}
		} else {
			changeFilePath(path);
		}

	}

	public ArrayList<String> retrieveHelp() {
		logger.getLogger().info("inside retrieveHelp function");
		ArrayList<String> help;
		help = _help.getHelp();
		logger.getLogger().info("retrieved from file. ");
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

	public ArrayList<String> getDemoText() {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<Integer> getDemoSelectionIndexes() {
		// TODO Auto-generated method stub
		return null;
	}
}
