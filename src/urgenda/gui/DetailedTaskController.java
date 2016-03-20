package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import urgenda.gui.DisplayController.TaskDisplayType;
import urgenda.util.Task;

public class DetailedTaskController extends TaskController {
	
	private static final String PATH_DETAILEDTASKVIEW_FXML = "DetailedTaskView.fxml";

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
	
	public DetailedTaskController(Task task, int index, TaskDisplayType taskDisplayType, boolean showHeader) {
		super(task, index, taskDisplayType, showHeader);
		dateCreatedLabel.setText(formatDetailsDateTime(task.getDateAdded()));
		dateModifiedLabel.setText(formatDetailsDateTime(task.getDateModified()));
		if(task.getLocation() != null) {
			taskLocationLabel.setText(task.getLocation());
		} 
		if(task.getLocation() == null || task.getLocation().equals("")) {
			taskLocationLabel.setText("");
			locationIcon.setVisible(false);
		}
		if(!showHeader) {
			taskPane.setMaxHeight(HEIGHT_DEFAULT_DETAILEDTASK);
		}
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
		System.out.println(text.getLayoutBounds().getWidth());
		System.out.println(taskDescLabel.getPrefWidth());
		if(text.getLayoutBounds().getWidth() >= taskDescLabel.getPrefWidth() * 2.5) {
			taskDescLabel.setMinHeight(HEIGHT_MULTILINE_EXPAND);
			taskPane.setMaxHeight(taskPane.getMaxHeight() + HEIGHT_DETAILED_TASK_EXPAND);
		} 
	}

	private String formatDetailsDateTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy hh:mm:ss");
		return dateTime.format(formatter);
	}
	//TODO consider use for formatting countdown label
//	private String formatCountdown(LocalDateTime deadline) {
//		String formattedDeadline = "";
//		LocalDateTimeDifference timeLeft = new LocalDateTimeDifference(LocalDateTime.now(), deadline);
//
//		if (timeLeft.getDays() > 0) {
//			if (timeLeft.firstIsBefore()) { // current time before deadline
//				formattedDeadline += timeLeft.getDays() + " more day";
//			} else { // deadline past current time
//				formattedDeadline += timeLeft.getDays() + " day";
//			}
//			if (timeLeft.getDays() > 1)
//				formattedDeadline += "s";
//		} else {
//			if (timeLeft.getHours() > 0) {
//				formattedDeadline += timeLeft.getHours() + " hour";
//				if (timeLeft.getHours() > 1)
//					formattedDeadline += "s";
//			}
//			if (timeLeft.getHours() > 0 && timeLeft.getMinutes() > 0) {
//				formattedDeadline += " & ";
//			}
//			if (timeLeft.getMinutes() > 0) {
//				formattedDeadline += timeLeft.getMinutes() + " minute";
//				if (timeLeft.getMinutes() > 1)
//					formattedDeadline += "s";
//			} else {
//				formattedDeadline += "less than a minute";
//			}
//		}
//		return formattedDeadline;
//	}
	
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
	protected void setStyle(String color, String weight) {
		taskIndexLabel.setStyle(color + weight);
		taskDescLabel.setStyle(color + weight);
		taskDateTimeLabel.setStyle(color + weight);
		dateCreatedHeader.setStyle(color);
		dateModifiedHeader.setStyle(color);
		dateCreatedLabel.setStyle(color);
		dateModifiedLabel.setStyle(color);
		taskLocationLabel.setStyle(color + weight);
		
	}
}