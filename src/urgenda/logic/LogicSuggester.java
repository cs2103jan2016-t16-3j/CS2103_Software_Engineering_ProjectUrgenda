package urgenda.logic;

import java.util.ArrayList;

import urgenda.util.SuggestCommand;
import urgenda.util.SuggestCommand.Command;
import urgenda.util.SuggestFeedback;

public class LogicSuggester {
	
	private static final String ADD_EVENT = "[desc] [start] to [end] @[location]";
	private static final String ADD_EVENT_MESSAGE = "Adds a task spanning across a time period";
	private static final String ADD_DEADLINE = "[desc] by [deadline] @[location]";
	private static final String ADD_DEADLINE_MESSAGE = "Adds a task with a deadline";
	private static final String ADD_FLOATING = "[desc] @[location]";
	private static final String ADD_FLOATING_MESSAGE = "Adds an untimed task";
	
	private static final String DEL_TASK = "<selected task> | [task no] | [task no]-[task no] | [desc]";
	private static final String DEL_MESSAGE = "Deletes selected task(s) by index or description";
	
	private static final String DONE_TASK = "<selected task> | [task no] | [task no]-[task no] | [desc]";
	private static final String DONE_MESSAGE = "Mark selected task(s) as done";
	
	// TODO feedback for edit
	private static final String EDIT = "<selected task> | [task no] [new desc] ";
	private static final String EDIT_MESSAGE = "Edits description, date(s) and timing(s), and/or location of selected task";
	
	private static final String SEARCH_TASK = "[desc] | [date/day/time] | [task type]";
	private static final String SEARCH_MESSAGE = "Searches for task(s) displayed that matches the given input";
	
	private static final String EXIT = "";
	private static final String EXIT_MESSAGE = "Saves and exits Urgenda";
	
	private static final String SHOWMORE = "<selected task> | [task no] | [task no]-[task no]";
	private static final String SHOWMORE_MESSAGE = "Toggle details of selected task(s)";
	
	private static final String ARCHIVE = "";
	private static final String ARCHIVE_MESSAGE = "Show all tasks marked as completed";
	
	private static final String UNDO = "";
	private static final String UNDO_MESSAGE = "Undo changes done by previous command";
	
	private static final String REDO = "";
	private static final String REDO_MESSAGE = "Redo changes done by previous undo";
	
	private static final String PRI = "<selected task> | [task no] | [task no]-[task no] | [desc]";
	private static final String PRI_MESSAGE = "Toggle the importance for selected task(s)";
	
	private static final String BLOCK = "[desc] at [start] to [end], (multiple) [start] to [end] @[location]";
	private static final String BLOCK_MESSAGE = "Blocks multiple timeslots for single task";
	
	private static final String CONFIRM = "<selected task> | [task no]  [start] to [end]";
	private static final String CONFIRM_MESSAGE = "Confirms the inputted slot for the timing of task selected";
	
	private static final String FIND_FREE = "[start] to [end]";
	private static final String FIND_FREE_MESSAGE = "Finds all available timeslots within given time range";
	
	private static final String HOME = "";
	private static final String HOME_MESSAGE = "Displays default view of all tasks";
	
	private static final String POSTPONE = "<selected task> | [task no] [duration]";
	private static final String POSTPONE_MESSAGE = "Postpones the selected task by given duration";
	
	private static final String HELP = "";
	private static final String HELP_MESSAGE = "Displays the help manual for Urgenda";
	
	private static final String SAVETO = "[path directory]";
	private static final String SAVETO_MESSAGE = "Change the save directory of Urgenda";
	
	
	public SuggestFeedback processSuggestions(SuggestCommand suggCmd) {
		ArrayList<String> suggestions;
		boolean isCommand = false;
		String currCmd = suggCmd.getCurrCmd();
		
		if(suggCmd.getConfirmedCommand() != null && currCmd != null 
				&& !currCmd.isEmpty() && !currCmd.equals("")) {
			// confirmed command filter possible formats
			suggestions = filterCommand(suggCmd.getConfirmedCommand(), suggCmd.isDeadline(), suggCmd.isEvent());
			isCommand = true;
			return new SuggestFeedback(suggestions, currCmd, isCommand);
		} else if (suggCmd.getPossibleCommands() != null && !suggCmd.getPossibleCommands().isEmpty()) {
			// possible commands only give list of possible commands
			suggestions = suggCmd.getPossibleCommands();
			SuggestFeedback feedback = new SuggestFeedback(suggestions, isCommand);
			feedback.setIsSuggestion(true);
			return feedback;
		} else {
			// both empty means add
			suggestions = addCommand(suggCmd.isDeadline(), suggCmd.isEvent());
			return new SuggestFeedback(suggestions, isCommand);
		}		
	}

