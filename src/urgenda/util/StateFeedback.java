package urgenda.util;

import java.util.ArrayList;

public class StateFeedback {
	
	public enum State {
		ALL_TASKS, MULTIPLE_MATCHES, SHOW_SEARCH, ALL_TASK_AND_COMPLETED, DISPLAY 
	}
	
	private State _state;
	private TaskList _tasks;
	private TaskList _archiveTasks;
	private String _feedback;
	private String _displayHeader;
	
	// default constructor
	public StateFeedback() {
		
	}
	
	// constructor for default generation of All tasks
	public StateFeedback(ArrayList<Task> tasks, int overdue, int today, int urgent, int remaining) {
		_state = State.ALL_TASKS;
		_tasks = new TaskList(tasks, overdue, today, urgent, remaining);   //swapped urgent and today
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

	public State getState() {
		return _state;
	}

	public void setState(State currState) {
		_state = currState;
	}
}
