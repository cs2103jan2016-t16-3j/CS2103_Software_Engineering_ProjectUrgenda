package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import urgenda.gui.DisplayController.TaskDisplayType;
import urgenda.util.DateTimePair;
import urgenda.util.Task;

public class SimpleTaskController extends GridPane {

	private static final Insets INSETS_ROWS = new Insets(1, 0, 0, 0);

	private static final String PATH_TASKVIEW_FXML = "SimpleTaskView.fxml";
	
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
		taskDateTimeLabel.setText(formatDateTime(_task.getStartTime(), _task.getEndTime()));
		switch (_taskDisplayType) {
		case OVERDUE:
			this.getStylesheets().addAll(getClass().getResource("TaskOverdue.css").toExternalForm());
			noviceHeaderLabel.setText("Overdue Tasks");
			break;
		case TODAY:
			if(_task.isCompleted()) {
				this.getStylesheets().addAll(getClass().getResource("TaskTodayOvertime.css").toExternalForm());
			} else {
				this.getStylesheets().addAll(getClass().getResource("TaskToday.css").toExternalForm());
			}	
			noviceHeaderLabel.setText("Today's Tasks");
			break;
		case NORMAL:
			this.getStylesheets().addAll(getClass().getResource("TaskNormal.css").toExternalForm());
			noviceHeaderLabel.setText("Other Tasks");
			break;
		case ARCHIVE:
			this.getStylesheets().addAll(getClass().getResource("TaskArchive.css").toExternalForm());
			noviceHeaderLabel.setText("Completed Tasks");
			break;
		}
	}

	private void setTaskClickHandler() {
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (_isSelected) {
					_displayController.toggleSelectedDetailsOnClick();
				} else {
					setSelected(true);
					_displayController.setSelectedIndexOnClick(_index);
				}
			}
		});
	}

	private String formatDateTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
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
				if (timeDiff.getDays() > 0) {
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TASKVIEW_FXML));
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
