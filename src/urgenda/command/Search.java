package urgenda.command;

import java.util.ArrayList;
import java.util.regex.Pattern;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class Search implements Command {
	
	private static final String MESSAGE_SEARCH = "Search completed. These are all the tasks found containing searched input.";
	private static final String MESSAGE_SEARCH_NOT_FOUND = "There is no match found for search.";
	
	private String _searchInput;
	private ArrayList<Task> _searchStorage;
	private LogicData _data;
	
	@Override
	public String execute(LogicData data) {
		_data = data;
		_searchStorage = new ArrayList<Task>();
		for (Task counter : _data.getTaskList()) {
			if (Pattern.compile(Pattern.quote(_searchInput), Pattern.CASE_INSENSITIVE).matcher(counter.getDesc()).find()) {
				_searchStorage.add(counter);
			}
		}
		if(_searchStorage.isEmpty()) {
			return MESSAGE_SEARCH_NOT_FOUND;
		} else {
			return MESSAGE_SEARCH;
		}
	}
		
		
	
	public void setSearchInput(String input) {
		_searchInput = input;
	}

	@Override
	public void getDetails(String[] details) {
		// TODO Auto-generated method stub

	}

	@Override
	public String undo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String redo() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
