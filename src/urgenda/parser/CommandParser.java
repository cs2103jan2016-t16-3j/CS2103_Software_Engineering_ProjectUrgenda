//@@author A0127764X
package urgenda.parser;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;
import java.util.Calendar;

import urgenda.command.*;
import urgenda.parser.PublicVariables.*;
import urgenda.parser.commandParser.AddCommandParser;
import urgenda.parser.commandParser.BlockSlotsCommandParser;
import urgenda.parser.commandParser.CompleteCommandParser;
import urgenda.parser.commandParser.ConfirmCommandParser;
import urgenda.parser.commandParser.DeleteCommandParser;
import urgenda.parser.commandParser.DemoCommandParser;
import urgenda.parser.commandParser.ExitCommandParser;
import urgenda.parser.commandParser.FindFreeCommandParser;
import urgenda.parser.commandParser.HelpCommandParser;
import urgenda.parser.commandParser.HideCommandParser;
import urgenda.parser.commandParser.HomeCommandParser;
import urgenda.parser.commandParser.InvalidCommandParser;
import urgenda.parser.commandParser.NewEditCommandParser;
import urgenda.parser.commandParser.PostponeCommandParser;
import urgenda.parser.commandParser.PrioritiseCommandParser;
import urgenda.parser.commandParser.RedoCommandParser;
import urgenda.parser.commandParser.SearchCommandParser;
import urgenda.parser.commandParser.SetDirectoryCommandParser;
import urgenda.parser.commandParser.ShowArchiveCommandParser;
import urgenda.parser.commandParser.ShowDetailsCommandParser;
import urgenda.parser.commandParser.UndoCommandParser;
import urgenda.util.*;


public class CommandParser {
	private static String _argsString;

	private static String deadlineKeyWordRegex = "\\s+by(\\s+|\\Z)";
	private static String reservedWordsRegex = "([^\\d+\\s+/-:]+)(\\d+)";


	/**
	 * This is the function to parse the commands given by logic when user hits enter
	 * @param commandString The command string being parsed
	 * @param index The passed in index of the currently selected task, can be used as parameter for certain commands
	 * @return the appropriate command objects with all the parsed attributes
	 */
	public static Command parseCommand(String commandString, int index) {
		PublicFunctions.reinitializePublicVariables();

		PublicVariables.commandType = CommandTypeParser.getCommandType(commandString);
		String argsString = CommandTypeParser.getArgsString(commandString);
		Command testReturn = generateAndReturnCommandObjects(PublicVariables.commandType, argsString, index);

		if (testReturn instanceof Invalid) {
			PublicFunctions.reinitializePublicVariables();
			return generateAddCommandAndReturn(commandString, index);
		} else {
			return testReturn;
		}
	}

	
	/**
	 * This is the function to parse the commands as they were being typed into the command line
	 * in order to give feedback and suggestions to the user
	 * @param commandString the command currently present on the command line when a keystroke is registered
	 * @return the SuggestCommand object that contains relevant information, such as suggested keywords, direction of input
	 */
	public static SuggestCommand parseRuntimeInput(String commandString) {
		if (isCommandStringValid(commandString)) {
			PublicFunctions.reinitializePublicVariables();
			commandString = formatCommandString(commandString);
			COMMAND_TYPE commandType = CommandTypeParser.getCommandType(commandString);
			return generateSuggestCommandAndReturn(commandString, commandType);
		} else {
			return new SuggestCommand(null, null, null);
		}
	}

	private static SuggestCommand generateSuggestCommandAndReturn(String commandString, COMMAND_TYPE commandType) {
		if (isInvalidCommandType(commandType)) {
			return handleReturnOfInvalidCommandType(commandString);
		} else {
			return handleReturnOfValidCommandType(commandString, commandType);
		}
	}

	private static SuggestCommand handleReturnOfInvalidCommandType(String commandString) {
		int numberOfWords = PublicFunctions.getNumberOfWords(commandString);
		if (numberOfWords == 1) {
			return handleReturnOfOneWordString(commandString);
		} else {
			return handleReturnOfMultipleWordString(commandString);
		}
	}

