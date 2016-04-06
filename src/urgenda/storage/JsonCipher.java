//@@author A0126888L
package urgenda.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import urgenda.util.MultipleSlot;
import urgenda.util.UrgendaLogger;
import urgenda.util.Task;
import urgenda.util.DateTimePair;

/**
 * JsonCipher class is the main class used for conversion between a Task and
 * String. JsonCipher extracts the relevant information from the Task, storing
 * it a LinkedHashMap before it is converted into a String. JsonCipher has two
 * derived classes from it: Encryptor and Decryptor.
 *
 */
public class JsonCipher {
	private static final String HASHMAP_KEY_DESC = "desc";
	private static final String HASHMAP_KEY_TYPE = "type";
	private static final String HASHMAP_KEY_LOCATION = "location";
	private static final String HASHMAP_KEY_STARTTIME = "startTime";
	private static final String HASHMAP_KEY_ENDTIME = "endTime";
	private static final String HASHMAP_KEY_DATEADDED = "dateAdded";
	private static final String HASHMAP_KEY_DATEMODIFIED = "dateModified";
	private static final String HASHMAP_KEY_COMPLETED = "completed";
	private static final String HASHMAP_KEY_IMPORTANT = "important";
	private static final String HASHMAP_KEY_OVERDUE = "overdue";
	private static final String HASHMAP_KEY_MULTIPLE_DESC = "multipleDesc";
	private static final String HASHMAP_KEY_FILE_DIRECTORY = "directory";
	private static final String HASHMAP_KEY_FILE_NAME = "name";
	private static final String HASHMAP_KEY_HEADER = "header";

	private static final String DELIMITER_MULTIPLE_WITHIN_PAIRS = "~";
	private static final String DELIMITER_MULTIPLE_BET_PAIRS = "`";

	private static final String TASKTYPE_EVENT = "EVENT";
	private static final String TASKTYPE_DEADLINE = "DEADLINE";
	private static final String TASKTYPE_FLOATING = "FLOATING";

	private static final String DEFAULT_FILE_LOCATION = "settings";
	private static final String DEFAULT_FILE_NAME = "data.txt";

	private static final int MULTIPLE_START = 0;
	private static final int MULTIPLE_END = 1;

	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	protected Gson _gson;
	protected LinkedHashMap<String, String> _detailsMap;
	protected String _detailsString;

	/**
	 * default constructor for JsonCipher class. Initializes a new Gson and new
	 * LinkedHashMap.
	 */
	public JsonCipher() {
		_gson = new Gson();
		_detailsMap = new LinkedHashMap<String, String>();
	}

	/**
	 * Constructor taking in a String, automatically converting it to a
	 * LinkedHashMap if possible.
	 * 
	 * @param detailsString
	 *            String should be JSON format for conversion to a LinkedHashMap
	 *            to take place.
	 */
	public JsonCipher(String detailsString) {
		_gson = new Gson();
		if (isJsonValid(detailsString)) {
			_detailsString = detailsString;
			convertToMap();
			checkIfMapExist();
		} else {
			_detailsMap = new LinkedHashMap<String, String>();
		}
	}

	/**
	 * Clears the attributes of JsonCipher.
	 */
	public void reset() {
		_detailsMap.clear();
		_detailsString = new String();
	}

	/**
	 * Checks if LinkedHashMap is of NULL value.
	 */
	public void checkIfMapExist() {
		if (_detailsMap == null) {
			_detailsMap = new LinkedHashMap<String, String>();
		}
	}

