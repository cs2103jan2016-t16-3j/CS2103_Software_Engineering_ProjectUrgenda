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
		ADD, DELETE, COMPLETE, EDIT, HELP, SEARCH, SHOW_DETAILS, BLOCK, 
		FIND_FREE, HOME, UNDO, REDO, SHOW_ARCHIVE, CONFIRM, SET_DIRECTORY, PRIORITISE, 
		POSTPONE, INVALID, EXIT
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
	public static final Set<String> doneKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "done", "complete", "completed", "mark", "finish", "fin" }));
	public static final Set<String> updateKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "edit", "change", "update", "mod" }));
	public static final Set<String> searchKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "find", "show", "view", "list", "search", "#\\w+" }));
	public static final Set<String> showDetailsKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "showmore" }));
	public static final Set<String> homeKeyWords = new HashSet<String>(Arrays.asList(new String[] { "home" }));
	public static final Set<String> blockKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "block", "rev", "reserve", "alloc" }));
	public static final Set<String> findFreeKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "findfree", "free", "checkfree" }));
	public static final Set<String> undoKeywords = new HashSet<String>(Arrays.asList(new String[] { "undo" }));
	public static final Set<String> redoKeywords = new HashSet<String>(Arrays.asList(new String[] { "redo" }));
	public static final Set<String> helpKeyWords = new HashSet<String>(Arrays.asList(new String[] { "help" }));
	public static final Set<String> archiveKeyWords = new HashSet<String>(Arrays.asList(new String[] { "archive" }));
	public static final Set<String> prioritiseKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "urgent", "important", "pri", "impt" }));
	public static final Set<String> postponeKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "postpone", "delay", "move" }));
	public static final Set<String> confirmKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "confirm", "cmf" }));
	public static final Set<String> setDirectoryKeyWords = new HashSet<String>(
			Arrays.asList(new String[] { "saveto", "cd" }));
	public static final Set<String> exitKeyWords = new HashSet<String>(Arrays.asList(new String[] { "exit", "quit" }));

	public static final Set<String> janWords = new HashSet<String>(Arrays.asList(new String[] { "jan", "january" }));
	public static final Set<String> febWords = new HashSet<String>(Arrays.asList(new String[] { "feb", "february" }));
	public static final Set<String> marWords = new HashSet<String>(Arrays.asList(new String[] { "mar", "march" }));
	public static final Set<String> aprWords = new HashSet<String>(Arrays.asList(new String[] { "apr", "april" }));
	public static final Set<String> mayWords = new HashSet<String>(Arrays.asList(new String[] { "may" }));
	public static final Set<String> junWords = new HashSet<String>(Arrays.asList(new String[] { "jun", "june" }));
	public static final Set<String> julWords = new HashSet<String>(Arrays.asList(new String[] { "jul", "july" }));
	public static final Set<String> augWords = new HashSet<String>(Arrays.asList(new String[] { "aug", "august" }));
	public static final Set<String> sepWords = new HashSet<String>(
			Arrays.asList(new String[] { "sep", "sept", "september" }));
	public static final Set<String> octWords = new HashSet<String>(Arrays.asList(new String[] { "oct", "october" }));
	public static final Set<String> novWords = new HashSet<String>(Arrays.asList(new String[] { "nov", "november" }));
	public static final Set<String> decWords = new HashSet<String>(Arrays.asList(new String[] { "dec", "december" }));

	public static final Set<String> startTimeWords = new HashSet<String>(
			Arrays.asList(new String[] { "on", "at", "from", "of", "starting" }));
	public static final Set<String> endTimeWords = new HashSet<String>(
			Arrays.asList(new String[] { "by", "latest", "before" }));
	public static final Set<String> periodWords = new HashSet<String>(
			Arrays.asList(new String[] { "between", "within", "spanning" }));

	public static int taskIndex;
	public static ArrayList<Integer> positions;
	public static String taskDescription;
	public static String taskLocation;
	public static LocalDateTime taskStartTime;
	public static LocalDateTime taskEndTime;
	public static ArrayList<String> taskHashtags;
	public static MultipleSlot taskSlots;

	public static TASK_TYPE taskType;
	public static COMMAND_TYPE commandType;

	public static ArrayList<LocalDateTime> taskDateTime;
	public static ArrayList<String> taskTimeType;

}
