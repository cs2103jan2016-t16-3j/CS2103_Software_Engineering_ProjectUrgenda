package urgenda.gui;

import javafx.beans.property.ReadOnlyObjectWrapper;
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
		idColumn.setCellValueFactory(column -> new ReadOnlyObjectWrapper<Number>(displayTable.getItems().indexOf(column.getValue()) + 1));
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().getTaskDate());
		descColumn.setCellValueFactory(cellData -> cellData.getValue().getDesc());
		startTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getStartTime());
		endTimeColumn.setCellValueFactory(cellData -> cellData.getValue().getEndTime());
	}
}