//@@author A0131857B
package urgenda.gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import urgenda.gui.DisplayController.Direction;
import urgenda.gui.DisplayController.TaskDisplayType;
import urgenda.util.Task;

/**
 * UI component and child of SimpleTaskController, invoked when showing a
 * detailed task panel for a single task.
 * 
 * @author KangSoon
 */
public class DetailedTaskController extends SimpleTaskController {

	// Constants
	private static final String FORMATTER_DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

	// File paths
	private static final String PATH_DETAILEDTASKVIEW_FXML = "fxml/DetailedTaskView.fxml";

	// Elements loaded using FXML
	@FXML
	private Text dateCreatedHeader;
	@FXML
	private Text dateModifiedHeader;
	@FXML
	private Text dateCreatedText;
	@FXML
	private Text dateModifiedText;
	@FXML
	private VBox detailsDisplayArea;
	@FXML
	private ImageView locationIcon;
	@FXML
	private Text taskLocationText;
	@FXML
	private VBox dateTimesHolder;
	@FXML
	private HBox locationHolder;

	/**
	 * Creates a DetailedTaskController using the given task.
	 * 
	 * @param task
	 *            task to show details for
	 * @param index
	 *            index of the task
	 * @param taskDisplayType
	 *            the enumerated type of the task
	 * @param showHeader
	 *            boolean to show headers for the task or not
	 */
	public DetailedTaskController(Task task, int index, TaskDisplayType taskDisplayType, boolean showHeader) {
		super(task, index, taskDisplayType, showHeader); // invoke SimpleTaskController constructor
		multipleSlotCounter.setVisible(false);
		initDetailedLabels();
	}

	private void initDetailedLabels() {
		dateCreatedText.setText(formatDetailsDateTime(_task.getDateAdded()));
		dateModifiedText.setText(formatDetailsDateTime(_task.getDateModified()));
		if (_task.getLocation() != null) {
			taskLocationText.setText(_task.getLocation());
		}
		if (_task.getLocation() == null || _task.getLocation().isEmpty()) {
			locationHolder.setVisible(false);
		}
		if (_task.getSlot() != null) { // task has multiple time slots
			taskDateTimeText.setText(formatDateTime(_multipleSlotList.get(0).getEarlierDateTime(),
					_multipleSlotList.get(0).getLaterDateTime()));
			for (int i = 1; i < _multipleSlotList.size(); i++) {
				Text newText = new Text(formatDateTime(_multipleSlotList.get(i).getEarlierDateTime(),
						_multipleSlotList.get(i).getLaterDateTime()));
				dateTimesHolder.getChildren().add(i, newText);
			}
		}
	}

	private String formatDetailsDateTime(LocalDateTime dateTime) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATTER_DATE_TIME_FORMAT);
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
		// disable traverses for detailed tasks
	}
}