	private static SuggestCommand handleReturnOfValidCommandType(String commandString, COMMAND_TYPE commandType) {
		SuggestCommand.Command command = convertCommandType(commandType);
		SuggestCommand suggestCommand;
		if (command != null) {
			suggestCommand = new SuggestCommand(command, null, PublicFunctions.getFirstWord(commandString));
		} else {
			suggestCommand = new SuggestCommand(null, null, null);
		}
		return checkEventOrDeadlineAndReturn(commandString, suggestCommand);
	}

	private static SuggestCommand handleReturnOfOneWordString(String commandString) {
		SuggestCommand suggestCommand;
		String firstWord = PublicFunctions.getFirstWord(commandString).trim();
		ArrayList<String> possibleCommands = getPossibleCommands(firstWord);
		if (!possibleCommands.isEmpty()) {
			suggestCommand = new SuggestCommand(null, possibleCommands, null);
		} else {
			suggestCommand = new SuggestCommand(null, null, null);
		}
		return checkEventOrDeadlineAndReturn(commandString, suggestCommand);
	}

	private static SuggestCommand handleReturnOfMultipleWordString(String commandString) {
		SuggestCommand suggestCommand = new SuggestCommand(null, null, null);
		return checkEventOrDeadlineAndReturn(commandString, suggestCommand);
	}

	private static SuggestCommand.Command convertCommandType(COMMAND_TYPE commandType) {
		SuggestCommand.Command command = null;
		switch (commandType) {
		case ADD:
			command = SuggestCommand.Command.ADD;
			break;
		case COMPLETE:
			command = SuggestCommand.Command.DONE;
			break;
		case DELETE:
			command = SuggestCommand.Command.DELETE;
			break;
		case EDIT:
			command = SuggestCommand.Command.EDIT;
			break;
		case EXIT:
			command = SuggestCommand.Command.EXIT;
			break;
		case PRIORITISE:
			command = SuggestCommand.Command.PRIORITISE;
			break;
		case REDO:
			command = SuggestCommand.Command.REDO;
			break;
		case SEARCH:
			command = SuggestCommand.Command.SEARCH;
			break;
		case SHOW_ARCHIVE:
			command = SuggestCommand.Command.ARCHIVE;
			break;
		case SHOW_DETAILS:
			command = SuggestCommand.Command.SHOWMORE;
			break;
		case BLOCK:
			command = SuggestCommand.Command.BLOCK;
			break;
		case FIND_FREE:
			command = SuggestCommand.Command.FIND_FREE;
			break;
		case HOME:
			command = SuggestCommand.Command.HOME;
			break;
		case UNDO:
			command = SuggestCommand.Command.UNDO;
			break;
		case POSTPONE:
			command = SuggestCommand.Command.POSTPONE;
			break;
		case CONFIRM:
			command = SuggestCommand.Command.CONFIRM;
			break;
		case SET_DIRECTORY:
			command = SuggestCommand.Command.SAVETO;
			break;
		case HELP:
			command = SuggestCommand.Command.HELP;
			break;
		case DEMO:
			command = SuggestCommand.Command.DEMO;
			break;
		case HIDE:
			command = SuggestCommand.Command.HIDE;
			break;
		default:
			break;
		}
		return command;
	}

