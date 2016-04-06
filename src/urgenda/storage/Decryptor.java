//@@author A0126888L
package urgenda.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.util.MultipleSlot;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

/**
 * Decryptor class is a sub-class of JsonCipher class. It is used specifically
 * for decrypting Strings in JSON formats to Task objects.
 *
 */
public class Decryptor extends JsonCipher {
	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	public Decryptor() {
		super();
	}

	/**
	 * Decrypts the Strings retrieved from the file into Task objects. Used
	 * specifically for current Tasks with ids starting from 0.
	 * 
	 * @param taskStrList
	 *            Strings must be in JSON format for a Task object.
	 * @return ArrayList of Tasks objects
	 */
	public ArrayList<Task> decryptTaskList(ArrayList<String> taskStrList) {
		ArrayList<Task> taskList = new ArrayList<Task>();
		for (int i = 0; i < taskStrList.size(); i++) {
			convertStringToTask(taskStrList, taskList, i);
		}
		return taskList;
	}

	private void convertStringToTask(ArrayList<String> taskStrList, ArrayList<Task> taskList, int i) {
		_detailsString = taskStrList.get(i);
		if (isJsonValid(_detailsString)) {
			convertToMap();
			Task newTask = generateTask(i + 1);
			taskList.add(newTask);
		} else {
			logger.getLogger().info("String is not in JSON format, discarded");
		}
	}

	/**
	 * Decrypts the Strings retrieved from the file into Tasks objects. Used
	 * specifically for completed Tasks with ids starting from -1 to
	 * differentiate between incomplete and completed tasks.
	 * 
	 * @param archiveStrList
	 *            Strings must be in JSON format for a Task object.
	 * @return ArrayList of Tasks objects for archives.
	 */
	public ArrayList<Task> decryptArchiveList(ArrayList<String> archiveStrList) {
		ArrayList<Task> archiveList = new ArrayList<Task>();
		for (int i = 0, j = -1; i < archiveStrList.size(); i++, j--) {
			convertStringtoArchiveTask(archiveStrList, archiveList, i, j);
		}
		return archiveList;
	}

	private void convertStringtoArchiveTask(ArrayList<String> archiveStrList, ArrayList<Task> archiveList, int i,
			int j) {
		_detailsString = archiveStrList.get(i);
		if (isJsonValid(_detailsString)) {
			convertToMap();
			Task newTask = generateTask(j);
			archiveList.add(newTask);
		} else {
			logger.getLogger().info("String is not in JSON format, discarded");
		}
	}

	/*
	 * Creates a task from the LinkedHashMap when converted from String to
	 * LinkedHashMap.
	 */
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
		MultipleSlot newSlot = getMultiple();
		Task newTask = new Task(id, desc, type, location, isCompleted, isImportant, isOverdue, startTime, endTime,
				dateAdded, dateModified, newSlot);
		return newTask;
	}

}
