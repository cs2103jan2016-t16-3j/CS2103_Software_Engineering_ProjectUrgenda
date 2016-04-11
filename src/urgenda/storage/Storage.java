//@@author A0126888L
package urgenda.storage;

import java.util.ArrayList;

import urgenda.util.*;

/**
 * Storage class for the Storage component of Urgenda. Acts as the facade for
 * the storage requirements of Urgenda.
 *
 */
public class Storage {
	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	private static final String DELIMITER_FILE_TYPE = "\\";
	private static final String HELP_TYPE = "HELP";
	private static final String DEMO_TYPE = "DEMO";
	private static final String TEXT_FILE_TYPE = ".txt";
	private static final String DIRECTORY_SEPARATOR_FWD_SLASH = "/";
	private static final String DIRECTORY_SEPARATOR_BCK_SLASH = "\\";

	private static final int FILE_TYPE_CHAR_SIZE = 4;

	protected FileEditor _file;
	protected Manual _help;
	protected Manual _demo;
	protected SettingsEditor _settings;
	protected ArrayList<String> _fileDataStringArr = new ArrayList<String>();
	protected ArrayList<String> _archiveStringArr = new ArrayList<String>();
	protected Decryptor _decryptor = new Decryptor();
	protected Encryptor _encryptor = new Encryptor();

	/**
	 * Constructor for Storage class. Retrieves previous settings from settings
	 * directory to initialize main data file.
	 */
	public Storage() {
		logger.getLogger().info("constructing Storage Object");
		_settings = new SettingsEditor();
		String path = _settings.getFileDir();
		String name = _settings.getFileName();
		_help = new Manual(HELP_TYPE);
		_demo = new Manual(DEMO_TYPE);
		_file = new FileEditor(path, name);
		_file.retrieveFromFile(_fileDataStringArr, _archiveStringArr);
		logger.getLogger().info("Storage object created.");
	}

	/**
	 * Constructor for Storage class. Takes in 2 parameters that defines the
	 * location and name of the main data file.
	 * 
	 * @param path
	 *            The directory where the main data file should be located.
	 * @param name
	 *            The name of the main data file.
	 */
	public Storage(String path, String name) {
		logger.getLogger().info("constructing Storage Object");
		_settings = new SettingsEditor();
		_help = new Manual("HELP");
		_demo = new Manual("DEMO");
		_file = new FileEditor(path, name);
		_file.retrieveFromFile(_fileDataStringArr, _archiveStringArr);
	}

	/**
	 * Creates an ArrayList of Tasks from an ArrayList of String formatted
	 * tasks. Uses the list from the current tasks.
	 * 
	 * @return ArrayList of Tasks from TaskList.
	 */
	public ArrayList<Task> updateCurrentTaskList() {
		ArrayList<Task> tasks = _decryptor.decryptTaskList(_fileDataStringArr);
		return tasks;
	}

	/**
	 * Creates an ArrayList of Tasks from an ArrayList of String formatted
	 * tasks. Uses the list from archives.
	 * 
	 * @return ArrayList of Tasks from Archives.
	 */
	public ArrayList<Task> updateArchiveTaskList() {
		ArrayList<Task> archives = _decryptor.decryptArchiveList(_archiveStringArr);
		return archives;
	}

	/**
	 * Saves 2 ArrayList of Tasks into a text file.
	 * 
	 * @param tasks
	 *            Contains a list of uncompleted tasks.
	 * @param archive
	 *            Contains a list of completed tasks.
	 */
	public void save(ArrayList<Task> tasks, ArrayList<Task> archive) {
		_fileDataStringArr = _encryptor.encrypt(tasks);
		_archiveStringArr = _encryptor.encrypt(archive);
		_file.writeToFile(_fileDataStringArr, _archiveStringArr);
	}

