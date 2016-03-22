package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;

import urgenda.logic.LogicData;
import urgenda.util.DateTimePair;
import urgenda.util.Task;
import urgenda.util.Task.Type;

public class FindFree extends Command {

	private static final String MESSAGE_SEARCH = "These are the range of free time found between \"%1$s\" and \"%2$s\"";
	private static final String MESSAGE_SEARCH_NOT_FOUND = "There is no match found for free time between \"%1$s\" and \"%2$s\"";

	private LocalDateTime _startOfRange;
	private LocalDateTime _endOfRange;

	public FindFree() {

	}

	public FindFree(LocalDateTime start, LocalDateTime end) {
		_startOfRange = start;
		_endOfRange = end;
	}

	public String execute() {
		LogicData data = LogicData.getInstance();
		ArrayList<Task> matches = new ArrayList<Task>(); //arraylist for storing of tasks that contains searched range
		ArrayList<Task> free = new ArrayList<Task>(); //arraylist to be returned to gui for display
		Deque<DateTimePair> _temp; // feel free to use other struct and delete this
		String feedback = null;
		for (Task task : data.getDisplays()) {
			if(task.getTaskType().equals(Type.EVENT) && isOverlapping(task)){
				matches.add(task);
			}
		}
		Collections.sort(matches, comparator);
		// TODO: to be edited for getting range of free time
		return null;
	}

	public void setStartOfRange(LocalDateTime start) {
		_startOfRange = start;
	}

	public void setEndOfRange(LocalDateTime end) {
		_endOfRange = end;
	}

	public boolean isOverlapping(Task task) {
		if (isBeforeOrEqual(task.getStartTime(), _startOfRange) && isAfterOrEqual(task.getEndTime(), _endOfRange)) {
			return true;
		} else if (task.getEndTime().isAfter(_startOfRange) && isBeforeOrEqual(task.getEndTime(), _endOfRange)) {
			return true;
		} else if (isAfterOrEqual(task.getStartTime(), _startOfRange) && task.getStartTime().isBefore(_endOfRange)) {
			return true;
		} else {
			return false;
		}
	}

	// a comparator to compare time return true if first timing is before or equals to second timing
	public boolean isBeforeOrEqual(LocalDateTime first, LocalDateTime second) {
		return first.isBefore(second) || first.isEqual(second);
	}

	// a comparator to compare time return true if first timing is after or equals to second timing
	public boolean isAfterOrEqual(LocalDateTime first, LocalDateTime second) {
		return first.isAfter(second) || first.isEqual(second);
	}
	
	//TODO: to be edited if the list for sorting consists of new structure and not tasks
	static Comparator<Task> comparator = new Comparator<Task>() {
		public int compare(final Task o1, final Task o2) {
			return o1.getStartTime().compareTo(o2.getStartTime());
		}
	};

}
