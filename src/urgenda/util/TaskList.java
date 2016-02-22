package urgenda.util;

import java.util.Iterator;

import urgenda.util.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TaskList implements Iterable<Task>{

	private ObservableList<Task> tasks;
	
	public TaskList() {
		tasks = FXCollections.observableArrayList();
	}
	
	@Override
	public Iterator<Task> iterator() {
		return tasks.iterator();
	}

}
