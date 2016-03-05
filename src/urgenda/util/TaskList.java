package urgenda.util;

import java.util.ArrayList;
import java.util.Iterator;

public class TaskList implements Iterable<Task>{

	private ArrayList<Task> _tasks;
	private ArrayList<Task> _archives;
	private int _overdueCount;
	private int _urgentCount;
	private int _todayCount;
	private int _remainingCount;
	private int _archiveCount;
	
	//default constructor creates empty TaskList
	public TaskList() {
		_tasks = new ArrayList<Task>();
		_archives = new ArrayList<Task>();
		setOverdueCount(0);
		setTodayCount(0);
		setUrgentCount(0);  //swapped urgent and today
		setRemainingCount(0);
		setArchiveCount(0);
	}
	
	// constructor for tasklist of only uncompleted task
	public TaskList(ArrayList<Task> tasks, int overdue, int today, int urgent, int remaining) {
		_tasks = tasks;
		_archives = new ArrayList<Task>();
		setOverdueCount(overdue);
		setTodayCount(today);
		setUrgentCount(urgent);                //swapped urgent and today and tat fn in signature
		setRemainingCount(remaining);
		setArchiveCount(0);
	}
	
	// constructor for tasklist with both uncompleted and completed task
	public TaskList(ArrayList<Task> tasks, ArrayList<Task> archives, int overdue, int today, int urgent, 
			int remaining, int archive) {
		_tasks = tasks;
		_archives = archives;
		setOverdueCount(overdue);
		setTodayCount(today);
		setUrgentCount(urgent);
		setRemainingCount(remaining);
		setArchiveCount(archive);
	}
	
	public ArrayList<Task> getList() {
		return _tasks;
	}
	@Override
	public Iterator<Task> iterator() {
		return _tasks.iterator();
	}

	public int getOverdueCount() {
		return _overdueCount;
	}

	public void setOverdueCount(int _overdueCount) {
		this._overdueCount = _overdueCount;
	}

	public int getUrgentCount() {
		return _urgentCount;
	}

	public void setUrgentCount(int _urgentCount) {
		this._urgentCount = _urgentCount;
	}

	public int getTodayCount() {
		return _todayCount;
	}

	public void setTodayCount(int _todayCount) {
		this._todayCount = _todayCount;
	}

	public int getRemainingCount() {
		return _remainingCount;
	}

	public void setRemainingCount(int _remainingCount) {
		this._remainingCount = _remainingCount;
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

}
