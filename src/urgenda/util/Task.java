//@@author A0127358Y
package urgenda.util;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Task class of Utils component in Urgenda. Used for storing of information
 * related to a task e.g. timings, location, desc etc.
 *
 */
public class Task {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	private static final String TASK_TYPE_FLOATING = "FLOATING";
	private static final String TASK_TYPE_EVENT = "EVENT";
	private static final String TASK_TYPE_DEADLINE = "DEADLINE";

	/**
	 * 
	 * Different categorisation of tasks.
	 *
	 */
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

	/**
	 * Default constructor for creating a Task object.
	 */
	public Task() {

	}

	/**
	 * Alternative constructor for Task object with String desc specified
	 * 
	 * @param desc
	 *            The String desc of the Task.
	 */
	public Task(String desc) {
		_desc = desc;
		updateTaskType(null, null);
	}

	/**
	 * Alternative constructor for Task object with inclusion of basic details
	 * and priority status.
	 * 
	 * @param desc
	 *            The String desc of the Task.
	 * @param location
	 *            The String location of the Task.
	 * @param start
	 *            The LocalDateTime starting date and time of the Task.
	 * @param end
	 *            The LocalDateTime ending date and time of the Task.
	 * @param isImportant
	 *            The boolean priority status of the Task.
	 */
	public Task(String desc, String location, LocalDateTime start, LocalDateTime end, boolean isImportant) {
		_desc = desc;
		_location = location;
		updateTaskType(start, end);
		_startTime = start;
		_endTime = end;
		_dateAdded = LocalDateTime.now();
		_dateModified = LocalDateTime.now();
		_isImportant = isImportant;
	}

	/**
	 * Alternative constructor for Task object with inclusion of basic details
	 * w/o priority status.
	 * 
	 * @param desc
	 *            The String desc of the Task.
	 * @param location
	 *            The String location of the Task.
	 * @param start
	 *            The LocalDateTime starting date and time of the Task.
	 * @param end
	 *            The LocalDateTime ending date and time of the Task.
	 */
	public Task(String desc, String location, LocalDateTime start, LocalDateTime end) {
		_desc = desc;
		_location = location;
		updateTaskType(start, end);
		_startTime = start;
		_endTime = end;
		_dateAdded = LocalDateTime.now();
		_dateModified = LocalDateTime.now();
	}

	/**
	 * A constructor for creating duplicate copy of a task.
	 * 
	 * @param originalTask
	 *            The Task object to be duplicated.
	 */
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

	/**
	 * This method is used for updating the type of a task based on the start
	 * and end time provided.
	 * 
	 * @param start
	 *            LocalDateTime starting date and time of the task.
	 * @param end
	 *            LocalDateTime ending date and time of the task.
	 */
	public void updateTaskType(LocalDateTime start, LocalDateTime end) {
		if (start == null && end == null) {
			_taskType = Type.FLOATING;
		} else if (start == null && end != null) {
			_taskType = Type.DEADLINE;
		} else if (start != null && end != null) {
			_taskType = Type.EVENT;
		}
	}

	/**
	 * This method is for updating the type of a task based on its own internal
	 * start and end time attributes.
	 */
	public void updateTaskType() {
		if (_startTime == null && _endTime == null) {
			_taskType = Type.FLOATING;
		} else if (_startTime == null && _endTime != null) {
			_taskType = Type.DEADLINE;
		} else if (_startTime != null && _endTime != null) {
			_taskType = Type.EVENT;
		}
	}

	/**
	 * Getter for the id of a task.
	 * 
	 * @return _id the int private attribute of a task object.
	 */
	public int getId() {
		return _id;
	}

	/**
	 * Getter for the desc of a task.
	 * 
	 * @return _desc the String private attribute of a task object.
	 */
	public String getDesc() {
		return _desc;
	}

	/**
	 * Getter for the tasktype of a task.
	 * 
	 * @return _taskType the private enum private attribute of a task object.
	 */
	public Type getTaskType() {
		return _taskType;
	}

	/**
	 * Getter for the location of a task.
	 * 
	 * @return _location the String private attribute of a task object.
	 */
	public String getLocation() {
		return _location;
	}

	/**
	 * Getter for the start datetime of a task object.
	 * 
	 * @return _startTime the LocalDateTime private attribute of a task object.
	 */
	public LocalDateTime getStartTime() {
		return _startTime;
	}

	/**
	 * Getter for the end datetime of a task object.
	 * 
	 * @return _endTime the LocalDateTime private attribute of a task object.
	 */
	public LocalDateTime getEndTime() {
		return _endTime;
	}

	/**
	 * Getter for the datetime at which a task object was added.
	 * 
	 * @return _dateAdded the LocalDateTime private attribute of a task object.
	 */
	public LocalDateTime getDateAdded() {
		return _dateAdded;
	}

	/**
	 * Getter for the datetime at which a task object has been last edited.
	 * 
	 * @return _dateModified the LocalDateTime private attribute of a task
	 *         object.
	 */
	public LocalDateTime getDateModified() {
		return _dateModified;
	}

	/**
	 * Getter for the complete status of a task object.
	 * 
	 * @return true if task has been set as completed, false otherwise.
	 */
	public boolean isCompleted() {
		return _isCompleted;
	}

