package urgenda.gui;

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

	public enum Style {
		OVERDUE, TODAY, NORMAL, ARCHIVE
	}

	public enum Direction {
		DOWN, UP
	}

	private static final String MESSAGE_ZERO_TASKS = "You have no tasks to display!";
	static final String TEXT_FILL_OVERDUE = "-fx-text-fill: white;";
	static final String TEXT_FILL_TODAY = "-fx-text-fill: black;";
	static final String TEXT_FILL_NORMAL = "-fx-text-fill: black;";
	static final String TEXT_FILL_COMPLETED = "-fx-text-fill: white;";
	static final String TEXT_WEIGHT_BOLD = "-fx-font-family: \"Montserrat\";";
	static final String TEXT_WEIGHT_REGULAR = "";
	static final String TEXT_MODIFY_NONE = "";

	private static final double DEFAULT_VERTICAL_SCROLL_HEIGHT = 0;
	private static final double DEFAULT_EMPTY_TASKS_DISPLAY_HEIGHT = 100;
	private static final double NORMAL_OPACITY_VALUE = 0.7;
	private static final double IMPORTANT_OPACITY_VALUE = 1;

	/*
	 * COLORS 
	 * red: FE9A9A, 254, 154, 154 
	 * orange: FFD99B 255, 217, 155 
	 * blue: CADEFF 202, 222, 255 
	 * gray: B2B2B2 178, 178, 178
	 */

	static final Color COLOR_OVERDUE = Color.rgb(255, 150, 150, NORMAL_OPACITY_VALUE);
	static final Color COLOR_TODAY_IMPORTANT = Color.rgb(255, 210, 150, IMPORTANT_OPACITY_VALUE);
	static final Color COLOR_TODAY = Color.rgb(255, 210, 150, NORMAL_OPACITY_VALUE);
	static final Color COLOR_NORMAL_IMPORTANT = Color.rgb(180, 200, 255, IMPORTANT_OPACITY_VALUE);
	static final Color COLOR_NORMAL = Color.rgb(180, 200, 255, NORMAL_OPACITY_VALUE);
	static final Color COLOR_COMPLETED = Color.rgb(178, 178, 178, NORMAL_OPACITY_VALUE);

	@FXML
	private Label displayHeader;
	@FXML
	private VBox displayHolder;
	@FXML
	private ScrollPane displayArea;

	private ArrayList<Task> _displayedTasks;
	private int _selectedTaskIndex;
	private int _indicatorShowmoreCount;

	public DisplayController() {
		_selectedTaskIndex = -1;
		_indicatorShowmoreCount = 0;
		_displayedTasks = new ArrayList<Task>();
	}

	public void setDisplay(TaskList updatedTasks, String displayHeader, ArrayList<Integer> showmoreIndexes) {
		displayHolder.getChildren().clear();
		_displayedTasks = new ArrayList<Task>();
		_displayedTasks.addAll(updatedTasks.getTasks());
		_displayedTasks.addAll(updatedTasks.getArchives());
		
		int indexCounter = 0;
		if (updatedTasks.getUncompletedCount() != 0) {
			indexCounter += showStyledTaskView(indexCounter, showmoreIndexes, updatedTasks.getOverdueCount(),
					Style.OVERDUE);
			indexCounter += showStyledTaskView(indexCounter, showmoreIndexes, updatedTasks.getTodayCount(),
					Style.TODAY);
			indexCounter += showStyledTaskView(indexCounter, showmoreIndexes, updatedTasks.getRemainingCount(),
					Style.NORMAL);
		}
		if (updatedTasks.getArchiveCount() != 0) {
			indexCounter += showStyledTaskView(indexCounter, showmoreIndexes, updatedTasks.getArchiveCount(),
					Style.ARCHIVE);
		}
		if (updatedTasks.getArchiveCount() + updatedTasks.getUncompletedCount() == 0) {
			showZeroTasksFeedback();
		}
		setDefaultSelectedTask();
		setDisplayScrollTop();
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

	private int showStyledTaskView(int currIndex, ArrayList<Integer> showmoreIndexes, int toAddCount, Style style) {
		int addedCount = 0;
		while (addedCount < toAddCount) {
			TaskController newTaskView = new TaskController(_displayedTasks.get(currIndex), currIndex);
			newTaskView.setDisplayController(this);
			newTaskView.setTaskStyle(style);
			displayHolder.getChildren().add(newTaskView);
			if (showmoreIndexes.contains(Integer.valueOf(currIndex))) {
				TaskDetailsController newTaskDetail = new TaskDetailsController(_displayedTasks.get(currIndex));
				newTaskDetail.setTaskStyle(style);
				displayHolder.getChildren().add(newTaskDetail);
			}
			addedCount++;
			currIndex++;
		}
		return addedCount;
	}

	private void setDisplayScrollTop() {
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
		if (direction == Direction.DOWN && _selectedTaskIndex < _displayedTasks.size() + _indicatorShowmoreCount - 1) { //to add detailed task count
			((TaskController) displayHolder.getChildren().get(_selectedTaskIndex)).setSelected(false);
			if (displayHolder.getChildren().get(++_selectedTaskIndex).getClass().equals(TaskDetailsController.class)) {
				_indicatorShowmoreCount++;
				((TaskController) displayHolder.getChildren().get(++_selectedTaskIndex)).setSelected(true);
			} else {
				((TaskController) displayHolder.getChildren().get(_selectedTaskIndex)).setSelected(true);
			}
		} else if (direction == Direction.UP && _selectedTaskIndex != 0) {
			((TaskController) displayHolder.getChildren().get(_selectedTaskIndex)).setSelected(false);
			if (displayHolder.getChildren().get(--_selectedTaskIndex).getClass().equals(TaskDetailsController.class)) {
				_indicatorShowmoreCount--;
				((TaskController) displayHolder.getChildren().get(--_selectedTaskIndex)).setSelected(true);
			} else {
				((TaskController) displayHolder.getChildren().get(_selectedTaskIndex)).setSelected(true);
			}
		}
	}

	private void setDisplayHeader(String displayed) {
		displayHeader.setText(displayed);
	}

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
