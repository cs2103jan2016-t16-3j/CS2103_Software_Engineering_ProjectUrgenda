package urgenda.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import urgenda.util.MultipleSlot;
import urgenda.util.MyLogger;
import urgenda.util.Task;
import urgenda.util.TimePair;

public class JsonCipher {
	private static final String HASHMAP_KEY_DESC = "desc";
	private static final String HASHMAP_KEY_TYPE = "type";
	private static final String HASHMAP_KEY_LOCATION = "location";
	private static final String HASHMAP_KEY_STARTTIME = "startTime";
	private static final String HASHMAP_KEY_ENDTIME = "endTime";
	private static final String HASHMAP_KEY_TAGS = "tags";
	private static final String HASHMAP_KEY_DATEADDED = "dateAdded";
	private static final String HASHMAP_KEY_DATEMODIFIED = "dateModified";
	private static final String HASHMAP_KEY_COMPLETED = "completed";
	private static final String HASHMAP_KEY_IMPORTANT = "important";
	private static final String HASHMAP_KEY_OVERDUE = "overdue";
	private static final String HASHMAP_KEY_MULTIPLE_DESC = "multipleDesc";
	private static final String HASHMAP_KEY_FILE_DIRECTORY = "directory";
	private static final String HASHMAP_KEY_FILE_NAME = "name";

	private static final String DELIMITER_HASHTAG = ",";
	private static final String DELIMITER_MULTIPLE_WITHIN_PAIRS = "~";
	private static final String DELIMITER_MULTIPLE_BET_PAIRS = "`";

	private static final String TASKTYPE_EVENT = "EVENT";
	private static final String TASKTYPE_DEADLINE = "DEADLINE";
	private static final String TASKTYPE_FLOATING = "FLOATING";

	private static final String DEFAULT_FILE_LOCATION = "settings";
	private static final String DEFAULT_FILE_NAME = "data.txt";

	private static final int MULTIPLE_START = 0;
	private static final int MULTIPLE_END = 1;
	
	private static MyLogger logger = MyLogger.getInstance();

	protected Gson _gson;
	protected LinkedHashMap<String, String> _detailsMap;
	protected String _detailsString;

	public JsonCipher() {
		_gson = new Gson();
		_detailsMap = new LinkedHashMap<String, String>();
	}

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

	public void reset() {
		_detailsMap.clear();
		_detailsString = new String();
	}

	public void checkIfMapExist() {
		if (_detailsMap == null) {
			_detailsMap = new LinkedHashMap<String, String>();
		}
	}

	public boolean isEmptyMap() {
		if (_detailsMap.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	private boolean isJsonValid(String phrase) {
		try {
			_gson.fromJson(phrase, new TypeToken<LinkedHashMap<String, String>>() {
			}.getType());
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			logger.getLogger().info(ex.toString());
			logger.getLogger().info("String not in required json format for Tasks. Unable to parse, will be overwritten.");
			return false;
		}
	}

	public void convertToMap() {
		_detailsMap = _gson.fromJson(_detailsString, new TypeToken<LinkedHashMap<String, String>>() {
		}.getType());
	}

	public void convertToString() {
		_detailsString = _gson.toJson(_detailsMap);
	}

	public void setMultiple(Task task) {
		if (task.getSlot() == null) {
			_detailsMap.put(HASHMAP_KEY_MULTIPLE_DESC, null);
		} else {
			ArrayList<TimePair> pairs = task.getSlot().getSlots();
			String slots = new String();
			for (TimePair pair : pairs) {
				slots = slots + pair.getStart().toString() + DELIMITER_MULTIPLE_WITHIN_PAIRS + pair.getEnd().toString();
				slots = slots + DELIMITER_MULTIPLE_BET_PAIRS;
			}
			_detailsMap.put(HASHMAP_KEY_MULTIPLE_DESC, slots);
		}
	}

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
			for (String pair : slotsArray) {
				String[] pairArray = pair.split(DELIMITER_MULTIPLE_WITHIN_PAIRS);
				LocalDateTime start = LocalDateTime.parse(pairArray[MULTIPLE_START]);
				LocalDateTime end = LocalDateTime.parse(pairArray[MULTIPLE_END]);
				slots.addTimeSlot(start, end);
			}

			return slots;
		}
	}

	public String getMultipleString() {
		return _detailsMap.get(HASHMAP_KEY_MULTIPLE_DESC);
	}

	public ArrayList<String> getHashTags() {
		ArrayList<String> hashTags;
		if (_detailsMap.get(HASHMAP_KEY_TAGS) == null) {
			hashTags = null;
		} else {
			String tagString = _detailsMap.get(HASHMAP_KEY_TAGS);
			String[] tagsArray = tagString.split(DELIMITER_HASHTAG);
			hashTags = new ArrayList<String>(Arrays.asList(tagsArray));
		}
		return hashTags;
	}

	public void setOverdue(Task task) {
		_detailsMap.put(HASHMAP_KEY_OVERDUE, String.valueOf(task.isOverdue()));
	}

	public void setImportant(Task task) {
		_detailsMap.put(HASHMAP_KEY_IMPORTANT, String.valueOf(task.isImportant()));
	}

	public void setCompleted(Task task) {
		_detailsMap.put(HASHMAP_KEY_COMPLETED, String.valueOf(task.isCompleted()));
	}

