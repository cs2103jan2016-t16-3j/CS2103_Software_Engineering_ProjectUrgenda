package urgenda.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.regex.Pattern;

import urgenda.storage.Storage;
import urgenda.util.MultipleSlot;
import urgenda.util.UrgendaLogger;
import urgenda.util.StateFeedback;
import urgenda.util.Task;
import urgenda.util.DateTimePair;

public class LogicData {

	public enum DisplayState {
		ALL_TASKS, MULTIPLE_DELETE, MULTIPLE_COMPLETE, MULTIPLE_PRIORITISE, SHOW_SEARCH, EXIT, INVALID_COMMAND, HELP, INVALID_TASK, ARCHIVE, FIND_FREE
	}

	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	// Singleton pattern to ensure that there is only one logicData
	private static LogicData _logicData;

	// for storage of full lists of tasks
	private ArrayList<Task> _tasks;
	// for storage of all completed tasks
	private ArrayList<Task> _archives;
	// for storage of tasks being displayed to user by last command
	private ArrayList<Task> _displays;

	private Storage _storage;

	private DisplayState _currState;
	private ArrayList<Task> _showMoreTasks;
	private Task _taskPointer;

	private int _currentId;

	// default constructor where singleton pattern is applied
	private LogicData() {
		logger.getLogger().info("constructing LogicData Object");
		_storage = new Storage();
		// updates arraylists from stored task objects into respective
		// arraylists
		_tasks = _storage.updateCurrentTaskList();
		_archives = _storage.updateArchiveTaskList();
		_currState = DisplayState.ALL_TASKS;
		_displays = new ArrayList<Task>();
		_showMoreTasks = new ArrayList<Task>();
		// sets the next id to be used for future labelling of tasks
		_currentId = _tasks.size() + 1;
	}

	public static LogicData getInstance() {
		if (_logicData == null) {
			logger.getLogger().info("creating instance of logicData");
			_logicData = new LogicData();
		}
		logger.getLogger().info("retrieving prev instance of logicData");
		return _logicData;
	}

	public void saveContents() {
		logger.getLogger().info("Saving contents to storage");
		_storage.save(_tasks, _archives);
	}

	// TODO: refactor function
	public StateFeedback getState() {
		StateFeedback state = null;
		switch (_currState) {
		case ALL_TASKS:
			// TODO update diagram
			updateState();
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
		default:
			state = displayAllTasks(_tasks);
			state.setState(StateFeedback.State.ALL_TASKS);
			break;
		}

		return state;
	}

	public StateFeedback displayArchiveTasks(ArrayList<Task> displayList) {
		clearDisplays();
		_displays.addAll(sortArchive(displayList));
		StateFeedback state = new StateFeedback(_displays, _displays.size());
		setFeedbackDisplayPosition(state);
		// TODO showmore for archive
		// setShowMorePositions(state);
		return state;
	}

	public StateFeedback displayAllTasks(ArrayList<Task> displayList) {
		ArrayList<Task> overdueTasks = new ArrayList<Task>();
		ArrayList<Task> todayTasks = new ArrayList<Task>();
		ArrayList<Task> otherTasks = new ArrayList<Task>();
		for (Task task : displayList) {
			if (task.isOverdue()) {
				overdueTasks.add(task);
			} else if (isTaskToday(task)) {
				todayTasks.add(task);
			} else { // remaining tasks and floating tasks
				otherTasks.add(task);
			}
		}
		clearDisplays();
		_displays.addAll(sortList(overdueTasks));
		_displays.addAll(sortList(todayTasks));
		_displays.addAll(sortList(otherTasks));

		StateFeedback state = new StateFeedback(_displays, overdueTasks.size(), todayTasks.size(), otherTasks.size());
		setFeedbackDisplayPosition(state);
		setShowMorePositions(state);
		return state;
	}

	public void setShowMorePositions(StateFeedback state) {
		for (Task task : _showMoreTasks) {
			if (_displays.contains(task)) {
				state.addDetailedTaskIdx(_displays.indexOf(task));
			}
		}
	}

