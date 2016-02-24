package urgenda.storage;

import urgenda.command.Command;
import urgenda.util.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Stack;

import com.google.gson.Gson;

public class Storage implements StorageInterface {

	// default constructor for no path included (use default location)?
	public Storage() {

	};

	// constructor for saving path/location
	public Storage(String path) {

	};

	public void updateArrayLists(ArrayList<Task> _events, ArrayList<Task> _deadlines, ArrayList<Task> _floats,
			ArrayList<Task> _blocks) {

	};

	public void save(ArrayList<Task> _events, ArrayList<Task> _deadlines, ArrayList<Task> _floats,
			ArrayList<Task> _blocks, Stack<Command> _undos, Stack<Command> _redos) {

	};

	public boolean checkUrgent() {
		Task testTask = new Task("test this", "ISE LAB", null, null, null, false);
		boolean test = testTask.isUrgent();
		return test;
	}

	public void GSONTester() {
		Gson gson = new Gson();
		Task testTask = new Task("test this", "ISE LAB", null, null, null, false);
		String testString = gson.toJson(testTask);
		System.out.println(testString);
		Task retrieve = gson.fromJson(testString, Task.class);
		boolean retrieveUrgent = retrieve.isUrgent();
		System.out.println(retrieveUrgent);

	}
	
	public static LinkedHashMap<String, String> getTaskDetail(Task task) {

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
