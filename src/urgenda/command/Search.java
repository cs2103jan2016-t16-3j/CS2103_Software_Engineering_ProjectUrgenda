//@@author A0127358Y
package urgenda.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import urgenda.logic.LogicData;
import urgenda.util.Task;

/**
 * 
 * Search is the command object used for searching of tasks in Urgenda.
 *
 */
public class Search extends Command {

	private static final String MESSAGE_SHOWING = "Showing: ";
	private static final String MESSAGE_PROGRESSIVE_SEARCH = "PROGRESSIVE SEARCH: %1$s based on the current view. Enter home to show all tasks";
	private static final String MESSAGE_SEARCH_DESC = "all task(s) found containing \"%1$s\"";
	private static final String MESSAGE_SEARCH_TYPE = "all task(s) found of type \"%1$s\"";
	private static final String MESSAGE_SEARCH_TIME = "These are all the task(s) falling on \"%1$s\"";
	private static final String MESSAGE_SEARCH_DATETIME = "These are all the task(s) falling on \"%1$s, %2$s\"";
	private static final String MESSAGE_REFINE_SEARCH_TIME = "PROGRESSIVE SEARCH: Showing task(s) that falls on \"%1$s\" based on the current view. Enter home to show all tasks";
	private static final String MESSAGE_REFINE_SEARCH_DATETIME = "PROGRESSIVE SEARCH: Showing task(s) that falls on \"%1$s, %2$s\" based on the current view. Enter home to show all tasks";
	private static final String MESSAGE_SEARCH_NOT_FOUND = "There is no match found for \"%1$s\"";
	private static final String MESSAGE_SEARCH_INT_NOT_FOUND = "There is no match found for task no. %1$s";
	private static final String MESSAGE_SEARCH_INT = "Search Result: Showing detailed info of task no. %1$s";
	private static final String MESSAGE_NEAR_MATCH = "%1$s near match(es)";
	private static final String MESSAGE_SEARCH_NO_EXACT = "No exact match found for \"%1$s\". ";

	private String _searchDesc;
	private LocalDate _searchDate;
	private LocalDateTime _searchDateTime;
	private Month _searchMonth;
	private Integer _searchId;

	/**
	 * Default constructor for creating a new Search object.
	 */
	public Search() {

	}

	/**
	 * Alternative constructor for Search command object with the String desc
	 * for searching.
	 * 
	 * @param String
	 *            input The desc entered by user for searching.
	 */
	public Search(String input) {
		_searchDesc = input;
	}

	/**
	 * Alternative constructor for Search command object with the LocalDate date
	 * for searching.
	 * 
	 * @param LocalDate
	 *            input The date entered by user for searching.
	 */
	public Search(LocalDate input) {
		_searchDate = input;
	}

	/**
	 * Alternative constructor for Search command object with the LocalDateTime
	 * date&time for searching.
	 * 
	 * @param input
	 *            The date and time entered by user for searching.
	 */
	public Search(LocalDateTime input) {
		_searchDateTime = input;
	}

	/**
	 * Alternative constructor for Search command object with the Month month
	 * for searching.
	 * 
	 * @param input
	 *            The month entered by user for searching of tasks falling on
	 *            that month.
	 */
	public Search(Month input) {
		_searchMonth = input;
	}

	/**
	 * Alternative constructor for Search command object with the int id for
	 * searching.
	 * 
	 * @param input
	 *            The position entered by user for searching of a task (fn
	 *            similarly as showmore).
	 */
	public Search(int input) {
		_searchId = Integer.valueOf(input);
	}

	/**
	 * Execute command of Search which searches for tasks in Urgenda that
	 * matches the user specified input.
	 */
	public String execute() {
		LogicData data = LogicData.getInstance();
		ArrayList<Task> matches;
		String feedback = null;
		data.clearShowMoreTasks();

		if (_searchDesc != null) {
			feedback = searchForDesc(data);

		} else if (_searchDate != null) {
			matches = data.findMatchingDates(_searchDate);
			feedback = generateSearchDateFeedback(data, matches);

		} else if (_searchDateTime != null) {
			matches = data.findMatchingDateTimes(_searchDateTime);
			feedback = generateSearchDateTimeFeedback(data, matches);

		} else if (_searchMonth != null) {
			matches = data.findMatchingMonths(_searchMonth);
			feedback = generateSearchMonthFeedback(data, matches);

		} else if (_searchId != null) {
			Task task = data.findMatchingPosition(_searchId);
			feedback = generateSearchIdFeedback(data, task);
		}
		return feedback;
	}

