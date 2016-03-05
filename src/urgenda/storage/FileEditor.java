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

	public FileEditor() {
		String defaultDir = "testfiles";
		_parentDir = new File(defaultDir);
		_parentDir.mkdir();
		_file = new File(_parentDir, "test.txt");
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

	public void retrieveFromFile(ArrayList<String> _fileDataStringArr) {
		try {
			FileReader reader = new FileReader(_file);
			BufferedReader breader = new BufferedReader(reader);
			addtoArray(breader, _fileDataStringArr);
			breader.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addtoArray(BufferedReader breader, ArrayList<String> fileDataStringArr) throws IOException {
		boolean isEmpty = false;
		while (!isEmpty) {
			String taskString = breader.readLine();
			if (taskString == null) {
				isEmpty = true;
			} else {
				fileDataStringArr.add(taskString);
			}
		}
	}
	
	public void writeToFile(ArrayList<String> fileDataStringArr){
		PrintWriter writer;
		try {
			writer = new PrintWriter(_file);
			for (String phrase : fileDataStringArr) {
				writer.println(phrase);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
