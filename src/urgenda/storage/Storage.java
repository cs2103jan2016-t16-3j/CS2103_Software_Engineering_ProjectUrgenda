package urgenda.storage;

import urgenda.util.*;

public class Storage implements StorageInterface {
	
	public boolean checkUrgent() {
		Task testTask = new Task("test this", "ISE LAB", null, null, false);
		boolean test = testTask.isUrgent();
		return test;
	}
}
