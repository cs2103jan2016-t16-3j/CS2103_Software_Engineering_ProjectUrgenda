package urgenda.storage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import urgenda.util.Task;

public class Encryptor extends JsonCipher {

	public Encryptor() {
		super();
	}

	public void encrypt(ArrayList<Task> taskList, ArrayList<String> stringList) {
		if (!stringList.isEmpty()) {
			stringList.clear();
		}
		for (Task task : taskList) {
			_detailsMap = new LinkedHashMap<String, String>();
			getTaskDetail(task);
			convertToString();
			stringList.add(_detailsString);
		}
	}
	
	

	// getting Task attributes and storing in a LinkedHashMap
	private void getTaskDetail(Task task) {
		// compulsory attributes will never be null
		setDesc(task);
		setType(task); 
		// optional attributes might be null
		setLocation(task);
		setStartTime(task);
		setEndTime(task);
		setHashTags(task);
		setDateAdded(task);
		setDateModified(task);
		setCompleted(task);
		setImportant(task);
		setOverdue(task);
		setMultipleDesc(task);
		setMultipleId(task);
	}

}
