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
import javafx.scene.layout.VBox;
import urgenda.util.Task;
import urgenda.util.TaskList;

public class DisplayController extends AnchorPane {

	public enum TaskDisplayType {
		OVERDUE, TODAY, NORMAL, ARCHIVE, FREE_TIME
	}

	public enum Direction {
		DOWN, UP, LEFT, RIGHT
	}

	private static final String MESSAGE_ZERO_TASKS = "You have no tasks to display!";
	private static final String KEYWORD_SHOWMORE = "showmore";

	private static final double DEFAULT_EMPTY_TASKS_DISPLAY_HEIGHT = 100;

	/*
	 * COLORS 
	 * red: FF9999, 255, 153, 153, FF4C4C, 255, 76, 76 
	 * orange: FFD299, 255, 210, 153 FFAE4C, 225, 174, 76 
	 * blue: 96B2FF 150, 178, 255, 4C7CFF, 76, 124, 255 
	 * green: 86E086 134, 224, 134, 15C815, 21, 200, 21 
	 * gray: B2B2B2 178, 178, 178, 666666, 102, 102, 102
	 */

	// FXML attributes
	@FXML
	private Label displayHeader;
	@FXML
	private VBox displayHolder;
	@FXML
	private ScrollPane displayArea;

	private ArrayList<Task> _displayedTasks;
	private ArrayDeque<Integer> _detailedIndexes;
	private IntegerProperty _selectedTaskIndex;
	private boolean _setup;
	private boolean _allowChangeScroll;
	private Main _main;

