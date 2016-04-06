//@@author A0080436J
package urgenda.command;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import urgenda.logic.LogicData;
import urgenda.util.LogicException;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

public class Complete extends TaskCommand {

	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static final String MESSAGE_DONE = "Done ";
	private static final String MESSAGE_UNDONE = " unmarked from done";
	private static final String MESSAGE_MULTIPLE_FOUND = "Multiple tasks with description \"%1$s\" found";
	private static final String MESSAGE_NO_COMPLETE_MATCH = "No matches found to complete";
	private static final String MESSAGE_NUM_DONE = "%1$s tasks have been marked as done: ";
	private static final String MESSAGE_NUM_UNDONE = "%1$s have been unmarked from done: ";
	private static final String MESSAGE_TASK_DESC = "\"%1$s\", ";
	private static final String COMMA_DELIMITER = ",";

	// for undo of completed task
	private String _desc;
	private ArrayList<Integer> _positions;

	private ArrayList<Task> _completedTasks;
	private LogicData _data;

	public String execute() throws LogicException {
		logger.getLogger().warning("Can cause exception");
		
		_data = LogicData.getInstance();
		ArrayList<Task> matches;
		if (_desc != null) {
			matches = _data.findMatchingDesc(_desc);
			if (matches.size() == 1) {
				_completedTasks = matches;
			} else if (matches.size() > 1) {
				_data.clearDisplays();
				_data.setDisplays(matches);
				_data.setCurrState(LogicData.DisplayState.MULTIPLE_COMPLETE);
				logger.getLogger().severe("Exception(Multiple complete) thrown");
				throw new LogicException(String.format(MESSAGE_MULTIPLE_FOUND, _desc));
			} // else matches has no match hence _completedTasks remains null
		} else if (_positions != null && _positions.size() != 0) {
			Collections.sort(_positions);
			_completedTasks = _data.findMatchingPosition(_positions);
		}
		_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
		if (_completedTasks == null || _completedTasks.isEmpty()) {
			logger.getLogger().severe("Exception(No complete match) thrown");
			throw new LogicException(MESSAGE_NO_COMPLETE_MATCH);
		}
		completeTasks(true);
		updateDateModified(_completedTasks);
		_data.deleteTasks(_completedTasks);
		_data.addArchive(_completedTasks);
		return completeFeedback();
	}

	private String completeFeedback() {
		if (_completedTasks.size() == 1) {
			return MESSAGE_DONE + taskMessageWithMulti(_completedTasks.get(0)) + "!";
		} else {
			String feedback = String.format(MESSAGE_NUM_DONE, _completedTasks.size());
			feedback += multipleTaskFeedback();
			return feedback;
		}
	}

	private String multipleTaskFeedback() {
		String feedback = "";
		for (Task task : _completedTasks) {
			feedback += String.format(MESSAGE_TASK_DESC, task.getDesc());
		}
		feedback = feedback.substring(0, feedback.lastIndexOf(COMMA_DELIMITER));
		return feedback;
	}

	private void completeTasks(boolean isComplete) {
		for (Task task : _completedTasks) {
			task.setIsCompleted(isComplete);
			task.setDateModified(LocalDateTime.now());			
		}
	}

	public String undo() {
		completeTasks(false);
		updateDateModified(_completedTasks);
		_data.removeArchive(_completedTasks);
		_data.addTasks(_completedTasks);
		_data.setTaskPointer(_completedTasks.get(0));
		return uncompletedFeedback();
	}

	private String uncompletedFeedback() {
		if (_completedTasks.size() == 1) {
			return taskMessageWithMulti(_completedTasks.get(0)) + MESSAGE_UNDONE + "!";
		} else {
			String feedback = String.format(MESSAGE_NUM_UNDONE, _completedTasks.size());
			feedback += multipleTaskFeedback();
			return feedback;
		}
	}

	public String redo() {
		completeTasks(true);
		updateDateModified(_completedTasks);
		_data.deleteTasks(_completedTasks);
		_data.addArchive(_completedTasks);
		return completeFeedback();
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
