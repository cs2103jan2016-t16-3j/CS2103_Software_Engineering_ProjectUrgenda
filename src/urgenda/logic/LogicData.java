package urgenda.logic;

import java.util.ArrayList;
import java.util.Stack;

import urgenda.command.Command;
import urgenda.storage.Storage;
import urgenda.util.StateFeedback;
import urgenda.util.Task;

public class LogicData {

	private ArrayList<Task> _events;
	private ArrayList<Task> _deadlines;
	private ArrayList<Task> _floats;
	private ArrayList<Task> _blocks;

	private Stack<Command> _undos;
	private Stack<Command> _redos;

	private Storage _storage;

	// default constructor for initialization of empty path
	public LogicData() {
		_events = new ArrayList<Task>();
		_deadlines = new ArrayList<Task>();
		_floats = new ArrayList<Task>();
		_blocks = new ArrayList<Task>();
		_storage = new Storage();
	}

	// constructor for a specific storage path being defined
	public LogicData(String path) {
		_storage = new Storage(path);
		_storage.updateArrayLists(_events, _deadlines, _floats, _blocks);
	}

	public void addUndo(Command currCmd) {
		_undos.push(currCmd);
	}
	
	public void saveContents() {
		_storage.save(_events, _deadlines, _floats, _blocks);
	}

	public StateFeedback getState() {
		StateFeedback state = new StateFeedback();
		// to sort into overdue, urgent, today, others and passed into TaskLists in state
		return state;
	}

}
