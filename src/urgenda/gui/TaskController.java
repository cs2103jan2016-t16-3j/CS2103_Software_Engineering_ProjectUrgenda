package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import urgenda.gui.DisplayController.Style;
import urgenda.util.Task;

public class TaskController extends GridPane {

	private static final String PATH_TASKVIEW_FXML = "TaskView.fxml";
	private static final String TEXT_FILL_OVERDUE = "-fx-text-fill: #FF1900;";
	private static final String TEXT_FILL_URGENT = "-fx-text-fill: #474E60;";
	private static final String TEXT_FILL_TODAY = "-fx-text-fill: #474E60;";
	private static final String TEXT_FILL_NORMAL = "-fx-text-fill: #474E60;";
	private static final String TEXT_FILL_COMPLETED = "-fx-text-fill: #808080;";
	private static final String TEXT_WEIGHT_BOLD = "-fx-font-weight: bold;";
	private static final String TEXT_WEIGHT_REGULAR = "";
	private static final String TEXT_MODIFY_ITALIC = "-fx-font-style: italic";
	private static final String TEXT_MODIFY_STRIKETHROUGH = "-fx-strikethrough: true;";
	private static final String TEXT_MODIFY_NONE = "";
	
	@FXML
	private Label indexLabel;
	@FXML
	private Label descLabel;
	@FXML
	private Label startLabel;
	@FXML
	private Label endLabel;
	@FXML
	private ImageView urgentIndicator;

	public TaskController(Task task, int index) {
		loadFXML();
		indexLabel.setText(String.valueOf(index));
		descLabel.setText(task.getDesc());
		startLabel.setText(formatDateTime(task.getStartTime()));
		endLabel.setText(formatDateTime(task.getEndTime()));
		if (task.isImportant()) {
			urgentIndicator.setVisible(true);
		}
	}

	private String formatDateTime(LocalDateTime dateTime) {
		
		DateTimeFormatter formatter;
		if(dateTime.getMinute() == 0) {
			formatter = DateTimeFormatter.ofPattern("dd MMM | h a");
		} else {
			formatter = DateTimeFormatter.ofPattern("dd MMM | hh:mma");
		}
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

	public void setTaskStyle(Style taskStyle) {
		if (taskStyle == Style.OVERDUE) {
			setStyle(TEXT_FILL_OVERDUE, TEXT_WEIGHT_BOLD, TEXT_MODIFY_NONE);
		} else if (taskStyle == Style.URGENT) {
			setStyle(TEXT_FILL_URGENT, TEXT_WEIGHT_REGULAR, TEXT_MODIFY_NONE);
		} else if (taskStyle == Style.TODAY) {
			setStyle(TEXT_FILL_TODAY, TEXT_WEIGHT_BOLD, TEXT_MODIFY_NONE);
		} else if (taskStyle == Style.NORMAL) {
			setStyle(TEXT_FILL_NORMAL, TEXT_WEIGHT_REGULAR, TEXT_MODIFY_NONE);
		} else if (taskStyle == Style.ARCHIVE) {
			setStyle(TEXT_FILL_COMPLETED, TEXT_MODIFY_ITALIC, TEXT_MODIFY_NONE);
		}
	}

	private void setStyle(String color, String weight, String modify) {
		indexLabel.setStyle(color + weight + modify);
		descLabel.setStyle(color + weight + modify);
		startLabel.setStyle(color + weight + modify);
		endLabel.setStyle(color + weight + modify);
	}
}
