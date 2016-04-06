//@@author jo
package urgenda.util;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Task {

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
	public Task(String desc, String location, LocalDateTime start, LocalDateTime end,
			boolean isImportant) {
		_desc = desc;
		_location = location;
		updateTaskType(start, end);
		_startTime = start;
		_endTime = end;
		_dateAdded = LocalDateTime.now();
		_dateModified = LocalDateTime.now();
		_isImportant = isImportant;
	}

	// constructor for inclusion of details without priority
	public Task(String desc, String location, LocalDateTime start, LocalDateTime end) {
		_desc = desc;
		_location = location;
		updateTaskType(start, end);
		_startTime = start;
		_endTime = end;
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
			LocalDateTime dateModified, MultipleSlot slot) {
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
		_slot = slot;

	}

	private void setType(String type) {
		if (type.equalsIgnoreCase(TASK_TYPE_EVENT)) {
			_taskType = Type.EVENT;
		} else if (type.equalsIgnoreCase(TASK_TYPE_DEADLINE)) {
			_taskType = Type.DEADLINE;
		} else if (type.equalsIgnoreCase(TASK_TYPE_FLOATING)) {
			_taskType = Type.FLOATING;
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
	
	public void updateTaskType() {
		if (_startTime == null && _endTime == null) {
			_taskType = Type.FLOATING;
		} else if (_startTime == null && _endTime != null) {
			_taskType = Type.DEADLINE;
		} else if (_startTime != null && _endTime != null) {
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

	// assumes that the task given is not a multipleslot type task
	public boolean isOverlapping(Task task) {
		// defensive code to prevent checking of non event types
		if (task.getTaskType() != Task.Type.EVENT || this.getTaskType() != Task.Type.EVENT) {
			return false;
		}
		LocalDateTime start = task.getStartTime();
		LocalDateTime end = task.getEndTime();
		
		// checks overlaps if there are multiple slots
		if (this.getSlot() != null) {
			ArrayList<DateTimePair> slots = this.getSlot().getSlots();
			for (DateTimePair pair : slots) {
				if (hasOverlap(start, end, pair.getEarlierDateTime(), pair.getLaterDateTime())) {
					return true;
				}
			}
		}
		// checks for the initial timings in task
		return hasOverlap(start, end, _startTime, _endTime);
	}

	public boolean hasOverlap(LocalDateTime start, LocalDateTime end, LocalDateTime compareStart, LocalDateTime compareEnd) {
		// TODO check if all cases are covered
		if (end.isAfter(compareStart) && (end.isBefore(compareEnd) || end.isEqual(compareEnd))) {
			return true;
		} else if ((start.isAfter(compareStart) || start.isEqual(compareStart)) && start.isBefore(compareEnd)) {
			return true;
		} else if (start.isBefore(compareStart) && end.isAfter(compareEnd)) {
			return true;
		} else if (start.isEqual(compareStart) && end.isEqual(compareEnd)) {
			return true;
		} else {
			return false;
		}
	}

}
