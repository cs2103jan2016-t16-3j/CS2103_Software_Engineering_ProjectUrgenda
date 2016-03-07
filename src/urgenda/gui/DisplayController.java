package urgenda.gui;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import urgenda.util.Task;
import urgenda.util.TaskList;

public class DisplayController extends AnchorPane {

	public enum Style {
		OVERDUE, URGENT, TODAY, NORMAL, ARCHIVE
	}

	private static final String MESSAGE_ZERO_TASKS = "You have no tasks to display!";

	private static final double DEFAULT_VERTICAL_SCROLL_HEIGHT = 0;

	private static final double DEFAULT_EMPTY_TASKS_DISPLAY_HEIGHT = 100;

	@FXML
	private Label displayHeader;
	@FXML 
	VBox displayHolder;
	@FXML
	private ScrollPane displayArea;

	private ArrayList<Task> _displayedTasks;

	public DisplayController() {
	}

	public int getFocusedLine() {
		// TODO to make traversable and return index of selected task
		return -1;
	}
	
	public void setDisplay(TaskList updatedTasks, String displayHeader, ArrayList<Integer> showmoreIndexes) {
		displayHolder.getChildren().clear();
		_displayedTasks = new ArrayList<Task>();
		_displayedTasks.addAll(updatedTasks.getTasks());
		_displayedTasks.addAll(updatedTasks.getArchives());
		
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
		setDisplayScrollTop();
		setDisplayHeader(displayHeader);
	}

	private void showZeroTasksFeedback() {
		Label emptyDisplay = new Label(MESSAGE_ZERO_TASKS);
		emptyDisplay.setPrefHeight(DEFAULT_EMPTY_TASKS_DISPLAY_HEIGHT);
		emptyDisplay.setFont(Main.BOLD_FONT);
		displayHolder.getChildren().add(emptyDisplay);
	}

	private int showStyledTaskView(int currIndex, ArrayList<Integer> showmoreIndexes, int toAddCount, Style style) {
		int addedCount = 0;
		while (addedCount < toAddCount) {
			TaskController newTaskView = new TaskController(_displayedTasks.get(currIndex), currIndex + 1);
			newTaskView.setTaskStyle(style);
			displayHolder.getChildren().add(newTaskView);
			if (showmoreIndexes.contains(Integer.valueOf(currIndex))) {
				TaskDetailsController newTaskDetail = new TaskDetailsController(
						_displayedTasks.get(currIndex));
				displayHolder.getChildren().add(newTaskDetail);
			}
			addedCount++;
			currIndex++;
		}
		return addedCount;
	}

	private void setDisplayScrollTop() {
		displayArea.setVvalue(DEFAULT_VERTICAL_SCROLL_HEIGHT);
		
	}
	
	private void setDisplayHeader(String displayed) {
		displayHeader.setText(displayed);
	}
}
