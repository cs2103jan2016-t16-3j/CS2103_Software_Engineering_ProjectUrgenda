package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import urgenda.logic.LogicData;
import urgenda.util.MyLogger;
import urgenda.util.Task;

public class Complete extends TaskCommand {

	private static MyLogger logger = MyLogger.getInstance();
	private static final String MESSAGE_DONE = "Done ";
	private static final String MESSAGE_UNDONE = "To do ";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	private static final String MESSAGE_NO_COMPLETE_MATCH = "No matches found to complete";
	private static final String MESSAGE_NUM = "%1$s tasks have been marked as important:\n";
	private static final String MESSAGE_INVALID_RANGE = "Invalid task range";
	
	// for undo of completed task
	private String _desc;
	private Integer _id;
	private ArrayList<Integer> _multiId;

	private Task _completedTask;
	private LogicData _data;


	public String execute() throws Exception {
		logger.getLogger().warning("Can cause exception");
		_data = LogicData.getInstance();
		ArrayList<Task> matches;
		if (_multiId == null || _multiId.isEmpty()) {
			if (_desc != null) {
				matches = _data.findMatchingDesc(_desc);
				if (matches.size() == 1) {
					_completedTask = matches.get(0);
				} else if (matches.size() > 1) {
					_data.clearDisplays();
					_data.setDisplays(matches);
					_data.setCurrState(LogicData.DisplayState.MULTIPLE_COMPLETE);
					logger.getLogger().severe("Exception(Multiple complete) thrown");
					throw new Exception(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
				} // else matches has no match hence _completedTask remains null
			} else if (_id != null && _id.intValue() != -1) {
				_completedTask = _data.findMatchingPosition(_id.intValue());
			}
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			if (_completedTask == null) {
				logger.getLogger().severe("Exception(No complete match) thrown");
				throw new Exception(MESSAGE_NO_COMPLETE_MATCH);
			}
			_completedTask.setIsCompleted(true);
			_completedTask.setDateModified(LocalDateTime.now());
			_data.deleteTask(_completedTask);
			_data.addArchive(_completedTask);
			return MESSAGE_DONE + taskMessage(_completedTask) + "!";
		} else {
			Collections.sort(_multiId);
			if (_multiId.get(0) >= 0 && _multiId.get((_multiId.size()-1)) <= _data.getDisplays().size()) {
				ArrayList<Task> _doneTaskList = new ArrayList<Task>();
				StringBuilder feedback = new StringBuilder();
				Iterator<Integer> i = _multiId.iterator();
				while (i.hasNext()) {
					Task temp = _data.findMatchingPosition(i.next().intValue());
					_doneTaskList.add(temp);
					feedback = feedback.append(taskMessage(temp)).append("\n");
				}
				_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				for (int j = 0; j < _doneTaskList.size(); j++) {
					_doneTaskList.get(j).setIsCompleted(true);
					_doneTaskList.get(j).setDateModified(LocalDateTime.now());
					_data.deleteTask(_doneTaskList.get(j));
					_data.addArchive(_doneTaskList.get(j));
				}
				return String.format(MESSAGE_NUM, _doneTaskList.size()) + feedback;
			} else {
				_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
				throw new Exception(MESSAGE_INVALID_RANGE);
			}
		}
	}

	public String undo() {
		_completedTask.setIsCompleted(false);
		_completedTask.setDateModified(LocalDateTime.now());
		_data.removeArchive(_completedTask);
		_data.addTask(_completedTask);
		_data.setTaskPointer(_completedTask);
		return MESSAGE_UNDONE + taskMessage(_completedTask) + "!";
	}

	public String redo() {
		_completedTask.setIsCompleted(true);
		_completedTask.setDateModified(LocalDateTime.now());
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
