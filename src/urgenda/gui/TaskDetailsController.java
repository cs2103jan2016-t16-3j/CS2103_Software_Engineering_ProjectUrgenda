package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import urgenda.util.Task;

public class TaskDetailsController extends GridPane {

	private static final String PATH_TASKDETAIL_FXML = "TaskDetailsView.fxml";
	@FXML
	private Label dateCreatedLabel;
	@FXML
	private Label dateModifiedLabel;
	@FXML
	private VBox detailsDisplayArea;

	public TaskDetailsController(Task task) {
		loadFXML();
		dateCreatedLabel.setText(formatDateTime(task.getDateAdded()));
		dateModifiedLabel.setText(formatDateTime(task.getDateModified()));
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
}
