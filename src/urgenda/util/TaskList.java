//@@author A0127358Y
package urgenda.util;

import java.util.ArrayList;

/**
 * TaskList class of the Utils component in Urgenda. Used for containg multiple
 * ArrayLists of Task objects into one object. Mainly used by the GUI component.
 *
 */
public class TaskList {

	private ArrayList<Task> _tasks;
	private ArrayList<Task> _archives;
	private int _overdueCount;
	private int _todayCount;
	private int _remainingCount;
	private int _archiveCount;

	/**
	 * Default constructor for creating an empty TaskList object.
	 */
	public TaskList() {
		_tasks = new ArrayList<Task>();
		_archives = new ArrayList<Task>();
		setOverdueCount(0);
		setTodayCount(0);
		setRemainingCount(0);
		setArchiveCount(0);
	}

	/**
	 * Constructor for creating a TaskList that contains only current ongoing
	 * Tasks.
	 * 
	 * @param tasks
	 *            the ArrayList of overdued tasks
	 * @param overdue
	 *            the int number of overdued tasks.
	 * @param today
	 *            the int number of tasks that falls on today.
	 * @param remaining
	 *            the int number of tasks that are in the arraylist but does not
	 *            belong to above two categories.
	 */
	public TaskList(ArrayList<Task> tasks, int overdue, int today, int remaining) {
		_tasks = tasks;
		_archives = new ArrayList<Task>();
		setOverdueCount(overdue);
		setTodayCount(today);
		setRemainingCount(remaining);
		setArchiveCount(0);
	}

	/**
	 * Constructor for creating a TaskList that contains completed and
	 * non-completed Tasks.
	 * 
	 * @param tasks
	 *            the ArrayList of current tasks
	 * @param archives
	 *            the ArrayList of completed tasks
	 * @param overdue
	 *            the int number of overdued tasks
	 * @param today
	 *            the int number of tasks that falls on today.
	 * @param remaining
	 *            the int number of tasks that are from the current task
	 *            arraylist but does not belong to above two categories.
	 * @param archive
	 *            the int number of tasks that are the archive arraylist.
	 */
	public TaskList(ArrayList<Task> tasks, ArrayList<Task> archives, int overdue, int today, int remaining,
			int archive) {
		_tasks = tasks;
		_archives = archives;
		setOverdueCount(overdue);
		setTodayCount(today);
		setRemainingCount(remaining);
		setArchiveCount(archive);
	}

	/**
	 * Constructor for creating a TaskList that contains only completed Tasks.
	 * 
	 * @param archives
	 *            the arraylist of completed tasks
	 * @param archive
	 *            the int number of completed tasks.
	 */
	public TaskList(ArrayList<Task> archives, int archive) {
		_tasks = new ArrayList<Task>();
		_archives = archives;
		setOverdueCount(0);
		setTodayCount(0);
		setRemainingCount(0);
		setArchiveCount(archive);
	}

	/**
	 * Getter for the current ongoing task in a TaskList object.
	 * 
	 * @return _tasks the ArrayList private attribute of a TaskList object
	 */
	public ArrayList<Task> getTasks() {
		return _tasks;
	}

	/**
	 * Getter for the number of overdue tasks in a TaskList object.
	 * 
	 * @return _overdueCount
	 */
	public int getOverdueCount() {
		return _overdueCount;
	}

	/**
	 * Setter for setting the number of overdue tasks in TaskList object.
	 * 
	 * @param overdueCount
	 *            the int to be set to the overdueCount attribute of a TaskList
	 */
	public void setOverdueCount(int overdueCount) {
		_overdueCount = overdueCount;
	}

	/**
	 * Getter for the number of tasks falling on today in a TaskList object.
	 * 
	 * @return _todayCount
	 */
	public int getTodayCount() {
		return _todayCount;
	}

	/**
	 * Setter for setting the number of tasks falling on today in TaskList
	 * object.
	 * 
	 * @param todayCount
	 *            the int to be set to the todayCount attribute of a TaskList.
	 */
	public void setTodayCount(int todayCount) {
		_todayCount = todayCount;
	}

	/**
	 * Getter for the number of tasks that falls in other category in a TaskList
	 * object.
	 * 
	 * @return _remainingCount
	 */
	public int getRemainingCount() {
		return _remainingCount;
	}

	/**
	 * Setter for the number of other tasks in a TaskList object.
	 * 
	 * @param remainingCount
	 *            the int to be set to the remainingCount attribute of a
	 *            TaskList.
	 */
	public void setRemainingCount(int remainingCount) {
		_remainingCount = remainingCount;
	}

	/**
	 * Getter for retrieving the ArrayList of completed tasks in a TaskList
	 * object.
	 * 
	 * @return _archives
	 */
	public ArrayList<Task> getArchives() {
		return _archives;
	}

	/**
	 * Getter for the number of completed tasks in a TaskList object.
	 * 
	 * @return _archiveCount
	 */
	public int getArchiveCount() {
		return _archiveCount;
	}

	/**
	 * Setter for the list of completed tasks in a TaskList object.
	 * 
	 * @param archives
	 *            The ArrayList of Tasks to be set to the _archive attribute of
	 *            a TaskList.
	 */
	public void setArchives(ArrayList<Task> archives) {
		_archives = archives;
	}

	/**
	 * Setter for the number of completed tasks in a TaskList object.
	 * 
	 * @param archiveCount
	 *            the int to be set to the _archiveCount of a TaskList object.
	 */
	public void setArchiveCount(int archiveCount) {
		_archiveCount = archiveCount;
	}

	/**
	 * Getter for the total number of uncompleted tasks within a TaskList
	 * object.
	 * 
	 * @return sum of number of overdue, today and others tasks.
	 */
	public int getUncompletedCount() {
		return _overdueCount + _todayCount + _remainingCount;
	}

}
