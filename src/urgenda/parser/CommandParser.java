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
import urgenda.parser.commandParser.EditCommandParser;
import urgenda.parser.commandParser.ExitCommandParser;
import urgenda.parser.commandParser.FindFreeCommandParser;
import urgenda.parser.commandParser.HelpCommandParser;
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
	public static Command parseCommand(String commandString, int index) {
		PublicFunctions.reinitializePublicVariables();

		PublicVariables.commandType = CommandTypeParser.getCommandType(commandString);
		String argsString = CommandTypeParser.getArgsString(commandString);
		Command testReturn = generateAndReturnCommandObjects(PublicVariables.commandType, argsString, index);

		if (testReturn instanceof Invalid) {
			PublicFunctions.reinitializePublicVariables();
			PublicVariables.commandType = COMMAND_TYPE.ADD;
			AddCommandParser addCommand = new AddCommandParser(commandString, index);
			return addCommand.generateAndReturn();
		} else {
			return testReturn;
		}
	}

	public static SuggestCommand parseRuntimeInput(String commandString) {
		PublicFunctions.reinitializePublicVariables();
		commandString = commandString.toLowerCase();
		COMMAND_TYPE commandType = CommandTypeParser.getCommandType(commandString);
		if (commandType == COMMAND_TYPE.INVALID) {
			int numberOfWords = PublicFunctions.getNumberOfWords(commandString);
			if (numberOfWords == 1) {
				String firstWord = PublicFunctions.getFirstWord(commandString).trim();
				ArrayList<String> possibleCommands = getPossibleCommands(firstWord);
				if (!possibleCommands.isEmpty()) {
					return new SuggestCommand(null, possibleCommands, null);
				} else {
					return new SuggestCommand(null, null, null);
				}
			} else {
				return new SuggestCommand(null, null, null);
			}
		} else {
			SuggestCommand.Command command = convertCommandType(commandType);
			DateTimeParser.searchTaskTimes(commandString);
			TaskDetailsParser.searchTaskType();
			SuggestCommand suggestCommand;
			if (command != null) {
				suggestCommand = new SuggestCommand(command, null, PublicFunctions.getFirstWord(commandString));
			} else {
				suggestCommand = new SuggestCommand(null, null, null);
			}
			if (PublicVariables.taskType == TASK_TYPE.DEADLINE) {
				suggestCommand.setIsDeadline(true);
			} else if (PublicVariables.taskType == TASK_TYPE.EVENT) {
				suggestCommand.setIsEvent(true);
			}
			return suggestCommand;
		}
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
		default:
			return null;
		}
	}
}
