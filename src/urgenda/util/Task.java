package urgenda.util;

import java.time.LocalDateTime;

public class Task {
	
	protected int _id;
	protected String _description;
	protected String _location;
	protected LocalDateTime _startTime;
	protected LocalDateTime _endTime;
	protected String[] _hashtags;
	protected LocalDateTime _dateAdded;
	protected LocalDateTime _dateModified;
	protected boolean _isCompleted = false;
	protected boolean _isUrgent = false;
	protected boolean _isOverdue = false;
	
	/**
	 * Constructor for Task objects which requires string array of details
	 * 
	 * @param argument is "" if empty
	 * taskDetails[0] id
	 * taskDetails[1] description
	 * taskDetails[2] location
	 * taskDetails[3] date (start)
	 * taskDetails[4] month (start)
	 * taskDetails[5] year (start)
	 * taskDetails[6] hour (start) (24hrs)
	 * taskDetails[7] minutes (start)	 
	 * taskDetails[8] date (end)
	 * taskDetails[9] month (end)
	 * taskDetails[10] year (end)
	 * taskDetails[11] hour (end) (24hrs)
	 * taskDetails[12] minutes (end)
	 * taskDetails[13...*] hashtag/s
	 */
	public Task (String[] taskDetails) {
		_id = Integer.parseInt(taskDetails[0]);
		_description = taskDetails[1];
		_location = taskDetails[2];
		int hashLength = 0;
		for (int i = 13; i < taskDetails.length; i++) {
			_hashtags[hashLength] = new String(taskDetails[i]);
		}
	}

	public int get_id() {
		return _id;
	}

	public String get_description() {
		return _description;
	}

	public String get_location() {
		return _location;
	}

	public LocalDateTime get_startTime() {
		return _startTime;
	}

	public LocalDateTime get_endTime() {
		return _endTime;
	}

	public String[] get_hashtags() {
		return _hashtags;
	}

	public LocalDateTime get_dateAdded() {
		return _dateAdded;
	}

	public LocalDateTime get_dateModified() {
		return _dateModified;
	}

	public boolean is_isCompleted() {
		return _isCompleted;
	}

	public boolean is_isUrgent() {
		return _isUrgent;
	}

	public boolean is_isOverdue() {
		return _isOverdue;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public void set_description(String _description) {
		this._description = _description;
	}

	public void set_location(String _location) {
		this._location = _location;
	}

	public void set_startTime(LocalDateTime _startTime) {
		this._startTime = _startTime;
	}

	public void set_endTime(LocalDateTime _endTime) {
		this._endTime = _endTime;
	}

	public void set_hashtags(String[] _hashtags) {
		this._hashtags = _hashtags;
	}

	public void set_dateAdded(LocalDateTime _dateAdded) {
		this._dateAdded = _dateAdded;
	}

	public void set_dateModified(LocalDateTime _dateModified) {
		this._dateModified = _dateModified;
	}

	public void set_isCompleted(boolean _isCompleted) {
		this._isCompleted = _isCompleted;
	}

	public void set_isUrgent(boolean _isUrgent) {
		this._isUrgent = _isUrgent;
	}

	public void set_isOverdue(boolean _isOverdue) {
		this._isOverdue = _isOverdue;
	}
	
}
