package urgenda.util;

import java.util.ArrayList;

public class StateFeedback {
	
	private TaskList _tasks;
	private TaskList _archiveTasks;
	private String _feedback;
	private String _displayHeader;
	
	// default constructor
	public StateFeedback() {
		
	}
	
	// constructor for creation of tasklist with list of tasks
	public StateFeedback(ArrayList<Task> tasks, int overdue, int today, int urgent, int remaining, int completed) {
		_tasks = new TaskList(tasks, overdue, today, urgent, remaining, completed);   //swapped urgent and today
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
	
	public String getDisplayHeader() {
		return _displayHeader;
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
	public void setDisplayHeader(String displayed) {
		_displayHeader = displayed;
	}
}
