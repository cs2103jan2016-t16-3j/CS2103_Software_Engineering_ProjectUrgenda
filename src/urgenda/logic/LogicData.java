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
import urgenda.util.MultipleSlot;
import urgenda.util.StateFeedback;
import urgenda.util.StorageException;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

/**
 * LogicData class of the Logic component in Urgenda.
 * Responsible for the handling of the different tasks of the user.
 * Main class where all the manipulation of the task objects are carried out.
 * Also responsible for storing of all the tasks.
 *
 */
public class LogicData {

	/**
	 * Different DisplayState for LogicData which represents the state of display 
	 * given to the user 
	 *
	 */
	public enum DisplayState {
		ALL_TASKS, MULTIPLE_DELETE, MULTIPLE_COMPLETE, MULTIPLE_PRIORITISE, SHOW_SEARCH, EXIT, 
		INVALID_COMMAND, HELP, INVALID_TASK, ARCHIVE, FIND_FREE, DEMO, HIDE
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
	 * Default constructor where singleton pattern is applied.
	 * To initialise the variables within LogicData.
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
	 * Alternative constructor where singleton pattern is applied.
	 * To initialise the variables within LogicData when doing testing.
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
	 * Singleton pattern method for instantiation of LogicData to 
	 * avoid multiple copies of tasks for the user
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
	 * Method used for saving all current contents into the data file for 
	 * usage in the future.
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
		// TODO: refactor function
		StateFeedback state;
		switch (_currState) {
		case ALL_TASKS:
			// TODO update diagram
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
		case DEMO :
			state = displayAllTasks(_displays);
			state.setState(StateFeedback.State.DEMO);
			break;
		case HIDE :
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
	 * Method for setting the display list to show the current list given.
	 * Sorts the list according to Overdue, Today, Others.
	 * 
	 * @param displayList Display List given for putting as display
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
		state.setOverdueCount(count);
	}

	private void setShowMorePositions(StateFeedback state) {
		for (Task task : _showMoreTasks) {
			if (_displays.contains(task)) {
				state.addDetailedTaskIdx(_displays.indexOf(task));
			}
		}
	}

	// sets the position if the pointer matches the display
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

	public boolean isTaskToday(Task task) {
		LocalDate now = LocalDate.now();
		if (task.getTaskType() == Task.Type.DEADLINE) {
			if (task.getEndTime().toLocalDate().isEqual(now)) {
				return true;
			} else {
				return false;
			}
		} else if (task.getTaskType() == Task.Type.EVENT) {
			if (task.getStartTime().toLocalDate().isEqual(now) || task.getEndTime().toLocalDate().isEqual(now)
					|| task.getStartTime().toLocalDate().isBefore(now)
							&& task.getEndTime().toLocalDate().isAfter(now)) {
				return true;
			} else {
				return false;
			}
		} else { // Type is floating
			return false;
		}
	}