	/**
	 * Changes the file settings of the main data file, the file directory and
	 * file name.
	 * 
	 * @param path
	 *            The absolute path of the new file directory and file name for
	 *            the main data file.
	 * @throws StorageException
	 *             If such a file with the same file type, same file name exists
	 *             in that particular file directory
	 * @throws InvalidFolderException
	 *             If the path given is not in the correct absolute path format.
	 */
	public void changeFileSettings(String path) throws StorageException, InvalidFolderException {
		String correctPath = checkDirectorySeparator(path);
		String fileType = getFileTypeFromPath(correctPath);
		checkValidName(correctPath);
		checkFileType(correctPath, fileType);
	}

	private void checkFileType(String correctPath, String fileType)
			throws StorageException, InvalidFolderException {
		if (fileType.equals(TEXT_FILE_TYPE)) {
			String dir = getDirFromPath(correctPath);
			String name = getNameFromPath(correctPath);
			checkIfFileExists(dir, name);
		} else {
			checkIfFileExists(correctPath, _file.getFileName());
		}
	}

	private void checkValidName(String correctPath) throws InvalidFolderException {
		if (correctPath.equals(TEXT_FILE_TYPE)) {
			throw new InvalidFolderException(correctPath);
		}
	}

	private String checkDirectorySeparator(String path) {
		String newPath = path;
		if (path.contains(DIRECTORY_SEPARATOR_FWD_SLASH)) {
			newPath = path.replace(DIRECTORY_SEPARATOR_FWD_SLASH, DIRECTORY_SEPARATOR_BCK_SLASH);
		}
		return newPath;
	}

	private String getFileTypeFromPath(String path) {
		if (path.length() > FILE_TYPE_CHAR_SIZE) {
			return path.trim().substring(path.length() - FILE_TYPE_CHAR_SIZE);
		} else {
			return path;
		}

	}

	private String getNameFromPath(String path) {
		return path.trim().substring(path.lastIndexOf(DELIMITER_FILE_TYPE) + 1, path.length());
	}

	private String getDirFromPath(String path) {
		return path.trim().substring(0, path.lastIndexOf(DELIMITER_FILE_TYPE));
	}

	/*
	 * checks if there is an existing file with such a name in that particular
	 * directory.
	 */
	private void checkIfFileExists(String dir, String name) throws StorageException, InvalidFolderException {
		if (!FileEditor.isExistingFile(dir, name)) {
			_file.relocate(dir);
			_file.rename(name);
			setFileSettings(dir, name);
		} else {
			setFileSettings(dir, name);
			throw new StorageException(dir, name);
		}
	}

	private void setFileSettings(String dir, String name) {
		_settings.setFileDir(dir);
		_settings.setFileName(name);
		_settings.saveSettings();
	}

	/**
	 * Retrieves help manual
	 * 
	 * @return ArrayList of String for Help.
	 */
	public ArrayList<String> getHelp() {
		logger.getLogger().info("inside getHelp function");
		ArrayList<String> help;
		help = _help.getManual();
		logger.getLogger().info("retrieved from file.");
		return help;
	}

	/**
	 * Retrieves the absolute directory of the main data file.
	 * 
	 * @return
	 */
	public String getDirPath() {
		return _file.getDirAbsolutePath();
	}

	/**
	 * For testing purposes, to delete all additional files used for testing.
	 */
	public void delete() {
		_file.delete();
		_settings.delete();
	}

	/**
	 * Retrieves demo manual
	 * 
	 * @return ArrayList of String for Demo.
	 */
	public ArrayList<String> getDemoText() {
		logger.getLogger().info("inside getDemoText function");
		ArrayList<String> demo;
		demo = _demo.getManual();
		logger.getLogger().info("retrieved from file. ");
		return demo;
	}

	/**
	 * Retrieves demo indexes for demo tasks selection
	 * 
	 * @return ArrayList of Integers consisting of Demo Indexes.
	 */
	public ArrayList<Integer> getDemoSelectionIndexes() {
		return _demo.getIndexes();
	}

	public boolean getNoviceSettings() {
		return _settings.getNoviceSettings();
	}

	public void setNoviceSettings(boolean isNovice) {
		_settings.setNoviceSettings(isNovice);
		_settings.saveSettings();

	}
}
