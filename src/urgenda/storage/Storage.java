package urgenda.storage;

import urgenda.util.*;

import com.google.gson.Gson;

public class Storage implements StorageInterface {
	
	public boolean checkUrgent() {
		Task testTask = new Task("test this", "ISE LAB", null, null, false);
		boolean test = testTask.isUrgent();
		return test;
	}
	
	public void GSONTester(){
		Gson gson = new Gson();
		Task testTask = new Task("test this", "ISE LAB", null, null, false);
		String testString = gson.toJson(testTask);
		System.out.println(testString);
		Task retrieve = gson.fromJson(testString, Task.class);
		boolean retrieveUrgent = retrieve.isUrgent();
		System.out.println(retrieveUrgent);
		
	}
}
