package urgenda.util;

import java.util.ArrayList;
import java.util.Iterator;

import urgenda.util.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskList implements Iterable<Task>{

	private ObservableList<TaskWrapper> _tasks;
	
	public TaskList(ArrayList<TaskWrapper> tasks) {
		_tasks = FXCollections.observableArrayList(tasks);
	}
	
	@Override
	public Iterator<TaskWrapper> iterator() {
		return _tasks.iterator();
	}

}
