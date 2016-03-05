package urgenda.util;

import java.util.ArrayList;

public class StateFeedback {
	
	public enum State {
		ALL_TASKS, MULTIPLE_MATCHES, SHOW_SEARCH, ALL_TASK_AND_COMPLETED, DISPLAY, ERROR
	}
	
	private State _state;
	private TaskList _tasks;
	private TaskList _archiveTasks;
	private String _feedback;
	private String _displayHeader;
	private ArrayList<Integer> _tasksDetailed; //index of tasks to show details for
	
	// default constructor
	public StateFeedback() {
		_tasksDetailed = new ArrayList<Integer>();
	}
	
	// constructor for default generation of All tasks
	public StateFeedback(ArrayList<Task> tasks, int overdue, int today, int urgent, int remaining) {
		_state = State.ALL_TASKS;
		_tasks = new TaskList(tasks, overdue, today, urgent, remaining);   //swapped urgent and today
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
	
	public void setDisplayHeader(String displayed) {
		_displayHeader = displayed;
	}

	public State getState() {
		return _state;
	}

	public void setState(State currState) {
		_state = currState;
	}
	
	public void addDetailedTaskIdx(int index) {
		_tasksDetailed.add(index);
	}
	
	public void resetDetailedTasks() {
		_tasksDetailed.clear();
	}
}
