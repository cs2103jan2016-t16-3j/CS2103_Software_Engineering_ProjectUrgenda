package urgenda.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import urgenda.util.TaskList;

public class DisplayController extends AnchorPane{	
	
	@FXML
	Label displayHeader;
	@FXML
	VBox displayArea;
	
	private TaskList displayedTasks;
	
	public DisplayController() {
	}
	
	public void setDisplay(TaskList updatedTasks, String displayHeader) {
		showOverdue();
		showUrgent();
		showToday();
		showRemaining();
		showCompleted();
		setDisplayHeader(displayHeader);
	}
	
	private void showOverdue() {
		// TODO Auto-generated method stub
		
	}

	private void showUrgent() {
		// TODO Auto-generated method stub
		
	}

	private void showToday() {
		// TODO Auto-generated method stub
		
	}

	private void showRemaining() {
		// TODO Auto-generated method stub
		
	}

	private void showCompleted() {
		// TODO Auto-generated method stub
		
	}

	private void setDisplayHeader(String displayed) {
		displayHeader.setText(displayed);
	}
}
