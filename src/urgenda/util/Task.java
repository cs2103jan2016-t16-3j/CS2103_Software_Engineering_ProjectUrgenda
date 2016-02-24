package urgenda.util;

import java.time.LocalDateTime;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
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
	private StringProperty[] _hashtags;
	private ObjectProperty<LocalDateTime> _dateAdded;
	private ObjectProperty<LocalDateTime> _dateModified;
	private BooleanProperty _isCompleted;
	private boolean _isUrgent = false;
	private boolean _isOverdue = false;
	
	// default constructor
	public Task() {
		
	}

	// constructor for inclusion of details
	public Task(String name, String location, LocalDateTime start, LocalDateTime end, boolean isUrgent) {
		//TODO: set default id
		_desc = new SimpleStringProperty(name);
		_location = new SimpleStringProperty(location);
		_startTime = new SimpleObjectProperty<LocalDateTime>(start);
		_endTime = new SimpleObjectProperty<LocalDateTime>(end);
		//TODO: set hashtags
		//TODO: set dates added and modified
		_isUrgent = isUrgent;
		setType(start, end);
		
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
	
	private void setType(LocalDateTime start, LocalDateTime end) {
		if(start == null && end == null) {
			_taskType = new SimpleObjectProperty<Type>(Type.FLOATING);
		} else if (start != null && end == null){
			_taskType = new SimpleObjectProperty<Type>(Type.START);
		} else if (start == null && end != null){
			_taskType = new SimpleObjectProperty<Type>(Type.DEADLINE);
		} else if (start != null && end != null){
			_taskType = new SimpleObjectProperty<Type>(Type.EVENT);
		}	
	}

	public int getId() {
		return _id;
	}

	public StringProperty getDateString() {
		//TODO: replace below with method to extract date from localdatetime
		StringProperty string = new SimpleStringProperty(_startTime.toString());
		return string;
	}
	
	public StringProperty getStartTimeString() {
		//TODO: replace below with method to extract starttime from localdatetime
		StringProperty string = new SimpleStringProperty(_startTime.toString());
		return string;
	}

	public StringProperty getEndTimeString() {
		//TODO: replace below with method to extract endtime from localdatetime
		StringProperty string = new SimpleStringProperty(_endTime.toString());
		return string;
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

	//TODO: change to objectproperty of type string array
	public StringProperty[] getHashtags() {
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

	public void setHashtags(StringProperty[] hashtags) {
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
