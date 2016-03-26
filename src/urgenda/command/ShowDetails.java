package urgenda.command;

import java.util.ArrayList;
import java.util.Collections;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class ShowDetails extends Command {
	
	private static final String MESSAGE_NO_SHOW_MATCH = "Invalid position(s) to showmore details";
	private static final String MESSAGE_SHOWING_MORE = "Showing more details for \"%1$s\"";
	private static final String MESSAGE_SHOWING_LESS = "Showing less details for \"%1$s\"";
	private static final String MESSAGE_NUM = "Changed showing details for %1$s tasks";

	private ArrayList<Integer> _positions;
	
	private ArrayList<Task> _tasks;
	private LogicData _data;

	public String execute() throws Exception {
		_data = LogicData.getInstance();
		if (_positions != null && _positions.size() != 0) {
			Collections.sort(_positions);
			_tasks = _data.findMatchingPosition(_positions);
		}
		// Does not change the previous state
		if (_tasks == null || _tasks.isEmpty()) {
			throw new Exception(MESSAGE_NO_SHOW_MATCH);
		} else {
			toggleTasks();
			_data.setTaskPointer(_tasks.get(0));
			return showMoreFeedback();			
		}
	}

	private void toggleTasks() {
		for (Task task : _tasks) {
			_data.toggleShowMoreTasks(task);
		}
	}

	private String showMoreFeedback() {
		if (_tasks.size() == 1) {
			return checkShowMore(_tasks.get(0));
		} else {
			String feedback = String.format(MESSAGE_NUM, _tasks.size());
			for (Task task : _tasks) {
				feedback += "\n" + checkShowMore(task);
			}
			return feedback;			
		}
	}
	
	private String checkShowMore(Task task) {
		if (_data.isShowingMore(task)) {
			return String.format(MESSAGE_SHOWING_MORE, task.getDesc());
		} else {
			return String.format(MESSAGE_SHOWING_LESS, task.getDesc());
		}
	}

//	public String undo() {
//		_data.toggleShowMoreTasks(_task);
//		return showMoreFeedback();
//	}
//
//	public String redo() {
//		_data.toggleShowMoreTasks(_task);
//		return showMoreFeedback();
//	}

	public void setPosition(ArrayList<Integer> positions) {
		_positions = positions;
	}

}
