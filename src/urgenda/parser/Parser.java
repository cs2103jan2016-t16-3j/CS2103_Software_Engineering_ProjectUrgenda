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
import urgenda.util.*;

public class Parser {
	public static String test = "HI";
	private static enum COMMAND_TYPE {
		ADD, DELETE, ALLOCATE, DONE, UPDATE, SEARCH, SHOW_DETAILS, UNDO, REDO, ARCHIVE, PRIORITISE, INVALID, EXIT
	}

	private static enum TASK_TYPE {
		EVENT, DEADLINE, FLOATING, INVALID
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
			Arrays.asList(new String[] { "find", "show", "view", "list", "search", "#\\w+" }));
	private static final Set<String> showDetailsKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "showmore" }));
	private static final Set<String> undoKeywords = new HashSet<String>(Arrays.asList(new String[] { "undo" }));
	private static final Set<String> redoKeywords = new HashSet<String>(Arrays.asList(new String[] { "redo" }));
	private static final Set<String> archiveKeyWords = new HashSet<String>(Arrays.asList(new String[] { "archive" }));
	private static final Set<String> prioritiseKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "urgent", "important", "pri", "impt" }));
	private static final Set<String> exitKeyWords = new HashSet<String>(Arrays.asList(new String[] { "exit", "quit" }));

	private static final Set<String> mondayWords = new HashSet<String>(Arrays.asList(new String[] { "monday", "mon" }));
	private static final Set<String> tuesdayWords = new HashSet<String>(Arrays.asList(new String[] { "tuesday", "tues", "tue"}));
	private static final Set<String> wednesdayWords = new HashSet<String>(Arrays.asList(new String[] { "wednesday", "wed" }));
	private static final Set<String> thursdayWords = new HashSet<String>(Arrays.asList(new String[] { "thursday", "thurs", "thu" }));
	private static final Set<String> fridayWords = new HashSet<String>(Arrays.asList(new String[] { "friday", "fri" }));
	private static final Set<String> saturdayWords = new HashSet<String>(Arrays.asList(new String[] { "saturday", "sat" }));
	private static final Set<String> sundayWords = new HashSet<String>(Arrays.asList(new String[] { "sunday", "sun" }));
	
	private static COMMAND_TYPE commandType;

	private static String dayOfWeekRegex = "(((next)( )+)?(monday|mon|tuesday|tues|tue|wednesday|wed|thursday|thurs|thu|friday|fri|saturday|sat|sunday|sun))";
	private static String dateRegexWithYear = "(([1-9]|0[1-9]|[12][0-9]|3[01])([-/.])([1-9]|0[1-9]|1[012])([-/.])20\\d\\d)";
	private static String dateRegexWithoutYear = "(([1-9]|0[1-9]|[12][0-9]|3[01])([-/.])([1-9]|0[1-9]|1[012]))";
	private static String hourRegex12 = "(0[1-9]|1[012]|[1-9])";
	private static String hourRegex24 = "([01][1-9]|2[0-4]|[1-9])";
	private static String minuteAndSecondRegex = "([0-5][0-9]|[1-9])";
	private static String timeRegexHour12 = hourRegex12 + "( )?(am|pm)\\b";
	private static String timeRegexHour24 = hourRegex24 + "( )?h\\b";
	private static String timeRegexHour12Minute = hourRegex12 + "[:]" + minuteAndSecondRegex + "( )?(am|pm)\\b";
	private static String timeRegexHour24Minute = hourRegex24 + "[:]" + minuteAndSecondRegex + "( )?h?\\b";
	private static String timeRegexHour12MinuteSecond = hourRegex12 + "[:]" + minuteAndSecondRegex + "[:]"
			+ minuteAndSecondRegex + "( )?(am|pm)\\b";
	private static String timeRegexHour24MinuteSecond = hourRegex24 + "[:]" + minuteAndSecondRegex + "[:]"
			+ minuteAndSecondRegex + "( )?h?\\b";

	private static String generalDateRegex = "(" + dateRegexWithYear + "|" + dateRegexWithoutYear + ")";
	private static String generalTimeRegex = "(" + timeRegexHour12MinuteSecond + "|" + timeRegexHour24MinuteSecond + "|"
			+ timeRegexHour12Minute + "|" + timeRegexHour24Minute + "|" + timeRegexHour12 + "|" + timeRegexHour24 + ")";

	private static Integer taskID = 0;
	private static String taskDescription = "";
	private static String taskLocation = "";
	private static LocalDateTime taskStartTime;
	private static LocalDateTime taskEndTime;
	private static ArrayList<String> taskHashtags = new ArrayList<String>();
	private static MultipleSlot taskSlots;
	private static TASK_TYPE taskType = TASK_TYPE.INVALID;

	private static ArrayList<LocalDateTime> taskDateTime = new ArrayList<LocalDateTime>();
	private static ArrayList<String> taskTimeType = new ArrayList<String>();

	public static Command parseCommand(String commandString) {
		reinitializeStorageVariables();
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

	private static void reinitializeStorageVariables() {
		taskID = 0;
		taskDescription = "";
		taskLocation = "";
		taskStartTime = null;
		taskEndTime = null;
		taskHashtags = new ArrayList<String>();
		taskType = TASK_TYPE.INVALID;
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
			break;
		case DELETE:
			searchTaskDescriptionOrID(commandArgs);
			break;
		case ALLOCATE:
			searchTaskDescriptionOrID(commandArgs);
			searchTaskMultipleSlots(commandArgs);
			break;
		case DONE:
			searchTaskDescriptionOrID(commandArgs);
			break;
		case UPDATE:
			searchTaskDescriptionOrID(commandArgs);
			searchTaskLocation(commandArgs);
			searchTaskDateTime(commandArgs);
			searchTaskHashtags(commandArgs);
			break;
		case SEARCH:
			searchTaskDescriptionOrID(commandArgs);
			searchTaskHashtags(commandArgs);
			break;
		case SHOW_DETAILS:
			searchTaskDescriptionOrID(commandArgs);
			break;
		case UNDO:
			break;
		case REDO:
			break;
		case ARCHIVE:
			break;
		case PRIORITISE:
			searchTaskDescriptionOrID(commandArgs);
		case EXIT:
			break;
		case INVALID:
			break;
		default:
			break;
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

		String dateTimeRegex = generalDateRegex + "( )" + generalTimeRegex;
		String timeDateRegex = generalTimeRegex + "( )" + generalDateRegex;
		String combinedRegex = "(at|by|from|to)( )(" + dateTimeRegex + "|" + timeDateRegex + ")";

		Matcher matcher = Pattern.compile(combinedRegex).matcher(temp);
		while (matcher.find()) {
			LocalDateTime dateTime = processCombinedDateTimeString(matcher.group());
			if (dateTime != null) {
				taskDateTime.add(dateTime);
			}
		}
	}

	private static LocalDateTime processCombinedDateTimeString(String dateTimeString) {
		String timeString = "";
		String dateString = "";

		Matcher matcher = Pattern.compile("(at|by|from|to)( )").matcher(dateTimeString);
		if (matcher.find()) {
			taskTimeType.add(matcher.group().trim());
			timeString = timeString.replace(matcher.group(), "").trim();
		}

		matcher = Pattern.compile(generalTimeRegex).matcher(dateTimeString);
		if (matcher.find()) {
			timeString = matcher.group();
		}

		matcher = Pattern.compile(generalDateRegex).matcher(dateTimeString);
		if (matcher.find()) {
			dateString = matcher.group();
		}

		LocalDate date = processDateString(dateString);
		LocalTime time = processTimeString(timeString);

		if ((date != null) && (time != null)) {
			return LocalDateTime.of(date, time);
		} else {
			return null;
		}
	}

	private static LocalDate processDateString(String dateString) {
		if (dateString != "") {
			String dayString = "";
			String monthString = "";
			String yearString = "";
			ArrayList<String> allMatches = new ArrayList<String>();

			Matcher matcher = Pattern.compile("[-/.]").matcher(dateString);
			while (matcher.find()) {
				allMatches.add(matcher.group());
			}

			if (allMatches.size() == 1) {
				dayString = dateString.split("[-/.]")[0];
				monthString = dateString.split("[-/.]")[1];
				yearString = currentYear.toString();

				if (dayString.length() == 1) {
					dayString = "0" + dayString;
				}
				if (monthString.length() == 1) {
					monthString = "0" + monthString;
				}
			} else if (allMatches.size() == 2) {
				dayString = dateString.split("[-/.]")[0];
				monthString = dateString.split("[-/.]")[1];
				yearString = dateString.split("[-/.]")[2];

				if (dayString.length() == 1) {
					dayString = "0" + dayString;
				}
				if (monthString.length() == 1) {
					monthString = "0" + monthString;
				}
			}

			try {
				String mergedDateString = dayString + "-" + monthString + "-" + yearString;
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				LocalDate date = LocalDate.parse(mergedDateString, formatter);
				return date;
			} catch (Exception e) {
				return null;
			}
		} else {
			return null;
		}
	}

	private static LocalTime processTimeString(String timeString) {
		if (timeString != "") {
			Matcher matcher = Pattern.compile("(( )?(am|pm))").matcher(timeString);
			if (matcher.find()) {
				return process12hTimeString(timeString);
			} else {
				return process24hTimeString(timeString);
			}
		} else {
			return null;
		}
	}

	private static LocalTime process12hTimeString(String timeString) {
		Boolean add12Hour = false;
		Matcher matcher = Pattern.compile("(( )?(am|pm))").matcher(timeString);
		if (matcher.find()) {
			if (matcher.group().trim().equals("pm")) {
				add12Hour = true;
			}
			timeString = timeString.replace(matcher.group(), "").trim();
		}

		ArrayList<String> temp = new ArrayList<String>();
		String hourString;
		String minuteString;
		String secondString;

		temp = parseTimeValues(timeString);
		hourString = temp.get(0);
		minuteString = temp.get(1);
		secondString = temp.get(2);

		if (hourString == "") {
			return null;
		} else {
			if (add12Hour) {
				Integer addedHour = Integer.parseInt(hourString) + 12;
				hourString = addedHour.toString();
			}

			return mergeAndParseTimeValues(hourString, minuteString, secondString);
		}
	}

	private static LocalTime process24hTimeString(String timeString) {
		Matcher matcher = Pattern.compile("( )?h").matcher(timeString);
		if (matcher.find()) {
			timeString = timeString.replace(matcher.group(), "").trim();
		}

		ArrayList<String> temp = new ArrayList<String>();
		String hourString;
		String minuteString;
		String secondString;

		temp = parseTimeValues(timeString);
		hourString = temp.get(0);
		minuteString = temp.get(1);
		secondString = temp.get(2);
		
		if (hourString == "") {
			return null;
		} else {
			return mergeAndParseTimeValues(hourString, minuteString, secondString);
		}
	}

	private static ArrayList<String> parseTimeValues(String timeString) {
		ArrayList<String> returnedArray = new ArrayList<String>();
		ArrayList<String> allMatches = new ArrayList<String>();
		String hourString;
		String minuteString;
		String secondString;

		Matcher matcher = Pattern.compile("[:]").matcher(timeString);
		while (matcher.find()) {
			allMatches.add(matcher.group());
		}

		switch (allMatches.size()) {
		case 0:
			hourString = timeString.split("[:]")[0].trim();
			minuteString = "00";
			secondString = "00";

			if (hourString.length() == 1) {
				hourString = "0" + hourString;
			}
			break;
		case 1:
			hourString = timeString.split("[:]")[0].trim();
			minuteString = timeString.split("[:]")[1].trim();
			secondString = "00";

			if (hourString.length() == 1) {
				hourString = "0" + hourString;
			}
			if (minuteString.length() == 1) {
				minuteString = "0" + minuteString;
			}
			break;
		case 2:
			hourString = timeString.split("[:]")[0].trim();
			minuteString = timeString.split("[:]")[1].trim();
			secondString = timeString.split("[:]")[2].trim();

			if (hourString.length() == 1) {
				hourString = "0" + hourString;
			}
			if (minuteString.length() == 1) {
				minuteString = "0" + minuteString;
			}
			if (secondString.length() == 1) {
				secondString = "0" + secondString;
			}
			break;
		default:
			hourString = "";
			minuteString = "";
			secondString = "";
			break;
		}

		returnedArray.add(hourString);
		returnedArray.add(minuteString);
		returnedArray.add(secondString);
		return returnedArray;
	}

	private static LocalTime mergeAndParseTimeValues(String hourString, String minuteString, String secondString) {
		String mergedTimeString = hourString + ":" + minuteString + ":" + secondString;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		try {
			LocalTime time = LocalTime.parse(mergedTimeString, formatter);
			return time;
		} catch (Exception e) {
			return null;
		}
	}

	private static void searchSeparateDateTime(String commandArgs) {
		String temp = commandArgs.toLowerCase();

		String dateTimeRegex = dayOfWeekRegex + "( )" + generalTimeRegex;
		String timeDateRegex = generalTimeRegex + "( )" + dayOfWeekRegex;
		String combinedRegex = "(at|by|from|to)( )(" + dateTimeRegex + "|" + timeDateRegex + ")";

		Matcher matcher = Pattern.compile(combinedRegex).matcher(temp);
		while (matcher.find()) {
			LocalDateTime dateTime = processSeparateDateTimeString(matcher.group());
			if (dateTime != null) {
				taskDateTime.add(dateTime);
			}
		}
	}

	private static LocalDateTime processSeparateDateTimeString(String dateTimeString) {
		String timeString = "";
		String dateStringInWeek = "";

		Matcher matcher = Pattern.compile("(at|by|from|to)( )").matcher(dateTimeString);
		if (matcher.find()) {
			taskTimeType.add(matcher.group().trim());
			dateTimeString = dateTimeString.replace(matcher.group(), "").trim();
		}

		matcher = Pattern.compile(generalTimeRegex).matcher(dateTimeString);
		if (matcher.find()) {
			timeString = matcher.group();
		}

		matcher = Pattern.compile(dayOfWeekRegex).matcher(dateTimeString);
		if (matcher.find()) {
			dateStringInWeek = matcher.group();
		}

		LocalDate date = processDateStringInWeek(dateStringInWeek);
		LocalTime time = processTimeString(timeString);

		if ((date != null) && (time != null)) {
			return LocalDateTime.of(date, time);
		} else {
			return null;
		}
	}

	private static LocalDate processDateStringInWeek(String dateStringInWeek) {
		if (dateStringInWeek != "") {
			String temp = dateStringInWeek.toLowerCase();
			Boolean add7Days = false;
			Calendar calendar = Calendar.getInstance();
			int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			int commandDayOfWeek = currentDayOfWeek;

			Matcher matcher = Pattern.compile("next( )?").matcher(dateStringInWeek);
			if (matcher.find()) {
				add7Days = true;
				temp = temp.replace(matcher.group(), "").trim();
			}
			
			if (mondayWords.contains(temp)) {
				commandDayOfWeek = 2;
			} else if (tuesdayWords.contains(temp)) {
				commandDayOfWeek = 3;
			} else if (wednesdayWords.contains(temp)) {
				commandDayOfWeek = 4;
			} else if (thursdayWords.contains(temp)) {
				commandDayOfWeek = 5;
			} else if (fridayWords.contains(temp)) {
				commandDayOfWeek = 6;
			} else if (saturdayWords.contains(temp)) {
				commandDayOfWeek = 7;
			} else if (sundayWords.contains(temp)) {
				commandDayOfWeek = 1;
			}

			if (currentDayOfWeek > commandDayOfWeek) {
				add7Days = true;
			}

			Integer numberOfDayDifference;
			if (add7Days) {
				numberOfDayDifference = commandDayOfWeek - currentDayOfWeek + 7;
			} else {
				numberOfDayDifference = commandDayOfWeek - currentDayOfWeek;
			}

			return LocalDateTime.now().toLocalDate().plusDays(numberOfDayDifference);

		} else {
			return null;
		}
	}

	private static void searchTaskDateTime(String commandArgs) {
		searchCombinedDateTime(commandArgs);
		searchSeparateDateTime(commandArgs);
		getTaskType(commandArgs);

		switch (taskDateTime.size()) {
		case 0:
			System.out.print("No time values inserted\n");
			break;
		case 1:
			switch (taskType) {
			case EVENT:
				taskStartTime = taskDateTime.get(0);
				taskEndTime = taskStartTime.plusHours(1);
				break;
			case DEADLINE:
				taskEndTime = taskDateTime.get(0);
				break;
			}
			break;
		case 2:
			if (taskType == TASK_TYPE.EVENT) {
				if (taskDateTime.get(0).compareTo(taskDateTime.get(1)) > 0) {
					taskStartTime = taskDateTime.get(1);
					taskEndTime = taskDateTime.get(0);
				} else {
					taskStartTime = taskDateTime.get(0);
					taskEndTime = taskDateTime.get(1);
				}
			} else {
				System.out.print("Invalid time values inserted\n");
			}
			break;
		default:
			System.out.print("Too many time values inserted\n");
			break;
		}
	}

	private static void getTaskType(String commandArgs) {
		switch (taskTimeType.size()) {
		case 0:
			taskType = TASK_TYPE.FLOATING;
			break;
		case 1:
			if (taskTimeType.get(0).contains("at") || taskTimeType.get(0).contains("from")) {
				taskType = TASK_TYPE.EVENT;
			} else if (taskTimeType.get(0).contains("by")) {
				taskType = TASK_TYPE.DEADLINE;
			} else {
				taskType = TASK_TYPE.INVALID;
			}
			break;
		case 2:
			if ((taskTimeType.get(0).contains("from") && taskTimeType.get(1).contains("to"))
					|| (taskTimeType.get(1).contains("from") && taskTimeType.get(0).contains("to"))) {
				taskType = TASK_TYPE.EVENT;
			} else {
				taskType = TASK_TYPE.INVALID;
			}
			break;
		default:
			taskType = TASK_TYPE.INVALID;
			break;
		}
	}

	private static void searchTaskHashtags(String commandArgs) {
		String temp = commandArgs;
		Matcher matcher = Pattern.compile("#\\S+").matcher(temp);
		while (matcher.find()) {
			taskHashtags.add(matcher.group());
		}
	}

	private static void searchTaskMultipleSlots(String commandArgs) {

	}

	private static Command generateCommandObjectAndReturn() {
		Command returnedCommandObject;
		switch (commandType) {
		case ADD:
			returnedCommandObject = generateAddCommandObject();
			break;
		case DELETE:
			returnedCommandObject = generateDeleteCommandObject();
			break;
		case ALLOCATE:
			returnedCommandObject = generateAllocateCommandObject();
			break;
		case DONE:
			returnedCommandObject = generateDoneCommandObject();
			break;
		case UPDATE:
			returnedCommandObject = generateUpdateCommandObject();
			break;
		case SEARCH:
			returnedCommandObject = generateSearchCommandObject();
			break;
		case SHOW_DETAILS:
			returnedCommandObject = generateShowDetailsCommandObject();
			break;
		case UNDO:
			returnedCommandObject = generateUndoCommandObject();
			break;
		case REDO:
			returnedCommandObject = generateRedoCommandObject();
			break;
		case ARCHIVE:
			returnedCommandObject = generateArchiveCommandObject();
			break;
		case PRIORITISE:
			returnedCommandObject = generatePrioritiseCommandObject();
			break;
		case EXIT:
			returnedCommandObject = generateExitCommandObject();
			break;
		case INVALID:
			returnedCommandObject = generateInvalidCommandObject();
			break;
		default:
			returnedCommandObject = generateInvalidCommandObject();
			break;
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
		switch (taskType) {
		case EVENT:
			newTask.setTaskType(Task.Type.EVENT);
			break;
		case DEADLINE:
			newTask.setTaskType(Task.Type.DEADLINE);
			break;
		case FLOATING:
			newTask.setTaskType(Task.Type.FLOATING);
			break;
		default:
			break;
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

	public static void testParser(String commandArgs) {
		searchTaskDescriptionOrID(commandArgs);
		searchTaskLocation(commandArgs);
		searchTaskDateTime(commandArgs);
		searchTaskHashtags(commandArgs);
		System.out.print("Task description: " + taskDescription + "\n");
		System.out.print("Task location: " + taskLocation + "\n");
		System.out.print("Task start time: " + taskStartTime + "\n");
		System.out.print("Task end time: " + taskEndTime + "\n");
		System.out.print("Task type: " + taskType.toString() + "\n");
		System.out.print("Task hastags:");
		for (Integer i = 0; i < taskHashtags.size(); i++) {
			System.out.print(" " + taskHashtags.get(i));
		}
		System.out.print("\n");
	}

	public static void testTimeParse(String timeString) {
		System.out.print(processTimeString(timeString) + "\n");
	}

	public static void testDateParse(String dateString) {
		System.out.print(processDateString(dateString) + "\n");
	}

	public static void testStringDetection(String commandArgs) {
		String temp = commandArgs.toLowerCase();

		String dateTimeRegex = generalDateRegex + "( )" + generalTimeRegex;
		String timeDateRegex = generalTimeRegex + "( )" + generalDateRegex;
		String combinedRegex = "(at|by|from|to)( )(" + dateTimeRegex + "|" + timeDateRegex + ")";

		Matcher matcher = Pattern.compile(combinedRegex).matcher(temp);
		while (matcher.find()) {
			String tempString = matcher.group();
			String timeString = "";
			String dateString = "";

			Matcher matcher2;
			matcher2 = Pattern.compile("(at|by|from|to)( )").matcher(tempString);
			if (matcher2.find()) {
				taskTimeType.add(matcher2.group().trim());
				timeString = timeString.replace(matcher2.group(), "").trim();
			}

			matcher2 = Pattern.compile(generalTimeRegex).matcher(tempString);
			if (matcher2.find()) {
				timeString = matcher2.group();
			}

			matcher2 = Pattern.compile(generalDateRegex).matcher(tempString);
			if (matcher2.find()) {
				dateString = matcher2.group();
			}
		}
	}
}
