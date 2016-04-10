//@@author A0127358Y
package urgenda.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Pattern;

import urgenda.storage.Storage;
import urgenda.storage.StorageTester;
import urgenda.util.DateTimePair;
import urgenda.util.InvalidFolderException;
import urgenda.util.MultipleSlot;
import urgenda.util.StateFeedback;
import urgenda.util.StorageException;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

/**
 * LogicData class of the Logic component in Urgenda. Responsible for the
 * handling of the different tasks of the user. Main class where all the
 * manipulation of the task objects are carried out. Also responsible for
 * storing of all the tasks.
 *
 */
public class LogicData {

	/**
	 * Different DisplayState for LogicData which represents the state of
	 * display given to the user
	 *
	 */
	public enum DisplayState {
		ALL_TASKS, MULTIPLE_DELETE, MULTIPLE_COMPLETE, MULTIPLE_PRIORITISE, SHOW_SEARCH, EXIT, INVALID_COMMAND, HELP, INVALID_TASK, ARCHIVE, FIND_FREE, DEMO, HIDE
	}

	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static LogicData _logicData;

	// storage object for retrieval and storing of tasks to data format
	private Storage _storage;

	// for storage of full lists of tasks
	private ArrayList<Task> _tasks;
	// for storage of all completed tasks
	private ArrayList<Task> _archives;
	// for storage of tasks being displayed to user by last command
	private ArrayList<Task> _displays;
	// for storage of tasks to have more details to be displayed
	private ArrayList<Task> _showMoreTasks;

	private Task _taskPointer;
	private DisplayState _currState;
	private int _currentId;

	/*
	 * Default constructor where singleton pattern is applied. To initialise the
	 * variables within LogicData.
	 */
	private LogicData() {
		logger.getLogger().info("constructing LogicData Object");
		_storage = new Storage();
		_tasks = _storage.updateCurrentTaskList();
		_archives = _storage.updateArchiveTaskList();
		_currState = DisplayState.ALL_TASKS;
		_displays = new ArrayList<Task>();
		_showMoreTasks = new ArrayList<Task>();
		// sets the next id to be used for future labelling of tasks
		_currentId = _tasks.size() + 1;
	}

	/*
	 * Alternative constructor where singleton pattern is applied. To initialise
	 * the variables within LogicData when doing testing.
	 */
	private LogicData(boolean isTest) {
		// stubs storage with storagetester
		_storage = new StorageTester();
		_tasks = _storage.updateCurrentTaskList();
		_archives = _storage.updateArchiveTaskList();
		_currState = DisplayState.ALL_TASKS;
		_displays = new ArrayList<Task>();
		_showMoreTasks = new ArrayList<Task>();
		_currentId = _tasks.size() + 1;
	}

	/**
	 * Singleton pattern method for instantiation of LogicData to avoid multiple
	 * copies of tasks for the user
	 * 
	 * @return LogicData object that is used currently or created
	 */
	public static LogicData getInstance() {
		if (_logicData == null) {
			logger.getLogger().info("creating instance of logicData");
			_logicData = new LogicData();
		}
		logger.getLogger().info("retrieving prev instance of logicData");
		return _logicData;
	}

	/**
	 * Alternate constructor for singleton pattern for stubbing of storage when
	 * testing
	 * 
	 * @param isTest
	 *            boolean of checking if the current mode is in testing
	 * @return LogicData object that is used currently or created
	 */
	public static LogicData getInstance(boolean isTest) {
		if (_logicData == null) {
			_logicData = new LogicData(isTest);
		}
		return _logicData;
	}

	/**
	 * Method used for saving all current contents into the data file for usage
	 * in the future.
	 */
	public void saveContents() {
		logger.getLogger().info("Saving contents to storage");
		_storage.save(_tasks, _archives);
	}

