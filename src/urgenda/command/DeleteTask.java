package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import urgenda.logic.LogicData;
import urgenda.util.LogicException;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

public class DeleteTask extends TaskCommand {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String MESSAGE_ADDED = " added";
	private static final String MESSAGE_REMOVE = " removed";
	private static final String MESSAGE_NO_DELETE_MATCH = "No matches found to delete";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	private static final String MESSAGE_NUM_REMOVED = "%1$s tasks have been removed:";
	private static final String MESSAGE_NUM_ADDED = "%1$s tasks have been added:";

	// desc will be entered if there was a description for deletion
	// else one or more integers will be indicated
	private String _desc;
	private ArrayList<Integer> _positions;

	// to store from deletion, so that undo can be done
	private ArrayList<Task> _deletedTasks;
	private LogicData _data;

	public String execute() throws LogicException {
		logger.getLogger().warning("Can cause exception");

		_data = LogicData.getInstance();
		ArrayList<Task> matches;
		if (_desc != null) {
			matches = _data.findMatchingDesc(_desc);
			if (matches.size() == 1) {
				_deletedTasks = matches;
			} else if (matches.size() > 1) {
				_data.clearDisplays();
				_data.setDisplays(matches);
				_data.setCurrState(LogicData.DisplayState.MULTIPLE_DELETE);
				logger.getLogger().severe("Exception(Multiple delete) thrown");
				throw new LogicException(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
			} // else matches has no match hence _deletedTasks remains null
		} else if (_positions != null && _positions.size() != 0) {
			Collections.sort(_positions);
			_deletedTasks = _data.findMatchingPosition(_positions);
		}
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		if (_deletedTasks == null || _deletedTasks.isEmpty()) {
			logger.getLogger().severe("Exception(No del match) thrown");
			throw new LogicException(MESSAGE_NO_DELETE_MATCH);
		}
		updateDateModified(_deletedTasks);
		_data.deleteTasks(_deletedTasks);
		_data.clearShowMoreTasks();
		
		return deleteFeedback();
	}

	private String deleteFeedback() {
		if (_deletedTasks.size() == 1) {
			return taskMessageWithMulti(_deletedTasks.get(0)) + MESSAGE_REMOVE;
		} else {
			String feedback = String.format(MESSAGE_NUM_REMOVED, _deletedTasks.size());
			feedback += multipleTaskFeedback();
			return feedback;
		}
	}

	private String multipleTaskFeedback() {
		String feedback = "";
		for (Task task : _deletedTasks) {
			feedback += "\n" + taskMessageWithMulti(task);
		}
		return feedback;
	}

	public String undo() {
		updateDateModified(_deletedTasks);
		_data.addTasks(_deletedTasks);
		_data.setTaskPointer(_deletedTasks.get(0));
		return addFeedback();
	}
	
	private String addFeedback() {
		if (_deletedTasks.size() == 1) {
			return taskMessageWithMulti(_deletedTasks.get(0)) + MESSAGE_ADDED;
		} else {
			String feedback = String.format(MESSAGE_NUM_ADDED, _deletedTasks.size());
			feedback += multipleTaskFeedback();
			return feedback;
		}
	}

	public String redo() {
		updateDateModified(_deletedTasks);
		_data.deleteTasks(_deletedTasks);
		return deleteFeedback();
	}
	
	public void updateDateModified(ArrayList<Task> tasks) {
		LocalDateTime now = LocalDateTime.now();
		for (Task task : tasks) {
			task.setDateModified(now);
		}
	}

	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setPositions(ArrayList<Integer> positions) {
		_positions = positions;
	}

}
