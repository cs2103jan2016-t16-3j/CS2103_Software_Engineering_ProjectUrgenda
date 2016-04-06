//@@author A0126888L
package urgenda.storage;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import urgenda.util.Task;

/**
 * Encryptor class is a sub-class of JsonCipher class. It is used specifically
 * for encrypting Tasks into Strings in JSON formats.
 *
 */
public class Encryptor extends JsonCipher {

	public Encryptor() {
		super();
	}

	/**
	 * Encrypts a list of Task objects into Strings in JSON format for Task objects.
	 * @param taskList The list of task to be converted to JSON Strings.
	 * @return an ArrayList of converted Tasks into JSON Strings.
	 */
	public ArrayList<String> encrypt(ArrayList<Task> taskList) {
		ArrayList<String> stringList = new ArrayList<String>();
		for (Task task : taskList) {
			_detailsMap = new LinkedHashMap<String, String>();
			getTaskDetail(task);
			convertToString();
			stringList.add(_detailsString);
		}
		return stringList;
	}
	
	

	// getting Task attributes and storing in a LinkedHashMap
	private void getTaskDetail(Task task) {
		setDesc(task);
		setType(task); 
		setLocation(task);
		setStartTime(task);
		setEndTime(task);
		setDateAdded(task);
		setDateModified(task);
		setCompleted(task);
		setImportant(task);
		setOverdue(task);
		setMultiple(task);
	}

}
