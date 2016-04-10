//@@author A0131857B
package urgenda.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.logging.Level;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import urgenda.util.UrgendaLogger;
import javafx.stage.Stage;
/**
 * UI component that handles all user interaction regarding input and feedback.
 * 
 * @author KangSoon
 */
public class MainController {

	// Constants
	private static final int WINDOWS_TASKBAR_HEIGHT = 30;
	private static final double DEMO_WINDOW_WIDTH = 800;
	private static final double ALLOWANCE_WINDOW_WIDTH = 5;
	private static final String TITLE_SAVE_DIRECTORY = "Set Save Directory";
	private static final String MESSAGE_NOVICE_USER_VIEW = "Activated novice user view";
	private static final String MESSAGE_ADVANCED_USER_VIEW = "Activated advanced user view";
	private static final String MENUTEXT_ADVANCED_VIEW = "Activate Advanced View";
	private static final String MENUTEXT_NOVICE_VIEW = "Activate Novice View";
	
	// File paths
	private static final String PATH_TYPESUGGESTIONS_FXML = "fxml/InputSuggestionsView.fxml";
	private static final String PATH_DEMOTEXT_FXML = "fxml/DemoTextView.fxml";
	
	// Command keywords
	private static final String KEYWORD_UNDO = "undo";
	private static final String KEYWORD_REDO = "redo";
	public static final String KEYWORD_SHOW_ALL = "home";
	private static final String KEYWORD_CHANGE_SAVE_PATH = "saveto ";
	private static final String KEYWORD_SHOWMORE = "showmore";
	private static final String MESSAGE_WARNING = "Warning: ";
	private static final String DELIMITER_WARNING = "Warning: ";
	private static final String MESSAGE_ERROR = "ERROR: ";
	private static final String DELIMITER_ERROR = "Error: ";
	
	// Colors for styling feedback text
	private static final Color COLOR_ERROR = Color.web("#FA6969");
	private static final Color COLOR_WARNING = Color.web("#FFFF00");

	// Elements loaded using FXML
	@FXML
	private BorderPane backgroundPane;
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
	private MenuItem menuToggleAdvancedView;
	@FXML
	private Label overdueIndicatorLabel;
	@FXML
	private Circle overdueIndicatorCircle;
	@FXML
	private DisplayController displayAreaController;
	
	// Private attributes
	private Main _main;
	private Deque<String> _prevCommandLines = new ArrayDeque<String>();
	private Deque<String> _nextCommandLines = new ArrayDeque<String>();
	private BooleanProperty _isDemo = new SimpleBooleanProperty(false);
	private Popup _popupInputSuggestions;
	private InputSuggestionsPopupController _popupController;
	private BorderPane _demoTextPane;
	private DemoController _demoController;
	private HelpController _helpController;
	
	/**
	 * Creates a MainController object.
	 */
	public MainController() {
		//default constructor
	}
	
	private void initTypeSuggestions() {	
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
	}
	