	// sets the position if the pointer matches the display
	public void setFeedbackDisplayPosition(StateFeedback state) {
		if (_taskPointer != null && _displays.contains(_taskPointer)) {
			state.setDisplayPosition(_displays.indexOf(_taskPointer));
		} else { // sets to 0 as default if no specific task is required to be
					// pointed at
			state.setDisplayPosition(0);
		}
		// clears task pointer for next iteration
		_taskPointer = null;
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

	public boolean isTaskToday(Task task) {
		LocalDate now = LocalDate.now();
		if (task.getTaskType() == Task.Type.DEADLINE) {
			if (task.getEndTime().toLocalDate().isEqual(now)) {
				return true;
			} else {
				return false;
			}
		} else if (task.getTaskType() == Task.Type.EVENT) {
			if (task.getStartTime().toLocalDate().isEqual(now)) {
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
					logger.getLogger().info("event task" + task + " has been completed");
					// only moves completed tasks when the day ends for that day
					if (task.getEndTime().toLocalDate().isBefore(LocalDate.now())) {
						completedTasks.add(task);
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

		while (task.getEndTime().isBefore(now) && !(task.getSlot().isEmpty())) {
			DateTimePair newTime = task.getSlot().getNextSlot();
			task.setStartTime(newTime.getEarlierDateTime());
			task.setEndTime(newTime.getLaterDateTime());
			task.getSlot().removeNextSlot();
		}
		// sets multipleslots to empty when latest timing is the current timing
		// makes block type to normal task event
		if (task.getSlot().isEmpty()) {
			task.setSlot(null);
		}
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
	}

	public void deleteTasks(ArrayList<Task> tasks) {
		_tasks.removeAll(tasks);
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

	public ArrayList<Task> findMatchingHashtags(String input) {
		logger.getLogger().info("Find matching hashtags, " + input);
		ArrayList<Task> matches = new ArrayList<Task>();
		boolean flag = true;
		if (!input.equals("")) {
			try {
				for (Task task : _displays) {
					if (task.getHashtags() != null || !task.getHashtags().isEmpty()) {
						Iterator<String> i = task.getHashtags().iterator();
						while (i.hasNext() && flag) {
							if (Pattern.compile(Pattern.quote(input), Pattern.CASE_INSENSITIVE).matcher(i.next())
									.find()) {
								matches.add(task);
								flag = false;
							}
						}
					}
				}
			} catch (NullPointerException e) {
			}
		}
		return matches;
	}

	public ArrayList<Task> findMatchingDates(LocalDate input) {
		logger.getLogger().info("Find matching dates, " + input);
		ArrayList<Task> matches = new ArrayList<Task>();
		boolean flag = true;
		try {
			for (Task task : _displays) {
				if (task.getStartTime() != null) {
					if (task.getStartTime().toLocalDate().isEqual(input)) {
						matches.add(task);
						flag = false;
					}
				} else if (task.getEndTime() != null) {
					if (task.getEndTime().toLocalDate().isEqual(input)) {
						matches.add(task);
						flag = false;
					}
				}
				if (task.getSlot() != null && flag) {
					ArrayList<DateTimePair> slots = task.getSlot().getSlots();
					for (DateTimePair pair : slots) {
						if (pair.getEarlierDateTime().toLocalDate().isEqual(input)
								|| pair.getLaterDateTime().toLocalDate().isEqual(input)) {
							matches.add(task);
							flag = false;
						}
					}
				}
			}
		} catch (NullPointerException e) {
		}
		return matches;
	}

	public ArrayList<Task> findMatchingDateTimes(LocalDateTime input) {
		logger.getLogger().info("Find matching datetime, " + input);
		ArrayList<Task> matches = new ArrayList<Task>();
		boolean flag = true;
		try {
			for (Task task : _displays) {
				if (task.getStartTime() != null) {
					if (task.getStartTime().isEqual(input)) {
						matches.add(task);
						flag = false;
					}
				} else if (task.getEndTime() != null) {
					if (task.getEndTime().isEqual(input)) {
						matches.add(task);
						flag = false;
					}
				}
				if (task.getSlot() != null && flag) {
					ArrayList<DateTimePair> slots = task.getSlot().getSlots();
					for (DateTimePair pair : slots) {
						if (pair.getEarlierDateTime().isEqual(input)
								|| pair.getLaterDateTime().isEqual(input)) {
							matches.add(task);
							flag = false;
						}
					}
				}
			}
		} catch (NullPointerException e) {
		}
		return matches;
	}

	public ArrayList<Task> findMatchingMonths(Month input) {
		logger.getLogger().info("Find matching months, " + input);
		ArrayList<Task> matches = new ArrayList<Task>();
		boolean flag = true;
		try {
			for (Task task : _displays) {
				if (task.getStartTime() != null) {
					if (task.getStartTime().getMonth() == input) {
						matches.add(task);
						flag = false;
					}
				} else if (task.getEndTime() != null) {
					if (task.getEndTime().getMonth() == input) {
						matches.add(task);
						flag = false;
					}
				}
				if (task.getSlot() != null && flag) {
					ArrayList<DateTimePair> slots = task.getSlot().getSlots();
					for (DateTimePair pair : slots) {
						if (pair.getEarlierDateTime().getMonth() == input
								|| pair.getLaterDateTime().getMonth() == input) {
							matches.add(task);
							flag = false;
						}
					}
				}
			}
		} catch (NullPointerException e) {
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

	public String generateHelpManual() {
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

	public void changeDirectory(String path) {
		_storage.changeFilePath(path);
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

}
