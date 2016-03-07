package urgenda.gui;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

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
	
	private Main _main;
	private Deque<String> _prevCommandLines;
	private Deque<String> _nextCommandLines;
	
	public MainController() {
		_prevCommandLines = new ArrayDeque<String>();
		_nextCommandLines = new ArrayDeque<String>();
	}
	
	@FXML
	private void commandLineListener(KeyEvent event) {
		KeyCode code = event.getCode();
			if(code == KeyCode.ENTER) {
				String feedback = _main.handleCommandLine(inputBar.getText());
				while(!_nextCommandLines.isEmpty()) {
					_prevCommandLines.addFirst(_nextCommandLines.getFirst());
					_nextCommandLines.removeFirst();
				}
				_prevCommandLines.addFirst(inputBar.getText());
				displayFeedback(feedback, false);
				inputBar.clear();
				return;
			}
			if(code == KeyCode.UP && !event.isControlDown()) {
				if(!_prevCommandLines.isEmpty()) {
					if(inputBar.getText().equals(_prevCommandLines.peekFirst()) && _prevCommandLines.size() > 1) {
						_nextCommandLines.addFirst(_prevCommandLines.getFirst());
						_prevCommandLines.removeFirst();
					}
					inputBar.setText(_prevCommandLines.getFirst());
				}
				return;
			}
			if(code == KeyCode.DOWN && !event.isControlDown()) {
				if(!_nextCommandLines.isEmpty()) {
					_prevCommandLines.addFirst(_nextCommandLines.getFirst());
					_nextCommandLines.removeFirst();
					inputBar.setText(_prevCommandLines.getFirst());
				}
				return;
			}
	}
	
	@FXML
	private void taskToggleDownListener(ActionEvent e) {
		displayAreaController.traverseTasks(DisplayController.Direction.DOWN);
	}
	
	@FXML
	private void taskToggleUpListener(ActionEvent e) {
		displayAreaController.traverseTasks(DisplayController.Direction.UP);
	}
	
	@FXML
	private void handleUndo(ActionEvent e){
		String feedback = _main.handleCommandLine("undo");
		displayFeedback(feedback, false);
	}
	
	@FXML
	private void handleRedo(ActionEvent e){
		String feedback = _main.handleCommandLine("redo");
		displayFeedback(feedback, false);
	}
	
	@FXML
	private void handleHelp(ActionEvent e) {
		_main.invokeHelpSplash();
		//TODO remove when help is enabled
		displayFeedback("Sorry! Help is currently unavailable!", false);
	}
	
	@FXML
	private void handleAboutUrgenda(ActionEvent e) {
		_main.invokeUrgendaSplash();
		//TODO remove when urgenda splash is enabled
		displayFeedback("Sorry! About Urgenda is currently unavailable!", true);
	}
	
	@FXML
	private void exit(ActionEvent e) {
		//TODO save all edits
		Platform.exit();
        System.exit(0);
	}
	
	public void displayFeedback(String feedback, boolean isWarning) {
		feedbackArea.setText(feedback);
		if(isWarning) {
			feedbackArea.setStyle(ERROR_TEXT_FILL);
		} else {
			feedbackArea.setStyle(NORMAL_TEXT_FILL);
		}
	}
	
	public void setUIMain(Main ui) {
		_main = ui;
	}
	
	public DisplayController getDisplayController() {
		return displayAreaController;
	}
}