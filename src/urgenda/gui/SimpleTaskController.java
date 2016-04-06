//@@author A0131857B
package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import urgenda.gui.DisplayController.Direction;
import urgenda.gui.DisplayController.TaskDisplayType;
import urgenda.util.DateTimePair;
import urgenda.util.Task;
import urgenda.util.UrgendaLogger;

/**
 * UI component, invoked when showing a simple task panel for a single task.
 * 
 * @author KangSoon
 */
public class SimpleTaskController extends GridPane {
	
	// Constants
	private static final String COUNT_DOUBLE_DIGIT = "9+";
	private static final double HEIGHT_DEFAULT_TASK = 35;
	private static final int INT_DOUBLE_DIGIT = 10;

	// Header texts
	private static final String HEADER_OVERDUE_TASK = "Overdue Tasks";
	private static final String HEADER_TODAY_TASK = "Today's Tasks";
	private static final String HEADER_OTHER_TASK = "Other Tasks";
	private static final String HEADER_ARCHIVE_TASK = "Completed Tasks";
	
	// File paths
	private static final String PATH_TASK_FREETIME_CSS = "styles/TaskFreeTime.css";
	private static final String PATH_TASK_OVERDUE_CSS = "styles/TaskOverdue.css";
	private static final String PATH_TASK_TODAY_CSS = "styles/TaskToday.css";
	private static final String PATH_TASK_TODAY_OVERTIME_CSS = "styles/TaskTodayOvertime.css";
	private static final String PATH_TASK_NORMAL_CSS = "styles/TaskNormal.css";
	private static final String PATH_TASK_ARCHIVE_CSS = "styles/TaskArchive.css";
	private static final String PATH_SIMPLETASKVIEW_FXML = "fxml/SimpleTaskView.fxml";

	
	// Elements loaded using FXML
	@FXML
	protected GridPane taskPane;
	@FXML
	protected Pane selector;
	@FXML
	protected Pane selectorPane;
	@FXML
	protected Text taskIndexText;
	@FXML
	protected ImageView importantIndicator;
	@FXML
	protected Text taskDescText;
	@FXML
	protected Text taskDateTimeText;
	@FXML
	protected BorderPane noviceHeaderPane;
	@FXML
	protected Text noviceHeaderLabel;
	@FXML
	protected Pane multipleSlotPane;
	@FXML
	protected Label multipleSlotCounter;

	//Private attributes
	private int _multipleSlotIndex;
	
	// Attributes also inherited by DetailedTaskController
	protected int _index;
	protected Task _task;
	protected TaskDisplayType _taskDisplayType;
	protected boolean _isSelected;
	protected boolean _showHeader;
	protected ArrayList<DateTimePair> _multipleSlotList;
	protected DisplayController _displayController;

