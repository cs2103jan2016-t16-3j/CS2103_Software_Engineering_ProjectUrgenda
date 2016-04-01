package test.testStorage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.Test;

import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import urgenda.util.Task;
import urgenda.storage.Decryptor;
import urgenda.storage.Encryptor;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DecryptEncryptTest {
	
	@Test
	public void test002invalidJsonString(){
		Decryptor decrypt = new Decryptor();
		ArrayList<String> taskString = new ArrayList<String>();
		ArrayList<Task> taskList = new ArrayList<Task>();
		ArrayList<Task> actualTaskList = new ArrayList<Task>();
		taskString.add("invalid strings");
		
		actualTaskList = decrypt.decryptTaskList(taskString);
		assertEquals(taskList.size(), actualTaskList.size());
		
		actualTaskList = decrypt.decryptArchiveList(taskString);
		assertEquals(taskList.size(), actualTaskList.size());
	}
	
	@Test
	public void test001NormalTask(){
		Encryptor encrypt = new Encryptor();
		Decryptor decrypt = new Decryptor();
		ArrayList<Task> taskList = new ArrayList<Task>();
		ArrayList<Task> actualTaskList = new ArrayList<Task>();
		ArrayList<String> taskString = new ArrayList<String>();
		Task task = new Task();
		String desc = "test desc";
		String location = "test loc";
		boolean isCompleted = true;
		boolean isImportant = true;
		boolean isOverdue = true;
		LocalDateTime startTime = LocalDateTime.of(2016, 8, 17, 17, 0);
		LocalDateTime endTime = LocalDateTime.of(2016, 8, 17, 20, 0);
		LocalDateTime dateAdded = LocalDateTime.of(2016, 8, 9, 7, 0);
		LocalDateTime dateModified = LocalDateTime.of(2016, 8, 9, 11, 0);
		
		task.setDesc(desc);
		task.setLocation(location);
		task.setIsCompleted(isCompleted);
		task.setIsImportant(isImportant);
		task.setIsOverdue(isOverdue);
		task.setStartTime(startTime);
		task.setEndTime(endTime);
		task.setDateAdded(dateAdded);
		task.setDateModified(dateModified);
		task.updateTaskType();
		
		taskList.add(task);
		
		taskString = encrypt.encrypt(taskList);
		actualTaskList = decrypt.decryptTaskList(taskString);
		for (int i = 0; i < taskList.size(); i++){
			Task exTask = taskList.get(i);
			Task actlTask = actualTaskList.get(i);
			assertEquals(exTask.getDesc(), actlTask.getDesc());
			assertEquals(exTask.getLocation(), actlTask.getLocation());
			assertEquals(exTask.isCompleted(), actlTask.isCompleted());
			assertEquals(exTask.isImportant(), actlTask.isImportant());
			assertEquals(exTask.isOverdue(), actlTask.isOverdue());
			assertEquals(exTask.getStartTime(), actlTask.getStartTime());
			assertEquals(exTask.getEndTime(), actlTask.getEndTime());
			assertEquals(exTask.getDateAdded(), actlTask.getDateAdded());
			assertEquals(exTask.getDateModified(), actlTask.getDateModified());
			assertEquals(exTask.getTaskType(), actlTask.getTaskType());
		}
		
		actualTaskList = decrypt.decryptArchiveList(taskString);
		for (int i = 0; i < taskList.size(); i++){
			Task exTask = taskList.get(i);
			Task actlTask = actualTaskList.get(i);
			assertEquals(exTask.getDesc(), actlTask.getDesc());
			assertEquals(exTask.getLocation(), actlTask.getLocation());
			assertEquals(exTask.isCompleted(), actlTask.isCompleted());
			assertEquals(exTask.isImportant(), actlTask.isImportant());
			assertEquals(exTask.isOverdue(), actlTask.isOverdue());
			assertEquals(exTask.getStartTime(), actlTask.getStartTime());
			assertEquals(exTask.getEndTime(), actlTask.getEndTime());
			assertEquals(exTask.getDateAdded(), actlTask.getDateAdded());
			assertEquals(exTask.getDateModified(), actlTask.getDateModified());
			assertEquals(exTask.getTaskType(), actlTask.getTaskType());
		}
		
	}

}
