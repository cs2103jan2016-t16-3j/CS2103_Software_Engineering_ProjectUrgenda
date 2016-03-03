package urgenda.gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import urgenda.util.Task;

public class TaskViewController extends GridPane{
	
	private static final String TASK_VIEW_FXML = "TaskView.fxml";
	
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
	
	public TaskViewController(Task task, int index) {
		loadFXML();
		indexLabel.setText(String.valueOf(index));
		descLabel.setText(task.getDesc());
		startLabel.setText(task.getStartTime().toString());
		endLabel.setText(task.getEndTime().toString());
		if(task.isUrgent()) {
			urgentIndicator.setVisible(true);
		}
	}
	
	private void loadFXML() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(TASK_VIEW_FXML));
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
