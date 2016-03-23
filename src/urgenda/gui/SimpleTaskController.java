package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import urgenda.gui.DisplayController.TaskDisplayType;
import urgenda.util.DateTimePair;
import urgenda.util.Task;

public class SimpleTaskController extends GridPane {

	private static final String HEADER_OVERDUE_TASK = "Overdue Tasks";
	private static final String HEADER_TODAY_TASK = "Today's Tasks";
	private static final String HEADER_OTHER_TASK = "Other Tasks";
	private static final String HEADER_ARCHIVE_TASK = "Completed Tasks";
	private static final String PATH_TASK_FREETIME_CSS = "TaskFreeTime.css";
	private static final String PATH_TASK_OVERDUE_CSS = "TaskOverdue.css";
	private static final String PATH_TASK_TODAY_CSS = "TaskToday.css";
	private static final String PATH_TASK_TODAY_OVERTIME_CSS = "TaskTodayOvertime.css";
	private static final String PATH_TASK_NORMAL_CSS = "TaskNormal.css";
	private static final String PATH_TASK_ARCHIVE_CSS = "TaskArchive.css";
	private static final String PATH_SIMPLETASKVIEW_FXML = "SimpleTaskView.fxml";

	private static final double HEIGHT_DEFAULT_TASK = 35;

	@FXML
	protected GridPane taskPane;
	@FXML
	protected Pane selector;
	@FXML
	protected Label taskIndexLabel;
	@FXML
	protected ImageView importantIndicator;
	@FXML
	protected Label taskDescLabel;
	@FXML
	protected Label taskDateTimeLabel;
	@FXML
	protected BorderPane noviceHeaderPane;
	@FXML
	protected Label noviceHeaderLabel;

	protected int _index;
	protected Task _task;
	protected TaskDisplayType _taskDisplayType;
	protected boolean _isSelected;
	protected boolean _showHeader;
	protected DisplayController _displayController;

	public SimpleTaskController(Task task, int index, TaskDisplayType taskDisplayType, boolean showHeader) {
		_task = task;
		_taskDisplayType = taskDisplayType;
		_index = index;
		_showHeader = showHeader;
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
			taskPane.setMaxHeight(HEIGHT_DEFAULT_TASK);
			noviceHeaderPane.setVisible(false);
		}
		//setTaskStyle(_taskDisplayType);
		setSelected(false);
	}

	private void initLabels() {
		taskIndexLabel.setText(String.valueOf(_index + 1));
		taskDescLabel.setText(_task.getDesc());
		switch (_taskDisplayType) {
		case FREE_TIME:
			taskDateTimeLabel.setText(formatDateTime(_task.getStartTime(), _task.getEndTime(), true));
			this.getStylesheets().addAll(getClass().getResource(PATH_TASK_FREETIME_CSS).toExternalForm());
			break;
		case OVERDUE:
			taskDateTimeLabel.setText(formatDateTime(_task.getStartTime(), _task.getEndTime(), false));
			this.getStylesheets().addAll(getClass().getResource(PATH_TASK_OVERDUE_CSS).toExternalForm());
			noviceHeaderLabel.setText(HEADER_OVERDUE_TASK);
			break;
		case TODAY:
			taskDateTimeLabel.setText(formatDateTime(_task.getStartTime(), _task.getEndTime(), false));
			if(_task.isCompleted()) {
				this.getStylesheets().addAll(getClass().getResource(PATH_TASK_TODAY_OVERTIME_CSS).toExternalForm());
			} else {
				this.getStylesheets().addAll(getClass().getResource(PATH_TASK_TODAY_CSS).toExternalForm());
			}	
			noviceHeaderLabel.setText(HEADER_TODAY_TASK);
			break;
		case NORMAL:
			taskDateTimeLabel.setText(formatDateTime(_task.getStartTime(), _task.getEndTime(), false));
			this.getStylesheets().addAll(getClass().getResource(PATH_TASK_NORMAL_CSS).toExternalForm());
			noviceHeaderLabel.setText(HEADER_OTHER_TASK);
			break;
		case ARCHIVE:
			taskDateTimeLabel.setText(formatDateTime(_task.getStartTime(), _task.getEndTime(), false));
			this.getStylesheets().addAll(getClass().getResource(PATH_TASK_ARCHIVE_CSS).toExternalForm());
			noviceHeaderLabel.setText(HEADER_ARCHIVE_TASK);
			break;
		}
	}

	private void setTaskClickHandler() {
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				setSelected(true);
				_displayController.setSelectedIndexOnClick(_index);
				if(e.getClickCount() == 2) { //double click
					_displayController.toggleSelectedDetailsOnClick();
				}
			}
		});
	}

	private String formatDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2, boolean isFreeTime) {
		String dateTimeFormatter = "";
		if(dateTime2 != null) {
			if(dateTime1 == null) {	//format for deadline
				dateTimeFormatter += "by ";
				dateTimeFormatter += formatDate(dateTime2) + " ";
				dateTimeFormatter += formatTime(dateTime2);
			} else { //format for event
				DateTimePair timeDiff = new DateTimePair(dateTime1, dateTime2);
				if(!isFreeTime) {
					dateTimeFormatter += formatDate(dateTime1) + " ";
				}
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
			formattedDate += timeLeft.getDateTime2().format(DateTimeFormatter.ofPattern("dd MMM"));
		}
		return formattedDate;
	}
	
	private String formatTime(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ofPattern("h:mma"));
	}

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

	public void setSelected(boolean isSelected) {
		_isSelected = isSelected;
		selector.setVisible(_isSelected);
	}

	public void setDisplayController(DisplayController displayController) {
		_displayController = displayController;
	}
}
