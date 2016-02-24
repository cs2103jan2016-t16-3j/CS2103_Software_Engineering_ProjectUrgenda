package urgenda.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
	private StringProperty _desc;
	private ObjectProperty<Type> _taskType;
	private StringProperty _location;
	private ObjectProperty<LocalDateTime> _startTime;
	private ObjectProperty<LocalDateTime> _endTime;
	private ObjectProperty<ArrayList<String>> _hashtags;
	private ObjectProperty<LocalDateTime> _dateAdded;
	private ObjectProperty<LocalDateTime> _dateModified;
	private BooleanProperty _isCompleted;
	private boolean _isUrgent = false;
	private boolean _isOverdue = false;

	// default constructor
	public Task() {

	}

	// constructor for inclusion of details
	public Task(String name, String location, LocalDateTime start, LocalDateTime end, boolean isUrgent,
			ArrayList<String> tags) {
		_desc = new SimpleStringProperty(name);
		_location = new SimpleStringProperty(location);
		setType(start, end);
		_startTime = new SimpleObjectProperty<LocalDateTime>(start);
		_endTime = new SimpleObjectProperty<LocalDateTime>(end);
		_hashtags = new SimpleObjectProperty<ArrayList<String>>(tags);
		LocalDateTime dateAdded = LocalDateTime.now();
		_dateAdded = new SimpleObjectProperty<LocalDateTime>(dateAdded);
		// TODO: set dates modified IMPT DO THISSSS
		_isCompleted = new SimpleBooleanProperty(false);
		_isUrgent = isUrgent;
		_isOverdue = false;

	}

	// constructor for copying task objects
	public Task(Task originalTask) {
		setId(originalTask.getId());
		setDescription(originalTask.getDesc());
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

	private void setType(LocalDateTime start, LocalDateTime end) {
		if (start == null && end == null) {
			_taskType = new SimpleObjectProperty<Type>(Type.FLOATING);
		} else if (start != null && end == null) {
			_taskType = new SimpleObjectProperty<Type>(Type.START);
		} else if (start == null && end != null) {
			_taskType = new SimpleObjectProperty<Type>(Type.DEADLINE);
		} else if (start != null && end != null) {
			_taskType = new SimpleObjectProperty<Type>(Type.EVENT);
		}
	}

	public int getId() {
		return _id;
	}

	public StringProperty getDesc() {
		return _desc;
	}

	public ObjectProperty<Type> getTaskType() {
		return _taskType;
	}

	public StringProperty getLocation() {
		return _location;
	}

	public ObjectProperty<LocalDateTime> getStartTime() {
		return _startTime;
	}

	public ObjectProperty<LocalDateTime> getEndTime() {
		return _endTime;
	}

	public ObjectProperty<ArrayList<String>> getHashtags() {
		return _hashtags;
	}

	public ObjectProperty<LocalDateTime> getDateAdded() {
		return _dateAdded;
	}

	public ObjectProperty<LocalDateTime> getDateModified() {
		return _dateModified;
	}

	public BooleanProperty isCompleted() {
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

	public void setDescription(StringProperty stringProperty) {
		_desc = stringProperty;
	}

	public void setTaskType(ObjectProperty<Type> taskType) {
		_taskType = taskType;
	}

	public void setLocation(StringProperty location) {
		_location = location;
	}

	public void setStartTime(ObjectProperty<LocalDateTime> startTime) {
		_startTime = startTime;
	}

	public void setEndTime(ObjectProperty<LocalDateTime> endTime) {
		_endTime = endTime;
	}

	public void setHashtags(ObjectProperty<ArrayList<String>> hashtags) {
		_hashtags = hashtags;
	}

	public void setDateAdded(ObjectProperty<LocalDateTime> dateAdded) {
		_dateAdded = dateAdded;
	}

	public void setDateModified(ObjectProperty<LocalDateTime> dateModified) {
		_dateModified = dateModified;
	}

	public void setIsCompleted(BooleanProperty isCompleted) {
		_isCompleted = isCompleted;
	}

	public void setIsUrgent(boolean isUrgent) {
		_isUrgent = isUrgent;
	}

	public void setIsOverdue(boolean isOverdue) {
		_isOverdue = isOverdue;
	}

}
