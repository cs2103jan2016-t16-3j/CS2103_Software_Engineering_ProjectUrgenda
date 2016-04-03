package urgenda.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.logging.Level;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import urgenda.util.UrgendaLogger;
import javafx.stage.Stage;

public class MainController {
	
	//constants for MainController
	private static final int WINDOWS_TASKBAR_HEIGHT = 30;
	
	private static final String PATH_TYPESUGGESTIONS_FXML = "fxml/InputSuggestionsView.fxml";
	private static final String TITLE_SAVE_DIRECTORY = "Set Save Directory";
	private static final String KEYWORD_UNDO = "undo";
	private static final String KEYWORD_REDO = "redo";
	private static final String KEYWORD_SHOW_ALL = "home";
	private static final String KEYWORD_CHANGE_SAVE_PATH = "saveto ";
	private static final String KEYWORD_DEMO = "demo";
	private static final String KEYWORD_SHOWMORE = "showmore";
	private static final String MESSAGE_WARNING = "Warning: ";
	private static final String DELIMITER_WARNING = "Warning: ";
	private static final String MESSAGE_ERROR = "ERROR: ";
	private static final String DELIMITER_ERROR = "Error: ";

	private static final Color COLOR_ERROR = Color.web("#FA6969");
	private static final Color COLOR_WARNING = Color.web("#FFFF00");

	// Elements loaded using FXML
	@FXML
	private TextField inputBar;
	@FXML
	private TextFlow feedbackArea;
	@FXML
	private Parent displayArea;
	@FXML
	private SeparatorMenuItem multipleSlotSeparator;
	@FXML
	private MenuItem menuPrevMultipleSlot;
	@FXML
	private MenuItem menuNextMultipleSlot;
	@FXML
	private Label overdueIndicatorLabel;
	@FXML
	private Circle overdueIndicatorCircle;
	@FXML
	private DisplayController displayAreaController;

	private Main _main;
	private Deque<String> _prevCommandLines;
	private Deque<String> _nextCommandLines;
	private HelpController _helpController;
	private Popup _popupInputSuggestions;
	private InputSuggestionsPopupController _popupController;
	
	public MainController() {
		_prevCommandLines = new ArrayDeque<String>();
		_nextCommandLines = new ArrayDeque<String>();
	}
	
