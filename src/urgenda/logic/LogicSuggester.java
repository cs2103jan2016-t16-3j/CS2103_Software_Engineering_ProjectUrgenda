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
	
	private static final String DEL_TASK = "<selected task> | [task no] | [task no]-[task no] | [desc]";
	private static final String DEL_MESSAGE = "Deletes the task selected by index or description";
	
	
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
			case FIND_FERE :
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
	
}
