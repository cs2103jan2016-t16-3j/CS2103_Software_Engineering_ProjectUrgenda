package urgenda.gui;

import java.util.ArrayDeque;
import java.util.ArrayList;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
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

	private ArrayList<Task> _displayedTasks;
	private ArrayDeque<Integer> _detailedIndexes;
	private IntegerProperty _selectedTaskIndex;
	private Main _main;

	public DisplayController() {
		_selectedTaskIndex = new SimpleIntegerProperty(-1);
		_selectedTaskIndex.addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> value, Number oldIndex, Number newIndex) {
				setDisplayScroll(oldIndex, newIndex);
			}});
		_displayedTasks = new ArrayList<Task>();
		_detailedIndexes = new ArrayDeque<Integer>();
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
			_selectedTaskIndex.set(index);
			((TaskController) displayHolder.getChildren().get(index)).setSelected(true);
		} else {
			_selectedTaskIndex.set(-1);	//TODO magic number
		}
	}
	
	// TODO set display scroll position according to latest changed/added task
	private void setDisplayScroll(Number oldIndex, Number newIndex) {
		if(newIndex.intValue() >= 0) {
			double oldSelectedTaskHeightTop = 0;	
			double oldSelectedTaskHeightBottom = 0;
			double newSelectedTaskHeightTop = 0;
			double newSelectedTaskHeightBottom = 0;
			double taskHeightSum = 0.0;
			for(int i = 0; i < displayHolder.getChildren().size(); i++) {					
				taskHeightSum += ((TaskController) displayHolder.getChildren().get(i)).getMaxHeight();
				if(oldIndex.intValue() >= 0 && i == oldIndex.intValue()) {
					oldSelectedTaskHeightBottom = taskHeightSum;
				}
				if(oldIndex.intValue() >= 0 && i == oldIndex.intValue() - 1) {
					oldSelectedTaskHeightTop = taskHeightSum;
				}
				if(i == newIndex.intValue() - 1) {
					newSelectedTaskHeightTop = taskHeightSum;
				}
				if(i == newIndex.intValue()) {
					newSelectedTaskHeightBottom = taskHeightSum;
				}
			}
			displayArea.setVmax(taskHeightSum - displayArea.getHeight());
			System.out.println("ths " + taskHeightSum);
			double oldScrollHeightTop = displayArea.getVvalue();
			System.out.println(oldScrollHeightTop);
			if (!isFullyWithinRange(oldScrollHeightTop, oldScrollHeightTop + displayArea.getHeight(), newSelectedTaskHeightTop, newSelectedTaskHeightBottom)) { //new selected task cannot be fully visible
				System.out.println("out of range");
				if (oldSelectedTaskHeightBottom == newSelectedTaskHeightTop) { //indicator move downwards
					System.out.println(newSelectedTaskHeightBottom - displayArea.getHeight());
					displayArea.setVvalue(newSelectedTaskHeightBottom - displayArea.getHeight());
				} else if (oldSelectedTaskHeightTop == newSelectedTaskHeightBottom) { //indicator move upwards
					System.out.println(newSelectedTaskHeightTop);
					displayArea.setVvalue(newSelectedTaskHeightTop);
				} else {
					//TODO implement scroll for returned task index					
				}
			}			
		}
		
	}
	
	private boolean isFullyWithinRange(double rangeTop, double rangeBottom, double top, double bottom) {
		if(top < rangeTop) {
			return false;
		}
		if(bottom > rangeBottom) {
			return false;
		}
		return true;
	}

	protected void toggleDetailedOnClick(Task task, int index, TaskDisplayType taskDisplayType) {
		_main.handleCommandLine(KEYWORD_SHOWMORE);
	}
	
	public void traverseTasks(Direction direction) {
		if (direction == Direction.DOWN && _selectedTaskIndex.getValue() < _displayedTasks.size() - 1) {
			((TaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).setSelected(false);
			_selectedTaskIndex.set(_selectedTaskIndex.getValue() + 1);
			((TaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).setSelected(true);
		} else if (direction == Direction.UP && _selectedTaskIndex.getValue() != 0) {
			((TaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).setSelected(false);
			_selectedTaskIndex.set(_selectedTaskIndex.getValue() - 1);
			((TaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).setSelected(true);
		}
	}
	
	protected void setSelectedIndexOnClick(int index) {
		if (index != _selectedTaskIndex.getValue()) {
			((TaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).setSelected(false);
			_selectedTaskIndex.set(index);
		}
	}

	public void setDisplayHeader(String displayed) {
		displayHeader.setText(displayed);
	}

	public int getSelectedTaskIndex() {
		return _selectedTaskIndex.getValue();
	}

	public void setMain(Main main) {
		_main = main;
		
	}

	public void setStartupDisplay(TaskList allTasks, String createDisplayHeader, ArrayList<Integer> detailedIndexes,
			int displayPosition, boolean showNoviceHeaders) {
		setDisplay(allTasks, createDisplayHeader, detailedIndexes, displayPosition, showNoviceHeaders);
		displayArea.setOnScroll(new EventHandler<ScrollEvent>() {
		      @Override
		      public void handle(ScrollEvent scrollEvent) {
		    	  if(scrollEvent.getDeltaY() > 0) {
		    		  //TODO
		    	  } else if(scrollEvent.getDeltaY() < 0) {
		    		  //TODO
		    	  }
		      }
		    });
	}
}