	private static ArrayList<String> getPossibleCommands(String commandString) {
		ArrayList<String> returnedArray = new ArrayList<String>();

		Set<Set<String>> commandSet = new HashSet<Set<String>>();
		commandSet.add(PublicVariables.addKeyWords);
		commandSet.add(PublicVariables.deleteKeyWords);
		commandSet.add(PublicVariables.doneKeyWords);
		commandSet.add(PublicVariables.updateKeyWords);
		commandSet.add(PublicVariables.exitKeyWords);
		commandSet.add(PublicVariables.prioritiseKeyWords);
		commandSet.add(PublicVariables.redoKeywords);
		commandSet.add(PublicVariables.undoKeywords);
		commandSet.add(PublicVariables.searchKeyWords);
		commandSet.add(PublicVariables.undoKeywords);
		commandSet.add(PublicVariables.helpKeyWords);
		commandSet.add(PublicVariables.homeKeyWords);
		commandSet.add(PublicVariables.blockKeyWords);
		commandSet.add(PublicVariables.confirmKeyWords);
		commandSet.add(PublicVariables.postponeKeyWords);
		commandSet.add(PublicVariables.setDirectoryKeyWords);
		commandSet.add(PublicVariables.showDetailsKeyWords);
		commandSet.add(PublicVariables.archiveKeyWords);
		commandSet.add(PublicVariables.findFreeKeyWords);
		commandSet.add(PublicVariables.hideKeyWords);
		commandSet.add(PublicVariables.demoKeyWords);

		for (Set<String> setString : commandSet) {
			for (String string : setString) {
				if (string.length() > commandString.length() && commandString.length() != 0) {
					if (string.substring(0, commandString.length()).equals(commandString)) {
						returnedArray.add(string);
					}
				}
			}
		}

		return returnedArray;
	}

	private static Command generateAndReturnCommandObjects(COMMAND_TYPE commandType, String argsString, int index) {
		switch (commandType) {
		case ADD:
			AddCommandParser addCommand = new AddCommandParser(argsString, index);
			return addCommand.generateAndReturn();
		case COMPLETE:
			CompleteCommandParser completeCommand = new CompleteCommandParser(argsString, index);
			return completeCommand.generateAndReturn();
		case DELETE:
			DeleteCommandParser deleteCommand = new DeleteCommandParser(argsString, index);
			return deleteCommand.generateAndReturn();
		case EDIT:
			NewEditCommandParser editCommand = new NewEditCommandParser(argsString, index);
			return editCommand.generateAndReturn();
		case EXIT:
			ExitCommandParser exitCommand = new ExitCommandParser(argsString, index);
			return exitCommand.generateAndReturn();
		case INVALID:
			InvalidCommandParser invalidCommand = new InvalidCommandParser(argsString, index);
			return invalidCommand.generateAndReturn();
		case PRIORITISE:
			PrioritiseCommandParser priCommand = new PrioritiseCommandParser(argsString, index);
			return priCommand.generateAndReturn();
		case REDO:
			RedoCommandParser redoCommand = new RedoCommandParser(argsString, index);
			return redoCommand.generateAndReturn();
		case SEARCH:
			SearchCommandParser searchCommand = new SearchCommandParser(argsString, index);
			return searchCommand.generateAndReturn();
		case SHOW_ARCHIVE:
			ShowArchiveCommandParser showArchiveCommand = new ShowArchiveCommandParser(argsString, index);
			return showArchiveCommand.generateAndReturn();
		case SHOW_DETAILS:
			ShowDetailsCommandParser showDetailsCommand = new ShowDetailsCommandParser(argsString, index);
			return showDetailsCommand.generateAndReturn();
		case BLOCK:
			BlockSlotsCommandParser blockCommand = new BlockSlotsCommandParser(argsString, index);
			return blockCommand.generateAndReturn();
		case FIND_FREE:
			FindFreeCommandParser findFreeCommand = new FindFreeCommandParser(argsString, index);
			return findFreeCommand.generateAndReturn();
		case HOME:
			HomeCommandParser homeCommand = new HomeCommandParser(argsString, index);
			return homeCommand.generateAndReturn();
		case UNDO:
			UndoCommandParser undoCommand = new UndoCommandParser(argsString, index);
			return undoCommand.generateAndReturn();
		case POSTPONE:
			PostponeCommandParser postponeCommand = new PostponeCommandParser(argsString, index);
			return postponeCommand.generateAndReturn();
		case CONFIRM:
			ConfirmCommandParser confirmCommand = new ConfirmCommandParser(argsString, index);
			return confirmCommand.generateAndReturn();
		case SET_DIRECTORY:
			SetDirectoryCommandParser setDirectoryCommand = new SetDirectoryCommandParser(argsString, index);
			return setDirectoryCommand.generateAndReturn();
		case HELP:
			HelpCommandParser helpCommand = new HelpCommandParser(argsString, index);
			return helpCommand.generateAndReturn();
		case DEMO:
			DemoCommandParser demoCommand = new DemoCommandParser(argsString, index);
			return demoCommand.generateAndReturn();
		case HIDE:
			HideCommandParser hideCommand = new HideCommandParser(argsString, index);
			return hideCommand.generateAndReturn();
		default:
			return null;
		}
	}