	/**
	 * Method to retrieve the current state of LogicData into a StateFeedback
	 * object for packaging all required information. Different sets of lists
	 * are used for different states that it is currently in.
	 * 
	 * @return StateFeedback object that holds all the required information
	 */
	public StateFeedback getState() {
		StateFeedback state;
		switch (_currState) {
		case ALL_TASKS:
			state = displayAllTasks(_tasks);
			state.setState(StateFeedback.State.ALL_TASKS);
			break;
		case MULTIPLE_DELETE: // Fallthrough
		case MULTIPLE_COMPLETE: // Fallthrough
		case MULTIPLE_PRIORITISE:
			state = displayAllTasks(_displays);
			state.setState(StateFeedback.State.MULTIPLE_MATCHES);
			break;
		case SHOW_SEARCH:
			state = displayAllTasks(_displays);
			state.setState(StateFeedback.State.SHOW_SEARCH);
			break;
		case HELP:
			state = displayAllTasks(_displays);
			state.setState(StateFeedback.State.SHOW_HELP);
			break;
		case EXIT:
			saveContents();
			state = displayAllTasks(_tasks);
			state.setState(StateFeedback.State.EXIT);
			break;
		case INVALID_COMMAND: // Fallthrough
		case INVALID_TASK:
			state = displayAllTasks(_tasks);
			state.setState(StateFeedback.State.ERROR);
			break;
		case ARCHIVE:
			state = displayArchiveTasks(_archives);
			state.setState(StateFeedback.State.ARCHIVE);
			break;
		case FIND_FREE:
			state = displayAllTasks(_displays);
			state.setState(StateFeedback.State.FIND_FREE);
			break;
		case DEMO:
			state = displayAllTasks(_displays);
			state.setState(StateFeedback.State.DEMO);
			break;
		case HIDE:
			state = displayAllTasks(_displays);
			state.setState(StateFeedback.State.HIDE);
			break;
		default:
			state = displayAllTasks(_tasks);
			state.setState(StateFeedback.State.ALL_TASKS);
			break;
		}
		return state;
	}

	/**
	 * Method for setting the display list to show archive tasks
	 * 
	 * @param displayList
	 *            Archive list for putting as display
	 * @return StateFeedback object containing the list given
	 */
	public StateFeedback displayArchiveTasks(ArrayList<Task> displayList) {
		clearDisplays();
		_displays.addAll(sortArchive(displayList));
		StateFeedback state = new StateFeedback(_displays, _displays.size());
		setOverdueCount(state);
		setFeedbackDisplayPosition(state);
		setShowMorePositions(state);
		return state;
	}

	/**
	 * Method for setting the display list to show the current list given. Sorts
	 * the list according to Overdue, Today, Others.
	 * 
	 * @param displayList
	 *            Display List given for putting as display
	 * @return StateFeedback object containing the list given
	 */
	public StateFeedback displayAllTasks(ArrayList<Task> displayList) {
		ArrayList<Task> overdueTasks = new ArrayList<Task>();
		ArrayList<Task> todayTasks = new ArrayList<Task>();
		ArrayList<Task> otherTasks = new ArrayList<Task>();
		sortTasksIntoList(displayList, overdueTasks, todayTasks, otherTasks);
		clearDisplays();
		_displays.addAll(sortList(overdueTasks));
		_displays.addAll(sortList(todayTasks));
		_displays.addAll(sortList(otherTasks));

		assert (overdueTasks.size() >= 0);
		assert (todayTasks.size() >= 0);
		assert (otherTasks.size() >= 0);

		StateFeedback state = new StateFeedback(_displays, overdueTasks.size(), todayTasks.size(),
				otherTasks.size());
		setOverdueCount(state);
		setFeedbackDisplayPosition(state);
		setShowMorePositions(state);
		return state;
	}

	private void sortTasksIntoList(ArrayList<Task> displayList, ArrayList<Task> overdueTasks,
			ArrayList<Task> todayTasks, ArrayList<Task> otherTasks) {
		for (Task task : displayList) {
			if (task.isOverdue()) {
				overdueTasks.add(task);
			} else if (isTaskToday(task)) {
				todayTasks.add(task);
			} else { // remaining tasks and floating tasks
				otherTasks.add(task);
			}
		}
	}

	private void setOverdueCount(StateFeedback state) {
		int count = 0;
		for (Task task : _tasks) {
			if (task.isOverdue()) {
				count++;
			}
		}
		assert (count >= 0);
		state.setOverdueCount(count);
	}

	private void setShowMorePositions(StateFeedback state) {
		for (Task task : _showMoreTasks) {
			if (_displays.contains(task)) {
				state.addDetailedTaskIdx(_displays.indexOf(task));
			}
		}
	}

	/*
	 * sets the position if the pointer matches the display
	 */
	private void setFeedbackDisplayPosition(StateFeedback state) {
		if (_taskPointer != null && _displays.contains(_taskPointer)) {
			state.setDisplayPosition(_displays.indexOf(_taskPointer));
		} else {
			// sets to 0 (default) if no specific task to pointed at
			state.setDisplayPosition(0);
		}
		// clears task pointer for next iteration
		_taskPointer = null;
	}

