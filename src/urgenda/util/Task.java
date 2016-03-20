package urgenda.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class Task {

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
	private static final String HASHMAP_KEY_IMPORTANT = "important";
	private static final String HASHMAP_KEY_OVERDUE = "overdue";

	private static final String TASK_TYPE_FLOATING = "FLOATING";
	private static final String TASK_TYPE_EVENT = "EVENT";
	private static final String TASK_TYPE_DEADLINE = "DEADLINE";

	public enum Type {

		// Event type have start + end time
		EVENT,
		// Deadline type has only end time
		DEADLINE,
		// Floating type has no start and no end time
		FLOATING
	}

	private int _id;
	private String _desc;
	private Type _taskType;
	private String _location;
	private LocalDateTime _startTime;
	private LocalDateTime _endTime;
	private ArrayList<String> _hashtags;
	private LocalDateTime _dateAdded;
	private LocalDateTime _dateModified;
	private boolean _isCompleted = false;
	private boolean _isImportant = false;
	private boolean _isOverdue = false;
	private MultipleSlot _slot;

	// default constructor
	public Task() {

	}

	// constructor with only desc
	public Task(String desc) {
		_desc = desc;
		updateTaskType(null, null);
	}

	// constructor for inclusion of details if there is priority
	public Task(String desc, String location, LocalDateTime start, LocalDateTime end, ArrayList<String> tags,
			boolean isImportant) {
		_desc = desc;
		_location = location;
		updateTaskType(start, end);
		_startTime = start;
		_endTime = end;
		_hashtags = tags;
		_dateAdded = LocalDateTime.now();
		_dateModified = LocalDateTime.now();
		_isImportant = isImportant;
	}

	// constructor for inclusion of details without priority
	public Task(String desc, String location, LocalDateTime start, LocalDateTime end, ArrayList<String> tags) {
		_desc = desc;
		_location = location;
		updateTaskType(start, end);
		_startTime = start;
		_endTime = end;
		_hashtags = tags;
		_dateAdded = LocalDateTime.now();
		_dateModified = LocalDateTime.now();
	}

	// constructor for copying task objects
	public Task(Task originalTask) {
		setId(originalTask.getId());
		setDesc(originalTask.getDesc());
		setTaskType(originalTask.getTaskType());
		setLocation(originalTask.getLocation());
		setStartTime(originalTask.getStartTime());
		setEndTime(originalTask.getEndTime());
		setHashtags(originalTask.getHashtags());
		setDateAdded(originalTask.getDateAdded());
		setDateModified(originalTask.getDateModified());
		setIsCompleted(originalTask.isCompleted());
		setIsImportant(originalTask.isImportant());
		setIsOverdue(originalTask.isOverdue());
		setSlot(originalTask.getSlot());
	}

	/**
	 * Full constructor for all variables.
	 * 
	 * @param type
	 *            required to be of String type, will be converted to enum Type
	 *            in constructor
	 */
	public Task(int id, String desc, String type, String location, boolean isCompleted, boolean isImportant,
			boolean isOverdue, LocalDateTime startTime, LocalDateTime endTime, LocalDateTime dateAdded,
			LocalDateTime dateModified, ArrayList<String> hashTags, MultipleSlot slot) {
		_id = id;
		_desc = desc;
		setType(type);
		_location = location;
		_isCompleted = isCompleted;
		_isImportant = isImportant;
		_isOverdue = isOverdue;
		_startTime = startTime;
		_endTime = endTime;
		_dateAdded = dateAdded;
		_dateModified = dateModified;
		_hashtags = hashTags;
		_slot = slot;

	}

	private void setType(String type) {
		if (type.equals(TASK_TYPE_EVENT)) {
			_taskType = Type.EVENT;
		} else if (type.equals(TASK_TYPE_DEADLINE)) {
			_taskType = Type.DEADLINE;
		} else if (type.equals(TASK_TYPE_FLOATING)) {
			_taskType = Type.FLOATING;
		}
	}

	// TODO: support for _slot in storage
	// constructor for creating Task object from LinkedHashMap
	public Task(LinkedHashMap<String, String> taskDetails, int id) {
		_id = id;
		_desc = taskDetails.get(HASHMAP_KEY_DESC);

		String type = taskDetails.get(HASHMAP_KEY_TYPE);
		setType(type);

		_location = taskDetails.get(HASHMAP_KEY_LOCATION);
		_isCompleted = Boolean.parseBoolean(taskDetails.get(HASHMAP_KEY_COMPLETED));
		_isImportant = Boolean.parseBoolean(taskDetails.get(HASHMAP_KEY_IMPORTANT));
		_isOverdue = Boolean.parseBoolean(taskDetails.get(HASHMAP_KEY_OVERDUE));

		if (taskDetails.get(HASHMAP_KEY_STARTTIME) == null) {
			_startTime = null;
		} else {
			_startTime = LocalDateTime.parse(taskDetails.get(HASHMAP_KEY_STARTTIME));
		}

		if (taskDetails.get(HASHMAP_KEY_ENDTIME) == null) {
			_endTime = null;
		} else {
			_endTime = LocalDateTime.parse(taskDetails.get(HASHMAP_KEY_ENDTIME));
		}

		if (taskDetails.get(HASHMAP_KEY_DATEADDED) == null) {
			_dateAdded = null;
		} else {
			_dateAdded = LocalDateTime.parse(taskDetails.get(HASHMAP_KEY_DATEADDED));
		}

		if (taskDetails.get(HASHMAP_KEY_DATEMODIFIED) == null) {
			_dateModified = null;
		} else {
			_dateModified = LocalDateTime.parse(taskDetails.get(HASHMAP_KEY_DATEMODIFIED));
		}

		if (taskDetails.get(HASHMAP_KEY_TAGS) == null) {
			_hashtags = null;
		} else {
			String tagstring = taskDetails.get(HASHMAP_KEY_TAGS);
			System.out.println(tagstring);
			String[] tagstrray = tagstring.split(",");
			_hashtags = new ArrayList<String>(Arrays.asList(tagstrray));
		}
	}

	public void updateTaskType(LocalDateTime start, LocalDateTime end) {
		if (start == null && end == null) {
			_taskType = Type.FLOATING;
		} else if (start == null && end != null) {
			_taskType = Type.DEADLINE;
		} else if (start != null && end != null) {
			_taskType = Type.EVENT;
		}
	}

	public int getId() {
		return _id;
	}

	public String getDesc() {
		return _desc;
	}

	public Type getTaskType() {
		return _taskType;
	}

	public String getLocation() {
		return _location;
	}

	public LocalDateTime getStartTime() {
		return _startTime;
	}

	public LocalDateTime getEndTime() {
		return _endTime;
	}

	public ArrayList<String> getHashtags() {
		return _hashtags;
	}

	public LocalDateTime getDateAdded() {
		return _dateAdded;
	}

	public LocalDateTime getDateModified() {
		return _dateModified;
	}

	public boolean isCompleted() {
		return _isCompleted;
	}

	public boolean isImportant() {
		return _isImportant;
	}

	public boolean isOverdue() {
		return _isOverdue;
	}

	public MultipleSlot getSlot() {
		return _slot;
	}

	public void setId(int id) {
		_id = id;
	}

	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setTaskType(Type taskType) {
		_taskType = taskType;
	}

	public void setLocation(String location) {
		_location = location;
	}

	public void setStartTime(LocalDateTime startTime) {
		_startTime = startTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		_endTime = endTime;
	}

	public void setHashtags(ArrayList<String> hashtags) {
		_hashtags = hashtags;
	}

	public void setDateAdded(LocalDateTime dateAdded) {
		_dateAdded = dateAdded;
	}

	public void setDateModified(LocalDateTime dateModified) {
		_dateModified = dateModified;
	}

	public void setIsCompleted(boolean isCompleted) {
		_isCompleted = isCompleted;
	}

	public void setIsImportant(boolean isImportant) {
		_isImportant = isImportant;
	}

	public void setIsOverdue(boolean isOverdue) {
		_isOverdue = isOverdue;
	}

	public void setSlot(MultipleSlot slot) {
		_slot = slot;
	}

	public void toggleImportant() {
		if (_isImportant) {
			_isImportant = false;
		} else {
			_isImportant = true;
		}
	}

	public boolean isOverlapping(Task task) {
		// defensive code to prevent checking of non event types
		if (task.getTaskType() != Task.Type.EVENT || this.getTaskType() != Task.Type.EVENT) {
			return false;
		}
		
		// TODO check if all cases are covered
		if (task.getEndTime().isAfter(_startTime) && task.getEndTime().isBefore(_endTime)) {
			return true;
		} else if (task.getStartTime().isAfter(_startTime) && task.getStartTime().isBefore(_endTime)) {
			return true;
		} else if (task.getStartTime().isBefore(_startTime) && task.getEndTime().isAfter(_endTime)) {
			return true;
		} else if (task.getStartTime().equals(_startTime) && task.getEndTime().equals(_endTime)) {
			return true;
		} else {
			return false;
		}
	}

}
