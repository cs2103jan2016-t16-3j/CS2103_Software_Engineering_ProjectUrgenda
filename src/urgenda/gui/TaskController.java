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
import javafx.scene.layout.GridPane;
import urgenda.gui.DisplayController.Style;
import urgenda.util.Task;

public class TaskController extends GridPane {

	private static final Insets INSETS_ROWS = new Insets(2, 0, 0, 0);
	
	private static final String PATH_TASKVIEW_FXML = "TaskView.fxml";
	
	@FXML
	private ImageView selector;
	@FXML
	private Label taskIndexLabel;
	@FXML
	private ImageView importantIndicator;
	@FXML
	private Label taskDescLabel;
	@FXML
	private Label taskStartLabel;
	@FXML
	private Label dateTimeTypeLabel;
	@FXML
	private Label taskEndLabel;
	
	private DisplayController _displayController;
	private boolean _isSelected;
	private Task _task;
	private int _index;
	
	public TaskController(Task task, int index) {
		_task = task;
		_index = index;
		loadFXML();
		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				setSelected(true);
				_displayController.setSelectedIndexOnClick(_index);
			}
		});
		taskIndexLabel.setText(String.valueOf(_index + 1));
		taskDescLabel.setText(task.getDesc());
		if(_task.getTaskType() == Task.Type.FLOATING) {
			taskStartLabel.setText("");
			taskEndLabel.setText("");
			dateTimeTypeLabel.setText("");
		}
		if(_task.getTaskType() == Task.Type.EVENT) {
			taskStartLabel.setText(formatDateTime(task.getStartTime()));
			taskEndLabel.setText(formatDateTime(task.getEndTime()));
			dateTimeTypeLabel.setText("to");
		}
		if(_task.getTaskType() == Task.Type.DEADLINE) {
			taskStartLabel.setText("");
			taskEndLabel.setText(formatDateTime(task.getEndTime()));
			dateTimeTypeLabel.setText("by");
		}
		if (_task.isImportant()) {
		importantIndicator.setVisible(true);
		} else {
			importantIndicator.setVisible(false);
		}
		setSelected(false);
	}
	
	public void setTaskStyle(Style taskStyle) {
		if (taskStyle == Style.OVERDUE) {
			this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_OVERDUE, null, INSETS_ROWS)));
			setStyle(DisplayController.TEXT_FILL_OVERDUE, DisplayController.TEXT_WEIGHT_BOLD, DisplayController.TEXT_MODIFY_NONE);
		} else if (taskStyle == Style.TODAY) {
			if(_task.isImportant()) {
				this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_TODAY_IMPORTANT, null, INSETS_ROWS)));
			} else {
				this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_TODAY, null, INSETS_ROWS)));
			}
			setStyle(DisplayController.TEXT_FILL_TODAY, DisplayController.TEXT_WEIGHT_BOLD, DisplayController.TEXT_MODIFY_NONE);
		} else if (taskStyle == Style.NORMAL) {
			if(_task.isImportant()) {
				this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_NORMAL_IMPORTANT, null, INSETS_ROWS)));
			} else {
				this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_NORMAL, null, INSETS_ROWS)));
			}
			setStyle(DisplayController.TEXT_FILL_NORMAL, DisplayController.TEXT_WEIGHT_REGULAR, DisplayController.TEXT_MODIFY_NONE);
		} else if (taskStyle == Style.ARCHIVE) {
			this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_COMPLETED, null, INSETS_ROWS)));
			setStyle(DisplayController.TEXT_FILL_COMPLETED, DisplayController.TEXT_WEIGHT_REGULAR, DisplayController.TEXT_MODIFY_NONE);
		}
	}

	private void setStyle(String color, String weight, String modify) {
		taskIndexLabel.setStyle(color + weight + modify);
		taskDescLabel.setStyle(color + weight + modify);
		taskStartLabel.setStyle(color + weight + modify);
		dateTimeTypeLabel.setStyle(color + weight + modify); //TODO check
		taskEndLabel.setStyle(color + weight + modify);
	}
	
	private String formatDateTime(LocalDateTime dateTime) {
		
		DateTimeFormatter formatter;
			formatter = DateTimeFormatter.ofPattern("dd MMM h:mma");
		return dateTime.format(formatter);
	}
	
	private void loadFXML() {
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
