package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import urgenda.gui.DisplayController.TaskDisplayType;
import urgenda.util.LocalDateTimeDifference;
import urgenda.util.Task;

public class TaskController extends GridPane {

	private static final Insets INSETS_ROWS = new Insets(1, 0, 0, 0);

	private static final String PATH_TASKVIEW_FXML = "TaskView.fxml";

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

	protected int _index;
	protected Task _task;
	protected TaskDisplayType _taskDisplayType;
	protected boolean _isSelected;
	protected DisplayController _displayController;
	// TODO implement countdown of deadlines
	protected Timeline _timeline;

	public TaskController(Task task, int index, TaskDisplayType taskDisplayType) {
		_task = task;
		_taskDisplayType = taskDisplayType;
		_index = index;
		loadFXML();
		setTaskClickHandler();
		taskIndexLabel.setText(String.valueOf(_index + 1));
		taskDescLabel.setText(task.getDesc());
		switch (task.getTaskType()) {
		case FLOATING:
			taskStartLabel.setText("");
			taskEndLabel.setText("");
			dateTimeTypeLabel.setText("");
			break;
		case EVENT:
			taskStartLabel.setText(formatDateTime(task.getStartTime()));
			taskEndLabel.setText(formatDateTime(task.getEndTime()));
			dateTimeTypeLabel.setText("to");
			break;
		case DEADLINE:
			if (_task.isOverdue()) {
				taskStartLabel.setText("overdue by: ");
			} else {
				taskStartLabel.setText("due in: ");
			}
			taskStartLabel.setAlignment(Pos.CENTER_RIGHT);
			taskEndLabel.setText("");
			dateTimeTypeLabel.setText(formatDeadline(task.getEndTime()));
			break;
		}
		if (_task.isImportant()) {
			importantIndicator.setVisible(true);
		} else {
			importantIndicator.setVisible(false);
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

	private String formatDeadline(LocalDateTime deadline) {
		String formattedDeadline = "";
		LocalDateTimeDifference timeLeft = new LocalDateTimeDifference(LocalDateTime.now(), deadline);

		if (timeLeft.getDays() > 0) {
			if (timeLeft.firstIsBefore()) { // current time before deadline
				formattedDeadline += timeLeft.getDays() + " more day";
			} else { // deadline past current time
				formattedDeadline += timeLeft.getDays() + " day";
			}
			if (timeLeft.getDays() > 1)
				formattedDeadline += "s";
		} else {
			if (timeLeft.getHours() > 0) {
				formattedDeadline += timeLeft.getHours() + " hour";
				if (timeLeft.getHours() > 1)
					formattedDeadline += "s";
			}
			if (timeLeft.getHours() > 0 && timeLeft.getMinutes() > 0) {
				formattedDeadline += " & ";
			}
			if (timeLeft.getMinutes() > 0) {
				formattedDeadline += timeLeft.getMinutes() + " minute";
				if (timeLeft.getMinutes() > 1)
					formattedDeadline += "s";
			} else {
				formattedDeadline += "less than a minute";
			}
		}

		return formattedDeadline;
	}

	public void setTaskStyle(TaskDisplayType taskDisplayType) {
		switch (taskDisplayType) {
		case OVERDUE:
			this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_OVERDUE, null, INSETS_ROWS)));
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
			setStyle(DisplayController.TEXT_FILL_NORMAL, DisplayController.TEXT_WEIGHT_REGULAR);
			break;
		case ARCHIVE:
			this.setBackground(
					new Background(new BackgroundFill(DisplayController.COLOR_COMPLETED, null, INSETS_ROWS)));
			setStyle(DisplayController.TEXT_FILL_COMPLETED, DisplayController.TEXT_WEIGHT_REGULAR);
			break;
		}
	}

	protected void setStyle(String color, String weight) {
		taskIndexLabel.setStyle(color + weight);
		taskDescLabel.setStyle(color + weight);
		taskStartLabel.setStyle(color + weight);
		dateTimeTypeLabel.setStyle(color + weight);
		taskEndLabel.setStyle(color + weight);
	}

	private String formatDateTime(LocalDateTime dateTime) {
		return dateTime.format(DateTimeFormatter.ofPattern("dd MMM | h:mma"));
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
