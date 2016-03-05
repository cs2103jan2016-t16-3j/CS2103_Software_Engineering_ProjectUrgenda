package urgenda.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileEditor {
	private File _file;
	private File _parentDir;
	
	public FileEditor(String path, String name){
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
	
	public String retrieveFromFile(){
		String phrase = null;
		try {
			FileReader reader = new FileReader(_file);
			BufferedReader breader = new BufferedReader(reader);
			phrase = breader.readLine();
			breader.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return phrase;
	}
	
	public void retrieveCurrentTask(ArrayList<String> fileDataStringArr) {
		try {
			FileReader reader = new FileReader(_file);
			BufferedReader breader = new BufferedReader(reader);
			addtoTaskArray(breader, fileDataStringArr);
			breader.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addtoTaskArray(BufferedReader breader, ArrayList<String> fileDataStringArr) throws IOException {
		boolean hasNoMoreTasks = false;
		while (!hasNoMoreTasks) {
			String taskString = breader.readLine();
			if (taskString == null) {
				hasNoMoreTasks = true;
			} else if (taskString.equals("archive")) {
				hasNoMoreTasks = true;
			}else {
				fileDataStringArr.add(taskString);
			}
		}
	}
	
	public void writeToFile(ArrayList<String> fileDataStringArr){
		try {
			PrintWriter writer = new PrintWriter(_file);
			for (String phrase : fileDataStringArr) {
				writer.println(phrase);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void writeToFile(String phrase){
		try {
			PrintWriter writer = new PrintWriter(_file);
			writer.println(phrase);
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