	/*
	 * method for searching for tasks w desc and or tasktype that matches input.
	 */
	private String searchForDesc(LogicData data) {
		ArrayList<Task> matches;
		String feedback;
		// copy of _searchDesc for modification, trimming and caseignore
		// and just in case prevent editing of original _searchDesc.
		String copy = _searchDesc;
		int descCount = 0;
		int typeCount = 0;
		int nearMatchCount = 0;
		switch (copy.toLowerCase().trim()) {
		case "overdue" :
			matches = data.findMatchingDesc(_searchDesc);
			descCount = matches.size();
			typeCount = findTypeOverdue(data, matches, typeCount);
			break;
		case "completed" :
			matches = data.findMatchingDesc(_searchDesc);
			descCount = matches.size();
			typeCount = findTypeCompleted(data, matches, typeCount);
			break;
		case "important" : // Fallthrough
		case "impt" : // Fallthrough
		case "prioritise" :
			matches = data.findMatchingDesc(_searchDesc);
			descCount = matches.size();
			typeCount = findTypePri(data, matches, typeCount);
			break;
		case "twotime" : // Fallthrough
		case "event" :
			matches = data.findMatchingDesc(_searchDesc);
			descCount = matches.size();
			typeCount = findTypeEvent(data, matches, typeCount);
			break;
		case "onetime" : // Fallthrough
		case "duedate" : // Fallthrough
		case "deadline" : // Fallthrough
			matches = data.findMatchingDesc(_searchDesc);
			descCount = matches.size();
			typeCount = findTypeDeadLine(data, matches, typeCount);
			break;
		case "floating" : // Fallthrough
		case "untimed" :
			matches = data.findMatchingDesc(_searchDesc);
			descCount = matches.size();
			typeCount = findTypeFloat(data, matches, typeCount);
			break;
		case "archive" :
			matches = data.getArchives();
			typeCount = matches.size();
			break;
		default :
			matches = data.findRefinedMatchingDesc(_searchDesc);
			descCount = matches.size();
			nearMatchCount = findNearMatch(data, matches, nearMatchCount);
			break;
		}
		feedback = generateSearchDescFeedback(data, matches, descCount, typeCount)
				+ getNearMatchFeedback(nearMatchCount, descCount, typeCount);
		return feedback;
	}

	private String getNearMatchFeedback(int nearMatchCount, int descCount, int typeCount) {
		if (nearMatchCount != 0) {
			if (descCount == 0 && typeCount == 0) {
				return String.format(MESSAGE_SEARCH_NO_EXACT, _searchDesc) + MESSAGE_SHOWING
						+ String.format(MESSAGE_NEAR_MATCH, nearMatchCount);
			} else {
				return " and " + String.format(MESSAGE_NEAR_MATCH, nearMatchCount);
			}
		}
		return "";
	}

	private int findNearMatch(LogicData data, ArrayList<Task> matches, int nearMatchCount) {
		for (Task task : data.getDisplays()) {
			if (StringUtils.getJaroWinklerDistance(_searchDesc, task.getDesc()) >= 0.8
					&& !matches.contains(task)) {
				matches.add(task);
				nearMatchCount++;
			}
		}
		return nearMatchCount;
	}

	private String generateSearchIdFeedback(LogicData data, Task task) {
		String feedback;
		if (task != null) {
			data.toggleShowMoreTasks(task);
			data.setTaskPointer(task);
			data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			feedback = String.format(MESSAGE_SEARCH_INT, _searchId + 1);
		} else {
			data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			feedback = String.format(MESSAGE_SEARCH_INT_NOT_FOUND, _searchId + 1);
		}
		return feedback;
	}