	/**
	 * Creates a SimpleTaskController using the given task.
	 * @param task task to show details for
	 * @param index index of the task
	 * @param taskDisplayType the enumerated type of the task
	 * @param showHeader boolean to show headers for the task or not
	 */
	public SimpleTaskController(Task task, int index, TaskDisplayType taskDisplayType, boolean showHeader) {
		_task = task;
		_taskDisplayType = taskDisplayType;
		_index = index;
		_showHeader = showHeader;
		_multipleSlotList = new ArrayList<DateTimePair>();
		loadFXML();
		setTaskClickHandler();
		initLabels();
		if (_task.isImportant()) {
			importantIndicator.setVisible(true);
		} else {
			importantIndicator.setVisible(false);
		}
		if(!showHeader) {
			taskPane.getRowConstraints().set(0, new RowConstraints(0));
			taskPane.setPrefHeight(HEIGHT_DEFAULT_TASK);
			noviceHeaderPane.setVisible(false);
		}
		if(_task.getSlot() != null) {
			multipleSlotPane.setVisible(true);
			multipleSlotCounter.setText(countMultipleSlots());
		} else {
			multipleSlotPane.setVisible(false);
			multipleSlotCounter.setVisible(false);
		}
		setSelected(false);
		this.heightProperty().addListener(new ChangeListener<Number>(){
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(index == _displayController.getDisplayedTasksCount() - 1) {	//invoke when height of all tasks panels are set
					_displayController.setDisplayScrollHeight();
					_displayController.setSetup(false);
				}
			}		
		});
	}

	private String countMultipleSlots() {
		int count = _task.getSlot().getSlots().size() + 1;
		if (count < INT_DOUBLE_DIGIT) {
			return String.valueOf(count);
		} else {
			return COUNT_DOUBLE_DIGIT;
		}
	}
	
	//initialize text labels
	private void initLabels() {
		taskIndexText.setText(String.valueOf(_index + 1));
		taskDescText.setText(_task.getDesc());
		if (_task.getSlot() != null) { //task has multiple time slots
			_multipleSlotIndex = 0;
			_multipleSlotList.add(new DateTimePair(_task.getStartTime(), _task.getEndTime()));
			_multipleSlotList.addAll(_task.getSlot().getSlots());
			taskDateTimeText.setText(formatMultipleSlotDateTime());
		} else {
			_multipleSlotIndex = -1; //task has no multiple slots
			taskDateTimeText.setText(formatDateTime(_task.getStartTime(), _task.getEndTime()));
		}
		switch (_taskDisplayType) {
		case FREE_TIME:
			this.getStylesheets().addAll(getClass().getResource(PATH_TASK_FREETIME_CSS).toExternalForm());
			break;
		case OVERDUE:
			this.getStylesheets().addAll(getClass().getResource(PATH_TASK_OVERDUE_CSS).toExternalForm());
			noviceHeaderLabel.setText(HEADER_OVERDUE_TASK);
			break;
		case TODAY:
			if(_task.isCompleted()) {
				this.getStylesheets().addAll(getClass().getResource(PATH_TASK_TODAY_OVERTIME_CSS).toExternalForm());
			} else {
				this.getStylesheets().addAll(getClass().getResource(PATH_TASK_TODAY_CSS).toExternalForm());
			}	
			noviceHeaderLabel.setText(HEADER_TODAY_TASK);
			break;
		case NORMAL:
			this.getStylesheets().addAll(getClass().getResource(PATH_TASK_NORMAL_CSS).toExternalForm());
			noviceHeaderLabel.setText(HEADER_OTHER_TASK);
			break;
		case ARCHIVE:
			this.getStylesheets().addAll(getClass().getResource(PATH_TASK_ARCHIVE_CSS).toExternalForm());
			noviceHeaderLabel.setText(HEADER_ARCHIVE_TASK);
			break;
		}
	}

	//set event handler for mouse clicks on view to set as selected
	private void setTaskClickHandler() {
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				if(!_displayController.getMain().getController().isDemo()) {
					_displayController.setSelectedTaskByCall(_index, false);
				}
				if(e.getClickCount() == 2) { //double click
					_displayController.toggleSelectedDetailsOnClick();
				}
			}
		});
	}
	
	/**
	 * Formats dates and times of task according to number of instances of dates and times.
	 * @param dateTime1 first date-time instance
	 * @param dateTime2 second date-time instance
	 * @return formatted text for dates and times
	 */
	protected String formatDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
		String dateTimeFormatter = "";
		if(dateTime2 != null) {
			if(dateTime1 == null) {	//format for deadline
				dateTimeFormatter += "by ";
				dateTimeFormatter += formatDate(dateTime2) + " ";
				dateTimeFormatter += formatTime(dateTime2);
			} else { //format for event
				DateTimePair timeDiff = new DateTimePair(dateTime1, dateTime2);
					dateTimeFormatter += formatDate(dateTime1) + " ";
				dateTimeFormatter += formatTime(dateTime1) + " ";
				dateTimeFormatter += "to ";
				if (!timeDiff.isSameDay()) {
					dateTimeFormatter += formatDate(dateTime2) + " ";
				}
				dateTimeFormatter += formatTime(dateTime2);				
			}
		}
		return dateTimeFormatter;
	}
	
	private String formatDate(LocalDateTime dateTime) {
		DateTimePair timeLeft = new DateTimePair(LocalDateTime.now(), dateTime);
		String formattedDate = "";
		if(timeLeft.getRoundedDays() == 0) {
			formattedDate += "Today";
		} else if(timeLeft.getRoundedDays() == 1) {
			if(timeLeft.firstIsBefore()) {
				formattedDate += "Tomorrow";
			} else {
				formattedDate += "Yesterday";
			}
		} else {
			if (dateTime.getYear() != LocalDateTime.now().getYear()) {
				formattedDate += timeLeft.getDateTime2().format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
			} else {				
				formattedDate += timeLeft.getDateTime2().format(DateTimeFormatter.ofPattern("dd MMM"));
			}
		}
		return formattedDate;
	}
	
	private String formatTime(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ofPattern("h:mma"));
	}
	
	/**
	 * Loads FXML resources to setup view in display.
	 */
	protected void loadFXML() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_SIMPLETASKVIEW_FXML));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Toggles this controller as selected.
	 * @param isSelected boolean whether this controller is selected or not
	 */
	public void setSelected(boolean isSelected) {
		_isSelected = isSelected;
		selector.setVisible(_isSelected);
		selectorPane.setVisible(_isSelected);
	}
	
	/**
	 * Sets the reference for DisplayController.
	 * @param displayController reference for DisplayController
	 */
	public void setDisplayController(DisplayController displayController) {
		_displayController = displayController;
	}

	/**
	 * Traverse multiple time slots for this controller if task has multiple slots
	 * @param direction direction to traverse to
	 */
	public void traverseMultipleSlot(Direction direction) {
		if (!_multipleSlotList.isEmpty()) {
			switch(direction) {
			case LEFT:
				if (_multipleSlotIndex > 0) {
					_multipleSlotIndex--;
					taskDateTimeText.setText(formatMultipleSlotDateTime());
				}
				break;
			case RIGHT:
				if (_multipleSlotIndex < _multipleSlotList.size() - 1) {
					_multipleSlotIndex++;
					taskDateTimeText.setText(formatMultipleSlotDateTime());
				}
				break;
			default:
				UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, "Issue with call of multipleslot toggle");
				break;
			}
		}
	}

	private String formatMultipleSlotDateTime() {
		return formatDateTime(_multipleSlotList.get(_multipleSlotIndex).getEarlierDateTime(), _multipleSlotList.get(_multipleSlotIndex).getLaterDateTime());
	}
	
	/**
	 * Returns whether task in this controller has multiple time slots or not.
	 * @return boolean indicating whether task has multiple time slots
	 */
	public boolean isMultipleSlot() {
		if (_multipleSlotIndex >= 0) {
			return true;
		} else {
			return false;
		}
	}
}
