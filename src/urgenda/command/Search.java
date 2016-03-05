package urgenda.command;

import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class Search implements Command {
	
	private static final String MESSAGE_SEARCH = "Search completed. These are all the tasks found containing \"%1$s\"";
	private static final String MESSAGE_SEARCH_NOT_FOUND = "There is no match found for \"%1$s\"";
	
	private String _searchInput;
	
	// default constructor
	public Search() {
		
	}
	
	public Search(String input) {
		_searchInput = input;
	}
	
	@Override
	public String execute(LogicData data) {   //to be edited to search for other combi e.g. time
		ArrayList<Task> matches = data.findMatchingTasks(_searchInput);
		if(matches.isEmpty()) {
			return String.format(MESSAGE_SEARCH_NOT_FOUND, _searchInput);
		} else { // TODO THROW EXCEPTION
			return String.format(MESSAGE_SEARCH, _searchInput);
		}
	}
		
	public void setSearchInput(String input) {
		_searchInput = input;
	}

}
