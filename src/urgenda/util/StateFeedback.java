package urgenda.util;

import java.util.ArrayList;

public class StateFeedback {
	
	private TaskList _tasks;
	private TaskList _archiveTasks;
	private String _feedback;
	private boolean _isError;
	private String _displayHeader;
	private ArrayList<Integer> _tasksDetailed; //index of tasks to show details for
	
	// default constructor
	public StateFeedback() {
		_tasksDetailed = new ArrayList<Integer>();
	}
	
	// constructor for creation of tasklist with list of tasks
	public StateFeedback(ArrayList<Task> tasks, int overdue, int today, int urgent, int remaining, int completed) {
		_tasks = new TaskList(tasks, overdue, today, urgent, remaining, completed);
		_tasksDetailed = new ArrayList<Integer>();
	}
	
	//constructor passing tasklist object
	public StateFeedback(TaskList taskList) {
		_tasks = taskList;
		_tasksDetailed = new ArrayList<Integer>();
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
	
	public boolean getIsError() {
		return _isError;
	}
	
	public String getDisplayHeader() {
		return _displayHeader;
	}
	
	public ArrayList<Integer> getDetailedTasks() {
		return _tasksDetailed;
		
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
	public void setIsError(boolean isError) {
		_isError = isError;
	}
	
	public void setDisplayHeader(String displayed) {
		_displayHeader = displayed;
	}
	
	public void addDetailedTaskIdx(int index) {
		_tasksDetailed.add(index);
	}
	
	public void resetDetailedTasks() {
		_tasksDetailed.clear();
	}
}
