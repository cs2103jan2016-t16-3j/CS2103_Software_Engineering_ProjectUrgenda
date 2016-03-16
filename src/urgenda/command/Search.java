package urgenda.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class Search extends Command {

	private static final String MESSAGE_SEARCH = "These are all the tasks found containing \"%1$s\"";
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

	public String execute() { // TODO: Further refactoring,exception handling and considering searching for task type
		LogicData data = LogicData.getInstance();
		ArrayList<Task> matches;
		String feedback = null;
		
	    if (_searchDesc != null) {
			matches = data.findMatchingDesc(_searchDesc);
			if (matches.isEmpty()) {
				data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchDesc);
			} else {
				data.setDisplays(matches);
				data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
				feedback = String.format(MESSAGE_SEARCH, _searchDesc);
			}
		} else if (_searchDate != null) {
			matches = data.findMatchingDates(_searchDate);
			if (matches.isEmpty()) {
				data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchDate.toString());
			} else {
				data.setDisplays(matches);
				data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
				feedback = String.format(MESSAGE_SEARCH, _searchDate.toString());
			}
		} else if (_searchDateTime != null) {
			matches = data.findMatchingDateTimes(_searchDateTime);
			if (matches.isEmpty()) {
				data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchDateTime.toString());
			} else {
				data.setDisplays(matches);
				data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
				feedback = String.format(MESSAGE_SEARCH, _searchDateTime.toString());
			}
		} else if (_searchMonth != null) {
			matches = data.findMatchingMonths(_searchMonth);
			if (matches.isEmpty()) {
				data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				feedback = String.format(MESSAGE_SEARCH_NOT_FOUND, _searchMonth.toString());
			} else {
				data.setDisplays(matches);
				data.setCurrState(LogicData.DisplayState.SHOW_SEARCH);
				feedback = String.format(MESSAGE_SEARCH, _searchMonth.toString());
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
