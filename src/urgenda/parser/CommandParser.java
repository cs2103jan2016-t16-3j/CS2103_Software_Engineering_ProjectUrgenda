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
import urgenda.parser.commandParser.DeleteCommandParser;
import urgenda.parser.commandParser.EditCommandParser;
import urgenda.parser.commandParser.ExitCommandParser;
import urgenda.parser.commandParser.FindFreeCommandParser;
import urgenda.parser.commandParser.HomeCommandParser;
import urgenda.parser.commandParser.InvalidCommandParser;
import urgenda.parser.commandParser.PostponeCommandParser;
import urgenda.parser.commandParser.PrioritiseCommandParser;
import urgenda.parser.commandParser.RedoCommandParser;
import urgenda.parser.commandParser.SearchCommandParser;
import urgenda.parser.commandParser.ShowArchiveCommandParser;
import urgenda.parser.commandParser.ShowDetailsCommandParser;
import urgenda.parser.commandParser.UndoCommandParser;
import urgenda.util.*;

public class CommandParser {
	public static Command parseCommand(String commandString, int index) {
		PublicFunctions.reinitializePublicVariables();

		commandString = reformatCommandString(commandString);

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

	private static Command generateAndReturnCommandObjects(COMMAND_TYPE commandType, String argsString, int index) {
		switch (commandType) {
		case ADD:
			AddCommandParser addCommand = new AddCommandParser(argsString, index);
			return addCommand.generateAndReturn();
		case ALLOCATE:
			BlockSlotsCommandParser blockSlotsCommand = new BlockSlotsCommandParser(argsString, index);
			return blockSlotsCommand.generateAndReturn();
		case COMPLETE:
			CompleteCommandParser completeCommand = new CompleteCommandParser(argsString, index);
			return completeCommand.generateAndReturn();
		case DELETE:
			DeleteCommandParser deleteCommand = new DeleteCommandParser(argsString, index);
			return deleteCommand.generateAndReturn();
		case EDIT:
			EditCommandParser editCommand = new EditCommandParser(argsString, index);
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
		default:
			return null;
		}
	}

	public static String reformatCommandString(String commandString) {
		String reverseDateRegex = "((\\D([1-9]|0[1-9]|[12][0-9]|3[01])([-/.])([1-9]|0[1-9]|1[012])(([-/.])([(19)|(20)])?\\d\\d)?\\D))";
		Matcher matcher = Pattern.compile(reverseDateRegex).matcher(commandString);
		while (matcher.find()) {
			commandString = commandString.replace(matcher.group(), " " + reverseDateMonth(matcher.group()) + " ");
		}
		return commandString;
	}

	private static String reverseDateMonth(String string) {
		String[] stringArray = string.split("([-/.])");
		if (stringArray.length == 2) {
			return stringArray[1].replaceAll("\\D+", "") + "/" + stringArray[0].replaceAll("\\D+", "");
		} else if (stringArray.length == 3) {
			return stringArray[1] + "/" + stringArray[0].replaceAll("\\D+", "") + "/"
					+ stringArray[2].replaceAll("\\D+", "");
		} else {
			return string;
		}
	}
}
