package urgenda.command;

import java.time.LocalDateTime;

import urgenda.logic.LogicData;
import urgenda.util.MultipleSlot;
import urgenda.util.Task;

public class BlockSlots extends TaskCommand {

	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_BLOCK = "Blocked ";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_ERROR = "Error: ";
	private static final String MESSAGE_INVALID_TYPE = "Please input an EVENT for blocking of timeslots";
	private static final String MESSAGE_INSUFFICIENT_SLOTS = "Insufficient slots entered for blocking of timeslots";

	private MultipleSlot _block;
	private LogicData _data;
	private Task _newTask;

	// default constructor
	public BlockSlots() {
		_block = new MultipleSlot();
	}

	public BlockSlots(Task newTask) {
		_newTask = newTask;
		_block = new MultipleSlot();
	}

	// throws exception to ensure that block is not stored in undo stack
	public String execute() throws Exception {
		if (_newTask.getTaskType() != Task.Type.EVENT) {
			throw new Exception(MESSAGE_ERROR + MESSAGE_INVALID_TYPE);
		} else if (_block.isEmpty()) {
			throw new Exception(MESSAGE_ERROR + MESSAGE_INSUFFICIENT_SLOTS);
		}
		_data = LogicData.getInstance();
		_block.sortSlots();
		_newTask.setSlot(_block);
		_newTask.setId(_data.getCurrentId());
		LocalDateTime now = LocalDateTime.now();
		_newTask.setDateAdded(now);
		_newTask.setDateModified(now);
		_data.addTask(_newTask);
		_data.updateCurrentId();
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		_data.setTaskPointer(_newTask);

		return MESSAGE_BLOCK + taskMessage(_newTask) + MESSAGE_ADDED;
	}

	public void setNewTask(Task newTask) {
		_newTask = newTask;
	}

	public void addNewSlots(LocalDateTime start, LocalDateTime end) {
		_block.addTimeSlot(start, end);
	}

	public String undo() {
		_data.deleteTask(_newTask);
		return taskMessage(_newTask) + MESSAGE_REMOVE;
	}

	public String redo() {
		_data.addTask(_newTask);
		_data.setTaskPointer(_newTask);
		return MESSAGE_BLOCK + taskMessage(_newTask) + MESSAGE_ADDED;
	}

}
