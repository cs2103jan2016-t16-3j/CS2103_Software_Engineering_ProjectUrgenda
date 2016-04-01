package urgenda.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PopupControl;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.PopupWindow.AnchorLocation;
import urgenda.util.UrgendaLogger;
import javafx.stage.Stage;

public class MainController {

	private static final String TITLE_SAVE_DIRECTORY = "Set Save Directory";
	private static final String KEYWORD_UNDO = "undo";
	private static final String KEYWORD_REDO = "redo";
	private static final String KEYWORD_SHOW_ALL = "home";
	private static final String KEYWORD_CHANGE_SAVE_PATH = "saveto ";
	private static final String KEYWORD_DEMO = "demo";
	private static final String KEYWORD_SHOWMORE = "showmore";
	private static final String PATH_TYPESUGGESTIONS_FXML = "InputSuggestionsView.fxml";

	// Elements loaded using FXML
	@FXML
	private TextField inputBar;
	@FXML
	private TextArea feedbackArea;
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
		} catch (IOException e) {
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, "Error setting up type suggestions popup");
			e.printStackTrace();
		}
		_popupInputSuggestions.show(_main.getPrimaryStage());
		_popupInputSuggestions.setX(_main.getPrimaryStage().getX());
		_popupInputSuggestions.setY(_main.getPrimaryStage().getY() + _main.getPrimaryStage().getHeight());
		anchorSuggestionsToPrimaryStage();
		setInputBarListener();
	}

	private void setInputBarListener() {
		inputBar.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if(newValue) {
					_popupInputSuggestions.show(_main.getPrimaryStage());
				} else {
					_popupInputSuggestions.hide();
				}
			}
			
		});
		
	}

	private void anchorSuggestionsToPrimaryStage() {
		_main.getPrimaryStage().xProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				_popupInputSuggestions.setX((double) newValue);
			}		
		});	
		_main.getPrimaryStage().yProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				_popupInputSuggestions.setY((double) newValue + _main.getPrimaryStage().getHeight());
			}	
		});
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
					feedback = _main.runDemoScreen();
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
		_popupController.updateSuggestions(_main.retriveSuggestions(inputBar.getText()));
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
	private void showmoreMenuListener (ActionEvent e) {
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
	
	public void showMultipleSlotMenuOption(boolean show) {
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
		feedbackArea.setText(feedback);
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
}