package urgenda.storage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import com.google.gson.Gson;

import urgenda.util.Task;

public class JsonCipher {
	private static final String HASHMAP_KEY_ID = "id";
	private static final String HASHMAP_KEY_DESC = "desc";
	private static final String HASHMAP_KEY_TYPE = "type";
	private static final String HASHMAP_KEY_LOCATION = "location";
	private static final String HASHMAP_KEY_STARTTIME = "startTime";
	private static final String HASHMAP_KEY_ENDTIME = "endTime";
	private static final String HASHMAP_KEY_TAGS = "tags";
	private static final String HASHMAP_KEY_DATEADDED = "dateAdded";
	private static final String HASHMAP_KEY_DATEMODIFIED = "dateModified";
	private static final String HASHMAP_KEY_COMPLETED = "completed";
	private static final String HASHMAP_KEY_URGENT = "urgent";
	private static final String HASHMAP_KEY_OVERDUE = "overdue";

	private static final String DELIMITER_HASHTAG = ",";
	
	private static final String TASKTYPE_EVENT = "EVENT";
	private static final String TASKTYPE_DEADLINE = "DEADLINE";
	private static final String TASKTYPE_FLOATING = "FLOATING";
	
	protected Gson _gson;
	protected LinkedHashMap<String, String> _taskDetails;
	protected String _taskString;

	protected JsonCipher() {
		_gson = new Gson();
		_taskDetails = new LinkedHashMap<String, String>();
	}


	protected void setOverdue(Task task) {
		_taskDetails.put(HASHMAP_KEY_OVERDUE, String.valueOf(task.isOverdue()));
	}

	protected void setUrgent(Task task) {
		_taskDetails.put(HASHMAP_KEY_URGENT, String.valueOf(task.isUrgent()));
	}

	protected void setCompleted(Task task) {
		_taskDetails.put(HASHMAP_KEY_COMPLETED, String.valueOf(task.isCompleted()));
	}

	protected void setDateModified(Task task) {
		if (task.getDateModified() == null) {
			_taskDetails.put(HASHMAP_KEY_DATEMODIFIED, null);
		} else {
			_taskDetails.put(HASHMAP_KEY_DATEMODIFIED, task.getDateModified().toString());
		}
	}

	protected void setDateAdded(Task task) {
		if (task.getDateAdded() == null) {
			_taskDetails.put(HASHMAP_KEY_DATEADDED, null);
		} else {
			_taskDetails.put(HASHMAP_KEY_DATEADDED, task.getDateAdded().toString());
		}
	}

	protected void setHashTags(Task task) {
		if (task.getHashtags() == null) {
			_taskDetails.put(HASHMAP_KEY_TAGS, null);
		} else {
			_taskDetails.put(HASHMAP_KEY_TAGS, String.join(DELIMITER_HASHTAG, task.getHashtags()));
		}
	}

	protected void setEndTime(Task task) {
		if (task.getEndTime() == null) {
			_taskDetails.put(HASHMAP_KEY_ENDTIME, null);
		} else {
			_taskDetails.put(HASHMAP_KEY_ENDTIME, task.getEndTime().toString());
		}
	}

	protected void setStartTime(Task task) {
		if (task.getStartTime() == null) {
			_taskDetails.put(HASHMAP_KEY_STARTTIME, null);
		} else {
			_taskDetails.put(HASHMAP_KEY_STARTTIME, task.getStartTime().toString());
		}
	}

	protected void setLocation(Task task) {
		if (task.getLocation() == null) {
			_taskDetails.put(HASHMAP_KEY_LOCATION, null);
		} else {
			_taskDetails.put(HASHMAP_KEY_LOCATION, task.getLocation());
		}
	}

	protected void setType(Task task) {
		if (task.getTaskType().toString().equals(TASKTYPE_EVENT)) {
			_taskDetails.put(HASHMAP_KEY_TYPE, TASKTYPE_EVENT);
		} else if (task.getTaskType().toString().equals(TASKTYPE_DEADLINE)) {
			_taskDetails.put(HASHMAP_KEY_TYPE, TASKTYPE_DEADLINE);
		} else {
			_taskDetails.put(HASHMAP_KEY_TYPE, TASKTYPE_FLOATING);
		}
	}

	protected void setDesc(Task task) {
		_taskDetails.put(HASHMAP_KEY_DESC, task.getDesc());
	}
	

	protected ArrayList<String> getHashTags() {
		ArrayList<String> hashTags;
		if (_taskDetails.get(HASHMAP_KEY_TAGS) == null) {
			hashTags = null;
		} else {
			String tagstring = _taskDetails.get(HASHMAP_KEY_TAGS);
			String[] tagstrray = tagstring.split(DELIMITER_HASHTAG);
			hashTags = new ArrayList<String>(Arrays.asList(tagstrray));
		}
		return hashTags;
	}

	protected LocalDateTime getDateModified() {
		if (_taskDetails.get(HASHMAP_KEY_DATEMODIFIED) == null) {
			return null;
		} else {
			return LocalDateTime.parse(_taskDetails.get(HASHMAP_KEY_DATEMODIFIED));
		}
	}

	protected LocalDateTime getDateAdded() {
		if (_taskDetails.get(HASHMAP_KEY_DATEADDED) == null) {
			return null;
		} else {
			return LocalDateTime.parse(_taskDetails.get(HASHMAP_KEY_DATEADDED));
		}
	}

	protected LocalDateTime getEndTime() {
		if (_taskDetails.get(HASHMAP_KEY_ENDTIME) == null) {
			return null;
		} else {
			return LocalDateTime.parse(_taskDetails.get(HASHMAP_KEY_ENDTIME));
		}
	}

	protected LocalDateTime getStartTime() {
		if (_taskDetails.get(HASHMAP_KEY_STARTTIME) == null) {
			return null;
		} else {
			return LocalDateTime.parse(_taskDetails.get(HASHMAP_KEY_STARTTIME));
		}
	}

	protected boolean checkOverdue() {
		return Boolean.parseBoolean(_taskDetails.get(HASHMAP_KEY_OVERDUE));
	}

	protected boolean checkUrgent() {
		return Boolean.parseBoolean(_taskDetails.get(HASHMAP_KEY_URGENT));
	}

	protected boolean checkCompleted() {
		return Boolean.parseBoolean(_taskDetails.get(HASHMAP_KEY_COMPLETED));
	}

	protected String getLocation() {
		return _taskDetails.get(HASHMAP_KEY_LOCATION);
	}

	protected String getType() {
		return _taskDetails.get(HASHMAP_KEY_TYPE);
	}

	protected String getDesc() {
		return _taskDetails.get(HASHMAP_KEY_DESC);
	}
	

}
