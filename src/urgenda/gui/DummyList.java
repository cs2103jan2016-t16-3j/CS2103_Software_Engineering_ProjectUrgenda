package urgenda.gui;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.util.Task;

public class DummyList {
	
	private ArrayList<Task> task;
	
	public DummyList() {
		Task taskU = new Task("Urgent task", "U location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), true);
		Task task1 = new Task("A task", "A location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), false);
		Task task2 = new Task("B task", "B location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), false);
		Task task3 = new Task("C task", "C location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), false);
		Task task4 = new Task("D task", "D location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), false);
		Task task5 = new Task("E task", "E location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), false);
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(taskU);
		tasks.add(task1);
		tasks.add(task2);
		tasks.add(task3);
		tasks.add(task4);
		tasks.add(task5);
	}
	
	public ArrayList<Task> getDummyList() {
		return task;
	}
}