	/**
	 * Getter for the priority status of a task object.
	 * 
	 * @return true if task has been marked as important, false otherwise.
	 */
	public boolean isImportant() {
		return _isImportant;
	}

	/**
	 * Getter for the overdue status of a task object.
	 * 
	 * @return true if task has been set as overdue, false otherwise.
	 */
	public boolean isOverdue() {
		return _isOverdue;
	}

	/**
	 * Getter for the MultipleSlot (multiple timings for the same task)
	 * attribute of a task.
	 * 
	 * @return _slot The private MultpleSlot attribute of a task object.
	 */
	public MultipleSlot getSlot() {
		return _slot;
	}

	/**
	 * Setter for the id of a task.
	 * 
	 * @param id
	 *            the int id to be set to a task.
	 */
	public void setId(int id) {
		_id = id;
	}

	/**
	 * Setter for the description of a task.
	 * 
	 * @param desc
	 *            the String to be set to a task's desc attribute.
	 */
	public void setDesc(String desc) {
		_desc = desc;
	}

	/**
	 * Setter for the taskType of a task
	 * 
	 * @param taskType
	 *            the Type enum to be set to a task.
	 */
	public void setTaskType(Type taskType) {
		_taskType = taskType;
	}

	/**
	 * Setter for the Location of a task.
	 * 
	 * @param location
	 *            the String to be set to a task's location attribute.
	 */
	public void setLocation(String location) {
		_location = location;
	}

	/**
	 * Setter for the start datetime of a task.
	 * 
	 * @param startTime
	 *            the LocalDateTime to be set as a task's start time.
	 */
	public void setStartTime(LocalDateTime startTime) {
		_startTime = startTime;
	}

	/**
	 * Setter for the start datetime of a task.
	 * 
	 * @param endTime
	 *            the LocalDateTime to be set as a task's end time.
	 */
	public void setEndTime(LocalDateTime endTime) {
		_endTime = endTime;
	}

	/**
	 * Setter for the datetime at which a task was added.
	 * 
	 * @param dateAdded
	 *            the LocalDateTime to be set to the dateadded of a task's .
	 */
	public void setDateAdded(LocalDateTime dateAdded) {
		_dateAdded = dateAdded;
	}

	/**
	 * Setter for the datetime at which a task was last edited.
	 * 
	 * @param dateModified
	 *            the LocalDateTime to be set to the datemodified of a task's
	 */
	public void setDateModified(LocalDateTime dateModified) {
		_dateModified = dateModified;
	}

	/**
	 * Setter for the complete status of a task.
	 * 
	 * @param isCompleted
	 *            the boolean to be set as the complete status of a task.
	 */
	public void setIsCompleted(boolean isCompleted) {
		_isCompleted = isCompleted;
	}

	/**
	 * Setter for the priority status of a task.
	 * 
	 * @param isImportant
	 *            the boolean to be set as the priority status of a task
	 */
	public void setIsImportant(boolean isImportant) {
		_isImportant = isImportant;
	}

	/**
	 * Setter for the overdue status of a task.
	 * 
	 * @param isOverdue
	 *            the boolean to be set as the overdue status of a task
	 */
	public void setIsOverdue(boolean isOverdue) {
		_isOverdue = isOverdue;
	}

	/**
	 * Setter for the Multiple timeslot of a task.
	 * 
	 * @param slot
	 *            the MultipleSlot object to be set as the multi time slot of a
	 *            task.
	 */
	public void setSlot(MultipleSlot slot) {
		_slot = slot;
	}

	/**
	 * This method is used for toggling the priority status of a task. if the
	 * task is originally set as important then set it to unmarked as
	 * non-important, vice versa
	 */
	public void toggleImportant() {
		if (_isImportant) {
			_isImportant = false;
		} else {
			_isImportant = true;
		}
	}

	/**
	 * This method is for checking whether a given task overlaps with other task
	 * within a list.
	 * 
	 * @param task
	 *            the task object to be checked and compared.
	 * @return true if the given task overlaps with others, false otherwise.
	 */
	public boolean isOverlapping(Task task) {
		// defensive code to prevent checking of non event types
		if (task.getTaskType() != Task.Type.EVENT || this.getTaskType() != Task.Type.EVENT) {
			return false;
		}
		LocalDateTime start = task.getStartTime();
		LocalDateTime end = task.getEndTime();
		// checks overlaps if there are multiple slots
		if (this.getSlot() != null) {
			logger.getLogger().info("checking for overlap within multipleslot");
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

	/**
	 * This method is used for checking whether two given sets of timing
	 * overlaps with one another
	 * 
	 * @param start
	 * @param end
	 * @param compareStart
	 * @param compareEnd
	 * @return true if they overlap, false otherwise.
	 */
	public boolean hasOverlap(LocalDateTime start, LocalDateTime end, LocalDateTime compareStart,
			LocalDateTime compareEnd) {
		if (end.isAfter(compareStart) && (end.isBefore(compareEnd) || end.isEqual(compareEnd))) {
			return true;
		} else if ((start.isAfter(compareStart) || start.isEqual(compareStart))
				&& start.isBefore(compareEnd)) {
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