	private ArrayList<String> filterCommand(Command confirmedCommand, boolean isDeadline, boolean isEvent) {
		switch(confirmedCommand) {
			case ADD :
				return addCommand(isDeadline, isEvent);
			case ARCHIVE :
				return archiveCommand();
			case BLOCK :
				return blockCommand();
			case CONFIRM :
				return confirmCommand();
			case DELETE :
				return deleteCommand();
			case DONE :
				return doneCommand();
			case EDIT :
				return editCommand();
			case EXIT :
				return exitCommand();
			case FIND_FREE :
				return findFreeCommand();
			case HELP :
				return helpCommand();
			case HOME :
				return homeCommand();
			case POSTPONE :
				return postponeCommand();
			case PRIORITISE :
				return prioritiseCommand();
			case REDO :
				return redoCommand();
			case SAVETO :
				return saveCommand();
			case SEARCH :
				return searchCommand();
			case SHOWMORE :
				return showCommand();
			case UNDO :
				return undoCommand();
		}
		return null;
	}
	
	private ArrayList<String> undoCommand() {
		return generateMessage(UNDO, UNDO_MESSAGE);
	}

	private ArrayList<String> showCommand() {
		return generateMessage(SHOWMORE, SHOWMORE_MESSAGE);
	}

	private ArrayList<String> searchCommand() {
		return generateMessage(SEARCH_TASK, SEARCH_MESSAGE);
	}

	private ArrayList<String> saveCommand() {
		return generateMessage(SAVETO, SAVETO_MESSAGE);
	}

	private ArrayList<String> redoCommand() {
		return generateMessage(REDO, REDO_MESSAGE);
	}

	private ArrayList<String> prioritiseCommand() {
		return generateMessage(PRI, PRI_MESSAGE);
	}

	private ArrayList<String> postponeCommand() {
		return generateMessage(POSTPONE, POSTPONE_MESSAGE);
	}

	private ArrayList<String> homeCommand() {
		return generateMessage(HOME, HOME_MESSAGE);
	}

	private ArrayList<String> helpCommand() {
		return generateMessage(HELP, HELP_MESSAGE);
	}

	private ArrayList<String> findFreeCommand() {
		return generateMessage(FIND_FREE, FIND_FREE_MESSAGE);
	}

	private ArrayList<String> exitCommand() {
		return generateMessage(EXIT, EXIT_MESSAGE);
	}

	private ArrayList<String> editCommand() {
		return generateMessage(EDIT, EDIT_MESSAGE);
	}

	private ArrayList<String> archiveCommand() {
		return generateMessage(ARCHIVE, ARCHIVE_MESSAGE);
	}

	private ArrayList<String> confirmCommand() {
		return generateMessage(CONFIRM, CONFIRM_MESSAGE);
	}

	private ArrayList<String> blockCommand() {
		return generateMessage(BLOCK, BLOCK_MESSAGE);
	}

	private ArrayList<String> doneCommand() {
		return generateMessage(DONE_TASK, DONE_MESSAGE);
	}

	private ArrayList<String> deleteCommand() {
		return generateMessage(DEL_TASK, DEL_MESSAGE);
	}

	private ArrayList<String> generateMessage(String parameters, String message) {
		ArrayList<String> suggestions = new ArrayList<String>();
		suggestions.add(parameters);
		suggestions.add(message);
		return suggestions;
	}

	private ArrayList<String> addCommand(boolean isDeadline, boolean isEvent) {
		ArrayList<String> suggestions = new ArrayList<String>();
		if (isDeadline) {
			suggestions.add(ADD_DEADLINE);
			suggestions.add(ADD_DEADLINE_MESSAGE);
		} else if (isEvent) {
			suggestions.add(ADD_EVENT);
			suggestions.add(ADD_EVENT_MESSAGE);
		} else {
			suggestions.add(ADD_FLOATING);
			suggestions.add(ADD_FLOATING_MESSAGE);
		}
		return suggestions;
	}

}
