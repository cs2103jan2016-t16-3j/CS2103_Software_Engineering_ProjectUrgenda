package urgenda.logic;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

import urgenda.command.Command;
import urgenda.command.Redo;
import urgenda.command.Search;
import urgenda.command.Undo;
import urgenda.storage.Storage;
import urgenda.util.MultipleSlot;
import urgenda.util.StateFeedback;
import urgenda.util.Task;
import urgenda.util.TaskWrapper;

public class LogicData {
	
	private static final String MESSAGE_EMPTY_UNDO = "Nothing to undo";
	private static final String MESSAGE_EMPTY_REDO = "Nothing to redo";

	private ArrayList<Task> _tasks;
	private ArrayList<Task> _archive;

	private Stack<Command> _undos;
	private Stack<Command> _redos;

	private Storage _storage;
	
	private int _currentId;

	// default constructor for initialization of empty path
	public LogicData() {
		_tasks = new ArrayList<Task>();
		_archive = new ArrayList<Task>();
		 _storage = new Storage();
		_currentId = 0;
		_undos = new Stack<Command>();
		_redos = new Stack<Command>();
	}

	// constructor for a specific storage path being defined
	public LogicData(String path) {
		_storage = new Storage(path);
		_tasks = new ArrayList<Task>();
		_archive = new ArrayList<Task>();
		// updateArrayLists adds stored task objects into respective arraylists
		// returns the next id to be used for future labelling of tasks
		_currentId = _storage.updateArrayLists(_tasks, _archive);
	}

	public void addUndo(Command currCmd) {
		if ((currCmd instanceof Undo) || (currCmd instanceof Redo)) {
			// to ignore if type is redo/undo
		} else if ((currCmd instanceof Search)) {
			// to ignore if type is search
		} else {
			_undos.push(currCmd);
			// clears the redo stack to ensure coherence with behavior of undo/redo and new actions
			_redos.clear();
		}
	}
	
	public void saveContents() {
		_storage.save(_tasks, _archive);
	}

	// TODO: refactor function
	public StateFeedback getState() {
		// To update if there are any deadlines that turned overdue
		updateState();
		
		// TODO: filter tasks into overdue, urgent, today, others
		// TODO: wrap tasks into TaskWrapper
		// TODO: sort tasks in each arraylist by date/time (needs a comparator)
		ArrayList<Task> overdueTasks = new ArrayList<Task>();
		ArrayList<Task> urgentTasks = new ArrayList<Task>();
		ArrayList<Task> todayTasks = new ArrayList<Task>();
		ArrayList<Task> otherTasks = new ArrayList<Task>();
		for (Task task : _tasks) {
			if(task.isOverdue()) {
				overdueTasks.add(task);
			} else if (task.isUrgent()) {
				urgentTasks.add(task);
			} else if (isTaskToday(task)) {
				todayTasks.add(task);
			} else { // remaining tasks including floating
				otherTasks.add(task);
			}
		}
		_tasks.clear();
		_tasks.addAll(overdueTasks);
		_tasks.addAll(urgentTasks);
		_tasks.addAll(todayTasks);
		_tasks.addAll(otherTasks);
		StateFeedback state = new StateFeedback(_tasks);
		return state;
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
		} else if (task.getTaskType() == Task.Type.START) {
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
	
	// TODO merge with search command (remove this function)
	public Task findMatchingDesc(String desc) {
		for (Task task : _tasks) {
			if (task.getDesc().contains(desc)) {
				return task;
			}
		}
		return null;
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
			return _tasks.get(position-1);
		}
	}
	
	public String undoCommand() {
		if (!_undos.isEmpty()) {
			Command undoCommand = _undos.pop();
			String feedback = undoCommand.undo();
			_redos.push(undoCommand);
			return feedback;
		} else {
			return MESSAGE_EMPTY_UNDO;
		}

	}

	public String redoCommand() {
		if (!_redos.isEmpty()) {
			Command redoCommand = _redos.pop();
			String feedback = redoCommand.redo();
			_undos.push(redoCommand);
			return feedback;
		} else {
			return MESSAGE_EMPTY_REDO;
		}

	}
	
	// TODO (to joanne) does it have to return an arraylist? should have sorted the original array alr
	public ArrayList<Task> sortList(ArrayList<Task> list) {
		Collections.sort(list, comparator);
		return list;
	}
	
	Comparator<Task> comparator = new Comparator<Task>() {

		public int compare(final Task t1, final Task t2) {
			if (t1.getEndTime() == null || t2.getEndTime() == null) {
				return 0;
			} else {
				return t1.getEndTime().compareTo(t2.getEndTime());
			}
		}
	};
	
}
