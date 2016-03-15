package urgenda.gui;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
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
		TASK, DETAILED_TASK, TASK_HEADER
	}

	private static final String MESSAGE_ZERO_TASKS = "You have no tasks to display!";
	private static final String KEYWORD_SHOWMORE = "showmore";
	static final String TEXT_FILL_OVERDUE = "-fx-text-fill: white;";
	static final String TEXT_FILL_TODAY = "-fx-text-fill: black;";
	static final String TEXT_FILL_NORMAL = "-fx-text-fill: black;";
	static final String TEXT_FILL_COMPLETED = "-fx-text-fill: black;";
	static final String TEXT_WEIGHT_BOLD = "-fx-font-family: \"Montserrat\";";
	static final String TEXT_WEIGHT_REGULAR = "-fx-font-family: \"Montserrat Light\";";	
	
	private static final double DEFAULT_VERTICAL_SCROLL_HEIGHT = 0;
	private static final double DEFAULT_EMPTY_TASKS_DISPLAY_HEIGHT = 100;
	private static final double NORMAL_OPACITY_VALUE = 0.7;
	private static final double IMPORTANT_OPACITY_VALUE = 1;

	/*
	 * COLORS 
	 * red: FF9999, 255, 153, 153, FF4C4C, 255, 76, 76
	 * orange: FFD299 255, 210, 153 FFAE4C, 225, 174, 76
	 * blue: 96B2FF 150, 178, 255, 4C7CFF , 76,124,255
	 * gray: B2B2B2 178, 178, 178, 666666, 102, 102, 102
	 */

	static final Color COLOR_OVERDUE = Color.rgb(255, 153, 153, IMPORTANT_OPACITY_VALUE);
	static final Color COLOR_TODAY_IMPORTANT = Color.rgb(255, 210, 153, IMPORTANT_OPACITY_VALUE);
	static final Color COLOR_TODAY = Color.rgb(255, 210, 153, NORMAL_OPACITY_VALUE);
	static final Color COLOR_NORMAL_IMPORTANT = Color.rgb(150, 178, 255, IMPORTANT_OPACITY_VALUE);
	static final Color COLOR_NORMAL = Color.rgb(150, 178, 255, NORMAL_OPACITY_VALUE);
	static final Color COLOR_COMPLETED = Color.rgb(178, 178, 178, IMPORTANT_OPACITY_VALUE);
	static final Color COLOR_INDICATOR_OVERDUE = Color.rgb(255, 76, 76, IMPORTANT_OPACITY_VALUE);
	static final Color COLOR_INDICATOR_TODAY = Color.rgb(255, 174, 76, NORMAL_OPACITY_VALUE);
	static final Color COLOR_INDICATOR_NORMAL = Color.rgb(76, 124, 255, NORMAL_OPACITY_VALUE);
	static final Color COLOR_INDICATOR_COMPLETED = Color.rgb(102, 102, 102, IMPORTANT_OPACITY_VALUE);

	@FXML
	private Label displayHeader;
	@FXML
	private VBox displayHolder;
	@FXML
	private ScrollPane displayArea;