	public void setupTypeSuggestions() {	
		_popupInputSuggestions = new Popup();
		_popupController = new InputSuggestionsPopupController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TYPESUGGESTIONS_FXML));
		loader.setController(_popupController);
		try {
			_popupInputSuggestions.getContent().add((Parent)loader.load());
			UrgendaLogger.getInstance().getLogger().log(Level.INFO, "setup of type suggestions popup successful");
		} catch (IOException e) {
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, "Error setting up type suggestions popup");
			e.printStackTrace();
		}
		_popupInputSuggestions.setX(_main.getPrimaryStage().getX());
		_popupInputSuggestions.setY(_main.getPrimaryStage().getY() + _main.getPrimaryStage().getHeight());
		if(inputBar.getText().isEmpty()) {
			_popupInputSuggestions.hide();
		} else {
			_popupInputSuggestions.show(_main.getPrimaryStage());
		}
		_popupController.updateSuggestions(_main.retrieveSuggestions(inputBar.getText()));
		setListeners();
	}

	private void setListeners() {
		inputBar.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				showSuggestionsPopup();
			}		
		});
		ChangeListener<Number> windowPosChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					_popupInputSuggestions.hide();
			}		
		};
		_main.getPrimaryStage().xProperty().addListener(windowPosChangeListener);	
		_main.getPrimaryStage().yProperty().addListener(windowPosChangeListener);
	}
	
	private void showSuggestionsPopup() {
		_popupController.updateSuggestions(_main.retrieveSuggestions(inputBar.getText()));
		if(!windowOutOfBounds()) {
			if(inputBar.getText().isEmpty()) {
				_popupInputSuggestions.hide();
			} else {
				_popupInputSuggestions.show(_main.getPrimaryStage());
			}
			_popupInputSuggestions.setX(_main.getPrimaryStage().getX());
			_popupInputSuggestions.setY(_main.getPrimaryStage().getY() + _main.getPrimaryStage().getHeight());
		}
	}
	
	private boolean windowOutOfBounds() {
		Bounds bounds = _main.computeAllScreenBounds();
		double x = _main.getPrimaryStage().getX();
		double y = _main.getPrimaryStage().getY();
		double width = _main.getPrimaryStage().getWidth();
		double height = _main.getPrimaryStage().getHeight() + _popupInputSuggestions.getHeight() + WINDOWS_TASKBAR_HEIGHT;
		
		if (x < bounds.getMinX()) {
			return true;
		}
		if (x + width > bounds.getMaxX()) {
			return true;
		}
		if (y + height > bounds.getMaxY()) {
			return true;
		}
		return false;
	}

	@FXML
	private void sceneListener(KeyEvent event) {
		KeyCode code = event.getCode();
		if (code == KeyCode.TAB && !inputBar.isFocused()) {
			inputBar.requestFocus();
		}
	}

	@FXML
	private void commandLineListener(KeyEvent event) {
		KeyCode code = event.getCode();
		if (code == KeyCode.ENTER) {
			if (!inputBar.getText().trim().equals("") && !inputBar.getText().equals("")) {
				while (!_nextCommandLines.isEmpty()) {
					_prevCommandLines.addFirst(_nextCommandLines.getFirst());
					_nextCommandLines.removeFirst();
				}
				_prevCommandLines.addFirst(inputBar.getText());
				String feedback;
				if (inputBar.getText().equalsIgnoreCase(KEYWORD_DEMO)) {
					feedback = _main.activateDemoScreen();
				} else {
					feedback = _main.handleCommandLine(inputBar.getText());
				}
				if (feedback != null) { // null feedback do not change feedback
										// text
					displayFeedback(feedback);
				}
				inputBar.clear();
			} else { // inputbar has whitespaces
				inputBar.clear();
			}
		} else if (code == KeyCode.UP && !event.isControlDown()) {
			if (!_prevCommandLines.isEmpty()) {
				if (inputBar.getText().equals(_prevCommandLines.peekFirst())
						&& _prevCommandLines.size() > 1) {
					_nextCommandLines.addFirst(_prevCommandLines.getFirst());
					_prevCommandLines.removeFirst();
				}
				inputBar.setText(_prevCommandLines.getFirst());
			}
		} else if (code == KeyCode.DOWN && !event.isControlDown()) {
			if (!_nextCommandLines.isEmpty()) {
				_prevCommandLines.addFirst(_nextCommandLines.getFirst());
				_nextCommandLines.removeFirst();
				inputBar.setText(_prevCommandLines.getFirst());
			} else {
				inputBar.clear();
			}
		} else if (code == KeyCode.LEFT && event.isControlDown()) {	//hard-coded since FXML accelerator doesn't work
			displayAreaController.executeTraverse(DisplayController.Direction.LEFT);
		} else if (code == KeyCode.RIGHT && event.isControlDown()) { //hard-coded since FXML accelerator doesn't work
			displayAreaController.executeTraverse(DisplayController.Direction.RIGHT);
		}
	}

	@FXML
	private void savePathChangeListener(ActionEvent e) {
		String feedback;
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(TITLE_SAVE_DIRECTORY);
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(".txt files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setInitialDirectory(_main.getSaveDirectory());
		File selectedFileDirectory = fileChooser.showSaveDialog(new Stage());
		if (selectedFileDirectory != null) {
			feedback = _main.handleCommandLine(KEYWORD_CHANGE_SAVE_PATH + selectedFileDirectory.getAbsolutePath());
			displayFeedback(feedback);
			inputBar.clear();
		}
	}
	
	@FXML
	private void minimiseWindowListener(ActionEvent e) {
		_main.getPrimaryStage().setIconified(true);
	}
	
	@FXML
	private void taskToggleDownListener(ActionEvent e) {
		displayAreaController.executeTraverse(DisplayController.Direction.DOWN);
	}

	@FXML
	private void taskToggleUpListener(ActionEvent e) {
		displayAreaController.executeTraverse(DisplayController.Direction.UP);
	}

	@FXML
	private void multipleSlotToggleLeftListener(ActionEvent e) {
		displayAreaController.executeTraverse(DisplayController.Direction.LEFT);
	}

	@FXML
	private void multipleSlotToggleRightListener(ActionEvent e) {
		displayAreaController.executeTraverse(DisplayController.Direction.RIGHT);
	}
	
	@FXML
	private void showmoreListener (ActionEvent e) {
		_main.handleCommandLine(KEYWORD_SHOWMORE);
	}

	@FXML
	private void showAllTasks(ActionEvent e) {
		String feedback = _main.handleCommandLine(KEYWORD_SHOW_ALL);
		displayFeedback(feedback);
		inputBar.clear();
	}

	@FXML
	private void handleUndo(ActionEvent e) {
		String feedback = _main.handleCommandLine(KEYWORD_UNDO);
		displayFeedback(feedback);
		inputBar.clear();
	}

	@FXML
	private void handleRedo(ActionEvent e) {
		String feedback = _main.handleCommandLine(KEYWORD_REDO);
		displayFeedback(feedback);
		inputBar.clear();
	}

	@FXML
	private void handleHelp(ActionEvent event) {
		showHelp();
	}

	public void showHelp() {
		if (_helpController == null) {
			_helpController = new HelpController();
			try {
				_helpController.setupHelpStage(_main.getHelpText());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			_helpController.showHelpStage();
		}
	}
	
	public void toggleMultipleSlotMenuOption(boolean show) {
		multipleSlotSeparator.setVisible(show);
		menuPrevMultipleSlot.setVisible(show);
		menuNextMultipleSlot.setVisible(show);
	}

	@FXML
	private void exit(ActionEvent e) {
		Platform.exit();
		System.exit(0);
	}

	public void displayFeedback(String feedback) {
		Text feedbackText = null;
		ArrayList<Text> warningTexts = new ArrayList<Text>();
		ArrayList<Text> errorTexts = new ArrayList<Text>();
		if (feedback.contains(DELIMITER_WARNING)) {
			String delim = DELIMITER_WARNING;
			@SuppressWarnings("unchecked")
			ArrayList<String> delimitedFeedback  = new ArrayList<String>(Arrays.asList(feedback.split(delim)));
			feedbackText = new Text(delimitedFeedback.get(0));
			for (int i = 1; i < delimitedFeedback.size(); i++) {
				Text warningText = new Text(MESSAGE_WARNING + delimitedFeedback.get(i));
				warningText.setFill(COLOR_WARNING);
				warningTexts.add(warningText);
			}
		} else if (feedback.contains("Error:")) {
			String delim = DELIMITER_ERROR;
			@SuppressWarnings("unchecked")
			ArrayList<String> delimitedFeedback  = new ArrayList<String>(Arrays.asList(feedback.split(delim)));
			feedbackText = new Text(delimitedFeedback.get(0));
			for (int i = 1; i < delimitedFeedback.size(); i++) {
				Text errorText = new Text(MESSAGE_ERROR + delimitedFeedback.get(i));
				errorText.setFill(COLOR_ERROR);
				errorTexts.add(errorText);
			}
		} else {
			feedbackText = new Text(feedback);
			
		}
		feedbackText.setFill(Color.WHITE);
		feedbackArea.getChildren().clear();
		feedbackArea.getChildren().add(feedbackText);
		if (warningTexts.size() > 0) {
			for(Text text: warningTexts) {
				feedbackArea.getChildren().add(text);
			}
		}
		if (errorTexts.size() > 0) {
			for(Text text: errorTexts) {
				feedbackArea.getChildren().add(text);
			}
		}
	}

	public void updateOverdueCount(int overdueCount) {
		if (overdueCount <= 0) {
			overdueIndicatorCircle.setVisible(false);
			overdueIndicatorLabel.setVisible(false);
		} else {
			overdueIndicatorCircle.setVisible(true);
			overdueIndicatorLabel.setVisible(true);
			if (overdueCount <= 99) {
				overdueIndicatorLabel.setText(String.valueOf(overdueCount));
			} else {
				overdueIndicatorLabel.setText("99+");
			}
		}
	}
	
	public void setMain(Main main) {
		_main = main;
		displayAreaController.setMain(_main);
	}

	public DisplayController getDisplayController() {
		return displayAreaController;
	}

	public HelpController getHelpController() {
		return _helpController;
	}
}