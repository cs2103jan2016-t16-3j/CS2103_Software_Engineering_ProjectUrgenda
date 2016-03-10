package urgenda.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileEditor {
	private static final String LIST_SEPARATOR_ARCHIVE = "archive";

	private File _file;
	private File _parentDir;

	public FileEditor(String path, String name) {
		_parentDir = new File(path);
		_parentDir.mkdir();
		_file = new File(_parentDir, name);
		checkIfFileExist();
	}

	private void checkIfFileExist() {
		if (_file.exists() == false) {
			try {
				_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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

//	public String retrieveFromFile() {
//		String phrase = null;
//		try {
//			FileReader reader = new FileReader(_file);
//			BufferedReader breader = new BufferedReader(reader);
//			phrase = breader.readLine();
//			breader.close();
//			reader.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return phrase;
//	}

	public void retrieveFromFile(ArrayList<String> fileDataStringArr, ArrayList<String> archiveStringArr) {
		try {
			FileReader reader = new FileReader(_file);
			BufferedReader breader = new BufferedReader(reader);
			addToTaskArray(breader, fileDataStringArr);
			addToArchiveArray(breader, archiveStringArr);
			breader.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addToTaskArray(BufferedReader breader, ArrayList<String> fileDataStringArr) throws IOException {
		boolean hasNoMoreTasks = false;
		while (!hasNoMoreTasks) {
			String taskString = breader.readLine();
			if (taskString == null) {
				hasNoMoreTasks = true;
			} else if (taskString.equals(LIST_SEPARATOR_ARCHIVE)) {
				hasNoMoreTasks = true;
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
			e.printStackTrace();
		}
	}

	public void writeToFile(String phrase) {
		try {
			PrintWriter writer = new PrintWriter(_file);
			writer.println(phrase);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void clearFile(){
		try {
			PrintWriter writer = new PrintWriter(_file);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
