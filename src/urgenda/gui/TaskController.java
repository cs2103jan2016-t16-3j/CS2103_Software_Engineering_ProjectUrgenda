package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import urgenda.gui.DisplayController.Style;
import urgenda.util.Task;

public class TaskController extends GridPane {

	private static final Insets INSETS_EVEN_ROWS = new Insets(2,0,2,0);

	private static final Color COLOR_EVEN_ROWS = Color.rgb(71, 76, 89, 0.2);
	
	private static final String PATH_TASKVIEW_FXML = "TaskView.fxml";
	private static final String TEXT_FILL_OVERDUE = "-fx-text-fill: #FF1900;";
	private static final String TEXT_FILL_URGENT = "-fx-text-fill: #474E60;";
	private static final String TEXT_FILL_TODAY = "-fx-text-fill: #474E60;";
	private static final String TEXT_FILL_NORMAL = "-fx-text-fill: #474E60;";
	private static final String TEXT_FILL_COMPLETED = "-fx-text-fill: #808080;";
	private static final String TEXT_WEIGHT_BOLD = "-fx-font-weight: bold;";
	private static final String TEXT_WEIGHT_REGULAR = "";
	private static final String TEXT_MODIFY_ITALIC = "-fx-font-style: italic";
	private static final String TEXT_MODIFY_NONE = "";
	
	@FXML
	private Label taskIndexLabel;
	@FXML
	private Label taskDescLabel;
	@FXML
	private Label taskStartLabel;
	@FXML
	private Label taskEndLabel;
	@FXML
	private ImageView urgentIndicator;
	
	private boolean _isSelected;
	
	public TaskController(Task task, int index) {
		loadFXML();
		taskIndexLabel.setText(String.valueOf(index));
		taskDescLabel.setText(task.getDesc());
		if(task.getTaskType() == Task.Type.EVENT) {
			taskStartLabel.setText(formatDateTime(task.getStartTime()));
		}
		if(task.getTaskType() == Task.Type.EVENT || task.getTaskType() == Task.Type.DEADLINE)
		taskEndLabel.setText(formatDateTime(task.getEndTime()));
		if (task.isImportant()) {
			urgentIndicator.setVisible(true);
		}
		if(index % 2 == 0) { //format even rows only
			this.backgroundProperty().set(new Background(new BackgroundFill(COLOR_EVEN_ROWS, new CornerRadii(3), INSETS_EVEN_ROWS)));
		}
		setSelected(false);
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
			setStyle(TEXT_FILL_COMPLETED, TEXT_WEIGHT_REGULAR, TEXT_MODIFY_ITALIC);
		}
	}

	private void setStyle(String color, String weight, String modify) {
		taskIndexLabel.setStyle(color + weight + modify);
		taskDescLabel.setStyle(color + weight + modify);
		taskStartLabel.setStyle(color + weight + modify);
		taskEndLabel.setStyle(color + weight + modify);
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

	public boolean isSelected() {
		return _isSelected;
	}

	public void setSelected(boolean isSelected) {
		_isSelected = isSelected;
		urgentIndicator.setVisible(_isSelected);
	}
}
