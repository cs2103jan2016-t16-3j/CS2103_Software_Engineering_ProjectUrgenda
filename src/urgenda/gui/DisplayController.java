//@@author A0131857B
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
import javafx.scene.text.Text;
import urgenda.util.Task;
import urgenda.util.TaskList;

/**
 * UI component, sets the display view for the relevant tasks to be shown
 * according to their respective attributes.
 * 
 * @author KangSoon
 */
public class DisplayController extends AnchorPane {


	// Enumerations
	public enum TaskDisplayType {
		OVERDUE, TODAY, NORMAL, ARCHIVE, FREE_TIME
	}

	public enum Direction {
		DOWN, UP, LEFT, RIGHT
	}

	// Constants
	private static final String MESSAGE_ZERO_TASKS = "\nYou have no tasks to display!";
	private static final String KEYWORD_SHOWMORE = "showmore";
	private static final int INVALID_INDEX = -1;

	// Elements loaded using FXML
	@FXML
	private Label displayHeader;
	@FXML
	private VBox displayHolder;
	@FXML
	private ScrollPane displayArea;

	// Private attributes
	private ArrayList<Task> _displayedTasks;
	private ArrayDeque<Integer> _detailedIndexes;
	private IntegerProperty _selectedTaskIndex;
	private boolean _isNoviceView;
	private boolean _setup;
	private boolean _allowChangeScroll;
	private Main _main;

