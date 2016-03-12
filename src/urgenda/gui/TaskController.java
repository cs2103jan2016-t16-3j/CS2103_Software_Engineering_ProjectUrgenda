package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

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
import urgenda.util.Task;

public class TaskController extends GridPane {

	private static final Insets INSETS_ROWS = new Insets(1, 0, 0, 0);

	private static final String PATH_TASKVIEW_FXML = "TaskView.fxml";

	private static final Duration DURATION_SINGLEDAY = Duration.ofDays(1);
	private static final Duration DURATION_SINGLEHOUR = Duration.ofHours(1);
	private static final Duration DURATION_SINGLEMINUTE = Duration.ofMinutes(1);

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

	public TaskController(Task task, int index, TaskDisplayType taskDisplayType) {
		_task = task;
		_taskDisplayType = taskDisplayType;
		_index = index;
		loadFXML();
		setTaskClickHandler();
		taskIndexLabel.setText(String.valueOf(_index + 1));
		taskDescLabel.setText(task.getDesc());
		switch(task.getTaskType()) {
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
				if(_isSelected) {
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
		Duration duration;
		if (deadline.isAfter(LocalDateTime.now())) {
			duration = Duration.between(LocalDateTime.now(), deadline);
		} else {
			duration = Duration.between(deadline, LocalDateTime.now());
		}
		if (duration.getSeconds() > DURATION_SINGLEDAY.getSeconds()) {
			formattedDeadline += generateChronoUnits(duration, DURATION_SINGLEDAY) + " days";
			
		} else {
			int hours = generateChronoUnits(duration, DURATION_SINGLEHOUR);
			int minutes = generateChronoUnits(duration.minus(Duration.ofHours(hours)), DURATION_SINGLEMINUTE);
			if (hours >= 1) {
				if (hours > 1) {
					formattedDeadline += String.valueOf(hours) + " hours ";
				} else {
					formattedDeadline += "1 hour ";
				}
			}
			if (hours >= 1 && minutes >= 1) {
				formattedDeadline += "& ";
			}
			if (minutes >= 0) {
				if (minutes > 1) {
					formattedDeadline += String.valueOf(minutes) + " minutes";
				} else {
					if (hours == 0) {
						formattedDeadline += "less than a minute";
					} else {
						formattedDeadline += "1 minute";
					}
				}
			}
		}
		// TODO implement timeline updater for deadlines
		return formattedDeadline;
	}

	private int generateChronoUnits(Duration duration, Duration chronoUnit) {
		int chronoUnitCount = 0;
		while (duration.getSeconds() > chronoUnit.getSeconds()) {
			duration = duration.minus(chronoUnit);
			chronoUnitCount++;
		}
		return chronoUnitCount;
	}
	

	public void setTaskStyle(TaskDisplayType taskDisplayType) {
		switch(taskDisplayType) {
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