	/**
	 * Checks if LinkedHashMap is empty.
	 * 
	 * @return true if LinkedHashMap is empty, false otherwise.
	 */
	public boolean isEmptyMap() {
		if (_detailsMap.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Checks if give phrase is of valid Json format.
	 * 
	 * @param phrase
	 *            given phrase for checking.
	 * @return true if is valid Json format for Task objects, false otherwise.
	 */
	protected boolean isJsonValid(String phrase) {
		try {
			_gson.fromJson(phrase, new TypeToken<LinkedHashMap<String, String>>() {
			}.getType());
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			logger.getLogger().info(ex.toString());
			logger.getLogger()
					.info("String not in required json format for Tasks. Unable to parse, will be overwritten.");
			return false;
		}
	}

	/**
	 * converts stored String to LinkedHashMap.
	 */
	public void convertToMap() {
		_detailsMap = _gson.fromJson(_detailsString, new TypeToken<LinkedHashMap<String, String>>() {
		}.getType());
	}

	/**
	 * Converts stored LinkedHashMap to String.
	 */
	public void convertToString() {
		_detailsString = _gson.toJson(_detailsMap);
	}

	/**
	 * extracts multiple slots from Task to be place in LinkedHashMap.
	 * 
	 * @param task
	 *            Given task to be converted to LinkedHashMap.
	 */
	public void setMultiple(Task task) {
		if (task.getSlot() == null) {
			_detailsMap.put(HASHMAP_KEY_MULTIPLE_DESC, null);
		} else {
			convertSlotsToString(task);
		}
	}

	/*
	 * converts multipleslots in a task to a string to be stored in
	 * LinkedHashMap.
	 */
	private void convertSlotsToString(Task task) {
		ArrayList<DateTimePair> pairs = task.getSlot().getSlots();
		String slots = new String();
		slots = convertPairToString(pairs, slots);
		_detailsMap.put(HASHMAP_KEY_MULTIPLE_DESC, slots);
	}

	private String convertPairToString(ArrayList<DateTimePair> pairs, String slots) {
		for (DateTimePair pair : pairs) {
			slots = slots + pair.getEarlierDateTime().toString() + DELIMITER_MULTIPLE_WITHIN_PAIRS
					+ pair.getLaterDateTime().toString();
			slots = slots + DELIMITER_MULTIPLE_BET_PAIRS;
		}
		return slots;
	}

	/**
	 * Returns the slots of a Task object.
	 * 
	 * @return MultipleSlot format for multiple timeslots of a Task object.
	 */
	public MultipleSlot getMultiple() {
		if (_detailsMap.get(HASHMAP_KEY_MULTIPLE_DESC) == null) {
			return null;
		} else if (_detailsMap.get(HASHMAP_KEY_MULTIPLE_DESC).isEmpty()) {
			MultipleSlot slots = new MultipleSlot();
			return slots;
		} else {
			MultipleSlot slots = new MultipleSlot();
			String slotsString = _detailsMap.get(HASHMAP_KEY_MULTIPLE_DESC);
			String[] slotsArray = slotsString.split(DELIMITER_MULTIPLE_BET_PAIRS);
			convertStringtoSlot(slots, slotsArray);
			return slots;
		}
	}

	private void convertStringtoSlot(MultipleSlot slots, String[] slotsArray) {
		for (String pair : slotsArray) {
			String[] pairArray = pair.split(DELIMITER_MULTIPLE_WITHIN_PAIRS);
			LocalDateTime start = LocalDateTime.parse(pairArray[MULTIPLE_START]);
			LocalDateTime end = LocalDateTime.parse(pairArray[MULTIPLE_END]);
			slots.addTimeSlot(start, end);
		}
	}

	/**
	 * Extracts the boolean value of Overdue from Task and associates the key
	 * with the boolean value, placing it in the relevant LinkedHashMap mapping.
	 * 
	 * @param task
	 *            Given task to be converted to LinkedHashMap.
	 */
	public void setOverdue(Task task) {
		_detailsMap.put(HASHMAP_KEY_OVERDUE, String.valueOf(task.isOverdue()));
	}

	/**
	 * Extracts the boolean value of Important from Task and associates the key
	 * with the boolean value, placing it in the relevant LinkedHashMap mapping.
	 * 
	 * @param task
	 *            Given task to be converted to LinkedHashMap.
	 */
	public void setImportant(Task task) {
		_detailsMap.put(HASHMAP_KEY_IMPORTANT, String.valueOf(task.isImportant()));
	}

	/**
	 * Extracts the boolean value of Completed from Task and associates the key
	 * with the boolean value, placing it in the relevant LinkedHashMap mapping.
	 * 
	 * @param task
	 *            Given task to be converted to LinkedHashMap.
	 */
	public void setCompleted(Task task) {
		_detailsMap.put(HASHMAP_KEY_COMPLETED, String.valueOf(task.isCompleted()));
	}

	/**
	 * Extracts the LocalDateTime value of DateModified from Task and associates
	 * the key with the LocalDateTime value, placing it in the relevant
	 * LinkedHashMap mapping.
	 * 
	 * @param task
	 *            Given task to be converted to LinkedHashMap.
	 */
	public void setDateModified(Task task) {
		if (task.getDateModified() == null) {
			_detailsMap.put(HASHMAP_KEY_DATEMODIFIED, null);
		} else {
			_detailsMap.put(HASHMAP_KEY_DATEMODIFIED, task.getDateModified().toString());
		}
	}

	/**
	 * Extracts the LocalDateTime value of DateAdded from Task and associates
	 * the key with the LocalDateTime value, placing it in the relevant
	 * LinkedHashMap mapping.
	 * 
	 * @param task
	 *            Given task to be converted to LinkedHashMap.
	 */
	public void setDateAdded(Task task) {
		if (task.getDateAdded() == null) {
			_detailsMap.put(HASHMAP_KEY_DATEADDED, null);
		} else {
			_detailsMap.put(HASHMAP_KEY_DATEADDED, task.getDateAdded().toString());
		}
	}

	/**
	 * Extracts the LocalDateTime value of EndTime from Task and associates the
	 * key with the LocalDateTime value, placing it in the relevant
	 * LinkedHashMap mapping.
	 * 
	 * @param task
	 *            Given task to be converted to LinkedHashMap.
	 */
	public void setEndTime(Task task) {
		if (task.getEndTime() == null) {
			_detailsMap.put(HASHMAP_KEY_ENDTIME, null);
		} else {
			_detailsMap.put(HASHMAP_KEY_ENDTIME, task.getEndTime().toString());
		}
	}

	/**
	 * Extracts the LocalDateTime value of StartTime from Task and associates
	 * the key with the LocalDateTime value, placing it in the relevant
	 * LinkedHashMap mapping.
	 * 
	 * @param task
	 *            Given task to be converted to LinkedHashMap.
	 */
	public void setStartTime(Task task) {
		if (task.getStartTime() == null) {
			_detailsMap.put(HASHMAP_KEY_STARTTIME, null);
		} else {
			_detailsMap.put(HASHMAP_KEY_STARTTIME, task.getStartTime().toString());
		}
	}

	/**
	 * Extracts the String value of Location from Task and associates the key
	 * with the String value, placing it in the relevant LinkedHashMap mapping.
	 * 
	 * @param task
	 *            Given task to be converted to LinkedHashMap.
	 */
	public void setLocation(Task task) {
		if (task.getLocation() == null) {
			_detailsMap.put(HASHMAP_KEY_LOCATION, null);
		} else {
			_detailsMap.put(HASHMAP_KEY_LOCATION, task.getLocation());
		}
	}

	/**
	 * Extracts the Type value of TaskType from Task and associates the key with
	 * the Type value, placing it in the relevant LinkedHashMap mapping.
	 * 
	 * @param task
	 *            Given task to be converted to LinkedHashMap.
	 */
	public void setType(Task task) {
		if (task.getTaskType().toString().equals(TASKTYPE_EVENT)) {
			_detailsMap.put(HASHMAP_KEY_TYPE, TASKTYPE_EVENT);
		} else if (task.getTaskType().toString().equals(TASKTYPE_DEADLINE)) {
			_detailsMap.put(HASHMAP_KEY_TYPE, TASKTYPE_DEADLINE);
		} else {
			_detailsMap.put(HASHMAP_KEY_TYPE, TASKTYPE_FLOATING);
		}
	}

	/**
	 * Extracts the String value of Description from Task and associates the key
	 * with the String value, placing it in the relevant LinkedHashMap mapping.
	 * 
	 * @param task
	 *            Given task to be converted to LinkedHashMap.
	 */
	public void setDesc(Task task) {
		if (task.getDesc() == null) {
			_detailsMap.put(HASHMAP_KEY_DESC, null);
		} else {
			_detailsMap.put(HASHMAP_KEY_DESC, task.getDesc());
		}
	}

	/**
	 * Associates the String given with the key to place it in the relevant
	 * LinkedHashMap mapping of the file directory.
	 * 
	 * @param path
	 *            Given String for file directory.
	 */
	public void setDirectory(String path) {
		_detailsMap.put(HASHMAP_KEY_FILE_DIRECTORY, path);
	}

	/**
	 * Associates the String given with the key to place it in the relevant
	 * LinkedHashMap mapping of the file name.
	 * 
	 * @param name
	 *            Given String for file name.
	 */
	public void setFileName(String name) {
		_detailsMap.put(HASHMAP_KEY_FILE_NAME, name);
	}
	
	
	/**
	 * Retrieves the value stored in the LinkedHashMap associated with the DateModified key.
	 * 
	 * @return LocalDateTime format of DateModified.
	 */
	public LocalDateTime getDateModified() {
		if (_detailsMap.get(HASHMAP_KEY_DATEMODIFIED) == null) {
			return null;
		} else {
			return LocalDateTime.parse(_detailsMap.get(HASHMAP_KEY_DATEMODIFIED));
		}
	}
	
	/**
	 * Retrieves the value stored in the LinkedHashMap associated with the DateAdded key.
	 * 
	 * @return LocalDateTime format of DateAdded.
	 */
	public LocalDateTime getDateAdded() {
		if (_detailsMap.get(HASHMAP_KEY_DATEADDED) == null) {
			return null;
		} else {
			return LocalDateTime.parse(_detailsMap.get(HASHMAP_KEY_DATEADDED));
		}
	}
	
	/**
	 * Retrieves the value stored in the LinkedHashMap associated with the EndTime key.
	 * 
	 * @return LocalDateTime format of EndTime.
	 */
	public LocalDateTime getEndTime() {
		if (_detailsMap.get(HASHMAP_KEY_ENDTIME) == null) {
			return null;
		} else {
			return LocalDateTime.parse(_detailsMap.get(HASHMAP_KEY_ENDTIME));
		}
	}

	/**
	 * Retrieves the value stored in the LinkedHashMap associated with the StartTime key.
	 * 
	 * @return LocalDateTime format of StartTime.
	 */
	public LocalDateTime getStartTime() {
		if (_detailsMap.get(HASHMAP_KEY_STARTTIME) == null) {
			return null;
		} else {
			return LocalDateTime.parse(_detailsMap.get(HASHMAP_KEY_STARTTIME));
		}
	}
	
	/**
	 * Retrieves the value stored in the LinkedHashMap associated with the Overdue key.
	 * 
	 * @return boolean value of Overdue.
	 */
	public boolean checkOverdue() {
		return Boolean.parseBoolean(_detailsMap.get(HASHMAP_KEY_OVERDUE));
	}

	/**
	 * Retrieves the value stored in the LinkedHashMap associated with the Important key.
	 * 
	 * @return boolean value of Important.
	 */
	public boolean checkImportant() {
		return Boolean.parseBoolean(_detailsMap.get(HASHMAP_KEY_IMPORTANT));
	}

	/**
	 * Retrieves the value stored in the LinkedHashMap associated with the Completed key.
	 * 
	 * @return boolean value of Completed.
	 */
	public boolean checkCompleted() {
		return Boolean.parseBoolean(_detailsMap.get(HASHMAP_KEY_COMPLETED));
	}

	/**
	 * Retrieves the value stored in the LinkedHashMap associated with the Location key.
	 * 
	 * @return String value of Location.
	 */
	public String getLocation() {
		return _detailsMap.get(HASHMAP_KEY_LOCATION);
	}

	/**
	 * Retrieves the value stored in the LinkedHashMap associated with the Type key.
	 * 
	 * @return String value of Type.
	 */
	public String getType() {
		return _detailsMap.get(HASHMAP_KEY_TYPE);
	}

	/**
	 * Retrieves the value stored in the LinkedHashMap associated with the Description key.
	 * 
	 * @return String value of Description.
	 */
	public String getDesc() {
		return _detailsMap.get(HASHMAP_KEY_DESC);
	}
	
	/**
	 * Retrieves the value stored in the LinkedHashMap associated with the Directory key.
	 * 
	 * @return String value of Directory.
	 */
	public String getDirectory() {
		if (_detailsMap.get(HASHMAP_KEY_FILE_DIRECTORY) == null) {
			return DEFAULT_FILE_LOCATION;
		} else {
			return _detailsMap.get(HASHMAP_KEY_FILE_DIRECTORY);
		}
	}
	
	/**
	 * Retrieves the value stored in the LinkedHashMap associated with the FileName key.
	 * 
	 * @return String value of FileName.
	 */
	public String getFileName() {
		if (_detailsMap.get(HASHMAP_KEY_FILE_NAME) == null) {
			return DEFAULT_FILE_NAME;
		} else {
			return _detailsMap.get(HASHMAP_KEY_FILE_NAME);
		}
	}
	
	/**
	 * Returns the JSON format String of a Task object.
	 * 
	 * @return String of a Task object
	 */
	public String getDetailsString() {
		return _detailsString;
	}

	/**
	 * Returns the LinkedHashMap of a Task object. 
	 * 
	 * @return LinkedHashMap of a Task object.
	 */
	public LinkedHashMap<String, String> getDetailsMap() {
		return _detailsMap;
	}

}
