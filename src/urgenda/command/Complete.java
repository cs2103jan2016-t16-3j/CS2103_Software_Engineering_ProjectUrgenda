package urgenda.command;

import java.util.ArrayList;

import urgenda.logic.LogicData;
import urgenda.util.MyLogger;
import urgenda.util.Task;

public class Complete extends TaskCommand {
	
	private static final String MESSAGE_DONE = "Done ";
	private static final String MESSAGE_UNDONE = "To do ";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	private static final String MESSAGE_NO_COMPLETE_MATCH = "No matches found to complete";
	
	// for undo of completed task
	private String _desc;
	private Integer _id;
	
	private Task _completedTask;
	private LogicData _data;
	
	@SuppressWarnings("static-access")
	public String execute() throws Exception {
		MyLogger logger = MyLogger.getInstance();
		logger.myLogger.warning("Can cause exception");
		_data = LogicData.getInstance();
		ArrayList<Task> matches;
		if (_desc != null) {
			matches = _data.findMatchingDesc(_desc);
			if (matches.size() == 1) {
				_completedTask = matches.get(0);
			} else if (matches.size() > 1) {
				_data.clearDisplays();
				_data.setDisplays(matches);
				_data.setCurrState(LogicData.DisplayState.MULTIPLE_COMPLETE);
				logger.myLogger.severe("Exception(Multiple complete) thrown");
				throw new Exception(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
			} // else matches has no match hence _completedTask remains null
		} else if (_id != null && _id.intValue() != -1) {
			_completedTask = _data.findMatchingPosition(_id.intValue());
		}
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		if (_completedTask == null) {
			logger.myLogger.severe("Exception(No complete match) thrown");
			throw new Exception(MESSAGE_NO_COMPLETE_MATCH);
		}
		_completedTask.setIsCompleted(true);
		_data.deleteTask(_completedTask);
		_data.addArchive(_completedTask);
		return MESSAGE_DONE + taskMessage(_completedTask) + "!";
	}

	public String undo() {
		_completedTask.setIsCompleted(false);
		_data.removeArchive(_completedTask);
		_data.addTask(_completedTask);
		_data.setTaskPointer(_completedTask);
		return MESSAGE_UNDONE + taskMessage(_completedTask) + "!";
	}

	public String redo() {
		_completedTask.setIsCompleted(true);
		_data.deleteTask(_completedTask);
		_data.addArchive(_completedTask);
		return MESSAGE_DONE + taskMessage(_completedTask) + "!";
	}


	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setId(int id) {
		_id = Integer.valueOf(id);
	}

}