	public void setDateModified(Task task) {
		if (task.getDateModified() == null) {
			_detailsMap.put(HASHMAP_KEY_DATEMODIFIED, null);
		} else {
			_detailsMap.put(HASHMAP_KEY_DATEMODIFIED, task.getDateModified().toString());
		}
	}

	public void setDateAdded(Task task) {
		if (task.getDateAdded() == null) {
			_detailsMap.put(HASHMAP_KEY_DATEADDED, null);
		} else {
			_detailsMap.put(HASHMAP_KEY_DATEADDED, task.getDateAdded().toString());
		}
	}

	public void setHashTags(Task task) {
		if (task.getHashtags() == null) {
			_detailsMap.put(HASHMAP_KEY_TAGS, null);
		} else {
			_detailsMap.put(HASHMAP_KEY_TAGS, String.join(DELIMITER_HASHTAG, task.getHashtags()));
		}
	}

	public void setEndTime(Task task) {
		if (task.getEndTime() == null) {
			_detailsMap.put(HASHMAP_KEY_ENDTIME, null);
		} else {
			_detailsMap.put(HASHMAP_KEY_ENDTIME, task.getEndTime().toString());
		}
	}

	public void setStartTime(Task task) {
		if (task.getStartTime() == null) {
			_detailsMap.put(HASHMAP_KEY_STARTTIME, null);
		} else {
			_detailsMap.put(HASHMAP_KEY_STARTTIME, task.getStartTime().toString());
		}
	}

	public void setLocation(Task task) {
		if (task.getLocation() == null) {
			_detailsMap.put(HASHMAP_KEY_LOCATION, null);
		} else {
			_detailsMap.put(HASHMAP_KEY_LOCATION, task.getLocation());
		}
	}

	public void setType(Task task) {
		assert (task.getTaskType() != null);
		if (task.getTaskType().toString().equals(TASKTYPE_EVENT)) {
			_detailsMap.put(HASHMAP_KEY_TYPE, TASKTYPE_EVENT);
		} else if (task.getTaskType().toString().equals(TASKTYPE_DEADLINE)) {
			_detailsMap.put(HASHMAP_KEY_TYPE, TASKTYPE_DEADLINE);
		} else {
			_detailsMap.put(HASHMAP_KEY_TYPE, TASKTYPE_FLOATING);
		}
	}

	public void setDesc(Task task) {
		_detailsMap.put(HASHMAP_KEY_DESC, task.getDesc());
	}

	public void setDirectory(String path) {
		_detailsMap.put(HASHMAP_KEY_FILE_DIRECTORY, path);
	}

	public void setFileName(String name) {
		_detailsMap.put(HASHMAP_KEY_FILE_NAME, name);
	}

	public LocalDateTime getDateModified() {
		if (_detailsMap.get(HASHMAP_KEY_DATEMODIFIED) == null) {
			return null;
		} else {
			return LocalDateTime.parse(_detailsMap.get(HASHMAP_KEY_DATEMODIFIED));
		}
	}

	public LocalDateTime getDateAdded() {
		if (_detailsMap.get(HASHMAP_KEY_DATEADDED) == null) {
			return null;
		} else {
			return LocalDateTime.parse(_detailsMap.get(HASHMAP_KEY_DATEADDED));
		}
	}

	public LocalDateTime getEndTime() {
		if (_detailsMap.get(HASHMAP_KEY_ENDTIME) == null) {
			return null;
		} else {
			return LocalDateTime.parse(_detailsMap.get(HASHMAP_KEY_ENDTIME));
		}
	}

	public LocalDateTime getStartTime() {
		if (_detailsMap.get(HASHMAP_KEY_STARTTIME) == null) {
			return null;
		} else {
			return LocalDateTime.parse(_detailsMap.get(HASHMAP_KEY_STARTTIME));
		}
	}

	public boolean checkOverdue() {
		return Boolean.parseBoolean(_detailsMap.get(HASHMAP_KEY_OVERDUE));
	}

	public boolean checkImportant() {
		return Boolean.parseBoolean(_detailsMap.get(HASHMAP_KEY_IMPORTANT));
	}

	public boolean checkCompleted() {
		return Boolean.parseBoolean(_detailsMap.get(HASHMAP_KEY_COMPLETED));
	}

	public String getLocation() {
		return _detailsMap.get(HASHMAP_KEY_LOCATION);
	}

	public String getType() {
		return _detailsMap.get(HASHMAP_KEY_TYPE);
	}

	public String getDesc() {
		return _detailsMap.get(HASHMAP_KEY_DESC);
	}

	public String getDirectory() {
		if (_detailsMap.get(HASHMAP_KEY_FILE_DIRECTORY) == null) {
			return DEFAULT_FILE_LOCATION;
		} else {
			return _detailsMap.get(HASHMAP_KEY_FILE_DIRECTORY);
		}
	}

	public String getFileName() {
		if (_detailsMap.get(HASHMAP_KEY_FILE_NAME) == null) {
			return DEFAULT_FILE_NAME;
		} else {
			return _detailsMap.get(HASHMAP_KEY_FILE_NAME);
		}
	}

	public String getDetailsString() {
		return _detailsString;
	}

	public LinkedHashMap<String, String> getDetailsMap() {
		return _detailsMap;
	}

}
