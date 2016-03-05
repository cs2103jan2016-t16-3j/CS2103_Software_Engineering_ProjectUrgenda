package urgenda.gui;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import urgenda.util.Task;
import urgenda.util.TaskList;

public class DisplayController extends AnchorPane {

	public enum Style {
		OVERDUE, URGENT, TODAY, NORMAL, ARCHIVE
	}

	private static final String MESSAGE_ZERO_TASKS = "You have no tasks to display! :(";

	@FXML
	Label displayHeader;
	@FXML
	VBox displayHolder;
	@FXML
	ScrollPane displayArea;

	private ArrayList<Task> displayedTasks;

	public DisplayController() {
	}

	public void setDisplay(TaskList updatedTasks, String displayHeader, ArrayList<Integer> showmoreIndexes) {
		displayHolder.getChildren().clear();
		displayedTasks = new ArrayList<Task>();
		displayedTasks.addAll(updatedTasks.getTasks());
		displayedTasks.addAll(updatedTasks.getArchives());
		int indexCounter = 0;
		if (updatedTasks.getUncompletedCount() != 0) {
			indexCounter += showStyledTaskView(indexCounter, showmoreIndexes, updatedTasks.getOverdueCount(),
					Style.OVERDUE);
			indexCounter += showStyledTaskView(indexCounter, showmoreIndexes, updatedTasks.getTodayCount(),
					Style.TODAY);
			indexCounter += showStyledTaskView(indexCounter, showmoreIndexes, updatedTasks.getUrgentCount(),
					Style.URGENT);
			indexCounter += showStyledTaskView(indexCounter, showmoreIndexes, updatedTasks.getRemainingCount(),
					Style.NORMAL);
		}
		if(updatedTasks.getArchiveCount() != 0) {
			indexCounter += showStyledTaskView(indexCounter, showmoreIndexes, updatedTasks.getArchiveCount(),
					Style.ARCHIVE);
		}
		if(updatedTasks.getArchiveCount()+ updatedTasks.getUncompletedCount() == 0) {
			showZeroTasksFeedback();
		}
		// add completed tasks
		setDisplayHeader(displayHeader);
	}

	private void showZeroTasksFeedback() {
		displayHolder.getChildren().add(new Label(MESSAGE_ZERO_TASKS));		//TODO correct this
	}

	private int showStyledTaskView(int currIndex, ArrayList<Integer> showmoreIndexes, int toAddCount, Style style) {
		int addedCount = 0;
		while (addedCount < toAddCount) {
			TaskController newTaskView = new TaskController(displayedTasks.get(currIndex), currIndex + 1);
			newTaskView.setTaskStyle(style);
			displayHolder.getChildren().add(newTaskView);
			if (showmoreIndexes.contains(Integer.valueOf(currIndex))) {
				TaskDetailsController newTaskDetail = new TaskDetailsController(
						displayedTasks.get(currIndex));
				displayHolder.getChildren().add(newTaskDetail);
			}
			addedCount++;
			currIndex++;
		}
		return addedCount;
	}

	public int getFocusedLine() {
		// TODO to make traversable and return index of selected task
		return -1;
	}

	private void setDisplayHeader(String displayed) {
		displayHeader.setText(displayed);
	}
}
