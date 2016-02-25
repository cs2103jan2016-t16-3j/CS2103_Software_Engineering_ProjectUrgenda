package urgenda.util;

import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TaskWrapper {

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
	private StringProperty _taskDate;
	private ObjectProperty<Type> _taskType;
	private StringProperty _location;
	private StringProperty _startTime;
	private StringProperty _endTime;
	private ObjectProperty<ArrayList<String>> _hashtags;
	private StringProperty _dateAdded;
	private StringProperty _dateModified;
	private BooleanProperty _isCompleted;
	private BooleanProperty _isUrgent;
	private BooleanProperty _isOverdue;
	private ObjectProperty<MultipleSlot> _slot;

	// default constructor
	public TaskWrapper() {

	}

	// for wrapping of original task object
	public TaskWrapper(Task task) {
		_desc = new SimpleStringProperty(task.getDesc());
		_taskDate = new SimpleStringProperty(getDateOnly(task.getStartTime()));
		_location = new SimpleStringProperty(task.getLocation());
		setType(task.getStartTime(), task.getEndTime());
		_startTime = new SimpleStringProperty(task.getStartTime().toString());
		_endTime = new SimpleStringProperty(task.getEndTime().toString());
		_hashtags = new SimpleObjectProperty<ArrayList<String>>(task.getHashtags());
		_dateAdded = new SimpleStringProperty(task.getDateAdded().toString());
		_dateModified = new SimpleStringProperty(task.getDateModified().toString());
		_isCompleted = new SimpleBooleanProperty(task.isCompleted());
		_isUrgent = new SimpleBooleanProperty(task.isUrgent());
		_isOverdue = new SimpleBooleanProperty(task.isOverdue());
		_slot = new SimpleObjectProperty<MultipleSlot>(task.getSlot());
	}

	// TODO: decide if this is redundant (can remove)
	// constructor for inclusion of details
	public TaskWrapper(String name, String location, LocalDateTime start, LocalDateTime end, boolean isUrgent,
			ArrayList<String> tags) {
		_desc = new SimpleStringProperty(name);
		_taskDate = new SimpleStringProperty(getDateOnly(start));
		_location = new SimpleStringProperty(location);
		setType(start, end);
		_startTime = new SimpleStringProperty(start.toString());
		_endTime = new SimpleStringProperty(end.toString());
		_hashtags = new SimpleObjectProperty<ArrayList<String>>(tags);
		_dateAdded = new SimpleStringProperty(LocalDateTime.now().toString());
		_dateModified = new SimpleStringProperty(LocalDateTime.now().toString());
		_isCompleted = new SimpleBooleanProperty(false);
		_isUrgent = new SimpleBooleanProperty(isUrgent);
		_isOverdue = new SimpleBooleanProperty(false);
		_slot = new SimpleObjectProperty<MultipleSlot>();

	}


	// constructor for copying taskwrapper objects
	public TaskWrapper(TaskWrapper originalTask) {
		setId(originalTask.getId());
		setDescription(originalTask.getDesc());
		setTaskDate(originalTask.getTaskDate());
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
		setSlot(originalTask.getSlot());
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

	private String getDateOnly(LocalDateTime start) {
		// TODO to return only date from start time as string
		return null;
	}

	public int getId() {
		return _id;
	}

	public StringProperty getDesc() {
		return _desc;
	}

	public StringProperty getTaskDate() {
		return _taskDate;
	}
	
	public ObjectProperty<Type> getTaskType() {
		return _taskType;
	}

	public StringProperty getLocation() {
		return _location;
	}

	public StringProperty getStartTime() {
		return _startTime;
	}

	public StringProperty getEndTime() {
		return _endTime;
	}

	public ObjectProperty<ArrayList<String>> getHashtags() {
		return _hashtags;
	}

	public StringProperty getDateAdded() {
		return _dateAdded;
	}

	public StringProperty getDateModified() {
		return _dateModified;
	}

	public BooleanProperty isCompleted() {
		return _isCompleted;
	}

	public BooleanProperty isUrgent() {
		return _isUrgent;
	}

	public BooleanProperty isOverdue() {
		return _isOverdue;
	}

	public ObjectProperty<MultipleSlot> getSlot() {
		return _slot;
	}

	public void setId(int id) {
		_id = id;
	}

	public void setDescription(StringProperty stringProperty) {
		_desc = stringProperty;
	}
	
	public void setTaskDate(StringProperty taskDate) {
		_taskDate = taskDate;
	}
	
	public void setTaskType(ObjectProperty<Type> taskType) {
		_taskType = taskType;
	}

	public void setLocation(StringProperty location) {
		_location = location;
	}

	public void setStartTime(StringProperty startTime) {
		_startTime = startTime;
	}

	public void setEndTime(StringProperty endTime) {
		_endTime = endTime;
	}

	public void setHashtags(ObjectProperty<ArrayList<String>> hashtags) {
		_hashtags = hashtags;
	}

	public void setDateAdded(StringProperty dateAdded) {
		_dateAdded = dateAdded;
	}

	public void setDateModified(StringProperty dateModified) {
		_dateModified = dateModified;
	}

	public void setIsCompleted(BooleanProperty isCompleted) {
		_isCompleted = isCompleted;
	}

	public void setIsUrgent(BooleanProperty isUrgent) {
		_isUrgent = isUrgent;
	}

	public void setIsOverdue(BooleanProperty isOverdue) {
		_isOverdue = isOverdue;
	}

	public void setSlot(ObjectProperty<MultipleSlot> slot) {
		_slot = slot;
	}

}