//	@FXML
//	private ScrollBar visibleScrollbar;

	private ArrayList<Task> _displayedTasks;
	private ArrayDeque<Integer> _detailedIndexes;
	private int _selectedTaskIndex;
	private Main _main;

	public DisplayController() {
		_selectedTaskIndex = -1;
		_displayedTasks = new ArrayList<Task>();
		_detailedIndexes = new ArrayDeque<Integer>();
	}
	
	public void setScrollBar () {
		//TODO scrollbar
	}
	
	
	public void setDisplay(TaskList updatedTasks, String displayHeader, ArrayList<Integer> showmoreIndexes, int modifiedTaskIndex, boolean showNoviceHeaders) {
		displayHolder.getChildren().clear();
		_displayedTasks.clear();
		_displayedTasks.addAll(updatedTasks.getTasks());
		_displayedTasks.addAll(updatedTasks.getArchives());
		_detailedIndexes.clear();
		_detailedIndexes.addAll(showmoreIndexes);

		int indexCounter = 0;
		if (updatedTasks.getUncompletedCount() != 0) {
			if(showNoviceHeaders) {
				if(updatedTasks.getOverdueCount() > 0) {
					indexCounter += showStyledTaskView(indexCounter, 1, TaskDisplayType.OVERDUE, true);
					indexCounter += showStyledTaskView(indexCounter, updatedTasks.getOverdueCount() - 1, TaskDisplayType.OVERDUE, false);
				} 
				if(updatedTasks.getTodayCount() > 0) {
					indexCounter += showStyledTaskView(indexCounter, 1, TaskDisplayType.TODAY, true);
					indexCounter += showStyledTaskView(indexCounter, updatedTasks.getTodayCount() - 1, TaskDisplayType.TODAY, false);
				}
				if(updatedTasks.getRemainingCount() > 0) {
					indexCounter += showStyledTaskView(indexCounter, 1, TaskDisplayType.NORMAL, true);
					indexCounter += showStyledTaskView(indexCounter, updatedTasks.getRemainingCount() - 1, TaskDisplayType.NORMAL, false);
				}
			} else {
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getOverdueCount(), TaskDisplayType.OVERDUE, false);
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getTodayCount(), TaskDisplayType.TODAY, false);
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getRemainingCount(), TaskDisplayType.NORMAL, false);
			}
		}
		if (updatedTasks.getArchiveCount() != 0) {
			if(showNoviceHeaders) {
				indexCounter += showStyledTaskView(indexCounter, 1, TaskDisplayType.ARCHIVE, true);
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getArchiveCount() - 1, TaskDisplayType.ARCHIVE, false);
			} else {
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getArchiveCount(), TaskDisplayType.ARCHIVE, false);
			}
		}
		if (updatedTasks.getArchiveCount() + updatedTasks.getUncompletedCount() == 0) {
			showZeroTasksFeedback();
		}
		initSelectedTask(modifiedTaskIndex);
		setDisplayScroll();
		if (displayHeader != null) { //display header needs to be changed
			setDisplayHeader(displayHeader);
		}
	}
	
	private int showStyledTaskView(int currIndex, int toAddCount, TaskDisplayType taskDisplayType, boolean showHeader) {
		int addedCount = 0;
		while (addedCount < toAddCount) {
			if (isDetailed(currIndex)) {
				DetailedTaskController newDetailedTaskView = new DetailedTaskController(_displayedTasks.get(currIndex),
						currIndex, taskDisplayType, showHeader);
				newDetailedTaskView.setDisplayController(this);
				displayHolder.getChildren().add(newDetailedTaskView);
				newDetailedTaskView.resizeOverrunDescLabel();
			} else {
				TaskController newTaskView = new TaskController(_displayedTasks.get(currIndex), currIndex,
						taskDisplayType, showHeader);
				newTaskView.setDisplayController(this);
				displayHolder.getChildren().add(newTaskView);
			}
			addedCount++;
			currIndex++;
		}
		return addedCount;
	}

	private boolean isDetailed(int currIndex) {
		if (!_detailedIndexes.isEmpty() && currIndex == _detailedIndexes.peekFirst()) {
			_detailedIndexes.removeFirst();
			return true;
		}
		return false;
	}
	
	private void showZeroTasksFeedback() {
		Label emptyDisplay = new Label(MESSAGE_ZERO_TASKS);
		emptyDisplay.setPrefHeight(DEFAULT_EMPTY_TASKS_DISPLAY_HEIGHT);
		emptyDisplay.setFont(Main.BOLD_FONT);
		displayHolder.getChildren().add(emptyDisplay);
	}

	private void initSelectedTask(int index) {
		if (!_displayedTasks.isEmpty()) {
			_selectedTaskIndex = index;
			((TaskController) displayHolder.getChildren().get(index)).setSelected(true);
		} else {
			_selectedTaskIndex = -1;
		}
	}
	
	// TODO set display scroll position according to latest changed/added task
	private void setDisplayScroll() {
		displayArea.setVvalue(DEFAULT_VERTICAL_SCROLL_HEIGHT);
	}
	
	protected void toggleDetailedOnClick(Task task, int index, TaskDisplayType taskDisplayType) {
		_main.handleCommandLine(KEYWORD_SHOWMORE);
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

	// TODO: implement scrollbar
	// public void setScrollbar() {
	// visibleScrollbar.setMax(displayArea.getViewportBounds().getHeight());
	// System.out.println(displayArea.getViewportBounds().getHeight());
	// visibleScrollbar.setMin(0);
	// vPosition = new SimpleDoubleProperty();
	// vPosition.bind(visibleScrollbar.valueProperty());
	// vPosition.addListener(new ChangeListener<Number>(){
	// @Override
	// public void changed(ObservableValue<? extends Number> observable, Number
	// oldValue, Number newValue) {
	// displayArea.setVvalue((double) newValue);
	// }
	// });
	// }
	
	protected void setSelectedIndexOnClick(int index) {
		if (index != _selectedTaskIndex) {
			((TaskController) displayHolder.getChildren().get(_selectedTaskIndex)).setSelected(false);
			_selectedTaskIndex = index;
		}
	}

	public void setDisplayHeader(String displayed) {
		displayHeader.setText(displayed);
	}

	public int getSelectedTaskIndex() {
		return _selectedTaskIndex;
	}

	public void setMain(Main main) {
		_main = main;
		
	}
}