	private void initDemoPane() {	
		_demoController = new DemoController(this, _main.getDemoText(), _main.getDemoSelectionIndexes());
		FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_DEMOTEXT_FXML));
		loader.setController(_demoController);
		try {
			_demoTextPane = loader.load();
			UrgendaLogger.getInstance().getLogger().log(Level.INFO, "setup of demo text pane successful");
		} catch (IOException e) {
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, "Error setting up demo text pane");
			e.printStackTrace();
		}
		backgroundPane.setRight(_demoTextPane);
		_demoController.init();
	}	
	
	/**
	 * Setup overdue indicators, listeners and menu options at initialization.
	 * 
	 * @param overdueCount total number of overdue tasks
	 */
	public void setup(int overdueCount) {
		// setup overdue indicator
		updateOverdueCount(overdueCount);
		// setup menu for novice and advanced option
		boolean isNovice = _main.getController().getDisplayController().getNoviceSettings();
		if(isNovice) {
			menuToggleAdvancedView.setText(MENUTEXT_ADVANCED_VIEW);
		} else {
			menuToggleAdvancedView.setText(MENUTEXT_NOVICE_VIEW);
		}	
		// listeners for demo view
		_isDemo.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (!newValue.equals(oldValue)) { //check change
					if (newValue.equals(true)) {
						initDemoPane();
						_popupInputSuggestions.hide();
						_main.getPrimaryStage().setWidth(DEMO_WINDOW_WIDTH + ALLOWANCE_WINDOW_WIDTH);
					} else {
						_demoController = null;
						backgroundPane.getChildren().remove(_demoTextPane);
						_main.getPrimaryStage().setWidth(backgroundPane.getPrefWidth() + ALLOWANCE_WINDOW_WIDTH);
						displayFeedback(_main.handleCommandLine(KEYWORD_SHOW_ALL));	//return to all tasks view
					}
				}
			}
		});
		
		// listeners for suggestions popup
		inputBar.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!_isDemo.get()) {
					showSuggestionsPopup();
				} else {
					_popupInputSuggestions.hide();
				}
			}		
		});
		ChangeListener<Number> windowPosChangeListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if(getSuggestionsPopup() != null) {
					_popupInputSuggestions.hide();					
				}
			}		
		};
		_main.getPrimaryStage().xProperty().addListener(windowPosChangeListener);	
		_main.getPrimaryStage().yProperty().addListener(windowPosChangeListener);
	}
	
	/**
	 * Displays popup for input suggestions.
	 */
	public void showSuggestionsPopup() {
		if(_popupInputSuggestions == null) {
			initTypeSuggestions();
		}
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
	
	/**
	 * Hides popup for input suggestions.
	 */
	public void hideSuggestionsPopup() {
		_popupInputSuggestions.hide();
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
	private void sceneListener(KeyEvent e) {
		KeyCode code = e.getCode();
		if (code == KeyCode.TAB) {
			if (!inputBar.isFocused()) {
				inputBar.requestFocus();
			}
			if(_isDemo.get()) {
				if(e.isShiftDown()) {
					_demoController.prevPart();
				} else {
					_demoController.nextPart();
				}
			} 
		}
	}

	@FXML
	private void commandLineListener(KeyEvent e) {
		KeyCode code = e.getCode();
		if(code == KeyCode.TAB) {
			sceneListener(e); //pass control to scene
		} else if (code == KeyCode.ENTER) {
			if (!inputBar.getText().trim().equals("") && !inputBar.getText().equals("")) {
				while (!_nextCommandLines.isEmpty()) {
					_prevCommandLines.addFirst(_nextCommandLines.getFirst());
					_nextCommandLines.removeFirst();
				}
				_prevCommandLines.addFirst(inputBar.getText());
				String feedback = _main.handleCommandLine(inputBar.getText());
				if (feedback != null) { // null does not change feedback text
					displayFeedback(feedback);
				}
				inputBar.clear();
			} else { // inputbar has whitespaces
				inputBar.clear();
			}
		} else if (code == KeyCode.UP && !e.isControlDown()) {
			if (!_prevCommandLines.isEmpty()) {
				if (inputBar.getText().equals(_prevCommandLines.peekFirst())
						&& _prevCommandLines.size() > 1) {
					_nextCommandLines.addFirst(_prevCommandLines.getFirst());
					_prevCommandLines.removeFirst();
				}
				inputBar.setText(_prevCommandLines.getFirst());
			}
		} else if (code == KeyCode.DOWN && !e.isControlDown()) {
			if (!_nextCommandLines.isEmpty()) {
				_prevCommandLines.addFirst(_nextCommandLines.getFirst());
				_nextCommandLines.removeFirst();
				inputBar.setText(_prevCommandLines.getFirst());
			} else {
				inputBar.clear();
			}
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
	private void toggleAdvancedViewListener(ActionEvent e) {
		boolean isNovice = _main.getController().getDisplayController().getNoviceSettings();
		if(isNovice) {
			menuToggleAdvancedView.setText(MENUTEXT_NOVICE_VIEW);
		} else {
			menuToggleAdvancedView.setText(MENUTEXT_ADVANCED_VIEW);
		}
		_main.getController().getDisplayController().setNoviceSettings(!isNovice);
		_main.changeNoviceSettings(!isNovice);
		_main.handleCommandLine(KEYWORD_SHOW_ALL);
		if(isNovice) {
			displayFeedback(MESSAGE_ADVANCED_USER_VIEW);
		} else {
			displayFeedback(MESSAGE_NOVICE_USER_VIEW);
		}
		inputBar.clear();
	}

	@FXML
	private void handleHelp(ActionEvent e) {
		e.consume();
		showHelp();
	}
	
	/**
	 * Sets up and displays help window.
	 */
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
	
	@FXML
	private void exit(ActionEvent e) {
		Platform.exit();
		System.exit(0);
	}
	
	/**
	 * Toggles showing of multiple slot options in menu.
	 * 
	 * @param show boolean to show multiple slot options or not
	 */
	public void toggleMultipleSlotMenuOption(boolean show) {
		multipleSlotSeparator.setVisible(show);
		menuPrevMultipleSlot.setVisible(show);
		menuNextMultipleSlot.setVisible(show);
	}
	/**
	 * Displays feedback from user interaction and styles any warnings or errors.
	 * 
	 * @param feedback feedback text
	 */
	public void displayFeedback(String feedback) {
		Text feedbackText = null;
		ArrayList<Text> warningTexts = new ArrayList<Text>();
		ArrayList<Text> errorTexts = new ArrayList<Text>();
		if (feedback.contains(DELIMITER_WARNING)) {
			feedbackText = styleKeywords(feedback, warningTexts, DELIMITER_WARNING, MESSAGE_WARNING, COLOR_WARNING);
		} else if (feedback.contains(DELIMITER_ERROR)) {
			feedbackText = styleKeywords(feedback, errorTexts, DELIMITER_ERROR, MESSAGE_ERROR, COLOR_ERROR);
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

	private Text styleKeywords(String feedback, ArrayList<Text> texts, String delim, String message, Color color) {
		Text feedbackText;
		@SuppressWarnings("unchecked")
		ArrayList<String> delimitedFeedback  = new ArrayList<String>(Arrays.asList(feedback.split(delim)));
		feedbackText = new Text(delimitedFeedback.get(0));
		for (int i = 1; i < delimitedFeedback.size(); i++) {
			Text text = new Text(message + delimitedFeedback.get(i));
			text.setFill(color);
			texts.add(text);
		}
		return feedbackText;
	}

	/**
	 * Updates the number of overdue tasks shown at the icon of Urgenda.
	 * 
	 * @param overdueCount current total number of overdue tasks
	 */
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
	
	/**
	 * Sets the reference to the Main UI object instance for this class and displayController instance.
	 * 
	 * @param main Main UI object instance
	 */
	public void setMain(Main main) {
		_main = main;
		displayAreaController.setMain(_main);
	}
	
	/**
	 * Returns DisplayController instance.
	 * 
	 * @return DisplayController instance.
	 */
	public DisplayController getDisplayController() {
		return displayAreaController;
	}
	
	/**
	 * Returns HelpController instance.
	 * 
	 * @return HelpController instance.
	 */
	public HelpController getHelpController() {
		return _helpController;
	}
	
	/**
	 * Returns DemoController instance.
	 * 
	 * @return DemoController instance.
	 */
	public DemoController getDemoController() {
		return _demoController;
	}
	
	/**
	 * Returns Popup instance for suggestions.
	 * 
	 * @return Popup instance for suggestions
	 */
	public Popup getSuggestionsPopup(){
		return _popupInputSuggestions;
	}
	
	/**
	 * Sets the demo state.
	 * 
	 * @param demo boolean for demo state
	 */
	public void setDemo(boolean demo) {
		_isDemo.set(demo);
	}
	
	/**
	 * Returns whether demo state is activated or not.
	 * 
	 * @return boolean whether demo state is activated or not
	 */
	public boolean isDemo() {
		return _isDemo.get();
	}
}