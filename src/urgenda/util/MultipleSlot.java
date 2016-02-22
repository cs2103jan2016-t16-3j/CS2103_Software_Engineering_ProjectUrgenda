package urgenda.util;

import java.util.ArrayList;

public class MultipleSlot {

	private String _desc;
	private ArrayList<Task> _blockedSlots;

	public void addTasks(Task task) {
		_blockedSlots.add(task);
	}
	
	public String getDesc() {
		return _desc;
	}

	public ArrayList<Task> getBlockedSlots() {
		return _blockedSlots;
	}

	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setBlockedSlots(ArrayList<Task> blockedSlots) {
		_blockedSlots = blockedSlots;
	}

}
