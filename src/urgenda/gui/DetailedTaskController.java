package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import urgenda.gui.DisplayController.Direction;
import urgenda.gui.DisplayController.TaskDisplayType;
import urgenda.util.Task;

public class DetailedTaskController extends SimpleTaskController {
	
	private static final String PATH_DETAILEDTASKVIEW_FXML = "DetailedTaskView.fxml";

	private static final double WIDTH_TASK_LABEL_EXPAND_BENCHMARK = 2.5;
	private static final double HEIGHT_DEFAULT_DETAILEDTASK = 70;
	private static final double HEIGHT_MULTILINE_EXPAND = 70;
	private static final double HEIGHT_DETAILED_TASK_EXPAND = 35;
	
	@FXML
	private Label dateCreatedHeader;
	@FXML
	private Label dateModifiedHeader;
	@FXML
	private Label dateCreatedLabel;
	@FXML
	private Label dateModifiedLabel;
	@FXML
	private VBox detailsDisplayArea;
	@FXML
	private ImageView locationIcon;
	@FXML
	private Label taskLocationLabel;
	@FXML
	private VBox dateTimesHolder;
	@FXML
	private HBox locationHolder;
	
	public DetailedTaskController(Task task, int index, TaskDisplayType taskDisplayType, boolean showHeader) {
		super(task, index, taskDisplayType, showHeader);
		initDetailedLabels();
		if(!showHeader) {
			taskPane.setPrefHeight(HEIGHT_DEFAULT_DETAILEDTASK);
		}
	}

	private void initDetailedLabels() {
		dateCreatedLabel.setText(formatDetailsDateTime(_task.getDateAdded()));
		dateModifiedLabel.setText(formatDetailsDateTime(_task.getDateModified()));
		if(_task.getLocation() != null) {
			taskLocationLabel.setText(_task.getLocation());
		}
		if(_task.getLocation() == null || _task.getLocation().equals("")) {
			locationHolder.setVisible(false);
		}
		if (_task.getSlot() != null) { //task has multiple time slots
			taskDateTimeLabel.setText(formatDateTime(_multipleSlotList.get(0).getEarlierDateTime(), _multipleSlotList.get(0).getLaterDateTime()));
			for(int i = 1; i < _multipleSlotList.size(); i++) {
				Label newLabel = new Label(formatDateTime(_multipleSlotList.get(i).getEarlierDateTime(), _multipleSlotList.get(i).getLaterDateTime()));
				dateTimesHolder.getChildren().add(i, newLabel);	
			}
		}
	}
	
	public void resizeOverrunDescLabel() {
		Text text = new Text(taskDescLabel.getText());
		switch(_taskDisplayType) {
		case OVERDUE:
			text.setFont(Main.BOLD_FONT);
			break;
		case FREE_TIME: //fall-through
		case TODAY: //fall-through
		case NORMAL: //fall-through
		case ARCHIVE:
			text.setFont(Main.REGULAR_FONT);
			break;
		}
		if(text.getLayoutBounds().getWidth() >= taskDescLabel.getPrefWidth() * WIDTH_TASK_LABEL_EXPAND_BENCHMARK) {
			taskDescLabel.setMinHeight(HEIGHT_MULTILINE_EXPAND);
			taskPane.setMaxHeight(taskPane.getMaxHeight() + HEIGHT_DETAILED_TASK_EXPAND);
		} 
	}

	private String formatDetailsDateTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
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
	public void traverseMultipleSlot(Direction direction) {
		//disable traverses for detailed tasks
	}
}