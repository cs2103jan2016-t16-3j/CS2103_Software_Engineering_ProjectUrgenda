package urgenda.gui;

import java.util.ArrayDeque;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import urgenda.util.Task;
import urgenda.util.TaskList;

public class DisplayController extends AnchorPane {

	public enum TaskDisplayType {
		OVERDUE, TODAY, NORMAL, ARCHIVE
	}

	public enum Direction {
		DOWN, UP
	}
	
	public enum TaskType {
		TASK, DETAILED_TASK
	}

	private static final String MESSAGE_ZERO_TASKS = "You have no tasks to display!";
	static final String TEXT_FILL_OVERDUE = "-fx-text-fill: white;";
	static final String TEXT_FILL_TODAY = "-fx-text-fill: black;";
	static final String TEXT_FILL_NORMAL = "-fx-text-fill: black;";
	static final String TEXT_FILL_COMPLETED = "-fx-text-fill: black;";
	static final String TEXT_WEIGHT_BOLD = "-fx-font-family: \"Montserrat\";";
	static final String TEXT_WEIGHT_REGULAR = "-fx-font-family: \"Montserrat Light\";";
	static final String TEXT_MODIFY_NONE = "";

	private static final double DEFAULT_VERTICAL_SCROLL_HEIGHT = 0;
	private static final double DEFAULT_EMPTY_TASKS_DISPLAY_HEIGHT = 100;
	private static final double NORMAL_OPACITY_VALUE = 0.7;
	private static final double IMPORTANT_OPACITY_VALUE = 1;

	/*
	 * COLORS 
	 * red: FF9999, 255, 153, 153 
	 * orange: FFD299 255, 210, 153 
	 * blue: 96B2FF 150, 178, 255 
	 * gray: B2B2B2 178, 178, 178
	 */

	static final Color COLOR_OVERDUE = Color.rgb(255, 153, 153, IMPORTANT_OPACITY_VALUE);
	static final Color COLOR_TODAY_IMPORTANT = Color.rgb(255, 210, 153, IMPORTANT_OPACITY_VALUE);
	static final Color COLOR_TODAY = Color.rgb(255, 210, 153, NORMAL_OPACITY_VALUE);
	static final Color COLOR_NORMAL_IMPORTANT = Color.rgb(150, 178, 255, IMPORTANT_OPACITY_VALUE);
	static final Color COLOR_NORMAL = Color.rgb(150, 178, 255, NORMAL_OPACITY_VALUE);
	static final Color COLOR_COMPLETED = Color.rgb(178, 178, 178, IMPORTANT_OPACITY_VALUE);

	@FXML
	private Label displayHeader;
	@FXML
	private VBox displayHolder;
	@FXML
	private ScrollPane displayArea;

	private ArrayList<Task> _displayedTasks;
	private ArrayDeque<Integer> _detailedIndexes;
	private int _selectedTaskIndex;

	public DisplayController() {
		_selectedTaskIndex = -1;
		_displayedTasks = new ArrayList<Task>();
		_detailedIndexes = new ArrayDeque<Integer>();
	}

	public void setDisplay(TaskList updatedTasks, String displayHeader, ArrayList<Integer> showmoreIndexes) {
		displayHolder.getChildren().clear();
		_displayedTasks.clear();
		_displayedTasks.addAll(updatedTasks.getTasks());
		_displayedTasks.addAll(updatedTasks.getArchives());
		_detailedIndexes.clear();
		_detailedIndexes.addAll(showmoreIndexes);
		
		int indexCounter = 0;
		if (updatedTasks.getUncompletedCount() != 0) {
			indexCounter += showStyledTaskView(indexCounter, updatedTasks.getOverdueCount(),
					TaskDisplayType.OVERDUE);
			indexCounter += showStyledTaskView(indexCounter, updatedTasks.getTodayCount(),
					TaskDisplayType.TODAY);
			indexCounter += showStyledTaskView(indexCounter, updatedTasks.getRemainingCount(),
					TaskDisplayType.NORMAL);
		}
		if (updatedTasks.getArchiveCount() != 0) {
			indexCounter += showStyledTaskView(indexCounter, updatedTasks.getArchiveCount(),
					TaskDisplayType.ARCHIVE);
		}
		if (updatedTasks.getArchiveCount() + updatedTasks.getUncompletedCount() == 0) {
			showZeroTasksFeedback();
		}
		setDefaultSelectedTask();
		setDisplayScroll();
		if(displayHeader != null) {
			setDisplayHeader(displayHeader);
		}
	}