	public void updateState() {
		logger.getLogger().info("Updating state of prog");

		ArrayList<Task> completedTasks = new ArrayList<Task>();
		LocalDateTime now = LocalDateTime.now();
		for (Task task : _tasks) {
			if (task.getTaskType() == Task.Type.DEADLINE) {
				if (task.getEndTime().isBefore(now)) {
					task.setIsOverdue(true);
					task.setDateModified(LocalDateTime.now());
					logger.getLogger().info("deadline task " + task + " has turned overdue");
				} else {
					task.setIsOverdue(false);
				}
			}
			if (task.getSlot() != null) {
				updateMultipleSlot(task);
			}
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
		_tasks.removeAll(completedTasks);
		_archives.addAll(completedTasks);
	}

	// assumes that the multipleslots are sorted
	public void updateMultipleSlot(Task task) {
		LocalDateTime now = LocalDateTime.now();

		if (task.getStartTime() == null || task.getEndTime() == null) {
			return;
		}

		task.getSlot().addTimeSlot(task.getStartTime(), task.getEndTime());
		task.getSlot().sortSlots();

		do {
			DateTimePair newTime = task.getSlot().getNextSlot();
			task.setStartTime(newTime.getEarlierDateTime());
			task.setEndTime(newTime.getLaterDateTime());
			task.getSlot().removeNextSlot();
		} while (task.getEndTime().isBefore(now) && !(task.getSlot().isEmpty()));

		// sets multipleslots to empty when latest timing is the current timing
		// makes block type to normal task event
		if (task.getSlot().isEmpty()) {
			task.setSlot(null);
		}
	}

	public ArrayList<Task> findMatchingDesc(String desc) {
		ArrayList<Task> matches = new ArrayList<Task>();
		if (!desc.equals("")) {
			for (Task task : _displays) {
				if (Pattern.compile(Pattern.quote(desc), Pattern.CASE_INSENSITIVE).matcher(task.getDesc()).find()) {
					matches.add(task);
				}
			}
		}
		logger.getLogger().info("Find matching desc: " + desc);
		return matches;
	}

	public ArrayList<Task> findRefinedMatchingDesc(String desc) {
		ArrayList<Task> matches = new ArrayList<Task>();
		if (!desc.equals("")) {
			String[] substr = desc.split("\\s+");
			ArrayList<String> substr2 = new ArrayList<String>(Arrays.asList(substr));
			if (substr2.size() > 1) {
				for (Task task : _displays) {
					boolean flag = true;
					for (String s : substr2) {
						if (!(Pattern.compile(Pattern.quote(s + " "), Pattern.CASE_INSENSITIVE).matcher(task.getDesc()).find())
								&& !(Pattern.compile(Pattern.quote(" " + s), Pattern.CASE_INSENSITIVE).matcher(task.getDesc()).find()) && flag) {
							flag = false;
						}
					}
					if (flag) {
						matches.add(task);
					}
				}
			} else {
				for (Task task : _displays) {
					if (Pattern.compile(Pattern.quote(desc), Pattern.CASE_INSENSITIVE).matcher(task.getDesc()).find()) {
						matches.add(task);
					}
				}
			}
		}
		return matches;
	}

	public void addArchive(Task task) {
		logger.getLogger().info("adding task " + task + " to archive");
		_archives.add(task);
	}

	public void addArchive(ArrayList<Task> tasks) {
		logger.getLogger().info("adding task multiple tasks to archive");
		_archives.addAll(tasks);
	}

	public void removeArchive(Task task) {
		_archives.remove(task);
	}

	public void removeArchive(ArrayList<Task> tasks) {
		_archives.removeAll(tasks);
	}

	public ArrayList<Task> getTaskList() {
		return _tasks;
	}

	public int getCurrentId() {
		return _currentId;
	}

	public void updateCurrentId() {
		_currentId++;
	}

	public void addTask(Task newTask) {
		_tasks.add(newTask);
	}

	public void addTasks(ArrayList<Task> tasks) {
		_tasks.addAll(tasks);
	}

	public void deleteTask(Task task) {
		_tasks.remove(task);
		_archives.remove(task);
	}

	public void deleteTasks(ArrayList<Task> tasks) {
		_tasks.removeAll(tasks);
		_archives.removeAll(tasks);
	}

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

	// overloaded function for finding matching positions with arraylists of
	// positions
	// returns only matching positions that are valid else ignored
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


	public ArrayList<Task> findMatchingDates(LocalDate input) {
		logger.getLogger().info("Find matching dates, " + input);
		ArrayList<Task> matches = new ArrayList<Task>();
		for (Task task : _displays) {
			if (task.getTaskType() == Task.Type.DEADLINE) {
				if (task.getEndTime().toLocalDate().isEqual(input)) {
					matches.add(task);
				}
			} else if (task.getTaskType() == Task.Type.EVENT) {
				if (task.getStartTime().toLocalDate().isEqual(input)
						|| task.getEndTime().toLocalDate().isEqual(input)) {
					matches.add(task);
				} else if (task.getStartTime().toLocalDate().isBefore(input)
						&& task.getEndTime().toLocalDate().isAfter(input)) {
					matches.add(task);
				} else if (task.getSlot() != null && !task.getSlot().isEmpty()) {
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
			}
		}
		return matches;
	}

	public ArrayList<Task> findMatchingDateTimes(LocalDateTime input) {
		logger.getLogger().info("Find matching datetime, " + input);
		ArrayList<Task> matches = new ArrayList<Task>();
		for (Task task : _displays) {
			if (task.getTaskType() == Task.Type.DEADLINE) {
				if (task.getEndTime().isEqual(input)) {
					matches.add(task);
				}
			} else if (task.getTaskType() == Task.Type.EVENT) {
				if (task.getStartTime().isEqual(input) || task.getEndTime().isEqual(input)) {
					matches.add(task);
				} else if (task.getStartTime().isBefore(input) && task.getEndTime().isAfter(input)) {
					matches.add(task);
				} else if (task.getSlot() != null && !task.getSlot().isEmpty()) {
					ArrayList<DateTimePair> slots = task.getSlot().getSlots();
					for (DateTimePair pair : slots) {
						if (pair.getEarlierDateTime().isEqual(input) || pair.getLaterDateTime().isEqual(input)) {
							matches.add(task);
						} else if (pair.getEarlierDateTime().isBefore(input)
								&& pair.getLaterDateTime().isAfter(input)) {
							matches.add(task);
						}
					}
				}
			}
		}
		return matches;
	}

	public ArrayList<Task> findMatchingMonths(Month input) {
		logger.getLogger().info("Find matching months, " + input);
		ArrayList<Task> matches = new ArrayList<Task>();
		for (Task task : _displays) {
			if (task.getTaskType() == Task.Type.DEADLINE) {
				if (task.getEndTime().getMonth() == input) {
					matches.add(task);
				}
			} else if (task.getTaskType() == Task.Type.EVENT) {
				if (task.getStartTime().getMonth() == input || task.getEndTime().getMonth() == input) {
					matches.add(task);
				} else if (task.getStartTime().getMonth() == input && task.getEndTime().getMonth() == input) {
					matches.add(task);
				} else if (task.getSlot() != null && !task.getSlot().isEmpty()) {
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
			}
		}
		return matches;
	}

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

	public ArrayList<Task> sortList(ArrayList<Task> list) {
		Collections.sort(list, comparator);
		Collections.sort(list, imptComparator);
		return list;
	}

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

	public ArrayList<Task> sortArchive(ArrayList<Task> list) {
		Collections.sort(list, archiveComparator);
		return list;
	}

	// new comparator for sorting archive
	static Comparator<Task> archiveComparator = new Comparator<Task>() {
		public int compare(final Task o1, final Task o2) {
			return o2.getDateModified().compareTo(o1.getDateModified());
		}
	};

	public ArrayList<Task> getDisplays() {
		return _displays;
	}

	public ArrayList<Task> getArchives() {
		return _archives;
	}

	public void setDisplays(ArrayList<Task> displays) {
		_displays = displays;
	}

	public DisplayState getCurrState() {
		return _currState;
	}

	public void setCurrState(DisplayState currState) {
		_currState = currState;
	}

	public void clearDisplays() {
		_displays.clear();
	}

	// for testing purposes only. delete if necessary. Can be found in
	// FreeTimeTest.java
	public void clearTasks() {
		_tasks.clear();
	}

	public ArrayList<String> generateHelpManual() {
		return _storage.retrieveHelp();
	}

	public Task getTaskPointer() {
		return _taskPointer;
	}

	public void setTaskPointer(Task taskPointer) {
		_taskPointer = taskPointer;
	}

	public boolean isShowingMore(Task task) {
		return _showMoreTasks.contains(task);
	}

	public void toggleShowMoreTasks(Task task) {
		logger.getLogger().info("toggle showmore status of " + task);
		if (_showMoreTasks.contains(task)) {
			_showMoreTasks.remove(task);
		} else {
			_showMoreTasks.add(task);
		}
	}

	// TODO command that clears all showmore
	public void clearShowMoreTasks() {
		_showMoreTasks.clear();
	}

	public String retrieveCurrentDirectory() {
		return _storage.getDirPath();
	}

	public void changeDirectory(String path) throws StorageException {
		_storage.changeFileSettings(path);
	}

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
			}
		}

		return overlaps;
	}

	// removes archived tasks that are beyond one month old
	public void clearOldArchive() {
		ArrayList<Task> outdatedTasks = new ArrayList<Task>();
		for (Task task : _archives) {
			if (task.getDateModified().isBefore(LocalDateTime.now().minusMonths(1))) {
				outdatedTasks.add(task);
			}
		}
		_archives.removeAll(outdatedTasks);
	}

	public void checkPointer() {
		if (_taskPointer != null) {
			if (_archives.contains(_taskPointer)) {
				_currState = LogicData.DisplayState.ARCHIVE;
			}
		}
	}

	public void reinitialiseStorage() {
		_storage = new Storage();
		_tasks = _storage.updateCurrentTaskList();
		_archives = _storage.updateArchiveTaskList();
	}

	public void reinitialiseStorageTester() {
		_storage.delete();
		_storage = new StorageTester();
		_tasks = _storage.updateCurrentTaskList();
		_archives = _storage.updateArchiveTaskList();
	}

	public ArrayList<String> generateDemoText() {
		return _storage.getDemoText();
	}

	public ArrayList<Integer> generateDemoSelectionIndexes() {
		return _storage.getDemoSelectionIndexes();
	}

}
