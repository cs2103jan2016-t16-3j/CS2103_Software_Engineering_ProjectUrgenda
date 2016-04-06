//@@author A0126888L
package urgenda.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.util.MultipleSlot;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

/**
 * Decryptor class is a sub-class of JsonCipher class. It is used specifically for decrypting Strings in JSON formats to 
 * @author User
 *
 */
public class Decryptor extends JsonCipher {
	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	public Decryptor() {
		super();
	}

	public ArrayList<Task> decryptTaskList(ArrayList<String> taskStrList) {
		ArrayList<Task> taskList = new ArrayList<Task>();
		for (int i = 0; i < taskStrList.size(); i++) {
			_detailsString = taskStrList.get(i);
			if (isJsonValid(_detailsString)) {
				convertToMap();
				Task newTask = generateTask(i + 1);
				taskList.add(newTask);
			} else {
				logger.getLogger().info("String is not in JSON format, discarded");
			}
		}
		return taskList;
	}

	public ArrayList<Task> decryptArchiveList(ArrayList<String> archiveStrList) {
		ArrayList<Task> archiveList = new ArrayList<Task>();
		for (int i = 0, j = -1; i < archiveStrList.size(); i++, j--) {
			_detailsString = archiveStrList.get(i);
			if (isJsonValid(_detailsString)) {
				convertToMap();
				Task newTask = generateTask(j);
				archiveList.add(newTask);
			} else {
				logger.getLogger().info("String is not in JSON format, discarded");
			}
		}
		return archiveList;
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
		MultipleSlot newSlot = getMultiple();
		Task newTask = new Task(id, desc, type, location, isCompleted, isImportant, isOverdue, startTime, endTime,
				dateAdded, dateModified, newSlot);
		return newTask;
	}

}
