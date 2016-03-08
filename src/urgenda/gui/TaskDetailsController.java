package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import urgenda.gui.DisplayController.Style;
import urgenda.util.Task;

public class TaskDetailsController extends GridPane {

	private static final String PATH_TASKDETAIL_FXML = "TaskDetailsView.fxml";
	@FXML
	private Label dateCreatedLabel;
	@FXML
	private Label dateModifiedLabel;
	@FXML
	private VBox detailsDisplayArea;
	@FXML
	private Label locationLabel;
	
	private Task _task;

	public TaskDetailsController(Task task) {
		_task = task;
		loadFXML();
		dateCreatedLabel.setText(formatDateTime(_task.getDateAdded()));
		dateModifiedLabel.setText(formatDateTime(_task.getDateModified()));
		locationLabel.setText(_task.getLocation());
	}

	private String formatDateTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss");
		return dateTime.format(formatter);
	}

	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TASKDETAIL_FXML));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setTaskStyle(Style taskStyle) {
		if (taskStyle == Style.OVERDUE) {
			this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_OVERDUE, null, null)));
			setStyle(DisplayController.TEXT_FILL_OVERDUE, DisplayController.TEXT_MODIFY_NONE);
		} else if (taskStyle == Style.TODAY) {
			if(_task.isImportant()) {
				this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_TODAY_IMPORTANT, null, null)));
			} else {
				this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_TODAY, null, null)));
			}
			setStyle(DisplayController.TEXT_FILL_TODAY, DisplayController.TEXT_MODIFY_NONE);
		} else if (taskStyle == Style.NORMAL) {
			if(_task.isImportant()) {
				this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_NORMAL_IMPORTANT, null, null)));
			} else {
				this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_NORMAL, null, null)));
			}
			setStyle(DisplayController.TEXT_FILL_NORMAL, DisplayController.TEXT_MODIFY_NONE);
		} else if (taskStyle == Style.ARCHIVE) {
			this.setBackground(new Background(new BackgroundFill(DisplayController.COLOR_COMPLETED, null, null)));
			setStyle(DisplayController.TEXT_FILL_COMPLETED, DisplayController.TEXT_MODIFY_NONE);
	}
}

	private void setStyle(String color, String modify) {
		dateCreatedLabel.setStyle(color  + modify);
		dateModifiedLabel.setStyle(color + modify);
		locationLabel.setStyle(color + modify);
	}
}
