package urgenda.gui;

import javafx.beans.value.ChangeListener;
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
		int tasksCounter = 0; 
		tasksCounter += showOverdue(updatedTasks, tasksCounter);
		tasksCounter += showUrgent(updatedTasks, tasksCounter);
		tasksCounter += showToday(updatedTasks, tasksCounter);
		tasksCounter += showRemaining(updatedTasks, tasksCounter);
		tasksCounter += showCompleted(updatedTasks, tasksCounter);
		setDisplayHeader(displayHeader);
	}
	
	private int showOverdue(TaskList updatedTasks, int index) {
		int overdueCount = 0;
		while(overdueCount < updatedTasks.getOverdueCount()) {
			displayArea.getChildren().add(new TaskViewController(updatedTasks.getList().get(index), index + 1));
			overdueCount++;
			index++;
		}
		return index;
	}

	private int showUrgent(TaskList updatedTasks, int index) {
		int urgentCount = 0;
		while(urgentCount < updatedTasks.getUrgentCount()) {
			displayArea.getChildren().add(new TaskViewController(updatedTasks.getList().get(index), index + 1));
			urgentCount++;
			index++;
		}
		return index;
	}

	private int showToday(TaskList updatedTasks, int index) {
		int todayCount = 0;
		while(todayCount < updatedTasks.getTodayCount()) {
			displayArea.getChildren().add(new TaskViewController(updatedTasks.getList().get(index), index + 1));
			todayCount++;
			index++;
		}
		return index;
	}

	private int showRemaining(TaskList updatedTasks, int index) {
		// TODO Auto-generated method stub
		return index;
	}

	private int showCompleted(TaskList updatedTasks, int index) {
		// TODO Auto-generated method stub
		return index;
	}

	private void setDisplayHeader(String displayed) {
		displayHeader.setText(displayed);
	}
}
