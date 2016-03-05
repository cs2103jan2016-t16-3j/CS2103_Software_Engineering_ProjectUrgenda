package urgenda.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.google.gson.reflect.TypeToken;

import urgenda.util.Task;

public class Decryptor extends JsonCipher{
	
	public Decryptor(){
		super();
	}
	
	public int decrypt(ArrayList<Task> taskList, ArrayList<String> stringList) {
		int i;
		for (i = 0; i < stringList.size(); i++) {
			_detailsString = stringList.get(i);
			convertToMap();
			Task newTask = generateTask(i + 1);
			taskList.add(newTask);
		}
		return i;
	}
	

	private Task generateTask(int i) {
		int id = i;
		String desc = getDesc();
		String type = getType();
		String location = getLocation();
		boolean isCompleted = checkCompleted();
		boolean isImportant = checkImportant();
		boolean isOverdue = checkOverdue();
		LocalDateTime startTime = getStartTime();
		LocalDateTime endTime = getEndTime();
		LocalDateTime dateAdded = getDateAdded();
		LocalDateTime dateModified = getDateModified();
		ArrayList<String> hashTags = getHashTags();
		Task newTask = new Task(id, desc, type, location, isCompleted, isImportant, isOverdue, startTime, endTime,
				dateAdded, dateModified, hashTags);
		return newTask;
	}
	

}
