package urgenda.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import urgenda.util.MultipleSlot;

public class PublicVariables {
	public static enum COMMAND_TYPE {
		ADD, DELETE, ALLOCATE, COMPLETE, EDIT, SEARCH, SHOW_DETAILS, UNDO, REDO, SHOW_ARCHIVE, PRIORITISE, INVALID, EXIT
	}

	public static enum TASK_TYPE {
		EVENT, DEADLINE, FLOATING, INVALID
	}

	public static int currentYear = LocalDate.now().getYear();
	public static int currentMonth = LocalDate.now().getMonthValue();
	public static int currentDayOfMonth = LocalDate.now().getDayOfMonth();

	public static final String MESSAGE_INVALID_COMMAND = "\"%1$s\" is not a valid command";

	public static final Set<String> deleteKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "delete", "del", "erase", "remove" }));
	public static final Set<String> addKeyWords = new HashSet<String>(Arrays.asList(new String[] { "add", "create" }));
	public static final Set<String> allocateKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "block", "reserve", "alloc", "comfirm", "cmf" }));
	public static final Set<String> doneKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "done", "complete", "completed", "mark", "finish", "fin" }));
	public static final Set<String> updateKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "edit", "change", "update", "mod" }));
	public static final Set<String> searchKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "find", "show", "view", "list", "search", "#\\w+" }));
	public static final Set<String> showDetailsKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "showmore" }));
	public static final Set<String> undoKeywords = new HashSet<String>(Arrays.asList(new String[] { "undo" }));
	public static final Set<String> redoKeywords = new HashSet<String>(Arrays.asList(new String[] { "redo" }));
	public static final Set<String> archiveKeyWords = new HashSet<String>(Arrays.asList(new String[] { "archive" }));
	public static final Set<String> prioritiseKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "urgent", "important", "pri", "impt" }));
	public static final Set<String> exitKeyWords = new HashSet<String>(Arrays.asList(new String[] { "exit", "quit" }));

	public static final Set<String> mondayWords = new HashSet<String>(Arrays.asList(new String[] { "monday", "mon" }));
	public static final Set<String> tuesdayWords = new HashSet<String>(
			Arrays.asList(new String[] { "tuesday", "tues", "tue" }));
	public static final Set<String> wednesdayWords = new HashSet<String>(
			Arrays.asList(new String[] { "wednesday", "wed" }));
	public static final Set<String> thursdayWords = new HashSet<String>(
			Arrays.asList(new String[] { "thursday", "thurs", "thu" }));
	public static final Set<String> fridayWords = new HashSet<String>(Arrays.asList(new String[] { "friday", "fri" }));
	public static final Set<String> saturdayWords = new HashSet<String>(
			Arrays.asList(new String[] { "saturday", "sat" }));
	public static final Set<String> sundayWords = new HashSet<String>(Arrays.asList(new String[] { "sunday", "sun" }));

	public static COMMAND_TYPE commandType = COMMAND_TYPE.INVALID;

	public static String dayOfWeekRegex = "(((next)( )+)?(monday|mon|tuesday|tues|tue|wednesday|wed|thursday|thurs|thu|friday|fri|saturday|sat|sunday|sun))";
	public static String dateRegexWithYear = "(([1-9]|0[1-9]|[12][0-9]|3[01])([-/.])([1-9]|0[1-9]|1[012])([-/.])20\\d\\d)";
	public static String dateRegexWithoutYear = "(([1-9]|0[1-9]|[12][0-9]|3[01])([-/.])([1-9]|0[1-9]|1[012]))";
	public static String hourRegex12 = "([01][1-9]|2[0-3]|[1-9])";
	public static String hourRegex24 = "([01][1-9]|2[0-4]|[1-9])";
	public static String minuteAndSecondRegex = "([0-5][0-9]|[1-9])";
	public static String timeRegexHour12 = hourRegex12 + "( )?(am|pm)\\b";
	public static String timeRegexHour24 = hourRegex24 + "( )?h\\b";
	public static String timeRegexHour12Minute = hourRegex12 + "[:]" + minuteAndSecondRegex + "( )?(am|pm)\\b";
	public static String timeRegexHour24Minute = hourRegex24 + "[:]" + minuteAndSecondRegex + "( )?h?\\b";
	public static String timeRegexHour12MinuteSecond = hourRegex12 + "[:]" + minuteAndSecondRegex + "[:]"
			+ minuteAndSecondRegex + "( )?(am|pm)\\b";
	public static String timeRegexHour24MinuteSecond = hourRegex24 + "[:]" + minuteAndSecondRegex + "[:]"
			+ minuteAndSecondRegex + "( )?h?\\b";

	public static String generalDateRegex = "(" + dateRegexWithYear + "|" + dateRegexWithoutYear + ")";
	public static String generalTimeRegex = "(" + timeRegexHour12MinuteSecond + "|" + timeRegexHour24MinuteSecond + "|"
			+ timeRegexHour12Minute + "|" + timeRegexHour24Minute + "|" + timeRegexHour12 + "|" + timeRegexHour24 + ")";

	public static String passedInCommandString = "";
	public static int passedInIndex = -1;
	public static int taskIndex = -10;
	public static String taskDescription = "";
	public static String taskLocation = "";
	public static LocalDateTime taskStartTime;
	public static LocalDateTime taskEndTime;
	public static ArrayList<String> taskHashtags = new ArrayList<String>();
	public static MultipleSlot taskSlots;

	public static TASK_TYPE taskType = TASK_TYPE.INVALID;

	public static ArrayList<LocalDateTime> taskDateTime = new ArrayList<LocalDateTime>();
	public static ArrayList<String> taskTimeType = new ArrayList<String>();
}
