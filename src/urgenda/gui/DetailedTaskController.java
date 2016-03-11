package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import urgenda.gui.DisplayController.TaskDisplayType;
import urgenda.util.Task;

public class DetailedTaskController extends TaskController {
	
	private static final String PATH_DETAILEDTASKVIEW_FXML = "DetailedTaskView.fxml";
	
	@FXML
	private Label dateCreatedLabel;
	@FXML
	private Label dateModifiedLabel;
	@FXML
	private VBox detailsDisplayArea;
	@FXML
	private Label taskLocationLabel;
	
	public DetailedTaskController(Task task, int index, TaskDisplayType taskDisplayType) {
		super(task, index, taskDisplayType);
		dateCreatedLabel.setText(formatDetailsDateTime(task.getDateAdded()));
		dateModifiedLabel.setText(formatDetailsDateTime(task.getDateModified()));
		taskLocationLabel.setText(task.getLocation());
	}
	
	public void resizeOverrunDescLabel() {
		Text text = new Text(taskDescLabel.getText());
		switch(_taskDisplayType) {
		case OVERDUE:
			text.setFont(Main.BOLD_FONT);
			break;
		case TODAY:
		case NORMAL:
		case ARCHIVE:
			text.setFont(Main.REGULAR_FONT);
			break;
		}
		double textWidth = text.getLayoutBounds().getWidth();
		if(textWidth >= 200) {
			taskDescLabel.setMinHeight(40);
			taskPane.setMinHeight(80);
			//System.out.println(textWidth);
		} 
		//TODO work on scaling according to label, remove magic numbers
	}

	private String formatDetailsDateTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss");
		return dateTime.format(formatter);
	}
	
	@Override
	protected void loadFXML() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_DETAILEDTASKVIEW_FXML));
		loader.setController(this);
		loader.setRoot(this);
		try {
			loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void setStyle(String color, String weight, String modify) {
		taskIndexLabel.setStyle(color + weight + modify);
		taskDescLabel.setStyle(color + weight + modify);
		taskStartLabel.setStyle(color + weight + modify);
		dateTimeTypeLabel.setStyle(color + weight + modify);
		taskEndLabel.setStyle(color + weight + modify);
		dateCreatedLabel.setStyle(color);
		dateModifiedLabel.setStyle(color);
		taskLocationLabel.setStyle(color + weight + modify);
		
	}
}