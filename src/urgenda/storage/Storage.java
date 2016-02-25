package urgenda.storage;

import urgenda.command.Command;
import urgenda.util.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Storage implements StorageInterface {
	private File _file;
	private File _parentDir;
	private String _path;
	private ArrayList<String> _fileDataStringArr;

	// default constructor for no path included (use default location)?
	public Storage() {
		String defaultDir = "testfiles";
		_parentDir = new File(defaultDir);
		_parentDir.mkdir();
		_file = new File(_parentDir, "test.txt");
		checkIfFileExist();
		retrieveFromFile(_file);
	};

	// constructor for saving path/location
	public Storage(String path) {
		String defaultDir = "testfiles";
		_parentDir = new File(defaultDir);
		_parentDir.mkdir();
		_file = new File(_parentDir, path);
		retrieveFromFile(_file);
	};
	
	public void checkIfFileExist(){
		if (_file.exists() == false) {
			try {
				_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void retrieveFromFile(File file) {
		try {
			boolean isEmpty = false;
			FileReader reader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(reader);
			while (!isEmpty) {
				String taskString = bufferedReader.readLine();
				if (taskString == null) {
					isEmpty = true;
				} else {
					_fileDataStringArr.add(taskString);
				}
			}
			bufferedReader.close();
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int updateArrayLists(ArrayList<Task> _tasks, ArrayList<Task> _archive, ArrayList<MultipleSlot> _blocks) {
		int i;
		Gson gson = new Gson();
		for (i = 0; i < _fileDataStringArr.size(); i++) {
			String fileTaskString = _fileDataStringArr.get(i);
			LinkedHashMap<String, String> fileTaskDetail = gson.fromJson(fileTaskString,
					new TypeToken<LinkedHashMap<String, String>>() {
					}.getType());
			Task newTask = new Task(fileTaskDetail, i + 1);
			_tasks.add(newTask);
		}
		return i;
	};

	public void save(ArrayList<Task> _tasks, ArrayList<Task> _archive, ArrayList<MultipleSlot> _blocks) {
		Gson gson = new Gson();
		_fileDataStringArr.clear();
		for (Task task : _tasks) {
			LinkedHashMap<String, String> taskDetail = getTaskDetail(task);
			String taskString = gson.toJson(taskDetail);
			_fileDataStringArr.add(taskString);
		}

		PrintWriter writer;
		try {
			writer = new PrintWriter(_file);
			for (String phrase : _fileDataStringArr) {
				writer.println(phrase);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	};

	// getting Task attributes and storing in a LinkedHashMap
	public LinkedHashMap<String, String> getTaskDetail(Task task) {

		LinkedHashMap<String, String> taskDetail = new LinkedHashMap<>();

		// compulsory attributes will never be null
		taskDetail.put("desc", task.getDesc());
		if (task.getTaskType().toString().equals("EVENT")) {
			taskDetail.put("type", "EVENT");
		} else if (task.getTaskType().toString().equals("DEADLINE")) {
			taskDetail.put("type", "DEADLINE");
		} else if (task.getTaskType().toString().equals("FLOATING")) {
			taskDetail.put("type", "FLOATING");
		} else {
			taskDetail.put("type", "START");
		}

		// optional attributes might be null
		if (task.getLocation() == null) {
			taskDetail.put("location", null);
		} else {
			taskDetail.put("location", task.getLocation());
		}

		if (task.getStartTime() == null) {
			taskDetail.put("startTime", null);
		} else {
			taskDetail.put("startTime", task.getStartTime().toString());
		}

		if (task.getEndTime() == null) {
			taskDetail.put("endTime", null);
		} else {
			taskDetail.put("endTime", task.getEndTime().toString());
		}
		if (task.getHashtags() == null) {
			taskDetail.put("tags", null);
		} else {
			taskDetail.put("tags", String.join(",", task.getHashtags()));
		}

		if (task.getDateAdded() == null) {
			taskDetail.put("dateAdded", null);
		} else {
			taskDetail.put("dateAdded", task.getDateAdded().toString());
		}

		if (task.getDateModified() == null) {
			taskDetail.put("dateModified", null);
		} else {
			taskDetail.put("dateModified", task.getDateModified().toString());
		}

		taskDetail.put("isCompleted", String.valueOf(task.isCompleted()));
		taskDetail.put("isUrgent", String.valueOf(task.isUrgent()));
		taskDetail.put("isOverdue", String.valueOf(task.isOverdue()));
		return taskDetail;
	}
}
