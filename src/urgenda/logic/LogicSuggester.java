package urgenda.logic;

import java.util.ArrayList;

import urgenda.util.SuggestCommand;
import urgenda.util.SuggestCommand.Command;
import urgenda.util.SuggestFeedback;

public class LogicSuggester {
	
	private static final String ADD_EVENT = "[desc] [start] to [end] @[location](optional)";
	private static final String ADD_EVENT_MESSAGE = "Adds a task spanning across a time period";
	private static final String ADD_DEADLINE = "[desc] by [deadline] @[location](optional)";
	private static final String ADD_DEADLINE_MESSAGE = "Adds a task with a deadline";
	private static final String ADD_FLOATING = "[desc] @[location](optional)";
	private static final String ADD_FLOATING_MESSAGE = "Adds an untimed task";
	
	private static final String BLOCK_MESSAGE = "Blocks input dates and timings for task selected by index or description";
	
	private static final String DONE_MESSAGE = "Mark selected task(s) as done";
	
	private static final String CONFIRM_MESSAGE = "Confirms a date and timing for a task with multiple dates and timings";
	
	private static final String DEL_TASK = "<selected task> | [task no] | [task no]-[task no] | [desc]";
	private static final String DEL_MESSAGE = "Deletes selected task(s) by index or description";
	
	private static final String EXIT_MESSAGE = "Exits Urgenda. Are you sure you want to exit Urgenda?";
	
	private static final String FIND_FREE_MESSAGE = "Finds all available timeslots within given time range";
	
	private static final String HELP_MESSAGE = "Shows the help window";
	
	private static final String HOME_MESSAGE = "Displays default view of all tasks";
	
	private static final String EDIT_MESSAGE = "Edits description, date(s) and timing(s), and/or location of selected task";
	
	private static final String POSTPONE_MESSAGE = "Postpone selected task by given time period";
	
	private static final String PRI_MESSAGE = "Toggle marking of importance for selected task(s)";
	
	private static final String REDO_MESSAGE = "Redo next most recent action";
	
	private static final String SEARCH_MESSAGE = "Search by description, task type, or keywords"; //TODO
	
	private static final String SAVETO_MESSAGE = "Change the save directory of Urgenda";
	
	private static final String ARCHIVE_MESSAGE = "Show all tasks marked as completed";
	
	private static final String SHOWMORE_MESSAGE = "Toggle details of selected task(s)";
	
	private static final String UNDO_MESSAGE = "Undo previous most recent action";
	
	public SuggestFeedback processSuggestions(SuggestCommand suggCmd) {
		ArrayList<String> suggestions;
		boolean isCommand = false;
		String currCmd = suggCmd.getCurrCmd();
		if(suggCmd.getConfirmedCommand() != null && currCmd != null 
				&& !currCmd.isEmpty() && currCmd.equals("")) {
			// confirmed command filter possible formats
			suggestions = filterCommand(suggCmd.getConfirmedCommand());
			isCommand = true;
			return new SuggestFeedback(suggestions, currCmd, isCommand);
		} else if (suggCmd.getPossibleCommands() != null && !suggCmd.getPossibleCommands().isEmpty()) {
			// possible commands only give list of possible commands
			suggestions = suggCmd.getPossibleCommands();
		} else {
			// is empty means add
			suggestions = specialAddCommand();
		}
		
		SuggestFeedback feedback = new SuggestFeedback(suggestions, isCommand);
		return feedback;
	}

	private ArrayList<String> specialAddCommand() {
		// TODO Auto-generated method stub
		ArrayList<String> suggestions = new ArrayList<String>();
		suggestions.add(ADD_EVENT);
		return null;
	}

	private ArrayList<String> filterCommand(Command confirmedCommand) {
		switch(confirmedCommand) {
			case ADD :
				break;
			case ARCHIVE :
				break;
			case BLOCK :
				break;
			case CONFIRM :
				break;
			case DELETE :
				break;
			case DONE :
				break;
			case EDIT :
				break;
			case EXIT :
				break;
			case FIND_FREE :
				break;
			case HELP :
				break;
			case HOME :
				break;
			case POSTPONE :
				break;
			case PRIORITISE :
				break;
			case REDO :
				break;
			case SAVETO :
				break;
			case SEARCH :
				break;
			case SHOWMORE :
				break;
			case UNDO :
				break;
		}
		return null;
	}
	
	private String filterUserMessage(Command confirmedCommand) {
		switch(confirmedCommand) {
			case ADD :
				break;
			case ARCHIVE :
				return ARCHIVE_MESSAGE;
			case BLOCK :
				return BLOCK_MESSAGE;
			case CONFIRM :
				return CONFIRM_MESSAGE;
			case DELETE :
				return DEL_MESSAGE;
			case DONE :
				return DONE_MESSAGE;
			case EDIT :
				return EDIT_MESSAGE;
			case EXIT :
				return EXIT_MESSAGE;
			case FIND_FREE :
				return FIND_FREE_MESSAGE;
			case HELP :
				return HELP_MESSAGE;
			case HOME :
				return HOME_MESSAGE;
			case POSTPONE :
				return POSTPONE_MESSAGE;
			case PRIORITISE :
				return PRI_MESSAGE;
			case REDO :
				return REDO_MESSAGE;
			case SAVETO :
				return SAVETO_MESSAGE;
			case SEARCH :
				return SEARCH_MESSAGE;
			case SHOWMORE :
				return SHOWMORE_MESSAGE;
			case UNDO :
				return UNDO_MESSAGE;
		}
		return null;
	}
}