	/**
	 * Method for checking whether the task falls on today (calendar time).
	 * 
	 * @param task
	 *            task given for checking if falls on today.
	 * @return boolean true or false
	 */
	public boolean isTaskToday(Task task) {
		LocalDate now = LocalDate.now();
		if (task.getTaskType() == Task.Type.DEADLINE) {
			return verifyEndTime(task, now);
		} else if (task.getTaskType() == Task.Type.EVENT) {
			// check whether any of the time within the time span of the event
			// falls on today
			return verifyTimeSpan(task, now);
		} else { // Type is floating
			return false;
		}
	}

	/*
	 * method for checking whether an event falls on today (including
	 * consideration of tasks that spans multiple days
	 */
	private boolean verifyTimeSpan(Task task, LocalDate now) {
		if (task.getStartTime().toLocalDate().isEqual(now) || task.getEndTime().toLocalDate().isEqual(now)
				|| task.getStartTime().toLocalDate().isBefore(now)
						&& task.getEndTime().toLocalDate().isAfter(now)) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * method for checking whether a deadline task falls on today
	 */
	private boolean verifyEndTime(Task task, LocalDate now) {
		if (task.getEndTime().toLocalDate().isEqual(now)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Method for updating the state of the system e.g. called after every
	 * execution of command so that GUI knows the state for display.
	 */
	public void updateState() {
		logger.getLogger().info("Updating state of prog");
		ArrayList<Task> completedTasks = new ArrayList<Task>();
		LocalDateTime now = LocalDateTime.now();
		for (Task task : _tasks) {
			checkDeadline(now, task);
			checkMultipleSlot(task);
			checkEvents(completedTasks, now, task);
		}
		_tasks.removeAll(completedTasks);
		_archives.addAll(completedTasks);
		assert(_archives.size() >= 0);
	}

	private void checkEvents(ArrayList<Task> completedTasks, LocalDateTime now, Task task) {
		if (task.getTaskType() == Task.Type.EVENT) {
			if (task.getEndTime().isBefore(now)) {
				task.setIsCompleted(true);
				task.setDateModified(LocalDateTime.now());
				logger.getLogger().info("event task" + task + " has been completed");
				// only moves completed tasks when the day ends for that day
				if (task.getEndTime().toLocalDate().isBefore(LocalDate.now())) {
					completedTasks.add(task);
					task.setDateModified(LocalDateTime.now());
				}
			}
		}
	}

	private void checkMultipleSlot(Task task) {
		if (task.getSlot() != null) {
			updateMultipleSlot(task);
		}
	}

	private void checkDeadline(LocalDateTime now, Task task) {
		if (task.getTaskType() == Task.Type.DEADLINE) {
			if (task.getEndTime().isBefore(now)) {
				task.setIsOverdue(true);
				task.setDateModified(LocalDateTime.now());
				logger.getLogger().info("deadline task " + task + " has turned overdue");
			} else {
				task.setIsOverdue(false);
			}
		}
	}

	/**
	 * Method for updating the Multipleslot attribute (for blocking of several
	 * timings for same task) of a given task.
	 * 
	 * @param task
	 *            The task specified for updating of multipleslot.
	 */
	public void updateMultipleSlot(Task task) {
		LocalDateTime now = LocalDateTime.now();
		if (task.getStartTime() == null || task.getEndTime() == null) {
			return;
		}
		task.getSlot().addTimeSlot(task.getStartTime(), task.getEndTime());
		task.getSlot().sortSlots();
		updateTimeSlots(task, now);
		checkEmptySlot(task);
	}

	private void checkEmptySlot(Task task) {
		if (task.getSlot().isEmpty()) {
			task.setSlot(null);
		}
	}

	private void updateTimeSlots(Task task, LocalDateTime now) {
		do {
			DateTimePair newTime = task.getSlot().getNextSlot();
			task.setStartTime(newTime.getEarlierDateTime());
			task.setEndTime(newTime.getLaterDateTime());
			task.getSlot().removeNextSlot();
		} while (task.getEndTime().isBefore(now) && !(task.getSlot().isEmpty()));
	}

	/**
	 * Method for finding tasks w desc that matches input (in same order)
	 * regardless of case. e.g. for input of abc, Abc and ABCDDD will all be
	 * returned.
	 * 
	 * @param desc
	 *            The description given by user to find matches.
	 * @return ArrayList containing all matching tasks.
	 */
	public ArrayList<Task> findMatchingDesc(String desc) {
		ArrayList<Task> matches = new ArrayList<Task>();
		if (!desc.equals("")) {
			searchForMatchingDescInDisplayList(desc, matches);
		}
		logger.getLogger().info("Find matching desc: " + desc);
		return matches;
	}

	private void searchForMatchingDescInDisplayList(String desc, ArrayList<Task> matches) {
		for (Task task : _displays) {
			if (Pattern.compile(Pattern.quote(desc), Pattern.CASE_INSENSITIVE).matcher(task.getDesc())
					.find()) {
				matches.add(task);
			}
		}
	}

	/**
	 * Method for finding tasks w desc that matches input (more than one word)
	 * (words dont need be in same order) regardless of case. e.g. for input of
	 * Sweden Holiday, Holiday to Swenden and Sweden Public Holiday will both be
	 * returned.
	 * 
	 * @param desc
	 *            The desc given by user for finding matches within current task
	 *            list.
	 * @return ArrayList containing all the tasks with matching desc.
	 */
	public ArrayList<Task> findRefinedMatchingDesc(String desc) {
		ArrayList<Task> matches = new ArrayList<Task>();
		if (!desc.equals("")) {
			String[] substr = desc.split("\\s+");
			ArrayList<String> substr2 = new ArrayList<String>(Arrays.asList(substr));
			if (substr2.size() > 1) {
				searchForNonOrderedDesc(matches, substr2);
			} else {
				searchForMatchingDescInDisplayList(desc, matches);
			}
		}
		return matches;
	}

	private void searchForNonOrderedDesc(ArrayList<Task> matches, ArrayList<String> substr2) {
		logger.getLogger().info("searching fo non-ordered desc (input more than one word");
		for (Task task : _displays) {
			boolean flag = true;
			for (String s : substr2) {
				if (!(Pattern.compile(Pattern.quote(s + " "), Pattern.CASE_INSENSITIVE)
						.matcher(task.getDesc()).find())
						&& !(Pattern.compile(Pattern.quote(" " + s), Pattern.CASE_INSENSITIVE)
								.matcher(task.getDesc()).find())
						&& flag) {
					flag = false;
				}
			}
			if (flag) {
				matches.add(task);
			}
		}
	}

	/**
	 * Method for adding a completed task to archive.
	 * 
	 * @param task
	 *            The task to be moved to archive
	 */
	public void addArchive(Task task) {
		logger.getLogger().info("adding task " + task + " to archive");
		_archives.add(task);
	}

	/**
	 * Method for adding multiple completed tasks to archive at once.
	 * 
	 * @param tasks
	 *            The ArrayList of tasks to be moved to archive
	 */
	public void addArchive(ArrayList<Task> tasks) {
		logger.getLogger().info("adding task multiple tasks to archive");
		_archives.addAll(tasks);
	}

	/**
	 * Method for removing a task from archive.
	 * 
	 * @param tasks
	 *            The task to be removed from archive.
	 */
	public void removeArchive(Task task) {
		_archives.remove(task);
	}

	/**
	 * Method for removing multiple tasks from archive at once.
	 * 
	 * @param tasks
	 *            The ArrayList of tasks to be removed from archive.
	 */
	public void removeArchive(ArrayList<Task> tasks) {
		_archives.removeAll(tasks);
	}

	/**
	 * Getter for getting the private attribute _tasks in logicData.
	 * 
	 * @return _task, the list containing all current tasks.
	 */
	public ArrayList<Task> getTaskList() {
		return _tasks;
	}

	/**
	 * Getter for getting the latest id assigned to a new task.
	 * 
	 * @return _currentId, the current id of the system.
	 */
	public int getCurrentId() {
		return _currentId;
	}

	/**
	 * Method for updating the id of task list to the most recent number in
	 * logicData.
	 */
	public void updateCurrentId() {
		_currentId++;
	}

	/**
	 * Method for adding a new Task into the taskList in LogicData.
	 * 
	 * @param newTask
	 *            The new task given to be added to current task list.
	 */
	public void addTask(Task newTask) {
		_tasks.add(newTask);
	}

	/**
	 * Method for adding multiple new Tasks into the taskList in LogicData.
	 * 
	 * @param tasks
	 *            The ArrayList of new tasks to be added to current task list.
	 */
	public void addTasks(ArrayList<Task> tasks) {
		_tasks.addAll(tasks);
	}

	/**
	 * Method for removing a task from the taskList and archive in LogicData.
	 * 
	 * @param task
	 *            The task specified for the removal from Urgenda.
	 */
	public void deleteTask(Task task) {
		_tasks.remove(task);
		_archives.remove(task);
	}

	/**
	 * Method for removing a task from the taskList in LogicData.
	 * 
	 * @param tasks
	 *            The ArrayList of tasks specified for the removal from Urgenda.
	 */
	public void deleteTasks(ArrayList<Task> tasks) {
		_tasks.removeAll(tasks);
		_archives.removeAll(tasks);
	}

	/**
	 * Method for retrieving a task based on specified id (position in task list
	 * _tasks).
	 * 
	 * @param id
	 *            The id (position) specified for locating of a task.
	 * @return the task which corresponds to the position in _tasks(id) or null
	 *         if couldn't find a valid match.
	 */
	public Task findMatchingPosition(int id) {
		logger.getLogger().info("Find matching position, " + id);
		if (id >= 0 && id < _displays.size()) {
			logger.getLogger().info("Matching position found: " + id);
			return _displays.get(id);
		} else {
			logger.getLogger().info("Matching position not found");
			return null;
		}
	}

	/**
	 * Overloaded method for finding matching positions w arraylists of id given
	 * as input.
	 * 
	 * @param idPositions
	 *            The ArrayList of id (positions) specified for retrieving and
	 *            locating of multiple tasks.
	 * @return ArrayList of task which corresponds to the idpositions, none
	 *         valid id will just be ignored.
	 */
	public ArrayList<Task> findMatchingPosition(ArrayList<Integer> idPositions) {
		ArrayList<Task> matches = new ArrayList<Task>();
		for (Integer i : idPositions) {
			if (i != null && i >= 0 && i < _displays.size()) {
				matches.add(_displays.get(i));
				logger.getLogger().info("Found matching position, " + i);
			}
		}
		if (matches.isEmpty()) {
			return null;
		} else {
			return matches;
		}
	}

	/**
	 * Method for retrieving tasks that matches date specified by input.
	 * 
	 * @param input
	 *            The date specified for searching of tasks falling on it.
	 * @return Arraylist of tasks that matches input date.
	 */
	public ArrayList<Task> findMatchingDates(LocalDate input) {
		logger.getLogger().info("Find matching dates, " + input);
		ArrayList<Task> matches = new ArrayList<Task>();
		for (Task task : _displays) {
			if (task.getTaskType() == Task.Type.DEADLINE) {
				getDeadLineWithMatchingDate(input, matches, task);
			} else if (task.getTaskType() == Task.Type.EVENT) {
				getEventWithMatchingDate(input, matches, task);
			}
		}
		return matches;
	}

	private void getEventWithMatchingDate(LocalDate input, ArrayList<Task> matches, Task task) {
		if (task.getStartTime().toLocalDate().isEqual(input)
				|| task.getEndTime().toLocalDate().isEqual(input)) {
			matches.add(task);
		} else if (task.getStartTime().toLocalDate().isBefore(input)
				&& task.getEndTime().toLocalDate().isAfter(input)) {
			matches.add(task);
		} else if (task.getSlot() != null && !task.getSlot().isEmpty()) {
			checkForMatchingDateWithinSlots(input, matches, task);
		}
	}

	private void checkForMatchingDateWithinSlots(LocalDate input, ArrayList<Task> matches, Task task) {
		ArrayList<DateTimePair> slots = task.getSlot().getSlots();
		for (DateTimePair pair : slots) {
			if (pair.getEarlierDateTime().toLocalDate().isEqual(input)
					|| pair.getLaterDateTime().toLocalDate().isEqual(input)) {
				matches.add(task);
			} else if (pair.getEarlierDateTime().toLocalDate().isBefore(input)
					&& pair.getLaterDateTime().toLocalDate().isAfter(input)) {
				matches.add(task);
			}
		}
	}

	private void getDeadLineWithMatchingDate(LocalDate input, ArrayList<Task> matches, Task task) {
		if (task.getEndTime().toLocalDate().isEqual(input)) {
			matches.add(task);
		}
	}

	/**
	 * Method for retrieving tasks that matches both date and time specified by
	 * input.
	 * 
	 * @param input
	 *            The DateTime specified for searching of tasks falling it.
	 * @return Arraylist of tasks that matches input datetime.
	 */
	public ArrayList<Task> findMatchingDateTimes(LocalDateTime input) {
		logger.getLogger().info("Find matching datetime, " + input);
		ArrayList<Task> matches = new ArrayList<Task>();
		for (Task task : _displays) {
			if (task.getTaskType() == Task.Type.DEADLINE) {
				getDeadLineWithMatchingDateTime(input, matches, task);
			} else if (task.getTaskType() == Task.Type.EVENT) {
				getEventWithMatchingDateTime(input, matches, task);
			}
		}
		return matches;
	}

	private void getEventWithMatchingDateTime(LocalDateTime input, ArrayList<Task> matches, Task task) {
		if (task.getStartTime().isEqual(input) || task.getEndTime().isEqual(input)) {
			matches.add(task);
		} else if (task.getStartTime().isBefore(input) && task.getEndTime().isAfter(input)) {
			matches.add(task);
		} else if (task.getSlot() != null && !task.getSlot().isEmpty()) {
			getMatchingDateTimeWithinSlots(input, matches, task);
		}
	}

	private void getMatchingDateTimeWithinSlots(LocalDateTime input, ArrayList<Task> matches, Task task) {
		ArrayList<DateTimePair> slots = task.getSlot().getSlots();
		for (DateTimePair pair : slots) {
			if (pair.getEarlierDateTime().isEqual(input) || pair.getLaterDateTime().isEqual(input)) {
				matches.add(task);
			} else if (pair.getEarlierDateTime().isBefore(input) && pair.getLaterDateTime().isAfter(input)) {
				matches.add(task);
			}
		}
	}

	private void getDeadLineWithMatchingDateTime(LocalDateTime input, ArrayList<Task> matches, Task task) {
		if (task.getEndTime().isEqual(input)) {
			matches.add(task);
		}
	}

	/**
	 * Method for retrieving tasks that falls on the month specified by input.
	 * 
	 * @param input
	 *            The Month specified for searching of tasks falling on it.
	 * @return Arraylist of tasks that matches input month.
	 */
	public ArrayList<Task> findMatchingMonths(Month input) {
		logger.getLogger().info("Find matching months, " + input);
		ArrayList<Task> matches = new ArrayList<Task>();
		for (Task task : _displays) {
			if (task.getTaskType() == Task.Type.DEADLINE) {
				getDeadLineWithMatchingMonth(input, matches, task);
			} else if (task.getTaskType() == Task.Type.EVENT) {
				getEventWithMatchingMonth(input, matches, task);
			}
		}
		return matches;
	}

	private void getEventWithMatchingMonth(Month input, ArrayList<Task> matches, Task task) {
		if (task.getStartTime().getMonth() == input || task.getEndTime().getMonth() == input) {
			matches.add(task);
		} else if (task.getStartTime().getMonth() == input && task.getEndTime().getMonth() == input) {
			matches.add(task);
		} else if (task.getSlot() != null && !task.getSlot().isEmpty()) {
			getMatchingMonthWithinSlots(input, matches, task);
		}
	}

	private void getMatchingMonthWithinSlots(Month input, ArrayList<Task> matches, Task task) {
		ArrayList<DateTimePair> slots = task.getSlot().getSlots();
		for (DateTimePair pair : slots) {
			if (pair.getEarlierDateTime().getMonth() == input
					|| pair.getLaterDateTime().getMonth() == input) {
				matches.add(task);
			} else if (pair.getEarlierDateTime().getMonth() == input
					&& pair.getLaterDateTime().getMonth() == input) {
				matches.add(task);
			}
		}
	}

	private void getDeadLineWithMatchingMonth(Month input, ArrayList<Task> matches, Task task) {
		if (task.getEndTime().getMonth() == input) {
			matches.add(task);
		}
	}

	/**
	 * Method for finding task that has the same multipleslot as given input.
	 * 
	 * @param block
	 *            The MultipleSlot variable for searching of tasks that matches
	 *            this block.
	 * @return ArrayList of task that matches block.
	 */
	public ArrayList<Task> findBlocks(MultipleSlot block) {
		ArrayList<Task> _blocks = new ArrayList<Task>();
		for (Task task : _tasks) {
			if (task.getSlot() != null) {
				if (task.getSlot().equals(block)) {
					_blocks.add(task);
				}
			}
		}
		return _blocks;
	}

	/**
	 * Method for sorting of list of task according to datetime and priority and
	 * alpha order for tasks w/o datetime (floating).
	 * 
	 * @param list
	 *            The ArrayList of tasks to be sorted.
	 * @return sorted list.
	 */
	public ArrayList<Task> sortList(ArrayList<Task> list) {
		Collections.sort(list, comparator);
		Collections.sort(list, imptComparator);
		return list;
	}

	/*
	 * comparator for comparison of timing
	 */
	static Comparator<Task> comparator = new Comparator<Task>() {
		public int compare(final Task o1, final Task o2) {
			LocalDateTime c1, c2;
			if (o1.getStartTime() != null) {
				c1 = o1.getStartTime();
			} else {
				c1 = o1.getEndTime();
			}
			if (o2.getStartTime() != null) {
				c2 = o2.getStartTime();
			} else {
				c2 = o2.getEndTime();
			}
			if (c1 == null && c2 == null) {
				return o1.getDesc().compareToIgnoreCase(o2.getDesc());
			} else if (c1 == null) {
				return 1;
			} else if (c2 == null) {
				return -1;
			} else {
				if (c1.compareTo(c2) == 0) {
					return o1.getDesc().compareToIgnoreCase(o2.getDesc());
				}
				return c1.compareTo(c2);
			}
		}
	};

	/*
	 * comparator for comparing of priority status
	 */
	static Comparator<Task> imptComparator = new Comparator<Task>() {
		public int compare(final Task o1, final Task o2) {
			int compare = 0;
			if (o1.isImportant() && o2.isImportant()) {
				compare = 0;
			} else if (o1.isImportant()) {
				compare = -1;
			} else if (o2.isImportant()) {
				compare = 1;
			}
			return compare;
		}
	};

	/**
	 * Method for sorting list of tasks in archive according to date modified.
	 * 
	 * @param list
	 *            The ArrayList of archive tasks to be sorted.
	 * @return sorted list
	 */
	public ArrayList<Task> sortArchive(ArrayList<Task> list) {
		Collections.sort(list, archiveComparator);
		return list;
	}

	/*
	 * new comparator for sorting of archive, compares datemodified.
	 */
	static Comparator<Task> archiveComparator = new Comparator<Task>() {
		public int compare(final Task o1, final Task o2) {
			return o2.getDateModified().compareTo(o1.getDateModified());
		}
	};

	/**
	 * Getter for retrieving display list (all tasks currently displayed to
	 * user), the private attribute in LogicData.
	 * 
	 * @return _displays, the private attribute in logicData.
	 */
	public ArrayList<Task> getDisplays() {
		return _displays;
	}

	/**
	 * Getter for retrieving archive list (all tasks currently displayed to
	 * user), the private attribute in LogicData.
	 * 
	 * @return _archives, the private attribute in logicData.
	 */
	public ArrayList<Task> getArchives() {
		return _archives;
	}

	/**
	 * Setter for setting the attribute _display in logicData as the specified
	 * ArrayList<Task> input.
	 * 
	 * @param displays
	 *            The ArrayList to be set as display.
	 */
	public void setDisplays(ArrayList<Task> displays) {
		_displays = displays;
	}

	/**
	 * Getter for getting the currState of the program e.g. the state shown by
	 * ui.
	 * 
	 * @return _currState, a DisplayState private attribute in LogicData
	 */
	public DisplayState getCurrState() {
		return _currState;
	}

	/**
	 * Setter for setting currState of program to a specified Display State
	 * given by input e.g. indicating to UI the state of the system after a
	 * command is executed.
	 * 
	 * @param currState
	 *            The DisplayState to be set as current state of the system.
	 */
	public void setCurrState(DisplayState currState) {
		_currState = currState;
	}

	/**
	 * Method for clearing display list.
	 */
	public void clearDisplays() {
		_displays.clear();
	}

	/**
	 * Method for testing purposes only. Use in FreeTimeTest.java for clearing
	 * of entire tasklist.
	 */
	public void clearTasks() {
		_tasks.clear();
	}

	/**
	 * Method for retrieving userguide (help for user).
	 * 
	 * @return _storage.retrieveHelp, the help file stored in storage.
	 */
	public ArrayList<String> generateHelpManual() {
		return _storage.getHelp();
	}

	/**
	 * Getter for getting the selector position of the program e.g. the task
	 * that is currently pointing at.
	 * 
	 * @return _taskPointer, the private attribute in logicData which is the
	 *         selector of Urgenda.
	 */
	public Task getTaskPointer() {
		return _taskPointer;
	}

	/**
	 * Setter for setting the position of the selector, e.g. which task to point
	 * to by UI.
	 * 
	 * @param taskPointer
	 *            The Task for setting the pointer to be pointing at.
	 */
	public void setTaskPointer(Task taskPointer) {
		_taskPointer = taskPointer;
	}

	/**
	 * Method for getting the showmore status of a given task.
	 * 
	 * @param task
	 *            The task specified for checking of showmore status.
	 * @return true if status isShowingMore, false otherwise.
	 */
	public boolean isShowingMore(Task task) {
		return _showMoreTasks.contains(task);
	}

	/**
	 * Method to toggle showmore status of a specified task.
	 * 
	 * @param task
	 *            The task specified for toggling of showmore status.
	 */
	public void toggleShowMoreTasks(Task task) {
		logger.getLogger().info("toggle showmore status of " + task);
		if (_showMoreTasks.contains(task)) {
			_showMoreTasks.remove(task);
		} else {
			_showMoreTasks.add(task);
		}
	}

	/**
	 * Method for setting status of all task to not showing more.
	 */
	public void clearShowMoreTasks() {
		_showMoreTasks.clear();
	}

	/**
	 * Method to get current file directory.
	 * 
	 * @return _storage.getDirPath
	 */
	public String retrieveCurrentDirectory() {
		return _storage.getDirPath();
	}

	/**
	 * Method for changing file directory of where task is saved to.
	 * 
	 * @param path
	 *            The String path specified for changing of current directory to
	 *            the new one.
	 * @throws StorageException if invalid input format.
	 * @throws InvalidFolderException if folder to be saved to does not exist.
	 */
	public void changeDirectory(String path) throws StorageException, InvalidFolderException {
		_storage.changeFileSettings(path);
	}

	/**
	 * Method for retrieving all the tasks in tasklist that a specified task
	 * overlaps with.
	 * 
	 * @param newTask
	 *            The task used for comparing with all other tasks in LogicData
	 *            for searching of overlap in timing.
	 * @return ArrayList of overlapping tasks
	 */
	public ArrayList<Task> overlappingTasks(Task newTask) {
		ArrayList<Task> overlaps = new ArrayList<Task>();

		for (Task task : _tasks) {
			if (!task.equals(newTask)) {
				if (task.getTaskType() == Task.Type.EVENT) {
					if (task.isOverlapping(newTask)) {
						overlaps.add(task);
					}
				}
				// case when there are multiple slots, multiple copies of
				// overlaps are added
				if (task.getSlot() != null && !task.getSlot().isEmpty()) {
					getOverlapsWithinSlots(newTask, overlaps, task);
				}
			}
		}

		return overlaps;
	}

	private void getOverlapsWithinSlots(Task newTask, ArrayList<Task> overlaps, Task task) {
		MultipleSlot slot = new MultipleSlot(task.getSlot());
		while (!slot.isEmpty()) {
			DateTimePair pair = slot.getNextSlot();
			slot.removeNextSlot();
			Task currTask = new Task(task.getDesc(), task.getLocation(), pair.getDateTime1(),
					pair.getDateTime2());
			if (currTask.isOverlapping(newTask)) {
				overlaps.add(currTask);
			}
		}
	}

	/**
	 * Method for removing archived tasks that are beyond one month old
	 */
	public void clearOldArchive() {
		ArrayList<Task> outdatedTasks = new ArrayList<Task>();
		for (Task task : _archives) {
			if (task.getDateModified().isBefore(LocalDateTime.now().minusMonths(1))) {
				outdatedTasks.add(task);
			}
		}
		_archives.removeAll(outdatedTasks);
	}

	/**
	 * Method for checking whether taskpointer is currently pointing to an
	 * archived task. If yes, set Displaystate as archive.
	 */
	public void checkPointer() {
		if (_taskPointer != null) {
			if (_archives.contains(_taskPointer)) {
				_currState = LogicData.DisplayState.ARCHIVE;
			}
		}
	}

	/**
	 * Method to reinitialise Storage, called upon very launch of program.
	 */
	public void reinitialiseStorage() {
		_storage = new Storage();
		_tasks = _storage.updateCurrentTaskList();
		_archives = _storage.updateArchiveTaskList();
	}

	/**
	 * Method for to reinitialise Storage for integration testing.
	 */
	public void reinitialiseStorageTester() {
		_storage.delete();
		_storage = new StorageTester();
		_tasks = _storage.updateCurrentTaskList();
		_archives = _storage.updateArchiveTaskList();
	}

	/**
	 * Method for retrieving demo file stored in storage.
	 * 
	 * @return _storage.getDemoText().
	 */
	public ArrayList<String> generateDemoText() {
		return _storage.getDemoText();
	}

	/**
	 * Method for retrieving Indexes selected for demo.
	 * 
	 * @return _storage.getDemoSelectionIndexes().
	 */
	public ArrayList<Integer> generateDemoSelectionIndexes() {
		return _storage.getDemoSelectionIndexes();
	}

	/**
	 * Method for retrieving settings for novice view.
	 * 
	 * @return _storage.getNoviceSettings();
	 */
	public boolean getNoviceSettings() {
		return _storage.getNoviceSettings();
	}

	/**
	 * Sets the novice or advanced view settings.
	 * 
	 * @param isNovice
	 *            boolean to show novice or advanced view
	 */
	public void setNoviceSettings(boolean isNovice) {
		_storage.setNoviceSettings(isNovice);
	}

}
