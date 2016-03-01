package urgenda.parser;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;import java.import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Arrays;

import urgenda.command.*;
import urgenda.util.*;

public class Parser {
	private static enum COMMAND_TYPE {
		ADD, DELETE, ALLOCATE, DONE, UPDATE, SEARCH, SHOW_DETAILS, UNDO, REDO, ARCHIVE, PRIORITISE, INVALID, EXIT
	}

	private static Integer currentYear = LocalDate.now().getYear();
	private static Integer currentMonth = LocalDate.now().getMonthValue();
	private static Integer currentDayOfMonth = LocalDate.now().getDayOfMonth();

	private static final String MESSAGE_INVALID_COMMAND = "\"%1$s\" is not a valid command";

	private static final Set<String> deleteKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "delete", "del", "erase", "remove" }));
	private static final Set<String> addKeyWords = new HashSet<String>(Arrays.asList(new String[] { "add", "create" }));
	private static final Set<String> allocateKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "block", "reserve", "alloc", "comfirm", "cmf" }));
	private static final Set<String> doneKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "done", "complete", "completed", "mark", "finish", "fin" }));
	private static final Set<String> updateKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "edit", "change", "update", "mod" }));
	private static final Set<String> searchKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "find", "show", "view", "list", "search", "#" }));
	private static final Set<String> showDetailsKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "showmore" }));
	private static final Set<String> undoKeywords = new HashSet<String>(Arrays.asList(new String[] { "undo" }));
	private static final Set<String> redoKeywords = new HashSet<String>(Arrays.asList(new String[] { "redo" }));
	private static final Set<String> archiveKeyWords = new HashSet<String>(Arrays.asList(new String[] { "archive" }));
	private static final Set<String> prioritiseKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "urgent", "important", "pri", "impt" }));
	private static final Set<String> exitKeyWords = new HashSet<String>(Arrays.asList(new String[] { "exit", "quit" }));

	private static COMMAND_TYPE commandType;

	private static String dateRegexWithYear = "([1-9]|0[1-9]|[12][0-9]|3[01])([- /.])([1-9]|0[1-9]|1[012])([- /.])20\\d\\d";
	private static String dateRegexWithoutYear = "([1-9]|0[1-9]|[12][0-9]|3[01])([- /.])([1-9]|0[1-9]|1[012])";
	private static String hourRegex = "(((0[1-9]|1[012]|[1-9]))|([01][1-9]|2[0-4]|[1-9]))";
	private static String minuteAndSecondRegex = "([0-5][0-9]|[1-9])";
	private static String timeRegex = hourRegex + "[: ]" + minuteAndSecondRegex + "[: ]" + minuteAndSecondRegex
			+ "( )?(am|pm)";
	private static String dateOfWeekRegex = "((next)( )+)?(monday|mon|tuesday|tues|tue|wednesday|wed|thursday|thurs|thu|friday|fri|saturday|sat|sunday|sun)";

	private static Integer taskID;
	private static String taskDescription;
	private static String taskLocation;
	private static LocalDateTime taskStartTime;
	private static LocalDateTime taskEndTime;
	private static ArrayList<String> taskHashtags;
	private static MultipleSlot taskSlots;
	private static ArrayList<LocalDateTime> taskDateTime;

	public static Command parseCommand(String commandString) {
		String firstWord = getFirstWord(commandString);
		String commandArgs = removeFirstWord(commandString);
		Boolean isCommandValid = isCommandValid(firstWord, commandArgs);
		if (isCommandValid && parseCommandArgs(commandArgs)) {
			return generateCommandObjectAndReturn();
		} else {
			String formatedErrorMessage = String.format(MESSAGE_INVALID_COMMAND, commandString);
			System.out.print(formatedErrorMessage);
			return null;
		}
	}

	private static String getFirstWord(String commandString) {
		return commandString.split("\\s+")[0];
	}

	private static String removeFirstWord(String commandString) {
		try {
			return commandString.split("\\s+", 2)[1];
		} catch (Exception e) {
			return null;
		}

	}

	private static Boolean isCommandValid(String firstWord, String commandArgs) {
		Boolean returnedValue = false;

		getCommandType(firstWord);
		returnedValue = isCorrectNumberOfArgs(commandArgs);

		return returnedValue;
	}

	private static void getCommandType(String firstWord) {
		String lowerCaseFirstWord = firstWord.toLowerCase();

		if (deleteKeyWords.contains(lowerCaseFirstWord)) {
			commandType = COMMAND_TYPE.DELETE;
		} else if (addKeyWords.contains(lowerCaseFirstWord)) {
			commandType = COMMAND_TYPE.ADD;
		} else if (allocateKeyWords.contains(lowerCaseFirstWord)) {
			commandType = COMMAND_TYPE.ALLOCATE;
		} else if (doneKeyWords.contains(lowerCaseFirstWord)) {
			commandType = COMMAND_TYPE.DONE;
		} else if (updateKeyWords.contains(lowerCaseFirstWord)) {
			commandType = COMMAND_TYPE.UPDATE;
		} else if (searchKeyWords.contains(lowerCaseFirstWord)) {
			commandType = COMMAND_TYPE.SEARCH;
		} else if (showDetailsKeyWords.contains(lowerCaseFirstWord)) {
			commandType = COMMAND_TYPE.SHOW_DETAILS;
		} else if (undoKeywords.contains(lowerCaseFirstWord)) {
			commandType = COMMAND_TYPE.UNDO;
		} else if (redoKeywords.contains(lowerCaseFirstWord)) {
			commandType = COMMAND_TYPE.REDO;
		} else if (archiveKeyWords.contains(lowerCaseFirstWord)) {
			commandType = COMMAND_TYPE.ARCHIVE;
		} else if (prioritiseKeyWords.contains(lowerCaseFirstWord)) {
			commandType = COMMAND_TYPE.PRIORITISE;
		} else if (exitKeyWords.contains(lowerCaseFirstWord)) {
			commandType = COMMAND_TYPE.EXIT;
		} else {
			commandType = COMMAND_TYPE.INVALID;
		}
	}

	private static Boolean isCorrectNumberOfArgs(String commandArgs) {
		switch (commandType) {
		case REDO:
			return commandArgs.equals(null);
		case UNDO:
			return commandArgs.equals(null);
		case SEARCH:
			return !commandArgs.equals(null);
		case ADD:
			return !commandArgs.equals(null);
		default:
			return true;
		}
	}

	private static Boolean parseCommandArgs(String commandArgs) {
		Boolean returnedValue = true;
		switch (commandType) {
		case ADD:
			searchTaskDescriptionOrID(commandArgs);
			searchTaskLocation(commandArgs);
			searchTaskDateTime(commandArgs);
			searchTaskHashtags(commandArgs);
		case DELETE:
			searchTaskDescriptionOrID(commandArgs);
		case ALLOCATE:
			searchTaskDescriptionOrID(commandArgs);
			searchTaskMultipleSlots(commandArgs);
		case DONE:
			searchTaskDescriptionOrID(commandArgs);
		case UPDATE:
			searchTaskDescriptionOrID(commandArgs);
			searchTaskLocation(commandArgs);
			searchTaskDateTime(commandArgs);
			searchTaskHashtags(commandArgs);
		case SEARCH:
			searchTaskDescriptionOrID(commandArgs);
			searchTaskHashtags(commandArgs);
		case SHOW_DETAILS:
			searchTaskDescriptionOrID(commandArgs);
		case UNDO:

		case REDO:

		case ARCHIVE:

		case PRIORITISE:
			searchTaskDescriptionOrID(commandArgs);
		case EXIT:

		case INVALID:

		default:

		}
		return returnedValue;
	}

	private static void searchTaskDescriptionOrID(String commandArgs) {
		String temp = commandArgs;
		temp = temp.split("\\bat\\b")[0];
		temp = temp.split("\\bby\\b")[0];
		temp = temp.split("\\bfrom\\b")[0];
		temp = temp.split("@")[0];
		temp = temp.split("#")[0];
		try {
			taskID = Integer.parseInt(temp.trim());
		} catch (Exception e) {
			taskDescription = temp.trim();
		}
	}

	private static void searchTaskLocation(String commandArgs) {
		String temp = commandArgs;
		try {
			temp = temp.split("@")[1];
		} catch (Exception e) {
			return;
		}
		temp = temp.split("\\bat\\b")[0];
		temp = temp.split("\\bby\\b")[0];
		temp = temp.split("\\bfrom\\b")[0];
		temp = temp.split("#")[0];
		taskLocation = temp.trim();
	}

	private static void searchCombinedDateTime(String commandArgs) {
		String temp = commandArgs.toLowerCase();

		String dateTimeRegexWithYear = dateRegexWithYear + "( )" + timeRegex;
		String timeDateRegexWithYear = timeRegex + "( )" + dateRegexWithYear;
		String dateTimeRegexWithoutYear = dateRegexWithoutYear + "( )" + timeRegex;
		String timeDateRegexWithoutYear = timeRegex + "( )" + dateRegexWithoutYear;

		Matcher m1 = Pattern.compile(dateTimeRegexWithYear).matcher(temp);
		while (m1.find()) {
			LocalDateTime dateTime = processCombinedDateTimeString(m1.group());
			if (dateTime != null) {
				taskDateTime.add(dateTime);
			}
		}

		Matcher m2 = Pattern.compile(timeDateRegexWithYear).matcher(temp);
		while (m2.find()) {
			LocalDateTime dateTime = processCombinedDateTimeString(m2.group());
			if (dateTime != null) {
				taskDateTime.add(dateTime);
			}
		}

		Matcher m3 = Pattern.compile(dateTimeRegexWithoutYear).matcher(temp);
		while (m3.find()) {
			LocalDateTime dateTime = processCombinedDateTimeString(m3.group());
			if (dateTime != null) {
				taskDateTime.add(dateTime);
			}
		}

		Matcher m4 = Pattern.compile(timeDateRegexWithoutYear).matcher(temp);
		while (m4.find()) {
			LocalDateTime dateTime = processCombinedDateTimeString(m4.group());
			if (dateTime != null) {
				taskDateTime.add(dateTime);
			}
		}

	}

	public static LocalDateTime processCombinedDateTimeString(String dateTimeString) {
		String timeString = "";
		String dateString = "";

		Matcher m1 = Pattern.compile(timeRegex).matcher(dateTimeString);
		if (m1.find()) {
			timeString = m1.group();
		}

		Matcher m2 = Pattern.compile(dateRegexWithYear).matcher(dateTimeString);
		if (m2.find()) {
			dateString = m2.group(0);
		} else {
			Matcher m3 = Pattern.compile(dateRegexWithoutYear).matcher(dateTimeString);
			if (m3.find()) {
				dateString = m3.group();
			}
		}

		LocalDate date = processDateString(dateString);
		LocalTime time = processTimeString(timeString);

		if ((date != null) && (time != null)) {
			return LocalDateTime.of(date, time);
		} else {
			return null;
		}
	}

	public static LocalDate processDateString(String dateString) {
		if (dateString != "") {
			String dayString;
			String monthString;
			String yearString;
			ArrayList<String> allMatches = new ArrayList<String>();
			Matcher m1 = Pattern.compile("[- /.]").matcher(dateString);
			while (m1.find()) {
				allMatches.add(m1.group());
			}

			if (allMatches.size() == 1) {
				dayString = dateString.split("[- /.]")[0];
				monthString = dateString.split("[- /.]")[1];
				yearString = currentYear.toString();

				if (dayString.length() == 1) {
					dayString = "0" + dayString;
				}
				if (monthString.length() == 1) {
					monthString = "0" + monthString;
				}
			} else {
				dayString = dateString.split("[- /.]")[0];
				monthString = dateString.split("[- /.]")[1];
				yearString = dateString.split("[- /.]")[2];

				if (dayString.length() == 1) {
					dayString = "0" + dayString;
				}
				if (monthString.length() == 1) {
					monthString = "0" + monthString;
				}
			}

			try {
				String mergedDateString = dayString + "-" + monthString + "-" + yearString;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
				LocalDate date = LocalDate.parse(mergedDateString, formatter);
				return date;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	public static LocalTime processTimeString(String timeString) {
		if (timeString != "") {
			
		} else {
			return null;
		}
	}

	public static void searchSeperateDateTime(String commandArgs) {
		String temp = commandArgs.toLowerCase();

		ArrayList<String> datesString = new ArrayList<String>();

		Matcher m5 = Pattern.compile(dateOfWeekRegex).matcher(temp);
		while (m5.find()) {
			datesString.add(m5.group());
		}
	}

	private static void searchTaskDateTime(String commandArgs) {

	}

	private static void searchTaskHashtags(String commandArgs) {
		String temp = commandArgs;
		Matcher m = Pattern.compile("#\\S+").matcher(temp);
		while (m.find()) {
			taskHashtags.add(m.group());
		}
	}

	private static void searchTaskMultipleSlots(String commandArgs) {

	}

	private static Command generateCommandObjectAndReturn() {
		Command returnedCommandObject;
		switch (commandType) {
		case ADD:
			returnedCommandObject = generateAddCommandObject();
		case DELETE:
			returnedCommandObject = generateDeleteCommandObject();
		case ALLOCATE:
			returnedCommandObject = generateAllocateCommandObject();
		case DONE:
			returnedCommandObject = generateDoneCommandObject();
		case UPDATE:
			returnedCommandObject = generateUpdateCommandObject();
		case SEARCH:
			returnedCommandObject = generateSearchCommandObject();
		case SHOW_DETAILS:
			returnedCommandObject = generateShowDetailsCommandObject();
		case UNDO:
			returnedCommandObject = generateUndoCommandObject();
		case REDO:
			returnedCommandObject = generateRedoCommandObject();
		case ARCHIVE:
			returnedCommandObject = generateArchiveCommandObject();
		case PRIORITISE:
			returnedCommandObject = generatePrioritiseCommandObject();
		case EXIT:
			returnedCommandObject = generateExitCommandObject();
		case INVALID:
			returnedCommandObject = generateInvalidCommandObject();
		default:
			returnedCommandObject = generateInvalidCommandObject();
		}
		return returnedCommandObject;
	}

	private static Command generateAddCommandObject() {
		Task newTask = new Task();
		if (taskID != null) {
			newTask.setId(taskID);
		}
		if (taskDescription != null) {
			newTask.setDesc(taskDescription);
		}
		if (taskLocation != null) {
			newTask.setLocation(taskLocation);
		}
		if (taskStartTime != null) {
			newTask.setStartTime(taskStartTime);
		}
		if (taskEndTime != null) {
			newTask.setEndTime(taskEndTime);
		}
		if (!taskHashtags.isEmpty()) {
			newTask.setHashtags(taskHashtags);
		}
		if (taskSlots != null) {
			newTask.setSlot(taskSlots);
		}
		return new AddTask(newTask);
	}

	private static Command generateDeleteCommandObject() {
		DeleteTask deleteCommand = new DeleteTask();
		if (taskID != null) {
			deleteCommand.setId(taskID);
		}
		if (taskDescription != null) {
			deleteCommand.setDesc(taskDescription);
		}
		return deleteCommand;
	}

	private static Command generateAllocateCommandObject() {
		BlockSlots blockSlotsCommand = new BlockSlots();
		// TO DO: set methods
		return blockSlotsCommand;
	}

	private static Command generateDoneCommandObject() {
		Complete completeCommand = new Complete();
		// TO DO: set methods
		return completeCommand;
	}

	private static Command generateUpdateCommandObject() {
		Edit editCommand = new Edit();
		// TO DO: set methods
		return editCommand;
	}

	private static Command generateSearchCommandObject() {
		Search searchCommand = new Search();
		// TO DO: set methods
		return searchCommand;
	}

	private static Command generateShowDetailsCommandObject() {
		ShowDetails showDetailsCommand = new ShowDetails();
		// TO DO: set methods
		return showDetailsCommand;
	}

	private static Command generateUndoCommandObject() {
		Undo undoCommand = new Undo();
		// TO DO: set methods
		return undoCommand;
	}

	private static Command generateRedoCommandObject() {
		Redo redoCommand = new Redo();
		// TO DO: set methods
		return redoCommand;
	}

	private static Command generateArchiveCommandObject() {
		ShowArchive showArchiveCommand = new ShowArchive();
		// TO DO: set methods
		return showArchiveCommand;
	}

	private static Command generatePrioritiseCommandObject() {
		Prioritise prioritiseCommand = new Prioritise();
		// TO DO: set methods
		return prioritiseCommand;
	}

	private static Command generateExitCommandObject() {
		Exit exitCommand = new Exit();
		// TO DO: set methods
		return exitCommand;
	}

	private static Command generateInvalidCommandObject() {
		Invalid invalidCommand = new Invalid();
		// TO DO: set methods
		return invalidCommand;
	}
}
