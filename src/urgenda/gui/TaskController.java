package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;

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
import urgenda.util.LocalDateTimeDifference;
import urgenda.util.Task;

public class TaskController extends GridPane {

	private static final Insets INSETS_ROWS = new Insets(1, 0, 0, 0);

	private static final String PATH_TASKVIEW_FXML = "TaskView.fxml";
	
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
	protected Label taskStartLabel;
	@FXML
	protected Label dateTimeTypeLabel;
	@FXML
	protected Label taskEndLabel;
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

	public TaskController(Task task, int index, TaskDisplayType taskDisplayType, boolean showHeader) {
		_task = task;
		_taskDisplayType = taskDisplayType;
		_index = index;
		_showHeader = showHeader;
		loadFXML();
		setTaskClickHandler();
		taskIndexLabel.setText(String.valueOf(_index + 1));
		taskDescLabel.setText(task.getDesc());
		switch (_task.getTaskType()) {
		case FLOATING:
			taskStartLabel.setText("");
			taskEndLabel.setText("");
			dateTimeTypeLabel.setText("");
			break;
		case EVENT:
			taskStartLabel.setText(formatDateTime(_task.getStartTime()));
			taskEndLabel.setText(formatDateTime(_task.getEndTime()));
			dateTimeTypeLabel.setText("to");
			break;
		case DEADLINE:
			taskStartLabel.setText("");
			taskEndLabel.setText(formatDateTime(_task.getEndTime()));
			dateTimeTypeLabel.setText("by");
			break;
		}
		switch (_taskDisplayType) {
		case OVERDUE:
			noviceHeaderLabel.setText("Overdue Tasks");
			break;
		case TODAY:
			noviceHeaderLabel.setText("Today's Tasks");
			break;
		case NORMAL:
			noviceHeaderLabel.setText("Other Tasks");
			break;
		case ARCHIVE:
			noviceHeaderLabel.setText("Completed Tasks");
			break;
		}
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
		setTaskStyle(_taskDisplayType);
		setSelected(false);
	}

	private void setTaskClickHandler() {
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (_isSelected) {
					_displayController.toggleDetailedOnClick(_task, _index, _taskDisplayType);
				} else {
					setSelected(true);
					_displayController.setSelectedIndexOnClick(_index);
				}
			}
		});
	}

	public void setTaskStyle(TaskDisplayType taskDisplayType) {
		switch (taskDisplayType) {
		case OVERDUE:
			this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_OVERDUE, null, INSETS_ROWS)));
			if(_showHeader) {	
				selector.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_INDICATOR_OVERDUE, null, null)));
			} else {
				selector.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_INDICATOR_OVERDUE, null, INSETS_ROWS)));
			}
			setStyle(DisplayController.TEXT_FILL_OVERDUE, DisplayController.TEXT_WEIGHT_BOLD);
			break;
		case TODAY:
			if (_task.isImportant()) {
				this.setBackground(
						new Background(new BackgroundFill(DisplayController.COLOR_TODAY_IMPORTANT, null, INSETS_ROWS)));
			} else {
				this.setBackground(
						new Background(new BackgroundFill(DisplayController.COLOR_TODAY, null, INSETS_ROWS)));
			}
			if(_showHeader) {	
				selector.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_INDICATOR_TODAY, null, null)));
			} else {
				selector.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_INDICATOR_TODAY, null, INSETS_ROWS)));
			}
			setStyle(DisplayController.TEXT_FILL_TODAY, DisplayController.TEXT_WEIGHT_REGULAR);
			break;
		case NORMAL:
			if (_task.isImportant()) {
				this.setBackground(new Background(
						new BackgroundFill(DisplayController.COLOR_NORMAL_IMPORTANT, null, INSETS_ROWS)));
			} else {
				this.setBackground(
						new Background(new BackgroundFill(DisplayController.COLOR_NORMAL, null, INSETS_ROWS)));
			}
			if(_showHeader) {	
				selector.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_INDICATOR_NORMAL, null, null)));
			} else {
				selector.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_INDICATOR_NORMAL, null, INSETS_ROWS)));
			}
			setStyle(DisplayController.TEXT_FILL_NORMAL, DisplayController.TEXT_WEIGHT_REGULAR);
			break;
		case ARCHIVE:
			this.setBackground(
					new Background(new BackgroundFill(DisplayController.COLOR_COMPLETED, null, INSETS_ROWS)));
			if(_showHeader) {	
				selector.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_INDICATOR_COMPLETED, null, null)));
			} else {
				selector.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_INDICATOR_COMPLETED, null, INSETS_ROWS)));
			}
			setStyle(DisplayController.TEXT_FILL_COMPLETED, DisplayController.TEXT_WEIGHT_REGULAR);
			break;
		}
	}

	protected void setStyle(String backgroundColor, String weight) {
		taskIndexLabel.setStyle(backgroundColor + weight);
		taskDescLabel.setStyle(backgroundColor + weight);
		taskStartLabel.setStyle(backgroundColor + weight);
		dateTimeTypeLabel.setStyle(backgroundColor + weight);
		taskEndLabel.setStyle(backgroundColor + weight);
	}

	private String formatDateTime(LocalDateTime dateTime) {
		String dateTimeFormatter = "";
		LocalDateTimeDifference timeLeft = new LocalDateTimeDifference(LocalDateTime.now(), dateTime);
		if(timeLeft.getRoundedDays() == 0) {
				dateTimeFormatter += "Today ";
		} else if(timeLeft.getRoundedDays() == 1) {
			if(timeLeft.firstIsBefore()) {
				dateTimeFormatter += "Tomorrow ";
			} else {
				dateTimeFormatter += "Yesterday ";
			}
		//} else if(timeLeft.getRoundedDays() <= 7 && timeLeft.firstIsBefore()) {
		//	dateTimeFormatter += dateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.getDefault()) + " ";
		} else {
			dateTimeFormatter += dateTime.format(DateTimeFormatter.ofPattern("dd MMM "));
		}
		dateTimeFormatter += dateTime.format(DateTimeFormatter.ofPattern("h:mma"));
		return dateTimeFormatter;
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
