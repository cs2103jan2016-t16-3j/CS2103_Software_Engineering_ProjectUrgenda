package urgenda.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;
import java.util.regex.Pattern;

import urgenda.command.Command;
import urgenda.command.Undoable;
import urgenda.storage.Storage;
import urgenda.util.MultipleSlot;
import urgenda.util.StateFeedback;
import urgenda.util.Task;

public class LogicData {

	private static final String MESSAGE_EMPTY_UNDO = "Nothing to undo";
	private static final String MESSAGE_EMPTY_REDO = "Nothing to redo";

	private ArrayList<Task> _tasks;
	private ArrayList<Task> _archives;
	private ArrayList<Task> _displays;

	private Stack<Undoable> _undos;
	private Stack<Undoable> _redos;

	private Storage _storage;
	
	private StateFeedback.State _currState;

	private int _currentId;

	// default constructor for initialization of empty path
	public LogicData() {
		_tasks = new ArrayList<Task>();
		_archives = new ArrayList<Task>();
		_storage = new Storage();
		_currentId = 0;
		_undos = new Stack<Undoable>();
		_redos = new Stack<Undoable>();
	}

	// constructor for a specific storage path being defined
	public LogicData(String path) {
		_storage = new Storage(path);
		_tasks = new ArrayList<Task>();
		_archives = new ArrayList<Task>();
		// updateArrayLists adds stored task objects into respective arraylists
		// returns the next id to be used for future labelling of tasks
		_currentId = _storage.updateArrayLists(_tasks, _archives);
	}

	public void addUndo(Command currCmd) {
		if (currCmd instanceof Undoable) {
			_undos.push((Undoable) currCmd);
			_redos.clear();
		}
	}

	public void saveContents() {
		_storage.save(_tasks, _archives);
	}

	// TODO: refactor function
	public StateFeedback getState(StateFeedback.State currState) {
		// To update if there are any deadlines that turned overdue
		updateState();
		setCurrState(currState);

		ArrayList<Task> overdueTasks = new ArrayList<Task>();
		ArrayList<Task> todayTasks = new ArrayList<Task>();
		ArrayList<Task> importantTasks = new ArrayList<Task>();
		ArrayList<Task> otherTasks = new ArrayList<Task>();
		for (Task task : _tasks) {
			if (task.isOverdue()) {
				overdueTasks.add(task);
			} else if (isTaskToday(task)) {
				todayTasks.add(task); // swapped urgent and today
			} else if (task.isImportant()) {
				importantTasks.add(task);
			} else { // remaining floating tasks
				otherTasks.add(task);
			}
		}
		_tasks.clear();
		_tasks.addAll(sortList(overdueTasks));
		_tasks.addAll(sortList(todayTasks)); // swapped urgent and today
		_tasks.addAll(sortList(importantTasks));
		_tasks.addAll(sortList(otherTasks));
		StateFeedback state = new StateFeedback(_tasks, overdueTasks.size(), todayTasks.size(), importantTasks.size(),
				otherTasks.size());
		state.setState(currState);
		
		return state;
	}
	
	public StateFeedback getState(StateFeedback.State currState, ArrayList<Task> tasks) {
		// TODO replace top function with a refactored function that can be used by both calls
		return null;
	}
	
	public ArrayList<Task> findMatchingTasks(String desc) {
		ArrayList<Task> matches = new ArrayList<Task>();
		for (Task task : _tasks) {
			if (Pattern.compile(Pattern.quote(desc), Pattern.CASE_INSENSITIVE).matcher(task.getDesc()).find()) {
				matches.add(task);
			}
		}
		return matches;
	}

	private boolean isTaskToday(Task task) {
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

	private void updateState() {
		LocalDateTime now = LocalDateTime.now();
		for (Task task : _tasks) {
			if (task.getTaskType() == Task.Type.DEADLINE) {
				if (task.getEndTime().isBefore(now)) {
					task.setIsOverdue(true);
				} else {
					task.setIsOverdue(false);
				}
			}
		}

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

	public void deleteTask(Task newTask) {
		_tasks.remove(newTask);
	}

	public Task findMatchingId(int id) {
		for (Task task : _tasks) {
			if (task.getId() == id) {
				return task;
			}
		}
		return null;
	}

	public Task findTaskPosition(int position) {
		if (position <= 0 || position > _tasks.size()) {
			return null;
		} else {
			return _tasks.get(position - 1);
		}
	}

	public String undoCommand() {
		if (!_undos.isEmpty()) {
			Undoable undoCommand = _undos.pop();
			String feedback = undoCommand.undo();
			_redos.push(undoCommand);
			return feedback;
		} else {
			return MESSAGE_EMPTY_UNDO;
		}

	}

	public String redoCommand() {
		if (!_redos.isEmpty()) {
			Undoable redoCommand = _redos.pop();
			String feedback = redoCommand.redo();
			_undos.push(redoCommand);
			return feedback;
		} else {
			return MESSAGE_EMPTY_REDO;
		}

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

	public ArrayList<Task> getDisplays() {
		return _displays;
	}

	public void setDisplays(ArrayList<Task> displays) {
		_displays = displays;
	}

	public StateFeedback.State getCurrState() {
		return _currState;
	}

	public void setCurrState(StateFeedback.State currState) {
		_currState = currState;
	}
}