	private void showZeroTasksFeedback() {
		Label emptyDisplay = new Label(MESSAGE_ZERO_TASKS);
		emptyDisplay.setPrefHeight(DEFAULT_EMPTY_TASKS_DISPLAY_HEIGHT);
		emptyDisplay.setFont(Main.BOLD_FONT);
		displayHolder.getChildren().add(emptyDisplay);
	}

	private int showStyledTaskView(int currIndex, int toAddCount, TaskDisplayType taskDisplayType) {
		int addedCount = 0;
		while (addedCount < toAddCount) {
			if(isDetailed(currIndex)) {
				DetailedTaskController newDetailedTaskView = new DetailedTaskController(_displayedTasks.get(currIndex), currIndex, taskDisplayType);
				newDetailedTaskView.setDisplayController(this);
				displayHolder.getChildren().add(newDetailedTaskView);
				newDetailedTaskView.resizeOverrunDescLabel();
			} else {
				TaskController newTaskView = new TaskController(_displayedTasks.get(currIndex), currIndex, taskDisplayType);
				newTaskView.setDisplayController(this);
				displayHolder.getChildren().add(newTaskView);
			}
			addedCount++;
			currIndex++;
		}
		return addedCount;
	}
	
private boolean isDetailed(int currIndex) {
		if(!_detailedIndexes.isEmpty() && currIndex == _detailedIndexes.peekFirst()) {
			_detailedIndexes.removeFirst();
			return true;
		}
		return false;
}

//	TODO: implement show more or less by click
//	private void showMoreByIndex(int index) {
//		int taskCount = 0;
//		int objectCount = 0;
//		while(taskCount <= index) {
//			if(displayHolder.getChildren().get(objectCount).getClass().equals(TaskController.class)) {				
//				taskCount++;
//			}
//			objectCount++;
//		}
//		displayHolder.getChildren().add(objectCount, new TaskDetailsController(_displayedTasks.get(taskCount - 1)));
//		if(_selectedTaskIndex > taskCount) {
//			_selectedTaskIndex++;
//			_indicatorShowmoreCount++;
//		}
//	}
//	
//	private void showLessByIndex(int index) {
//		
//	}
	
	//TODO implement for counting tasks and detailed tasks
	//TODO set display scroll position according to latest changed/added task
	private void setDisplayScroll() {
		displayArea.setVvalue(DEFAULT_VERTICAL_SCROLL_HEIGHT);
	}

	private void setDefaultSelectedTask() {
		if (!_displayedTasks.isEmpty()) {
			((TaskController) displayHolder.getChildren().get(0)).setSelected(true);
			_selectedTaskIndex = 0;
		} else {
			_selectedTaskIndex = -1;
		}
	}

	public void traverseTasks(Direction direction) {
		if (direction == Direction.DOWN && _selectedTaskIndex < _displayedTasks.size() - 1) {
			((TaskController) displayHolder.getChildren().get(_selectedTaskIndex)).setSelected(false);
			((TaskController) displayHolder.getChildren().get(++_selectedTaskIndex)).setSelected(true);
		} else if (direction == Direction.UP && _selectedTaskIndex != 0) {
			((TaskController) displayHolder.getChildren().get(_selectedTaskIndex)).setSelected(false);
			((TaskController) displayHolder.getChildren().get(--_selectedTaskIndex)).setSelected(true);
		}
	}

	private void setDisplayHeader(String displayed) {
		displayHeader.setText(displayed);
	}

	//TODO does not work for detailed tasks displayed, to fix
	public void setSelectedIndexOnClick(int index) {
		if (index != _selectedTaskIndex) {
			((TaskController) displayHolder.getChildren().get(_selectedTaskIndex)).setSelected(false);
			_selectedTaskIndex = index;
		}
	}

	public int getSelectedTaskIndex() {
		return _selectedTaskIndex;
	}
}
