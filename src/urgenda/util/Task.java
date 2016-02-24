package urgenda.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class Task {

	public enum Type {

		// Event type have start + end time
		EVENT,
		// Deadline type has only end time
		DEADLINE,
		// Floating type has no start and no end time
		FLOATING,
		// Start type has only starting time
		START
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
	private boolean _isUrgent = false;
	private boolean _isOverdue = false;

	// default constructor
	public Task() {

	}

	// constructor for inclusion of details if there is priority
	public Task(String desc, String location, LocalDateTime start, LocalDateTime end,
			ArrayList<String> tags, boolean isUrgent) {
		_desc = desc;
		_location = location;
		setType(start, end);
		_startTime = start;
		_endTime = end;
		_hashtags = tags;
		_dateAdded = LocalDateTime.now();
		_dateModified = LocalDateTime.now();
		_isUrgent = isUrgent;
	}
	
	// constructor for inclusion of details without priority
	public Task(String desc, String location, LocalDateTime start, LocalDateTime end,
			ArrayList<String> tags) {
		_desc = desc;
		_location = location;
		setType(start, end);
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
		setIsUrgent(originalTask.isUrgent());
		setIsOverdue(originalTask.isOverdue());
	}

	/*
	// constructor for creating Task objects from LinkedHashMap
	public Task(LinkedHashMap<String, String> taskDetails) {
		int _id = Integer.parseInt(taskDetails.get("id"));

		String desc = taskDetails.get("description");
		_desc = new SimpleStringProperty(desc);

		String type = taskDetails.get("taskType");
		if (type.equals("EVENT")) {
			_taskType = new SimpleObjectProperty<Type>(Type.EVENT);
		} else if (type.equals("DEADLINE")) {
			_taskType = new SimpleObjectProperty<Type>(Type.DEADLINE);
		} else if (type.equals("FLOATNG")) {
			_taskType = new SimpleObjectProperty<Type>(Type.FLOATING);
		} else {
			_taskType = new SimpleObjectProperty<Type>(Type.START);
		}

		String location = taskDetails.get("location");
		_location = new SimpleStringProperty(location);

		LocalDateTime starttime = LocalDateTime.parse(taskDetails.get("startTime"));
		_startTime = new SimpleObjectProperty<LocalDateTime>(starttime);

		LocalDateTime endtime = LocalDateTime.parse(taskDetails.get("endTime"));
		_endTime = new SimpleObjectProperty<LocalDateTime>(endtime);

		LocalDateTime dateadded;
		if (taskDetails.get("dateAdded") == null) {
			dateadded = null;
		} else {
			dateadded = LocalDateTime.parse(taskDetails.get("dateAdded"));
		}
		_dateAdded = new SimpleObjectProperty<LocalDateTime>(dateadded);

		LocalDateTime datemodified;
		if (taskDetails.get("dateModified") == null) {
			datemodified = null;
		} else {
			datemodified = LocalDateTime.parse(taskDetails.get("dateModified"));
		}
		_dateModified = new SimpleObjectProperty<LocalDateTime>(datemodified);

		boolean completed;
		if (taskDetails.get("isCompleted") == null) {
			completed = false;
		} else {
			completed = Boolean.parseBoolean(taskDetails.get("isCompleted"));
		}
		_isCompleted = new SimpleBooleanProperty(completed);

		_isUrgent = Boolean.parseBoolean(taskDetails.get("isUrgent"));
		_isOverdue = Boolean.parseBoolean(taskDetails.get("isOverdue"));

		String tagstring = taskDetails.get("hashTags");
		String[] tagstrray = tagstring.split(",");
		ArrayList<String> tagsArray = new ArrayList<String>(Arrays.asList(tagstrray));
		_hashtags = new SimpleObjectProperty<ArrayList<String>>(tagsArray);
	}
	*/

	private void setType(LocalDateTime start, LocalDateTime end) {
		if (start == null && end == null) {
			_taskType = Type.FLOATING;
		} else if (start != null && end == null) {
			_taskType = Type.START;
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

	public boolean isUrgent() {
		return _isUrgent;
	}

	public boolean isOverdue() {
		return _isOverdue;
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

	public void setIsUrgent(boolean isUrgent) {
		_isUrgent = isUrgent;
	}

	public void setIsOverdue(boolean isOverdue) {
		_isOverdue = isOverdue;
	}

}