	public DisplayController() {
		_setup = true;
		_selectedTaskIndex = new SimpleIntegerProperty(-1);
		_selectedTaskIndex.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> value, Number oldIndex, Number newIndex) {
				if(!_setup) {
					setDisplayScrollHeight();
				}
				if (((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).isMultipleSlot()) {
					boolean isMultipleSlotTask = ((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).isMultipleSlot();
					boolean isDetailed = (displayHolder.getChildren().get(_selectedTaskIndex.getValue())).getClass().equals(DetailedTaskController.class);
					_main.getController().showMultipleSlotMenuOption(!isDetailed && isMultipleSlotTask);
				} else {
					_main.getController().showMultipleSlotMenuOption(false);
				}
			}
		});
		_displayedTasks = new ArrayList<Task>();
		_detailedIndexes = new ArrayDeque<Integer>();
		_allowChangeScroll = false; // set default setting to change scroll as false
	}

	public void initDisplay(TaskList updatedTasks, String displayHeader, ArrayList<Integer> showmoreIndexes,
			int modifiedTaskIndex, boolean showNoviceHeaders) {
		setDisplay(updatedTasks, displayHeader, showmoreIndexes, modifiedTaskIndex, showNoviceHeaders, false);
		displayArea.vvalueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> value, Number oldValue, Number newValue) {
				// prevent changes to scroll height of displayArea other than
				// method calls and mouse or touch scrolls
				if (!_allowChangeScroll && oldValue != newValue) {
					changeDisplayVvalue(oldValue.doubleValue());
				}
			}
		});
		displayArea.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent scrollEvent) {
				if (!_allowChangeScroll) { // allow if is mouse/touch scroll event
					_allowChangeScroll = true;
				}
			}
		});
	}

	public void setDisplay(TaskList updatedTasks, String displayHeader, ArrayList<Integer> showmoreIndexes,
			int modifiedTaskIndex, boolean showNoviceHeaders, boolean isShowFreeTime) {
		_setup = true;
		_allowChangeScroll = false;
		displayHolder.getChildren().clear();
		_displayedTasks.clear();
		_displayedTasks.addAll(updatedTasks.getTasks());
		_displayedTasks.addAll(updatedTasks.getArchives());
		_detailedIndexes.clear();
		_detailedIndexes.addAll(showmoreIndexes);

		int indexCounter = 0;
		if (updatedTasks.getUncompletedCount() != 0) {
			if(isShowFreeTime) {
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getTasks().size(),
						TaskDisplayType.FREE_TIME, false);
			} else if (showNoviceHeaders) {
				if (updatedTasks.getOverdueCount() > 0) {
					indexCounter += showStyledTaskView(indexCounter, 1, TaskDisplayType.OVERDUE, true);	
					indexCounter += showStyledTaskView(indexCounter, updatedTasks.getOverdueCount() - 1,
							TaskDisplayType.OVERDUE, false);
				}
				if (updatedTasks.getTodayCount() > 0) {
					indexCounter += showStyledTaskView(indexCounter, 1, TaskDisplayType.TODAY, true);
					indexCounter += showStyledTaskView(indexCounter, updatedTasks.getTodayCount() - 1,
							TaskDisplayType.TODAY, false);
				}
				if (updatedTasks.getRemainingCount() > 0) {
					indexCounter += showStyledTaskView(indexCounter, 1, TaskDisplayType.NORMAL, true);
					indexCounter += showStyledTaskView(indexCounter, updatedTasks.getRemainingCount() - 1,
							TaskDisplayType.NORMAL, false);
				}
			} else {
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getOverdueCount(),
						TaskDisplayType.OVERDUE, false);
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getTodayCount(), TaskDisplayType.TODAY,
						false);
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getRemainingCount(),
						TaskDisplayType.NORMAL, false);
			}
		}
		if (updatedTasks.getArchiveCount() != 0) {
			if (showNoviceHeaders) {
				indexCounter += showStyledTaskView(indexCounter, 1, TaskDisplayType.ARCHIVE, true);
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getArchiveCount() - 1,
						TaskDisplayType.ARCHIVE, false);
			} else {
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getArchiveCount(),
						TaskDisplayType.ARCHIVE, false);
			}
		}
		if (updatedTasks.getArchiveCount() + updatedTasks.getUncompletedCount() == 0) {
			showZeroTasksFeedback();
		} else {
			initSelectedTask(modifiedTaskIndex);
		}
		if (displayHeader != null) { // display header needs to be changed
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
				SimpleTaskController newTaskView = new SimpleTaskController(_displayedTasks.get(currIndex), currIndex,
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
			((SimpleTaskController) displayHolder.getChildren().get(index)).setSelected(true);
		} else {
			_selectedTaskIndex.set(-1);
		}
	}
	
	protected void setDisplayScrollHeight() {
		double selectedIndexTop = 0.0;
		double selectedIndexBottom = 0.0;
		double heightSum = 0.0;
		for (int i = 0; i < displayHolder.getChildren().size(); i++) {
			heightSum += ((SimpleTaskController) displayHolder.getChildren().get(i)).getHeight();
			if (i == _selectedTaskIndex.intValue() - 1) {
				selectedIndexTop = heightSum;
			}
			if (i == _selectedTaskIndex.intValue()) {
				selectedIndexBottom = heightSum;
			}
		}
		displayArea.setVmax(heightSum - displayArea.getHeight());
		double oldScrollHeightTop = displayArea.getVvalue();
		if (!isFullyWithinRange(oldScrollHeightTop, oldScrollHeightTop + displayArea.getViewportBounds().getHeight(), selectedIndexTop,
				selectedIndexBottom)) { // new selected task is not fully visible
			if (selectedIndexTop > oldScrollHeightTop) { //task below screen
				changeDisplayVvalue(selectedIndexBottom - displayArea.getViewportBounds().getHeight());
			} else if (selectedIndexBottom < (oldScrollHeightTop + displayArea.getViewportBounds().getHeight())) { //task above screen
				changeDisplayVvalue(selectedIndexTop);
			}
		}
	}

	private boolean isFullyWithinRange(double rangeTop, double rangeBottom, double top, double bottom) {
		if (top < rangeTop) {
			return false;
		}
		if (bottom > rangeBottom) {
			return false;
		}
		return true;
	}

	public void executeTraverse(Direction direction) {
		switch(direction) {
		case DOWN:
			if (_selectedTaskIndex.getValue() < _displayedTasks.size() - 1) {
				((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).setSelected(false);
				_selectedTaskIndex.set(_selectedTaskIndex.getValue() + 1);
				((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).setSelected(true);
			}
			break;
		case UP:
			if (_selectedTaskIndex.getValue() != 0) {
				((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).setSelected(false);
				_selectedTaskIndex.set(_selectedTaskIndex.getValue() - 1);
				((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).setSelected(true);
			}
			break;
		case LEFT: //fall-through
		case RIGHT:
			((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).traverseMultipleSlot(direction);
			break;
		}
	}

	protected void setSelectedIndexOnClick(int index) {
		if (index != _selectedTaskIndex.getValue()) {
			((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue())).setSelected(false);
			_selectedTaskIndex.set(index);
		}
	}

	protected void toggleSelectedDetailsOnClick() {
		_main.handleCommandLine(KEYWORD_SHOWMORE);
	}

	public void setDisplayHeader(String displayed) {
		displayHeader.setText(displayed);
	}

	void changeDisplayVvalue(double value) {
		_allowChangeScroll = true;
		displayArea.setVvalue(value);
		_allowChangeScroll = false;
	}

	public int getSelectedTaskIndex() {
		return _selectedTaskIndex.getValue();
	}

	public int getDisplayedTasksCount() {
		return _displayedTasks.size();
	}

	public void setSetup(boolean setup) {
		_setup = setup;
	}

	public void setMain(Main main) {
		_main = main;
	}
}