//@@author A0126888L
package urgenda.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import urgenda.gui.Main;
import urgenda.util.InvalidFolderException;
import urgenda.util.UrgendaLogger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;


public class FileEditor {
	private static final String LIST_SEPARATOR_ARCHIVE = "archive";
	private static final String PATH_HELP_TEXT = "../../resources/help.txt";
	private static final String PATH_HELP_TEXT_2 = "../build/src/resources/help.txt";
	private static final String PATH_HELP_STORAGE = "/src/urgenda/storage/help.txt";

	private File _file;
	private File _parentDir;
	private static UrgendaLogger logger = UrgendaLogger.getInstance();

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
			if(taskString == null || taskString.equals(LIST_SEPARATOR_ARCHIVE)) {
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

	public void writeToFile(String phrase) {
		try {
			PrintWriter writer = new PrintWriter(_file);
			writer.println(phrase);
			writer.close();
		} catch (FileNotFoundException e) {
			logger.getLogger().info("no such file found");
		}
	}
	
	public void clearFile(){
		try {
			PrintWriter writer = new PrintWriter(_file);
			writer.close();
		} catch (FileNotFoundException e) {
			logger.getLogger().info("no such file found");
		}
	}
	
	public void rename(String name){
		Path source = _file.toPath();
		try {
			Files.move(source, source.resolveSibling(name), REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		initFile(name);
	}
	
	public void relocate(String path) throws InvalidFolderException{
		Path source = _file.toPath();
		_parentDir = new File(path);
		_parentDir.mkdir();
		Path newSource = Paths.get(path);
		try {
			Files.move(source, newSource.resolve(source.getFileName()), REPLACE_EXISTING);
		} catch (NoSuchFileException e) {
			logger.getLogger().info(source + " does not exist, unable to proceed with file relocation");
			throw new InvalidFolderException(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		_file = new File(_parentDir, source.getFileName().toString());
	}
	
	public static boolean isExistingFile(String dir, String name){
		File file = new File(dir, name);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void paths(){
		System.out.println("Absolute Path " + _file.getAbsolutePath());
		System.out.println("Name " + _file.getName());
		System.out.println("Path " + _file.getPath());
		System.out.println("Parent " + _file.getParent());
		System.out.println("String " + _file.toString());
		System.out.println("Path path " + _file.toPath());
	}
	
	public String getFileName(){
		return _file.getName();
	}
	
	public String getAbsolutePath(){
		return _file.getAbsolutePath();
	}
	
	public String getDirAbsolutePath(){
		return _parentDir.getAbsolutePath();
	}
	
	public void delete(){
		_file.delete();
		_parentDir.delete();
	}
	
	public void deleteOnExit(){
		_file.deleteOnExit();
		_parentDir.deleteOnExit();
	}

}
