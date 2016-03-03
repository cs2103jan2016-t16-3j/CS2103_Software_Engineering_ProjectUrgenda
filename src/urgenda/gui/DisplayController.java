package urgenda.gui;

import java.time.LocalDateTime;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import urgenda.util.StateFeedback;
import urgenda.util.TaskWrapper;

public class DisplayController extends AnchorPane{	
	@FXML
	private TableView<TaskWrapper> displayTable;
	@FXML
	private TableColumn<TaskWrapper, String> idColumn;
	@FXML
	private TableColumn<TaskWrapper, String> dateColumn;	
	@FXML
	private TableColumn<TaskWrapper, String> descColumn;	
	@FXML
	private TableColumn<TaskWrapper, LocalDateTime> startTimeColumn;
	@FXML
	private TableColumn<TaskWrapper, LocalDateTime> endTimeColumn;
	
	private Main main;
	private ObservableList<TaskWrapper> displayedTasks;
	
	public DisplayController() {
	}
	
	public void setDisplay(ObservableList<TaskWrapper> updatedTasks) {
		displayedTasks = updatedTasks;
		
		displayTable.setItems(displayedTasks);
		
		//dateColumn.setCellValueFactory(new PropertyValueFactory<TaskWrapper, String>("_date"));
		descColumn.setCellValueFactory(new PropertyValueFactory<TaskWrapper, String>("_desc"));
		//startTimeColumn.setCellValueFactory(new PropertyValueFactory<TaskWrapper, LocalDateTime>("_startTime"));
		//endTimeColumn.setCellValueFactory(new PropertyValueFactory<TaskWrapper, LocalDateTime>("_endTime"));
		
	}
	public void setMain(Main main) {
		this.main = main;
	}
}
