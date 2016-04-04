package urgenda.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class Search extends Command {

	private static final String MESSAGE_SHOWING = "Showing: ";
	private static final String MESSAGE_PROGRESSIVE_SEARCH = "PROGRESSIVE SEARCH: %1$s  based on the current view. Enter home to show all tasks";
	private static final String MESSAGE_SEARCH_DESC = "all task(s) found containing \"%1$s\"";
	private static final String MESSAGE_SEARCH_TYPE = "all task(s) found of type \"%1$s\"";
	private static final String MESSAGE_SEARCH_TIME = "These are all the task(s) falling on \"%1$s\"";
	private static final String MESSAGE_REFINE_SEARCH_TIME = "PROGRESSIVE SEARCH: Showing task(s) that falls on \"%1$s\" based on the current view. Enter home to show all tasks";
	private static final String MESSAGE_SEARCH_NOT_FOUND = "There is no match found for \"%1$s\"";
	private static final String MESSAGE_SEARCH_INT = "Showing detailed info of task no. %1$s";

	private String _searchDesc;
	private LocalDate _searchDate;
	private LocalDateTime _searchDateTime;
	private Month _searchMonth;
	private Integer _searchId;

	// default constructor
	public Search() {

	}

	public Search(String input) {
		_searchDesc = input;
	}

	public Search(LocalDate input) {
		_searchDate = input;
	}

	public Search(LocalDateTime input) {
		_searchDateTime = input;
	}

	public Search(Month input) {
		_searchMonth = input;
	}

	// TODO: Further refactoring
	public String execute() {
		LogicData data = LogicData.getInstance();
		ArrayList<Task> matches;
		String feedback = null;
		data.clearShowMoreTasks();

		if (_searchDesc != null) {
			String copy = _searchDesc; // copy of _searchDesc for modification,
										// trimming and caseignore
			int descCount = 0;
			int typeCount = 0;
			switch (copy.toLowerCase().trim()) {
			case "overdue":
				matches = data.findMatchingDesc(_searchDesc);
				descCount = matches.size();
				for (Task task : data.getDisplays()) {
					if (task.isOverdue()) {
						typeCount++; // get num of overdue tasks (including those which are both overdue & has desc overdue)
					}
					if (task.isOverdue() && !matches.contains(task)) { // prevent duplicate copies
						matches.add(task);
					}
				}
				break;
			case "completed":
				matches = data.findMatchingDesc(_searchDesc);
				descCount = matches.size();
				for (Task task : data.getTaskList()) {
					if (task.isCompleted()) {
						typeCount++;
					}
					if (task.isCompleted() && !matches.contains(task)) {
						matches.add(task);
					}
				}
				for (Task task : data.getArchives()) {
					if (task.isCompleted() && !matches.contains(task)) {
						typeCount++; // just typeCount++ as mutually exclusive
										// event w above two
						matches.add(task);
					}
				}
				break;
			case "important": // Fallthrough
			case "impt": // Fallthrough
			case "prioritise":
				matches = data.findMatchingDesc(_searchDesc);
				descCount = matches.size();
				for (Task task : data.getDisplays()) {
					if (task.isImportant()) {
						typeCount++;
					}
					if (task.isImportant() && !matches.contains(task)) {
						matches.add(task);
					}
				}
				break;
			case "twotime": // Fallthrough
			case "event":
				matches = data.findMatchingDesc(_searchDesc);
				descCount = matches.size();
				for (Task task : data.getDisplays()) {
					if (task.getTaskType().equals(Task.Type.EVENT)) {
						typeCount++;
					}
					if (task.getTaskType().equals(Task.Type.EVENT) && !matches.contains(task)) {
						matches.add(task);
					}
				}
				break;
			case "onetime": // Fallthrough
			case "duedate": // Fallthrough
			case "deadline": // Fallthrough
				matches = data.findMatchingDesc(_searchDesc);
				descCount = matches.size();
				for (Task task : data.getDisplays()) {
					if (task.getTaskType().equals(Task.Type.DEADLINE)) {
						typeCount++;
					}
					if (task.getTaskType().equals(Task.Type.DEADLINE) && !matches.contains(task)) {
						matches.add(task);
					}
				}
				break;
			case "floating": // Fallthrough
			case "untimed":
				matches = data.findMatchingDesc(_searchDesc);
				descCount = matches.size();
				for (Task task : data.getDisplays()) {
					if (task.getTaskType().equals(Task.Type.FLOATING)) {
						typeCount++;
					}
					if (task.getTaskType().equals(Task.Type.FLOATING) && !matches.contains(task)) {
						matches.add(task);
					}
				}
				break;
			case "archive":
				matches = data.getArchives();
				typeCount = matches.size();
				break;
			default:
				matches = data.findRefinedMatchingDesc(_searchDesc);
				descCount = matches.size();
				break;
			}
			if (matches.isEmpty())

			{
				data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchDesc);
			} else

			{
				data.setDisplays(matches);
				if (data.getCurrState().equals(LogicData.DisplayState.ALL_TASKS)) {
					if (descCount != 0 && typeCount != 0) {
						feedback = MESSAGE_SHOWING + String.format(MESSAGE_SEARCH_DESC, _searchDesc) + " and "
								+ String.format(MESSAGE_SEARCH_TYPE, _searchDesc);
					} else if (descCount == 0) {
						feedback = MESSAGE_SHOWING + String.format(MESSAGE_SEARCH_TYPE, _searchDesc);
					} else {
						feedback = MESSAGE_SHOWING + String.format(MESSAGE_SEARCH_DESC, _searchDesc);
					}
				} else {
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
				}
				data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
			}

		} else if (_searchDate != null)

		{
			matches = data.findMatchingDates(_searchDate);
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
		} else if (_searchDateTime != null)

		{
			matches = data.findMatchingDateTimes(_searchDateTime);
			if (matches.isEmpty()) {
				data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchDateTime.toString());
			} else {
				data.setDisplays(matches);
				if (data.getCurrState().equals(LogicData.DisplayState.ALL_TASKS)) {
					feedback = String.format(MESSAGE_SEARCH_TIME, _searchDateTime.toString());
				} else {
					feedback = String.format(MESSAGE_REFINE_SEARCH_TIME, _searchDateTime.toString());
				}
				data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
			}
		} else if (_searchMonth != null) {
			matches = data.findMatchingMonths(_searchMonth);
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
		} else if (_searchId != null) {
			Task task = data.findMatchingPosition(_searchId);
			if(task != null) {
			data.toggleShowMoreTasks(task);
			data.setTaskPointer(task);
			data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			feedback = String.format(MESSAGE_SEARCH_INT, _searchId);
			} else {
				data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchId);
			}
			
		}
		return feedback;

	}

	public void setSearchInput(String input) {
		_searchDesc = input;
	}

	public void setSearchDate(LocalDate input) {
		_searchDate = input;
	}

	public void setSearchDateTime(LocalDateTime input) {
		_searchDateTime = input;
	}

	public void setSearchMonth(Month input) {
		_searchMonth = input;
	}
	
	public void setSearchId (int id) {
		_searchId = Integer.valueOf(id);
	}

}
