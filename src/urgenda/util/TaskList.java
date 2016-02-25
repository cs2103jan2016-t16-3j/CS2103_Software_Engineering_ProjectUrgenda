package urgenda.util;

import java.util.ArrayList;
import java.util.Iterator;

import urgenda.util.TaskWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskList implements Iterable<TaskWrapper>{

	private ObservableList<TaskWrapper> _tasks;
	
	public TaskList() {
		_tasks = FXCollections.observableArrayList();
	}
	
	public TaskList(ObservableList<TaskWrapper> t) {
        _tasks = t;
    }
	
	public TaskList(ArrayList<Task> tasks) {
		_tasks = FXCollections.observableArrayList();
		for (Task task : tasks) {
			_tasks.add(new TaskWrapper(task));
		}
	}
	
	@Override
	public Iterator<TaskWrapper> iterator() {
		return _tasks.iterator();
	}

}
