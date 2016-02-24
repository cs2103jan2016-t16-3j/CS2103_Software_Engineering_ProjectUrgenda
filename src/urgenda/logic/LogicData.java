package urgenda.logic;

import java.util.ArrayList;
import java.util.Stack;

import urgenda.command.Command;
import urgenda.command.Redo;
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
	private ArrayList<MultipleSlot> _blocks;

	private Stack<Command> _undos;
	private Stack<Command> _redos;

	private Storage _storage;
	
	private int _currentId;

	// default constructor for initialization of empty path
	public LogicData() {
		_tasks = new ArrayList<Task>();
		_archive = new ArrayList<Task>();
		_blocks = new ArrayList<MultipleSlot>();
		_storage = new Storage();
		_currentId = 0;
	}

	// constructor for a specific storage path being defined
	public LogicData(String path) {
		_storage = new Storage(path);
		_tasks = new ArrayList<Task>();
		_archive = new ArrayList<Task>();
		_blocks = new ArrayList<MultipleSlot>();
		// updateArrayLists adds stored task objects into respective arraylists
		// returns the next id to be used for future labelling of tasks
		_currentId = _storage.updateArrayLists(_tasks, _archive, _blocks);
	}

	public void addUndo(Command currCmd) {
		if (!(currCmd instanceof Undo) && !(currCmd instanceof Redo)) {
			_undos.push(currCmd);
			_redos.clear();
		}
	}
	
	public void saveContents() {
		_storage.save(_tasks, _archive, _blocks);
	}

	public StateFeedback getState() {
		// TODO: filter tasks into overdue, urgent, today, others
		// TODO: wrap tasks into TaskWrapper
		// TODO: sort tasks in each arraylist by date/time (needs a comparator)
		ArrayList<TaskWrapper> overdueTasks = new ArrayList<TaskWrapper>();
		ArrayList<TaskWrapper> urgentTasks = new ArrayList<TaskWrapper>();
		ArrayList<TaskWrapper> todayTasks = new ArrayList<TaskWrapper>();
		ArrayList<TaskWrapper> otherTasks = new ArrayList<TaskWrapper>();
		StateFeedback state = new StateFeedback(overdueTasks, urgentTasks, todayTasks, otherTasks);
		return state;
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

}
