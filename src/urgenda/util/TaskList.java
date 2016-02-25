package urgenda.util;

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
	
	@Override
	public Iterator<TaskWrapper> iterator() {
		return _tasks.iterator();
	}

}
