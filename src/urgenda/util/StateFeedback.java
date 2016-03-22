package urgenda.util;

import java.util.ArrayList;
import java.util.Collections;

public class StateFeedback {
	
	public enum State {
		ALL_TASKS, MULTIPLE_MATCHES, SHOW_SEARCH, ALL_TASK_AND_COMPLETED, DISPLAY, ERROR, SHOW_HELP, EXIT, ARCHIVE
	}
	
	private State _state;
	private TaskList _allTasks;
	private String _feedback;
	private int _displayPosition = 0;
	private ArrayList<Integer> _showMoreIndexes; //index of tasks to show details for
	
	// default constructor
	public StateFeedback() {
		_showMoreIndexes = new ArrayList<Integer>();
	}
	
	// constructor for default generation of All tasks
	public StateFeedback(ArrayList<Task> tasks, int overdue, int today, int remaining) {
		_allTasks = new TaskList(tasks, overdue, today, remaining);
		_showMoreIndexes = new ArrayList<Integer>();
	}
	
	public StateFeedback(ArrayList<Task> archives, int archive) {
		_allTasks = new TaskList(archives, archive);
		_showMoreIndexes = new ArrayList<Integer>();
	}

	//constructor passing tasklist object
	public StateFeedback(TaskList taskList) {
		_allTasks = taskList;
		_showMoreIndexes = new ArrayList<Integer>();
	}
	
	// TODO constructor for tasklist object with both tasks and archive
	
	public TaskList getAllTasks() {
		return _allTasks;
	}

	public String getFeedback() {
		return _feedback;
	}
	
	public ArrayList<Integer> getDetailedIndexes() {
		return _showMoreIndexes;
		
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
		_showMoreIndexes.add(index);
		Collections.sort(_showMoreIndexes);
	}

	public int getDisplayPosition() {
		return _displayPosition;
	}

	public void setDisplayPosition(int displayPosition) {
		_displayPosition = displayPosition;
	}
	
}
