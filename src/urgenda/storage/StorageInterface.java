package urgenda.storage;

import java.util.ArrayList;
import java.util.Stack;

import urgenda.command.Command;
import urgenda.util.Task;

public interface StorageInterface {
	boolean checkUrgent();

	// default constructor for no path included (use default location)?
	// public Storage();

	// constructor for saving path/location
	// public Storage(String path);

	// call from logic to initialize the arraylists with the stored objects. only for starting Urgenda
	// use .add(Task) to these arrays to retain data in logic
	// public void updateArrayLists(ArrayList<Task> _events, ArrayList<Task>
	// _deadlines, ArrayList<Task> _floats,
	// ArrayList<Task> _blocks);

	// logic will use this function to store all these arraylists in a txtfile
	// IMPT: DO NOT MODIFY CONTENTS INSIDE!
	// public void save(ArrayList<Task> _events, ArrayList<Task> _deadlines,
	// ArrayList<Task> _floats, ArrayList<Task> _blocks, Stack<Command> _undos,
	// Stack<Command> _redos);
}