	private String generateSearchMonthFeedback(LogicData data, ArrayList<Task> matches) {
		String feedback;
		if (matches.isEmpty()) {
			data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchMonth.toString());
		} else {
			data.setDisplays(matches);
			if (data.getCurrState().equals(LogicData.DisplayState.ALL_TASKS)) {
				feedback = String.format(MESSAGE_SEARCH_TIME, _searchMonth.toString());
			} else {
				feedback = String.format(MESSAGE_REFINE_SEARCH_TIME, _searchMonth.toString());
			}
			data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
		}
		return feedback;
	}

	private String generateSearchDateTimeFeedback(LogicData data, ArrayList<Task> matches) {
		String feedback;
		if (matches.isEmpty()) {
			data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchDateTime.toLocalDate().toString() + ", "
					+ _searchDateTime.toLocalTime().toString());
		} else {
			data.setDisplays(matches);
			if (data.getCurrState().equals(LogicData.DisplayState.ALL_TASKS)) {
				feedback = String.format(MESSAGE_SEARCH_DATETIME, _searchDateTime.toLocalDate().toString(),
						_searchDateTime.toLocalTime().toString());
			} else {
				feedback = String.format(MESSAGE_REFINE_SEARCH_DATETIME,
						_searchDateTime.toLocalDate().toString(), _searchDateTime.toLocalTime().toString());
			}
			data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
		}
		return feedback;
	}

	private String generateSearchDateFeedback(LogicData data, ArrayList<Task> matches) {
		String feedback;
		if (matches.isEmpty()) {
			data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchDate.toString());
		} else {
			data.setDisplays(matches);
			if (data.getCurrState().equals(LogicData.DisplayState.ALL_TASKS)) {
				feedback = String.format(MESSAGE_SEARCH_TIME, _searchDate.toString());
			} else {
				feedback = String.format(MESSAGE_REFINE_SEARCH_TIME, _searchDate.toString());
			}
			data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
		}
		return feedback;
	}

	private String generateSearchDescFeedback(LogicData data, ArrayList<Task> matches, int descCount,
			int typeCount) {
		String feedback;
		if (matches.isEmpty()) {
			data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchDesc);
		} else {
			data.setDisplays(matches);
			if (data.getCurrState().equals(LogicData.DisplayState.ALL_TASKS)) {
				feedback = getNormSearchFeedback(descCount, typeCount);
			} else {
				feedback = getProgressiveSearchFeedback(descCount, typeCount);
			}
			data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
		}
		return feedback;
	}

	private String getProgressiveSearchFeedback(int descCount, int typeCount) {
		String feedback;
		if (descCount != 0 && typeCount != 0) {
			String substr = String.format(MESSAGE_SEARCH_DESC, _searchDesc) + " and "
					+ String.format(MESSAGE_SEARCH_TYPE, _searchDesc);
			feedback = String.format(MESSAGE_PROGRESSIVE_SEARCH, substr);
		} else if (descCount == 0) {
			feedback = String.format(MESSAGE_PROGRESSIVE_SEARCH,
					String.format(MESSAGE_SEARCH_TYPE, _searchDesc));
		} else {
			feedback = String.format(MESSAGE_PROGRESSIVE_SEARCH,
					String.format(MESSAGE_SEARCH_DESC, _searchDesc));
		}
		return feedback;
	}

	private String getNormSearchFeedback(int descCount, int typeCount) {
		String feedback;
		if (descCount != 0 && typeCount != 0) {
			feedback = MESSAGE_SHOWING + String.format(MESSAGE_SEARCH_DESC, _searchDesc) + " and "
					+ String.format(MESSAGE_SEARCH_TYPE, _searchDesc);
		} else if (descCount == 0 && typeCount != 0) {
			feedback = MESSAGE_SHOWING + String.format(MESSAGE_SEARCH_TYPE, _searchDesc);
		} else if (descCount != 0 && typeCount == 0) {
			feedback = MESSAGE_SHOWING + String.format(MESSAGE_SEARCH_DESC, _searchDesc);
		} else {
			feedback = "";
		}
		return feedback;
	}

	/*
	 * find tasks that are of type floating
	 */
	private int findTypeFloat(LogicData data, ArrayList<Task> matches, int typeCount) {
		for (Task task : data.getDisplays()) {
			if (task.getTaskType().equals(Task.Type.FLOATING)) {
				typeCount++;
			}
			if (task.getTaskType().equals(Task.Type.FLOATING) && !matches.contains(task)) {
				matches.add(task);
			}
		}
		return typeCount;
	}

	/*
	 * find tasks that are of type deadline
	 */
	private int findTypeDeadLine(LogicData data, ArrayList<Task> matches, int typeCount) {
		for (Task task : data.getDisplays()) {
			if (task.getTaskType().equals(Task.Type.DEADLINE)) {
				typeCount++;
			}
			if (task.getTaskType().equals(Task.Type.DEADLINE) && !matches.contains(task)) {
				matches.add(task);
			}
		}
		return typeCount;
	}

	/*
	 * find tasks that are of type event
	 */
	private int findTypeEvent(LogicData data, ArrayList<Task> matches, int typeCount) {
		for (Task task : data.getDisplays()) {
			if (task.getTaskType().equals(Task.Type.EVENT)) {
				typeCount++;
			}
			if (task.getTaskType().equals(Task.Type.EVENT) && !matches.contains(task)) {
				matches.add(task);
			}
		}
		return typeCount;
	}

	/*
	 * find tasks that are marked as important
	 */
	private int findTypePri(LogicData data, ArrayList<Task> matches, int typeCount) {
		for (Task task : data.getDisplays()) {
			if (task.isImportant()) {
				typeCount++; // maintain count of all pri tasks
			}
			// prevent adding duplicate copies which falls under multi category
			// eg. desc impt and isImpt
			if (task.isImportant() && !matches.contains(task)) {
				matches.add(task);
			}
		}
		return typeCount;
	}

	/*
	 * find tasks that are marked as done
	 */
	private int findTypeCompleted(LogicData data, ArrayList<Task> matches, int typeCount) {
		for (Task task : data.getTaskList()) {
			if (task.isCompleted()) {
				typeCount++; // maintain count of all completed tasks
			}
			// prevent adding duplicate copies which falls under multi category
			if (task.isCompleted() && !matches.contains(task)) {
				matches.add(task);
			}
		}
		for (Task task : data.getArchives()) {
			if (task.isCompleted() && !matches.contains(task)) {
				typeCount++;
				matches.add(task);
			}
		}
		return typeCount;
	}

	/*
	 * find tasks that are overdue
	 */
	private int findTypeOverdue(LogicData data, ArrayList<Task> matches, int typeCount) {
		for (Task task : data.getDisplays()) {
			if (task.isOverdue()) {
				// get num of overdue tasks (including those which are both
				// overdue & has desc overdue)
				typeCount++;
			}
			// prevent adding of duplicate copies
			if (task.isOverdue() && !matches.contains(task)) {
				matches.add(task);
			}
		}
		return typeCount;
	}

	/**
	 * Setter for search by desc.
	 * 
	 * @param input
	 *            The string desc used for searching.
	 */
	public void setSearchInput(String input) {
		_searchDesc = input;
	}

	/**
	 * Setter for search by date
	 * 
	 * @param input
	 *            The LocalDate date used for searching.
	 */
	public void setSearchDate(LocalDate input) {
		_searchDate = input;
	}

	/**
	 * Setter for search by datetime
	 * 
	 * @param input
	 *            The LocalDateTime dateandtime used for searching.
	 */
	public void setSearchDateTime(LocalDateTime input) {
		_searchDateTime = input;
	}

	/**
	 * Setter for search by month
	 * 
	 * @param input
	 *            The Month month used for searching.
	 */
	public void setSearchMonth(Month input) {
		_searchMonth = input;
	}

	/**
	 * Setter for search by id
	 * 
	 * @param id
	 *            The int position of task used for searching.
	 */
	public void setSearchId(int id) {
		_searchId = Integer.valueOf(id);
	}

}
