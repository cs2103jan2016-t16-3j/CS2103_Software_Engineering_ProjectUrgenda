package urgenda.gui;

import java.io.IOException;
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
	
	private static final String KEYWORD_UNDO = "undo";
	private static final String KEYWORD_REDO = "redo";
	private static final String KEYWORD_SHOW_ALL = "home";
	private static final String KEYWORD_DELETE = "delete";		
	private static final String KEYWORD_TESTLIST = "dummytest";
	
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
	private HelpController _helpController;
	
	public MainController() {
		_prevCommandLines = new ArrayDeque<String>();
		_nextCommandLines = new ArrayDeque<String>();
	}
	
	@FXML
	private void sceneListener(KeyEvent event) {
		KeyCode code = event.getCode();
		if(code == KeyCode.TAB && !inputBar.isFocused()) {
			inputBar.requestFocus();
		}
		if(code == KeyCode.DELETE && event.isControlDown()) {
			String feedback = _main.handleCommandLine(KEYWORD_DELETE);
			if(feedback != null) {	//null feedback do not change feedback text
				displayFeedback(feedback);
			}
			inputBar.clear();
		}
	}
	
	@FXML
	private void commandLineListener(KeyEvent event) {
		KeyCode code = event.getCode();
			if(code == KeyCode.ENTER && !inputBar.getText().equals("")) {
				while(!_nextCommandLines.isEmpty()) {
					_prevCommandLines.addFirst(_nextCommandLines.getFirst());
					_nextCommandLines.removeFirst();
				}
				_prevCommandLines.addFirst(inputBar.getText());
				String feedback;
				if(inputBar.getText().equals(KEYWORD_TESTLIST)) {
					feedback = _main.setupDummyList();
				} else {
					feedback = _main.handleCommandLine(inputBar.getText());
				}
				if(feedback != null) {	//null feedback do not change feedback text
					displayFeedback(feedback);
				}
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
				} else {
					inputBar.clear();
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
	private void showAllTasks(ActionEvent e) {
		String feedback = _main.handleCommandLine(KEYWORD_SHOW_ALL);
		displayFeedback(feedback);
		inputBar.clear();
	}
	
	@FXML
	private void handleUndo(ActionEvent e){
		String feedback = _main.handleCommandLine(KEYWORD_UNDO);
		displayFeedback(feedback);
		inputBar.clear();
	}
	
	@FXML
	private void handleRedo(ActionEvent e){
		String feedback = _main.handleCommandLine(KEYWORD_REDO);
		displayFeedback(feedback);
		inputBar.clear();
	}
	
	@FXML
	private void handleHelp(ActionEvent event) {
		showHelp();
	}

	public void showHelp() {
		if(_helpController == null) {
			_helpController = new HelpController();
			try {
				_helpController.setupHelpStage(_main.getHelpText());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			_helpController.showHelpStage();
		}
	}
	
	@FXML
	private void exit(ActionEvent e) {
		//TODO save all edits
		Platform.exit();
        System.exit(0);
	}
	
	public void displayFeedback(String feedback) {
		feedbackArea.setText(feedback);
	}
	
	public void setMain(Main main) {
		_main = main;
	}
	
	public DisplayController getDisplayController() {
		return displayAreaController;
	}
	
	public HelpController getHelpController() {
		return _helpController;
	}
}