package urgenda.gui;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import urgenda.util.TaskList;

public class DisplayController extends AnchorPane {

	public enum Style {
		OVERDUE, URGENT, TODAY, NORMAL, COMPLETED
	}

	private static final double SCROLL_INCREMENT_HEIGHT = 10;
	@FXML
	Label displayHeader;
	@FXML
	VBox displayHolder;
	@FXML
	ScrollPane displayArea;

	private TaskList displayedTasks;

	public DisplayController() {
	}

	public void setDisplay(TaskList updatedTasks, String displayHeader, ArrayList<Integer> detailedTasks) {
		displayHolder.getChildren().clear();
		displayedTasks = updatedTasks;
		int indexCounter = 0;
		indexCounter += showStyledTaskView(indexCounter, detailedTasks, displayedTasks.getOverdueCount(),
				Style.OVERDUE);
		indexCounter += showStyledTaskView(indexCounter, detailedTasks, displayedTasks.getTodayCount(), Style.TODAY);
		indexCounter += showStyledTaskView(indexCounter, detailedTasks, displayedTasks.getUrgentCount(), Style.URGENT);
		indexCounter += showStyledTaskView(indexCounter, detailedTasks, displayedTasks.getRemainingCount(),
				Style.NORMAL);
		indexCounter += showStyledTaskView(indexCounter, detailedTasks, displayedTasks.getShownCompletedCount(),
				Style.COMPLETED);
		setDisplayHeader(displayHeader);
	}

	private int showStyledTaskView(int currIndex, ArrayList<Integer> detailedTasks, int toAddCount, Style style) {
		int addedCount = 0;
		while (addedCount < toAddCount) {
			TaskController newTaskView = new TaskController(displayedTasks.getList().get(currIndex), currIndex + 1);
			newTaskView.setTaskStyle(style);
			displayHolder.getChildren().add(newTaskView);
			if (detailedTasks.contains(Integer.valueOf(currIndex))) {
				TaskDetailsController newTaskDetail = new TaskDetailsController(
						displayedTasks.getList().get(currIndex));
				displayHolder.getChildren().add(newTaskDetail);
			}
			addedCount++;
			currIndex++;
		}
		return addedCount;
	}

	private void setDisplayHeader(String displayed) {
		displayHeader.setText(displayed);
	}
}
