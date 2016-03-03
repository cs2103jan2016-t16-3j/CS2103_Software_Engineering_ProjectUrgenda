package urgenda.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class MainController {
	
	private static final String ERROR_TEXT_FILL = "-fx-text-fill: #FF1900";
	private static final String NORMAL_TEXT_FILL = "-fx-text-fill: white";
	
	//Elements loaded using FXML
	@FXML
	private TextField inputBar;
	@FXML
	private TextArea feedbackArea;
	@FXML
	private Parent displayArea;
	@FXML
	private DisplayController displayAreaController;
	
	private Main _UI;
	
	public MainController() {
		
	}
	public void displayFeedback(String feedback, boolean isWarning) {
		feedbackArea.setText(feedback);
		if(isWarning) {
			feedbackArea.setStyle(ERROR_TEXT_FILL);
		} else {
			feedbackArea.setStyle(NORMAL_TEXT_FILL);
		}
	}
	
	@FXML
	private void commandLineListener(KeyEvent event) {
		KeyCode code = event.getCode();
		if(code == KeyCode.ENTER) {
			if(inputBar.getText() != "") {
				String feedback = _UI.handleCommandLine(inputBar.getText());
				displayFeedback(feedback, false);
				inputBar.clear();
			}
		}
	}
	
	@FXML
	private void handleUndo(ActionEvent e){
		@SuppressWarnings("unused")
		String feedback = _UI.handleCommandLine("undo");
		//change below to use feedback string
		displayFeedback("Sorry! Undo is currently unavailable!", false);
	}
	
	@FXML
	private void handleRedo(ActionEvent e){
		@SuppressWarnings("unused")
		String feedback = _UI.handleCommandLine("redo");
		//change below to use feedback string
		displayFeedback("Sorry! Redo is currently unavailable!", false);
	}
	
	@FXML
	private void handleHelp(ActionEvent e) {
		_UI.invokeHelpSplash();
		displayFeedback("Sorry! Help is currently unavailable!", false);
	}
	
	@FXML
	private void handleAboutUrgenda(ActionEvent e) {
		_UI.invokeUrgendaSplash();
		displayFeedback("Sorry! About Urgenda is currently unavailable!", true);
	}
	
	@FXML
	private void exit(ActionEvent e) {
		//save all edits
		Platform.exit();
        System.exit(0);
	}
	
	public void setUIMain(Main ui) {
		_UI = ui;
	}
	public DisplayController getDisplayController() {
		return displayAreaController;
	}
}