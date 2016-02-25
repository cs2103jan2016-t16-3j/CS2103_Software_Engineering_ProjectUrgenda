package urgenda.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
//import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import urgenda.util.TaskWrapper;

public class DisplayView{
	
	//Elements loaded using FXML
	@FXML
	private Label displayLabel;
	@FXML
	private TableView<TaskWrapper> displayTable;
	@FXML
	private TableColumn<TaskWrapper, Number> idColumn;
	@FXML
	private TableColumn<TaskWrapper, String> dateColumn;	
	@FXML
	private TableColumn<TaskWrapper, String> descColumn;	
	@FXML
	private TableColumn<TaskWrapper, String> startTimeColumn;
	@FXML
	private TableColumn<TaskWrapper, String> endTimeColumn;
	
	public DisplayView() {
		//initDisplayArea();
	}

	private void initDisplayArea() {
		initIdColumn();
		initDateColumn();
		initDescColumn();
		initStartTimeColumn();
		initEndTimeColumn();
	}

	private void initIdColumn() {
		//TODO: SET ID
	}

	private void initDateColumn() {
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().getTaskDate());
		
	}

	private void initDescColumn() {
		descColumn.setCellValueFactory(cellData -> cellData.getValue().getDesc());
		
	}

	private void initStartTimeColumn() {
		startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getStartTime());
		
	}

	private void initEndTimeColumn() {
		endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getEndTime());
		
	}
}
