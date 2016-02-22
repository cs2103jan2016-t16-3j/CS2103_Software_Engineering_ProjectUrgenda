package urgenda.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;

public class MainController {
	
	//Elements loaded using FXML
	@FXML
	private Label displayLabel;
	@FXML
	private TextField inputBar;
	@FXML
	private TextArea feedbackArea;
	@FXML
	private TableView<String> displayTable;
	@FXML
	private TableColumn<String, String> numberColumn;
	@FXML
	private TableColumn<String, String> dateColumn;

	//Resources to load
	@SuppressWarnings("unused")
	private static final Font LOGO_FONT = Font.loadFont(Main.class.getResourceAsStream("../../resources/Sacramento-Regular.ttf"), 10);
	@SuppressWarnings("unused")
	private static final Font REGULAR_FONT = Font.loadFont(Main.class.getResourceAsStream("../../resources/Montserrat-Light.otf"), 10);
	@SuppressWarnings("unused")
	private static final Font BOLD_FONT = Font.loadFont(Main.class.getResourceAsStream("../../resources/Montserrat-Regular.otf"), 10);
	
	@FXML
	private void initialise() {
		initDisplayArea();
		
	}
	
	private void initDisplayArea() {
		initIdColumn();
		initDateColumn();
		initDescColumn();
		initStartTimeColumn();
		initEndTimeColumn();
	}

	private void initIdColumn() {
		// TODO Auto-generated method stub
		
	}

	private void initDateColumn() {
		// TODO Auto-generated method stub
		
	}

	private void initDescColumn() {
		// TODO Auto-generated method stub
		
	}

	private void initStartTimeColumn() {
		// TODO Auto-generated method stub
		
	}

	private void initEndTimeColumn() {
		// TODO Auto-generated method stub
		
	}

	//tester method for detecting input in inputBar
	public void commandListener(KeyEvent event) {
		KeyCode code = event.getCode();
		if(code == KeyCode.ENTER) {
			feedbackArea.setText(("Entered text: \n" + inputBar.getText()));
		}
	}
	
	public void executeUndo(ActionEvent e){
		//run undo method
		inputBar.clear();
		inputBar.setText("undo!");
	}
	
	public void executeRedo(ActionEvent e){
		//run redo method
		inputBar.clear();
		inputBar.setText("redo!");
	}
	
	public void openHelp(ActionEvent e) {
		//open help menu
		inputBar.clear();
		inputBar.setText("help!");
	}
	
	public void exit(ActionEvent e) {
		//save all edits
		Platform.exit();
        System.exit(0);
	}
}
