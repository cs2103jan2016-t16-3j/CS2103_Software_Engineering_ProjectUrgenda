package urgenda.util;

import java.util.ArrayList;

public class StateFeedback {
	
	private TaskList _tasks;
	private TaskList _archiveTasks;
	private String _feedback;
	
	// default constructor
	public StateFeedback() {
		
	}
	
	// constructor for creation of tasklist with list of tasks
	public StateFeedback(ArrayList<Task> tasks) {
		_tasks = new TaskList(tasks);
	}
	
	// constructor for feedback string only
	public StateFeedback(String feedback) {
		_feedback = feedback;
	}
	
	public TaskList getTasks() {
		return _tasks;
	}
	
	public TaskList getArchiveTasks() {
		return _archiveTasks;
	}

	public String getFeedback() {
		return _feedback;
	}

	public void setTasks(TaskList tasks) {
		_tasks = tasks;
	}

	public void setArchiveTasks(TaskList archiveTasks) {
		_archiveTasks = archiveTasks;
	}

	public void setFeedback(String feedback) {
		_feedback = feedback;
	}

}
