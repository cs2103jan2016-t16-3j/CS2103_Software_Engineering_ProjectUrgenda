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
