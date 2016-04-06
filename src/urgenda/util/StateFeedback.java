//@@author A0131857B
package urgenda.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Utility used when returning from Logic component to UI component to pass the state to be displayed, all 
 * tasks to display, the task to set as selected, and the number of overdue tasks.
 * 
 * @author KangSoon
 *
 */
public class StateFeedback {

	// Enumerations
	public enum State {
		ALL_TASKS, MULTIPLE_MATCHES, SHOW_SEARCH, ALL_TASK_AND_COMPLETED, 
		DISPLAY, ERROR, SHOW_HELP, EXIT, ARCHIVE, FIND_FREE, HIDE, DEMO
	}
	
	// Private attributes
	private State _state;
	private TaskList _allTasks;
	private String _feedback;
	private int _displayPosition = 0;
	private int _overdueCount = 0;
	private ArrayList<Integer> _showMoreIndexes; // index of tasks to show details for

	/**
	 * Creates a StateFeedback with default private attribute values.
	 */
	public StateFeedback() {
		_showMoreIndexes = new ArrayList<Integer>();
	}

	/**
	 * Creates a StateFeeback with array of tasks, and count for overdue, today, and
	 * remaining tasks.
	 * 
	 * @param tasks
	 *            array of tasks
	 * @param overdue
	 *            number of overdue tasks
	 * @param today
	 *            number of today tasks
	 * @param remaining
	 *            number of remaining tasks
	 */
	public StateFeedback(ArrayList<Task> tasks, int overdue, int today, int remaining) {
		_allTasks = new TaskList(tasks, overdue, today, remaining);
		_showMoreIndexes = new ArrayList<Integer>();
	}

	/**
	 * constructor when the array of archive tasks, and count for archive tasks
	 * are passed.
	 * 
	 * @param archives
	 *            array of archive tasks
	 * @param archive
	 *            number of archive tasks
	 */
	public StateFeedback(ArrayList<Task> archives, int archive) {
		_allTasks = new TaskList(archives, archive);
		_showMoreIndexes = new ArrayList<Integer>();
	}

	/**
	 * constructor using a Tasklist object that contains the list of tasks.
	 * 
	 * @param taskList
	 *            TaskList object containing list of tasks
	 */
	public StateFeedback(TaskList taskList) {
		_allTasks = taskList;
		_showMoreIndexes = new ArrayList<Integer>();
	}

	/**
	 * getter for task list.
	 * 
	 * @return Tasklist object
	 */
	public TaskList getAllTasks() {
		return _allTasks;
	}

	/**
	 * getter for display feedback.
	 * 
	 * @return feedback string
	 */
	public String getFeedback() {
		return _feedback;
	}

	/**
	 * getter for array of detailed indexes.
	 * 
	 * @return array of detailed indexes
	 */
	public ArrayList<Integer> getDetailedIndexes() {
		return _showMoreIndexes;

	}

	/**
	 * setter for Tasklist object.
	 * 
	 * @param tasks
	 *            Tasklist object to be set
	 */
	public void setAllTasks(TaskList tasks) {
		_allTasks = tasks;
	}

	/**
	 * setter for feedback string.
	 * 
	 * @param feedback
	 *            feedback string to be set
	 */
	public void setFeedback(String feedback) {
		_feedback = feedback;
	}

	/**
	 * getter for state type.
	 * 
	 * @return state type enumeration used
	 */
	public State getState() {
		return _state;
	}

	/**
	 * setter for state type.
	 * 
	 * @param stateType
	 *            state type to be used
	 */
	public void setState(State stateType) {
		_state = stateType;
	}

	/**
	 * adds index for task to show details for.
	 * 
	 * @param index
	 *            index of task to show details for
	 */
	public void addDetailedTaskIdx(int index) {
		_showMoreIndexes.add(index);
		Collections.sort(_showMoreIndexes);
	}

	/**
	 * getter for index of task to set as selected.
	 * 
	 * @return index of task to be set as selected
	 */
	public int getDisplayPosition() {
		return _displayPosition;
	}

	/**
	 * setter for index of task to be set as selected.
	 * 
	 * @param displayPosition
	 *            index of task to be set as selected
	 */
	public void setDisplayPosition(int displayPosition) {
		_displayPosition = displayPosition;
	}

	/**
	 * setter for overdue tasks count.
	 * 
	 * @param overdueCount
	 *            count of overdue tasks to be set
	 */
	public void setOverdueCount(int overdueCount) {
		_overdueCount = overdueCount;
	}

	/**
	 * getter for overdue tasks count.
	 * 
	 * @return count of overdue tasks
	 */
	public int getOverdueCount() {
		return _overdueCount;
	}
}
