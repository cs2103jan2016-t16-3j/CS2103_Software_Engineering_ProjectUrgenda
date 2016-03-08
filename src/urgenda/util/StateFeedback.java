package urgenda.util;

import java.util.ArrayList;

public class StateFeedback {
	
	public enum State {
		ALL_TASKS, MULTIPLE_MATCHES, SHOW_SEARCH, ALL_TASK_AND_COMPLETED, DISPLAY, ERROR, SHOW_HELP
	}
	
	private State _state;
	private TaskList _allTasks;
	private String _feedback;
	private ArrayList<Integer> _showmoreIndexes; //index of tasks to show details for
	
	// default constructor
	public StateFeedback() {
		_showmoreIndexes = new ArrayList<Integer>();
	}
	
	// constructor for default generation of All tasks
	public StateFeedback(ArrayList<Task> tasks, int overdue, int today, int urgent, int remaining) {
		_allTasks = new TaskList(tasks, overdue, today, urgent, remaining);   //swapped urgent and today
		_showmoreIndexes = new ArrayList<Integer>();
	}

	//constructor passing tasklist object
	public StateFeedback(TaskList taskList) {
		_allTasks = taskList;
		_showmoreIndexes = new ArrayList<Integer>();
	}
	
	// TODO constructor for tasklist object with both tasks and archive
	
	public TaskList getAllTasks() {
		return _allTasks;
	}

	public String getFeedback() {
		return _feedback;
	}
	
	public ArrayList<Integer> getDetailedIndexes() {
		return _showmoreIndexes;
		
	}
	public void setAllTasks(TaskList tasks) {
		_allTasks = tasks;
	}

	public void setFeedback(String feedback) {
		_feedback = feedback;
	}

	public State getState() {
		return _state;
	}

	public void setState(State currState) {
		_state = currState;
	}
	
	public void addDetailedTaskIdx(int index) {
		_showmoreIndexes.add(index);
	}
	
}
