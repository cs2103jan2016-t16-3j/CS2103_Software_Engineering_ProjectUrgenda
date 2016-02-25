package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import urgenda.storage.Storage;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;

public class TestStorage {

	@Test
	public void testSave(){
		Storage store = new Storage();
		Task task1 = new Task("test1");
		Task task2 = new Task("test2");
		Task task3 = new Task("test3");
		Task task4 = new Task("test4");
		ArrayList<Task> _tasks = new ArrayList<Task>();
		ArrayList<Task> _archive = new ArrayList<Task>();
		ArrayList<MultipleSlot> _blocks = new ArrayList<MultipleSlot>();
		_tasks.add(task1);
		_tasks.add(task2);
		_tasks.add(task3);
		_tasks.add(task4);
		store.save(_tasks, _archive, _blocks);
	}

}
