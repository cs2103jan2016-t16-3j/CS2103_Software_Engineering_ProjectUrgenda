package urgenda.util;

import java.util.ArrayList;
import java.util.Iterator;

public class TaskList implements Iterable<Task>{

	private ArrayList<Task> _tasks;
	private int _overdueCount;
	private int _urgentCount;
	private int _todayCount;
	private int _remainingCount;
	
	//default constructor creates empty TaskList
	public TaskList() {
		setOverdueCount(0);
		setTodayCount(0);
		setUrgentCount(0);  //swapped urgent and today
		setRemainingCount(0);
	}
	
	public TaskList(ArrayList<Task> tasks, int overdue, int today, int urgent, int remaining) {
		_tasks = tasks;
		setOverdueCount(overdue);
		setTodayCount(today);
		setUrgentCount(urgent);                //swapped urgent and today and tat fn in signature
		setRemainingCount(remaining);
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

}
