package urgenda.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class Search extends Command {

	private static final String MESSAGE_SEARCH_DESC = "These are all the tasks found containing \"%1$s\"";
	private static final String MESSAGE_SEARCH_TIME = "These are all the tasks falling on \"%1$s\"";
	private static final String MESSAGE_SEARCH_NOT_FOUND = "There is no match found for \"%1$s\"";

	private String _searchDesc;
	private LocalDate _searchDate;
	private LocalDateTime _searchDateTime;
	private Month _searchMonth;

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
			if (copy.trim().charAt(0) == '#') {
				String input = copy.replaceFirst("#", "").trim();
				matches = data.findMatchingHashtags(input);
			} else {
				switch (copy.toLowerCase().trim()) {
				case "overdue":
					matches = data.findMatchingDesc(_searchDesc);
					for (Task task : data.getDisplays()) {
						if (task.isOverdue() && !matches.contains(task)) {
							matches.add(task);
						}
					}
					for (Task task : data.getArchives()) {
						if (task.isOverdue() && !matches.contains(task)) {
							matches.add(task);
						}
					}
					break;
				case "completed":
					matches = data.findMatchingDesc(_searchDesc);
					for (Task task : data.getDisplays()) {
						if (task.isCompleted() && !matches.contains(task)) {
							matches.add(task);
						}
					}
					for (Task task : data.getArchives()) {
						if (task.isCompleted() && !matches.contains(task)) {
							matches.add(task);
						}
					}
					break;
				case "important": // Fallthrough
				case "prioritise":
					matches = data.findMatchingDesc(_searchDesc);
					for (Task task : data.getDisplays()) {
						if (task.isImportant() && !matches.contains(task)) {
							matches.add(task);
						}
					}
					break;
				case "event":
					matches = data.findMatchingDesc(_searchDesc);
					for (Task task : data.getDisplays()) {
						if (task.getTaskType().equals(Task.Type.EVENT) && !matches.contains(task)) {
							matches.add(task);
						}
					}
					break;
				case "deadline":
					matches = data.findMatchingDesc(_searchDesc);
					for (Task task : data.getDisplays()) {
						if (task.getTaskType().equals(Task.Type.DEADLINE) && !matches.contains(task)) {
							matches.add(task);
						}
					}
					break;
				case "floating": // Fallthrough
				case "untimed":
					matches = data.findMatchingDesc(_searchDesc);
					for (Task task : data.getDisplays()) {
						if (task.getTaskType().equals(Task.Type.FLOATING) && !matches.contains(task)) {
							matches.add(task);
						}
					}
					break;
				default:
					matches = data.findMatchingDesc(_searchDesc);
					break;
				}
			}
			if (matches.isEmpty()) {
				data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchDesc);
			} else {
				data.setDisplays(matches);
				data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
				feedback = String.format(MESSAGE_SEARCH_DESC, _searchDesc);
			}
		} else if (_searchDate != null) {
			matches = data.findMatchingDates(_searchDate);
			if (matches.isEmpty()) {
				data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchDate.toString());
			} else {
				data.setDisplays(matches);
				data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
				feedback = String.format(MESSAGE_SEARCH_TIME, _searchDate.toString());
			}
		} else if (_searchDateTime != null) {
			matches = data.findMatchingDateTimes(_searchDateTime);
			if (matches.isEmpty()) {
				data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchDateTime.toString());
			} else {
				data.setDisplays(matches);
				data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
				feedback = String.format(MESSAGE_SEARCH_TIME, _searchDateTime.toString());
			}
		} else if (_searchMonth != null) {
			matches = data.findMatchingMonths(_searchMonth);
			if (matches.isEmpty()) {
				data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchMonth.toString());
			} else {
				data.setDisplays(matches);
				data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
				feedback = String.format(MESSAGE_SEARCH_TIME, _searchMonth.toString());
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

}
