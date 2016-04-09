//@@author A0126888L
package urgenda.storage;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import urgenda.util.InvalidFolderException;
import urgenda.util.UrgendaLogger;

/**
 * FileEditor class is the class used for file manipulation. The 2 files that
 * requires editing, the settings file and the main data file, are done using
 * this class.
 * 
 * @author User
 *
 */
public class FileEditor {
	private static final String LIST_SEPARATOR_ARCHIVE = "archive";

	private File _file;
	private File _parentDir;
	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	/**
	 * Constructor of the FileEditor class. Builds a folder and constructs the
	 * file.
	 * 
	 * @param path
	 *            The directory that stores the file to be used for retrieval
	 *            and editing.
	 * @param name
	 *            The name of the file to be used for retrieval and editing.
	 */
	public FileEditor(String path, String name) {
		logger.getLogger().info("constructing FileEditor Object");
		initParentDir(path);
		initFile(name);
		checkIfFileExist();
		logger.getLogger().info("FileEditor object created");
	}

	private void initFile(String name) {
		_file = new File(_parentDir, name);
	}

	private void initParentDir(String path) {
		_parentDir = new File(path);
		_parentDir.mkdir();
	}

	private void checkIfFileExist() {
		if (_file.exists() == false) {
			try {
				_file.createNewFile();
				setUpFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setUpFile() {
		ArrayList<String> create = new ArrayList<String>();
		writeToFile(create, create);

	}

	/**
	 * Reads the file into a single String. Used for settings file.
	 * 
	 * @return details from the file in a String.
	 */
	public String retrieveFromFile() {
		String phrase = null;
		try {
			FileReader reader = new FileReader(_file);
			BufferedReader breader = new BufferedReader(reader);
			StringBuffer stringBuffer = new StringBuffer();
			phrase = readFileToString(breader, stringBuffer);
			phrase = stringBuffer.toString().trim();
			breader.close();
			reader.close();
			logger.getLogger().info("successful retrieval of data");
		} catch (FileNotFoundException e) {
			logger.getLogger().info("no such file found" + e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return phrase;
	}

	private String readFileToString(BufferedReader breader, StringBuffer stringBuffer) throws IOException {
		String phrase;
		while ((phrase = breader.readLine()) != null) {
			stringBuffer.append(phrase).append("\n");
		}
		return phrase;
	}

	/**
	 * Reads the file into two separate arraylists. Strings before the word
	 * "archive" are added to the first arraylist while Strings after are added
	 * to the second arraylist.
	 * 
	 * @param fileDataStringArr
	 *            ArrayList for current uncompleted Tasks.
	 * @param archiveStringArr
	 *            ArrayList for completed archived Tasks.
	 */
	public void retrieveFromFile(ArrayList<String> fileDataStringArr, ArrayList<String> archiveStringArr) {
		try {
			FileReader reader = new FileReader(_file);
			BufferedReader breader = new BufferedReader(reader);
			addToTaskArray(breader, fileDataStringArr);
			addToArchiveArray(breader, archiveStringArr);
			breader.close();
			reader.close();
			logger.getLogger().info("successful retrieval of data");
		} catch (FileNotFoundException e) {
			logger.getLogger().info("no such file found");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addToTaskArray(BufferedReader breader, ArrayList<String> fileDataStringArr) throws IOException {
		boolean hasNoMoreTasks = false;
		while (!hasNoMoreTasks) {
			String taskString = breader.readLine();
			if (taskString == null || taskString.equals(LIST_SEPARATOR_ARCHIVE)) {
				hasNoMoreTasks = true;
			} else if (taskString.isEmpty()) {
				hasNoMoreTasks = false;
			} else {
				fileDataStringArr.add(taskString);
			}
		}
	}

	private void addToArchiveArray(BufferedReader breader, ArrayList<String> archiveStringArr) throws IOException {
		boolean isEmpty = false;
		while (!isEmpty) {
			String taskString = breader.readLine();
			if (taskString == null) {
				isEmpty = true;
			} else if (taskString.isEmpty()) {
				isEmpty = false;
			} else {
				archiveStringArr.add(taskString);
			}
		}
	}

	/**
	 * Writes into the file the two separate arraylists. The first arraylist is
	 * separated from the second arraylist by the word "archive".
	 * 
	 * @param fileDataStringArr
	 *            ArrayList for current uncompleted Tasks.
	 * @param archiveStringArr
	 *            ArrayList for completed archived Tasks.
	 */
	public void writeToFile(ArrayList<String> fileDataStringArr, ArrayList<String> archiveStringArr) {
		try {
			PrintWriter writer = new PrintWriter(_file);
			for (String phrase : fileDataStringArr) {
				writer.println(phrase);
			}
			writer.println(LIST_SEPARATOR_ARCHIVE);
			for (String phrase : archiveStringArr) {
				writer.println(phrase);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			logger.getLogger().info("no such file found");
		}
	}

	/**
	 * Writes into the file the String. Used for settings file.
	 * 
	 * @param phrase
	 *            details to be written into the file.
	 */
	public void writeToFile(String phrase) {
		try {
			if (_file.exists()) {
				PrintWriter writer = new PrintWriter(_file);
				writer.println(phrase);
				writer.close();
			}
		} catch (FileNotFoundException e) {
			logger.getLogger().info("no such file found");
		}
	}

	/**
	 * Clears the file, erasing all data in it.
	 */
	public void clearFile() {
		try {
			PrintWriter writer = new PrintWriter(_file);
			writer.close();
		} catch (FileNotFoundException e) {
			logger.getLogger().info("no such file found");
		}
	}

	/**
	 * Renames the file into the given name. If such a name already exist in
	 * that particular directory, it will be replaced.
	 * 
	 * @param name
	 *            the new name for the file to be renamed to.
	 */
	public void rename(String name) {
		Path source = _file.toPath();
		try {
			Files.move(source, source.resolveSibling(name), REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initFile(name);
	}

	/**
	 * Changes the file directory to the given file directory. If a file with
	 * the same name already exist in the new directory, it will be replaced.
	 * 
	 * @param path
	 *            the new file directory for the file to be moved to.
	 * @throws InvalidFolderException
	 *             If the given file directory is of an invalid folder type,
	 *             then the file will not be moved there.
	 */
	public void relocate(String path) throws InvalidFolderException {
		Path source = _file.toPath();
		File parentDir = new File(path);
		parentDir.mkdir();
		Path newSource = Paths.get(path);
		try {
			Files.move(source, newSource.resolve(source.getFileName()), REPLACE_EXISTING);
		} catch (NoSuchFileException e) {
			logger.getLogger().info(source + " does not exist, unable to proceed with file relocation");
			throw new InvalidFolderException(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		_parentDir = parentDir;
		_file = new File(_parentDir, source.getFileName().toString());
	}

	/**
	 * Checks if a file of a particular name currently exists in the particular
	 * directory.
	 * 
	 * @param dir
	 *            the file directory to be checked for existing file.
	 * @param name
	 *            the file name to be checked for existing file.
	 * @return true if file already exists, false if otherwise.
	 */
	public static boolean isExistingFile(String dir, String name) {
		File file = new File(dir, name);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Used for testing purposes only.
	 */
	public void paths() {
		System.out.println("Absolute Path " + _file.getAbsolutePath());
		System.out.println("Name " + _file.getName());
		System.out.println("Path " + _file.getPath());
		System.out.println("Parent " + _file.getParent());
		System.out.println("String " + _file.toString());
		System.out.println("Path path " + _file.toPath());
	}

	/**
	 * Returns the name of the file. This should only be the name, appended with
	 * ".txt".
	 * 
	 * @return name of the file.
	 */
	public String getFileName() {
		return _file.getName();
	}

	/**
	 * Returns the absolute path of the file. This includes the name of the
	 * file.
	 * 
	 * @return absolute path of the file.
	 */
	public String getAbsolutePath() {
		return _file.getAbsolutePath();
	}

	/**
	 * Returns the absolut epath of the directory.
	 * 
	 * @return absolute path of the directory.
	 */
	public String getDirAbsolutePath() {
		return _parentDir.getAbsolutePath();
	}

	/**
	 * Deletes the file, as well as the file directory. The file will be deleted
	 * only if no other pointers are pointing to it. The file directory will be
	 * deleted only if there are no other objects in it.
	 */
	public void delete() {
		_file.delete();
		_parentDir.delete();
	}

	/**
	 * Deletes the file and the file directory on exit, without needing to call
	 * them at the end. The file will be deleted only if no other pointers are
	 * pointing to it. The file directory will be deleted only if there are no
	 * other objects in it.
	 */
	public void deleteOnExit() {
		_file.deleteOnExit();
		_parentDir.deleteOnExit();
	}

}