	/**
	 * Creates a DisplayController.
	 */
	public DisplayController() {
		_isNoviceView = true;
		_setup = true;
		_selectedTaskIndex = new SimpleIntegerProperty(INVALID_INDEX);
		_selectedTaskIndex.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> value, Number oldIndex, Number newIndex) {
				if (!_setup) {
					setDisplayScrollHeight();
				}
				if (displayHolder.getChildren().get(_selectedTaskIndex.getValue()).getClass()
						.equals(SimpleTaskController.class)) {
					if (((SimpleTaskController) displayHolder.getChildren()
							.get(_selectedTaskIndex.getValue())).isMultipleSlot()) {
						boolean isMultipleSlotTask = ((SimpleTaskController) displayHolder.getChildren()
								.get(_selectedTaskIndex.getValue())).isMultipleSlot();
						boolean isDetailed = (displayHolder.getChildren().get(_selectedTaskIndex.getValue()))
								.getClass().equals(DetailedTaskController.class);
						_main.getController().toggleMultipleSlotMenuOption(!isDetailed && isMultipleSlotTask);
					} else {
						_main.getController().toggleMultipleSlotMenuOption(false);
					}
				}
			}
		});
		_displayedTasks = new ArrayList<Task>();
		_detailedIndexes = new ArrayDeque<Integer>();
		_allowChangeScroll = false; // set default change scroll as false
	}

	/**
	 * Initializes the display view at startup with the default tasks view.
	 * 
	 * @param updatedTasks
	 *            TaskList object containing all tasks to be shown in current
	 *            screen
	 * @param displayHeader
	 *            text to be set for the header indicating currently displayed
	 *            tasks
	 * @param showmoreIndexes
	 *            array of indexes for all tasks to show more details for
	 * @param modifiedTaskIndex
	 *            index of task to be set as selected
	 */
	public void initDisplay(TaskList updatedTasks, String displayHeader, ArrayList<Integer> showmoreIndexes,
			int modifiedTaskIndex, boolean isShowNoviceHeaders) {
		setDisplay(updatedTasks, displayHeader, showmoreIndexes, modifiedTaskIndex,
				false, false);
		// set listener for scrolling of pane
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
				if (!_allowChangeScroll) { // allow if is mouse/touch scroll
											// event
					_allowChangeScroll = true;
				}
			}
		});
	}

	/**
	 * Sets up display view with the list of tasks given.
	 * 
	 * @param updatedTasks
	 *            Tasklist object containing all tasks to be shown in current
	 *            screen
	 * @param displayHeader
	 *            text to be set for the header indicating currently displayed
	 *            tasks
	 * @param showmoreIndexes
	 *            array of indexes for all tasks to show more details for
	 * @param modifiedTaskIndex
	 *            index of task to be set as selected
	 * @param isShowFreeTime
	 *            boolean indicating view is show free time or not
	 * @param isDemo
	 *            boolean indicating view is demo view or not
	 */
	public void setDisplay(TaskList updatedTasks, String displayHeader, ArrayList<Integer> showmoreIndexes,
			int modifiedTaskIndex, boolean isShowFreeTime, boolean isDemo) {
		_setup = true;
		_allowChangeScroll = false;
		displayHolder.getChildren().clear();
		_displayedTasks.clear();
		_displayedTasks.addAll(updatedTasks.getTasks());
		_displayedTasks.addAll(updatedTasks.getArchives());
		_detailedIndexes.clear();
		_detailedIndexes.addAll(showmoreIndexes);

		createTaskControllers(updatedTasks, isShowFreeTime);
		
		if (updatedTasks.getArchiveCount() + updatedTasks.getUncompletedCount() == 0) {
			showZeroTasksFeedback();
		} else {
			if (!isDemo) {
				initSelectedTask(modifiedTaskIndex);
			} else {
				initSelectedTask(_selectedTaskIndex.get());
			}
		}
		if (displayHeader != null) { // display header needs to be changed
			setDisplayHeader(displayHeader);
		}
	}

	private void createTaskControllers(TaskList updatedTasks, boolean isShowFreeTime) {
		boolean isNoviceViewOld = _isNoviceView;
		if (_main.getController().isDemo()) {
			_isNoviceView = true;
		}
		int indexCounter = 0;
		if (updatedTasks.getUncompletedCount() != 0) {
			if (isShowFreeTime) {
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getTasks().size(),
						TaskDisplayType.FREE_TIME, false);
			} else if (_isNoviceView) {
				if (updatedTasks.getOverdueCount() > 0) {
					indexCounter += showStyledTaskView(indexCounter, 1, TaskDisplayType.OVERDUE, _isNoviceView);
					indexCounter += showStyledTaskView(indexCounter, updatedTasks.getOverdueCount() - 1,
							TaskDisplayType.OVERDUE, false);
				}
				if (updatedTasks.getTodayCount() > 0) {
					indexCounter += showStyledTaskView(indexCounter, 1, TaskDisplayType.TODAY, _isNoviceView);
					indexCounter += showStyledTaskView(indexCounter, updatedTasks.getTodayCount() - 1,
							TaskDisplayType.TODAY, false);
				}
				if (updatedTasks.getRemainingCount() > 0) {
					indexCounter += showStyledTaskView(indexCounter, 1, TaskDisplayType.NORMAL, _isNoviceView);
					indexCounter += showStyledTaskView(indexCounter, updatedTasks.getRemainingCount() - 1,
							TaskDisplayType.NORMAL, false);
				}
			} else {
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getOverdueCount(),
						TaskDisplayType.OVERDUE, false);
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getTodayCount(),
						TaskDisplayType.TODAY, false);
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getRemainingCount(),
						TaskDisplayType.NORMAL, false);
			}
		}
		if (updatedTasks.getArchiveCount() != 0) {
			if (_isNoviceView) {
				indexCounter += showStyledTaskView(indexCounter, 1, TaskDisplayType.ARCHIVE, _isNoviceView);
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getArchiveCount() - 1,
						TaskDisplayType.ARCHIVE, false);
			} else {
				indexCounter += showStyledTaskView(indexCounter, updatedTasks.getArchiveCount(),
						TaskDisplayType.ARCHIVE, false);
			}	
		}
		//reset old settings for novice view if changed
		_isNoviceView = isNoviceViewOld;
	}

	// create indicated number of tasks of given type with reference to whether
	// to show details for tasks or not
	private int showStyledTaskView(int currIndex, int toAddCount, TaskDisplayType taskDisplayType,
			boolean showHeader) {
		int addedCount = 0;
		while (addedCount < toAddCount) {
			if (isDetailed(currIndex)) {
				DetailedTaskController newDetailedTaskView = new DetailedTaskController(
						_displayedTasks.get(currIndex), currIndex, taskDisplayType, showHeader);
				newDetailedTaskView.setDisplayController(this);
				displayHolder.getChildren().add(newDetailedTaskView);
			} else {
				SimpleTaskController newTaskView = new SimpleTaskController(_displayedTasks.get(currIndex),
						currIndex, taskDisplayType, showHeader);
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
		Text emptyDisplay = new Text(MESSAGE_ZERO_TASKS);
		emptyDisplay.setFont(Main.BOLD_FONT);
		displayHolder.getChildren().add(emptyDisplay);
	}

	private void initSelectedTask(int index) {
		if (!_displayedTasks.isEmpty()) {
			_selectedTaskIndex.set(index);
			((SimpleTaskController) displayHolder.getChildren().get(index)).setSelected(true);
		} else {
			_selectedTaskIndex.set(INVALID_INDEX);
		}
	}

	/**
	 * Sets the scroll height of the display according to the currently selected
	 * task.
	 */
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
		double oldScrollHeightBottom = displayArea.getVvalue() + displayArea.getViewportBounds().getHeight();
		// check if task is fully visible
		if (!isFullyWithinRange(oldScrollHeightTop, oldScrollHeightBottom, selectedIndexTop, 
				selectedIndexBottom)) {
			if (selectedIndexTop > oldScrollHeightTop) { // task below screen
				changeDisplayVvalue(selectedIndexBottom - displayArea.getViewportBounds().getHeight());
			} else if (selectedIndexBottom < oldScrollHeightBottom) { // task above screen
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

	/**
	 * Traverses the task selection or the multiple slots of the selected task,
	 * according to the direction indicated.
	 * 
	 * @param direction
	 *            to traverse for
	 */
	public void executeTraverse(Direction direction) {
		if (!_main.getController().isDemo()) {
			switch (direction) {
			case DOWN:
				if (_selectedTaskIndex.getValue() < _displayedTasks.size() - 1) {
					((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue()))
							.setSelected(false);
					_selectedTaskIndex.set(_selectedTaskIndex.getValue() + 1);
					((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue()))
							.setSelected(true);
				}
				break;
			case UP:
				if (_selectedTaskIndex.getValue() != 0) {
					((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue()))
							.setSelected(false);
					_selectedTaskIndex.set(_selectedTaskIndex.getValue() - 1);
					((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue()))
							.setSelected(true);
				}
				break;
			case LEFT: // fall-through
			case RIGHT:
				((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue()))
						.traverseMultipleSlot(direction);
				break;
			}
		}
	}

	/**
	 * Sets indicated task as selected.
	 * 
	 * @param index
	 *            index of task to set as selected
	 * @param isInitDemo
	 *            boolean to indicate call by initialising demo view or not
	 */
	protected void setSelectedTaskByCall(int index, boolean isInitDemo) {
		if (_selectedTaskIndex.get() >= 0 && index != _selectedTaskIndex.getValue()) {
			if (!isInitDemo) {
				((SimpleTaskController) displayHolder.getChildren().get(_selectedTaskIndex.getValue()))
						.setSelected(false);
			}
		}
		if (displayHolder.getChildren().get(index).getClass().equals(SimpleTaskController.class)) {
			((SimpleTaskController) displayHolder.getChildren().get(index)).setSelected(true);
		} else if (displayHolder.getChildren().get(index).getClass().equals(DetailedTaskController.class)) {
			((DetailedTaskController) displayHolder.getChildren().get(index)).setSelected(true);
		}
		_selectedTaskIndex.set(index);
	}

	/**
	 * 
	 */
	protected void toggleSelectedDetailsOnClick() {
		_main.handleCommandLine(KEYWORD_SHOWMORE);
	}

	/**
	 * Sets the display header text.
	 * 
	 * @param headerText
	 *            text to display as header
	 */
	public void setDisplayHeader(String headerText) {
		displayHeader.setText(headerText);
	}

	/**
	 * Sets the display area scroll height.
	 * 
	 * @param value
	 *            scroll height to be set at
	 */
	void changeDisplayVvalue(double value) {
		_allowChangeScroll = true;
		displayArea.setVvalue(value);
		_allowChangeScroll = false;
	}

	/**
	 * Gets the index of the currently selected task.
	 * 
	 * @return index of currently selected task
	 */
	public int getSelectedTaskIndex() {
		return _selectedTaskIndex.getValue();
	}

	/**
	 * Gets the number of tasks displayed.
	 * 
	 * @return number of tasks displayed
	 */
	public int getDisplayedTasksCount() {
		return _displayedTasks.size();
	}

	/**
	 * Sets boolean for setup of display.
	 * 
	 * @param setup
	 *            boolean for setup for display
	 */
	public void setSetup(boolean setup) {
		_setup = setup;
	}

	/**
	 * Toggles the novice view.
	 * 
	 * @param isNovice boolean to set for novice view or not
	 */
	public void setNoviceSettings(boolean isNovice) {
		_isNoviceView = isNovice;
	}
	
	/**
	 * Returns novice view settings.
	 * 
	 * @return boolean for novice view settings
	 */
	public boolean getNoviceSettings() {
		return _isNoviceView;
	}
	
	/**
	 * Sets the reference to the Main UI object instance.
	 * 
	 * @param main
	 *            Main UI object instance
	 */	
	public void setMain(Main main) {
		_main = main;
	}

	/**
	 * Gets the reference to the Main UI object instance.
	 * 
	 * @return Main UI object instance
	 */
	public Main getMain() {
		return _main;
	}
}