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

	private DisplayController _displayController;
	private boolean _isSelected;
	protected Task _task;
	protected TaskDisplayType _taskDisplayType;
	protected int _index;

	public TaskController(Task task, int index, TaskDisplayType taskDisplayType) {
		_task = task;
		_taskDisplayType = taskDisplayType;
		_index = index;
		loadFXML();
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
		taskIndexLabel.setText(String.valueOf(_index + 1));
		taskDescLabel.setText(task.getDesc());
		if (_task.getTaskType() == Task.Type.FLOATING) {
			taskStartLabel.setText("");
			taskEndLabel.setText("");
			dateTimeTypeLabel.setText("");
		}
		if (_task.getTaskType() == Task.Type.EVENT) {
			taskStartLabel.setText(formatDateTime(task.getStartTime()));
			taskEndLabel.setText(formatDateTime(task.getEndTime()));
			dateTimeTypeLabel.setText("to");
		}
		if (_task.getTaskType() == Task.Type.DEADLINE) {
			if (_task.isOverdue()) {
				taskStartLabel.setText("overdue by ");
			} else {
				taskStartLabel.setText("due in ");
			}
			taskStartLabel.setAlignment(Pos.CENTER_RIGHT);
			taskEndLabel.setText("");
			dateTimeTypeLabel.setText(formatDeadline(task.getEndTime()));
		}
		if (_task.isImportant()) {
			importantIndicator.setVisible(true);
		} else {
			importantIndicator.setVisible(false);
		}
		setTaskStyle(_taskDisplayType);
		setSelected(false);
	}

	private String formatDeadline(LocalDateTime deadline) {
		String formattedDeadline = "";
		int days = 0;
		int hours = 0;
		int minutes = 0;
		Duration duration;
		if (deadline.isAfter(LocalDateTime.now())) {
			duration = Duration.between(LocalDateTime.now(), deadline);
		} else {
			duration = Duration.between(deadline, LocalDateTime.now());
		}
		Duration singleDay = Duration.ofDays(1);
		Duration singleHour = Duration.ofHours(1);
		Duration singleMinute = Duration.ofMinutes(1);
		if (duration.getSeconds() > singleDay.getSeconds()) {
			while (duration.getSeconds() > singleDay.getSeconds()) {
				duration = duration.minus(singleDay);
				days++;
			}
			formattedDeadline = String.valueOf(days) + " days";
		} else {
			while (duration.getSeconds() > singleHour.getSeconds()) {
				duration = duration.minus(singleHour);
				hours++;
			}
			while (duration.getSeconds() > singleMinute.getSeconds()) {
				duration = duration.minus(singleMinute);
				minutes++;
			}
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

	public void setTaskStyle(TaskDisplayType taskDisplayType) {
		if (taskDisplayType == TaskDisplayType.OVERDUE) {
			this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_OVERDUE, null, INSETS_ROWS)));
			setStyle(DisplayController.TEXT_FILL_OVERDUE, DisplayController.TEXT_WEIGHT_BOLD,
					DisplayController.TEXT_MODIFY_NONE);
		} else if (taskDisplayType == TaskDisplayType.TODAY) {
			if (_task.isImportant()) {
				this.setBackground(
						new Background(new BackgroundFill(DisplayController.COLOR_TODAY_IMPORTANT, null, INSETS_ROWS)));
			} else {
				this.setBackground(
						new Background(new BackgroundFill(DisplayController.COLOR_TODAY, null, INSETS_ROWS)));
			}
			setStyle(DisplayController.TEXT_FILL_TODAY, DisplayController.TEXT_WEIGHT_REGULAR,
					DisplayController.TEXT_MODIFY_NONE);
		} else if (taskDisplayType == TaskDisplayType.NORMAL) {
			if (_task.isImportant()) {
				this.setBackground(new Background(
						new BackgroundFill(DisplayController.COLOR_NORMAL_IMPORTANT, null, INSETS_ROWS)));
			} else {
				this.setBackground(
						new Background(new BackgroundFill(DisplayController.COLOR_NORMAL, null, INSETS_ROWS)));
			}
			setStyle(DisplayController.TEXT_FILL_NORMAL, DisplayController.TEXT_WEIGHT_REGULAR,
					DisplayController.TEXT_MODIFY_NONE);
		} else if (taskDisplayType == TaskDisplayType.ARCHIVE) {
			this.setBackground(
					new Background(new BackgroundFill(DisplayController.COLOR_COMPLETED, null, INSETS_ROWS)));
			setStyle(DisplayController.TEXT_FILL_COMPLETED, DisplayController.TEXT_WEIGHT_REGULAR,
					DisplayController.TEXT_MODIFY_NONE);
		}
	}

	protected void setStyle(String color, String weight, String modify) {
		taskIndexLabel.setStyle(color + weight + modify);
		taskDescLabel.setStyle(color + weight + modify);
		taskStartLabel.setStyle(color + weight + modify);
		dateTimeTypeLabel.setStyle(color + weight + modify);
		taskEndLabel.setStyle(color + weight + modify);
	}

	private String formatDateTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter;
		formatter = DateTimeFormatter.ofPattern("dd MMM | h:mma");
		return dateTime.format(formatter);
	}

	protected void loadFXML() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TASKVIEW_FXML));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