	private static boolean isDeadline(String string) {
		String lastWord = PublicFunctions.getLastWord(string);
		if (isLastWordDeadlineKeyWord(lastWord)) {
			return true;
		} else {
			return handleDeadlineCheckForNonKeyWordLastWord(string);
		}
	}

	private static boolean isEvent(String string) {
		String lastWord = PublicFunctions.getLastWord(string);
		if (isLastWordEventKeyWord(lastWord)) {
			return true;
		} else {
			return handleEventCheckForNonKeyWordLastWord(string, lastWord);
		}
	}
	
	private static boolean handleDeadlineCheckForNonKeyWordLastWord(String string) {
		Matcher matcher = Pattern.compile(deadlineKeyWordRegex).matcher(string);
		if (matcher.find()) {
			return true;
		} else {
			return false;
		}
	}
	
	private static Boolean handleEventCheckForNonKeyWordLastWord(String string, String lastWord) {
		try {
			Integer.parseInt(lastWord);
			return false;
		} catch (Exception e) {
			DateTimeParser.searchTaskTimes(string);
			if (PublicVariables.taskStartTime != null) {
				return true;
			} else {
				if (string.charAt(string.length() - 1) == ' ') {
					return false;
				} else {
					String secondLastWord = PublicFunctions.getSecondLastWord(string);
					if (PublicVariables.startTimeWords.contains(secondLastWord)
							|| PublicVariables.periodWords.contains(secondLastWord)) {
						return true;
					} else {
						return false;
					}
				}
			}
		}
	}
	
	private static Command generateAddCommandAndReturn(String commandString, int index) {
		PublicVariables.commandType = COMMAND_TYPE.ADD;
		AddCommandParser addCommand = new AddCommandParser(commandString, index);
		return addCommand.generateAndReturn();
	}

	private static ArrayList<String> getReservedWords() {
		ArrayList<String> array = new ArrayList<String>();
		Matcher matcher = Pattern.compile(reservedWordsRegex).matcher(_argsString);
		while (matcher.find()) {
			_argsString = _argsString.replace(matcher.group(), "<" + matcher.group() + ">");
			array.add("<" + matcher.group() + ">");
		}

		return array;
	}

	private static String undoReserveWords(ArrayList<String> array, String string) {
		for (String arrayString : array) {
			string = string.replace(arrayString, arrayString.substring(1, arrayString.length() - 1));
		}
		return string;
	}

	private static String formatCommandString(String commandString) {
		commandString = commandString.toLowerCase();
		commandString = PublicFunctions.reformatArgsString(commandString);
		commandString = reserveSpecialWords(commandString);
		return commandString;
	}

	private static String reserveSpecialWords(String commandString) {
		_argsString = commandString;
		getReservedWords();
		commandString = _argsString;
		return commandString;
	}

	private static Boolean isCommandStringValid(String commandString) {
		return (commandString.length() > 0 && !commandString.trim().equals(""));
	}

	private static Boolean isLastWordEventKeyWord(String lastWord) {
		return (PublicVariables.startTimeWords.contains(lastWord) || PublicVariables.periodWords.contains(lastWord));
	}
	
	private static Boolean isLastWordDeadlineKeyWord(String lastWord) {
		return PublicVariables.endTimeWords.contains(lastWord);
	}

	private static Boolean isInvalidCommandType(COMMAND_TYPE commandType) {
		return commandType == COMMAND_TYPE.INVALID;
	}

	private static SuggestCommand checkEventOrDeadlineAndReturn(String commandString, SuggestCommand suggestCommand) {
		boolean isDeadline = isDeadline(commandString);
		boolean isEvent = isEvent(commandString);
		suggestCommand.setDeadline(isDeadline);
		suggestCommand.setEvent(isEvent);
		return suggestCommand;
	}
}
