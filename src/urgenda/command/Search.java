package urgenda.command;

import java.util.ArrayList;
import java.util.regex.Pattern;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class Search implements Command {
	
	private static final String MESSAGE_SEARCH = "Search completed. These are all the tasks found containing \"%1$s\"";
	private static final String MESSAGE_SEARCH_NOT_FOUND = "There is no match found for \"%1$s\"";
	
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
			return String.format(MESSAGE_SEARCH_NOT_FOUND, _searchInput);
		} else {
			return String.format(MESSAGE_SEARCH, _searchInput);
		}
	}
		
	public void setSearchInput(String input) {
		_searchInput = input;
	}

}
