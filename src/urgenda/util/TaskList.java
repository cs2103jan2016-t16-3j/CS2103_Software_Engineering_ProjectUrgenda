//@@author A0127358Y
package urgenda.util;

import java.util.ArrayList;

public class TaskList {

	private ArrayList<Task> _tasks;
	private ArrayList<Task> _archives;
	private int _overdueCount;
	private int _todayCount;
	private int _remainingCount;
	private int _archiveCount;
	
	//default constructor creates empty TaskList
	public TaskList() {
		_tasks = new ArrayList<Task>();
		_archives = new ArrayList<Task>();
		setOverdueCount(0);
		setTodayCount(0);
		setRemainingCount(0);
		setArchiveCount(0);
	}
	
	// constructor for tasklist of only uncompleted task
	public TaskList(ArrayList<Task> tasks, int overdue, int today, int remaining) {
		_tasks = tasks;
		_archives = new ArrayList<Task>();
		setOverdueCount(overdue);
		setTodayCount(today);
		setRemainingCount(remaining);
		setArchiveCount(0);
	}
	
	// constructor for tasklist with both uncompleted and completed task
	public TaskList(ArrayList<Task> tasks, ArrayList<Task> archives, int overdue, int today, 
			int remaining, int archive) {
		_tasks = tasks;
		_archives = archives;
		setOverdueCount(overdue);
		setTodayCount(today);
		setRemainingCount(remaining);
		setArchiveCount(archive);
	}
	
	// constructor for tasklist with only archived tasks
	public TaskList(ArrayList<Task> archives, int archive) {
		_tasks = new ArrayList<Task>();
		_archives = archives;
		setOverdueCount(0);
		setTodayCount(0);
		setRemainingCount(0);
		setArchiveCount(archive);
	}
	
	public ArrayList<Task> getTasks() {
		return _tasks;
	}
	
	public int getOverdueCount() {
		return _overdueCount;
	}

	public void setOverdueCount(int overdueCount) {
		_overdueCount = overdueCount;
	}

	public int getTodayCount() {
		return _todayCount;
	}

	public void setTodayCount(int todayCount) {
		_todayCount = todayCount;
	}

	public int getRemainingCount() {
		return _remainingCount;
	}

	public void setRemainingCount(int remainingCount) {
		_remainingCount = remainingCount;
	}

	public ArrayList<Task> getArchives() {
		return _archives;
	}

	public int getArchiveCount() {
		return _archiveCount;
	}

	public void setArchives(ArrayList<Task> archives) {
		_archives = archives;
	}

	public void setArchiveCount(int archiveCount) {
		_archiveCount = archiveCount;
	}
	
	public int getUncompletedCount() {
		return _overdueCount + _todayCount + _remainingCount;
	}

}
