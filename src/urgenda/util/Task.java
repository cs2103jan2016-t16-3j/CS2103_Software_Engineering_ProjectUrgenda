package urgenda.util;

import java.time.LocalDateTime;

public class Task {
	
	public enum Type {
		EVENT, DEADLINE, FLOATING
	}

	private int _id;
	private String _description;
	private Type _taskType;
	private String _location;
	private LocalDateTime _startTime;
	private LocalDateTime _endTime;
	private String[] _hashtags;
	private LocalDateTime _dateAdded;
	private LocalDateTime _dateModified;
	private boolean _isCompleted = false;
	private boolean _isUrgent = false;
	private boolean _isOverdue = false;

	// default constructor
	public Task() {

	}
	
	// constructor for copying task objects
	public Task(Task originalTask) {
		setId(originalTask.getId());
		setDescription(originalTask.getDescription());
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

	public int getId() {
		return _id;
	}

	public String getDescription() {
		return _description;
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

	public String[] getHashtags() {
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

	public void setDescription(String description) {
		_description = description;
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

	public void setHashtags(String[] hashtags) {
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
