package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;

public class BlockSlots extends TaskCommand {
	
	private static final String MESSAGE_EVENT_DATETIME = ", %1$d/%2$d, %3$02d:%4$02d - %5$02d:%6$02d";
	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_BLOCK = "Block: ";
	private static final String MESSAGE_REMOVE = " removed";
	
	private MultipleSlot _block;
	private LogicData _data;
	private ArrayList<Task> _taskBlocks;
	
	// default constructor
	public BlockSlots() {
		_block = new MultipleSlot();
		_taskBlocks = new ArrayList<Task>();
	}

	// constructor when description is given
	public BlockSlots(String desc) {
		_block = new MultipleSlot(desc);
		_taskBlocks = new ArrayList<Task>();
	}
	
	// if default constructor is used, ensure that _block desc set
	public String execute(LogicData data) {
		assert (_block.getDesc() != null); // asserts that the desc is set
		_data = data;
		setDateAdded();
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		return MESSAGE_BLOCK + addBlockTasks();
	}

	private void setDateAdded() {
		LocalDateTime now = LocalDateTime.now();
		for (Task task : _taskBlocks) {
			task.setDateAdded(now);
			task.setDateModified(now);
		}
	}

	public String addBlockTasks() {
		for (Task task : _taskBlocks) {
			task.setSlot(_block);
			_data.addTask(task);
		}
		return taskMessage() + MESSAGE_ADDED;
	}
	
	private String taskMessage() {
		String feedback = _block.getDesc();
		for (Task task : _taskBlocks) {
			feedback += String.format(MESSAGE_EVENT_DATETIME,  task.getStartTime().getDayOfMonth(),
					task.getStartTime().getMonthValue(), task.getStartTime().getHour(), 
					task.getStartTime().getMinute(), task.getEndTime().getHour(), 
					task.getEndTime().getMinute());
		}
		return feedback;
	}

	public void addTask(Task task) {
		_taskBlocks.add(task);
	}

	public void setDesc(String desc) {
		_block.setDesc(desc);
	}
	
	public String undo() {
		for (Task task : _taskBlocks) {
			_data.deleteTask(task);
		}
		return taskMessage() + MESSAGE_REMOVE;
	}

	public String redo() {
		// TODO Decide if Block: to be returned
		return addBlockTasks();
	}

}
