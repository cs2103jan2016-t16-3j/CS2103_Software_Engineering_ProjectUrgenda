//@@author A0131857B
package urgenda.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import urgenda.logic.Logic;
import urgenda.util.DemoStateFeedback;
import urgenda.util.StateFeedback;
import urgenda.util.SuggestFeedback;
import urgenda.util.TaskList;
import urgenda.util.UrgendaLogger;

/**
 * The main class in the UI component, in charge of initializing the program and
 * the sub-components of UI which are MainController and DisplayController.
 *
 * @author KangSoon
 */
public class Main extends Application {

	private static final String APP_NAME = "Urgenda";

	// File paths
	private static final String PATH_GUI_FXML = "fxml/MainView.fxml";
	public static final String PATH_REGULAR_FONT = new String("../../resources/Montserrat-Light.otf");
	public static final String PATH_BOLD_FONT = new String("../../resources/Montserrat-Regular.otf");
	public static final String PATH_LIGHT_FONT = new String("../../resources/Montserrat-UltraLight.ttf");

	// Display header texts
	private static final String HEADER_ALL_TASKS = "ALL TASKS";
	private static final String HEADER_ALL_WITH_COMPLETED_TASKS = "ALL TASKS WITH COMPLETED TASKS";
	private static final String HEADER_FREE_TIME = "AVAILABLE TIME PERIODS";
	private static final String HEADER_SEARCH_RESULTS = "SEARCH RESULTS";
	private static final String HEADER_ARCHIVE_TASKS = "ARCHIVE TASKS";
	private static final String HEADER_MULTIPLE_MATCHES = "MULTIPLE MATCHES";

	private static final int DEFAULT_REGULAR_FONT_SIZE = 20;
	private static final int DEFAULT_BOLD_FONT_SIZE = 20;
	private static final int DEFAULT_LIGHT_FONT_SIZE = 20;

	// Fonts
	public static final Font REGULAR_FONT = Font.loadFont(Main.class.getResourceAsStream(PATH_REGULAR_FONT),
			DEFAULT_REGULAR_FONT_SIZE);
	public static final Font BOLD_FONT = Font.loadFont(Main.class.getResourceAsStream(PATH_BOLD_FONT),
			DEFAULT_BOLD_FONT_SIZE);
	public static final Font LIGHT_FONT = Font.loadFont(Main.class.getResourceAsStream(PATH_LIGHT_FONT),
			DEFAULT_LIGHT_FONT_SIZE);

	// Private attributes
	private BorderPane _rootLayout;
	private Scene _scene;
	private MainController _mainController;
	private DisplayController _displayController;
	private Logic _logic;
	private static Stage _primaryStage;
	private StateFeedback _currState;

	@Override
	public void start(Stage primaryStage) {
		initLogger();
		initLogicComponent();
		initRootLayout();
		initDisplay();
		initStage(primaryStage);
		initFeatures();
	}

	private void initLogger() {
		UrgendaLogger.getInstance().getLogger().log(Level.INFO, "Successful initialisation of logger");
	}

	private void initLogicComponent() {
		_logic = Logic.getInstance();
		UrgendaLogger.getInstance().getLogger().log(Level.INFO, "Successful initialisation of logic");
	}

	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(PATH_GUI_FXML));
			_rootLayout = loader.load();
			_mainController = loader.getController();
			_mainController.setMain(this);
		} catch (IOException e) {
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE,
					"Initialisation of root layout failed!");
			e.printStackTrace();
		}
		UrgendaLogger.getInstance().getLogger().log(Level.INFO, "Successful initialisation of root layout");
	}

	private void initDisplay() {
		_displayController = _mainController.getDisplayController();
		_displayController.setNoviceSettings(_logic.getNoviceSettings());
		_currState = retrieveStartupState();
		TaskList updatedTasks = _currState.getAllTasks();
		String displayHeader = createDisplayHeader(_currState);
		ArrayList<Integer> detailedIndexes = _currState.getDetailedIndexes();
		int displayPos = _currState.getDisplayPosition();
		boolean isShowNoviceHeaders = _logic.getNoviceSettings();
		_displayController.initDisplay(updatedTasks, displayHeader, detailedIndexes, displayPos, isShowNoviceHeaders);
		UrgendaLogger.getInstance().getLogger().log(Level.INFO, "Successful initialisation of display view");
	}

	private void initStage(Stage primaryStage) {
		_scene = new Scene(_rootLayout);
		_primaryStage = primaryStage;
		_primaryStage.initStyle(StageStyle.DECORATED);
		_primaryStage.setTitle(APP_NAME);
		_primaryStage.setResizable(false);
		_primaryStage.setScene(_scene);
		_primaryStage.sizeToScene();
		_primaryStage.show();
		UrgendaLogger.getInstance().getLogger().log(Level.INFO,
				"Successful initialisation of Urgenda window");
	}

	private void initFeatures() {
		// setup menu items and listeners for MainController
		_mainController.setup(_currState.getOverdueCount());
		// setup window focused listener
		_primaryStage.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
					Boolean newValue) {
				if (newValue) {
					_mainController.showSuggestionsPopup();
				} else {
					if (_mainController.getSuggestionsPopup() != null) {
						_mainController.hideSuggestionsPopup();
					}
				}

			}
		});
	}

	private StateFeedback retrieveStartupState() {
		StateFeedback state = _logic.retrieveStartupState();
		_mainController.displayFeedback(state.getFeedback());
		return state;
	}

	/**
	 * Calls the Logic component to handle user command line input.
	 * 
	 * @param commandLine
	 *            registered input command line by user
	 * @param isDemo
	 *            boolean indicating whether UI is currently in demo mode or not
	 * @return feedback to be displayed to the user
	 */
	protected String handleCommandLine(String commandLine) {
		_currState = _logic.executeCommand(commandLine, _displayController.getSelectedTaskIndex());
		TaskList updatedTasks = _currState.getAllTasks();
		String displayHeader = createDisplayHeader(_currState);
		ArrayList<Integer> detailedIndexes = _currState.getDetailedIndexes();
		int displayPos = _currState.getDisplayPosition();
		boolean isShowFreeTime = false;
		switch (_currState.getState()) {
		case HIDE:
			_primaryStage.setIconified(true);
			break;
		case SHOW_HELP:
			_mainController.showHelp();
			break;
		case EXIT:
			quit();
			break;
		case DEMO:
			return activateDemoScreen();
		case FIND_FREE:
			isShowFreeTime = true;
			// fall-through
		case ALL_TASKS:
			_mainController.setDemo(false);
			// fall-through
		default:
			_displayController.setDisplay(updatedTasks, displayHeader, detailedIndexes, displayPos, isShowFreeTime, false);
			break;
		}
		_mainController.updateOverdueCount(_currState.getOverdueCount());
		return _currState.getFeedback();
	}

	private String createDisplayHeader(StateFeedback state) {
		String display = "";
		switch (state.getState()) {
		case MULTIPLE_MATCHES:
			display = HEADER_MULTIPLE_MATCHES;
			break;
		case SHOW_SEARCH:
			display = HEADER_SEARCH_RESULTS;
			break;
		case ALL_TASK_AND_COMPLETED:
			display = HEADER_ALL_WITH_COMPLETED_TASKS;
			break;
		case SHOW_HELP:
			display = null; // previous display header not changed
			break;
		case FIND_FREE:
			display = HEADER_FREE_TIME;
			break;
		case ARCHIVE:
			display = HEADER_ARCHIVE_TASKS;
			break;
		case ALL_TASKS:
			// fall-through
		default:
			display = HEADER_ALL_TASKS;
			break;
		}
		return display;
	}

	private String activateDemoScreen() {
		_mainController.setDemo(true);
		StateFeedback state = new DemoStateFeedback();
		_displayController.setSelectedTaskByCall(0, true);
		_displayController.setDisplay(state.getAllTasks(), createDisplayHeader(state),
				state.getDetailedIndexes(), state.getDisplayPosition(), false, true);
		_mainController.updateOverdueCount(state.getOverdueCount());
		return state.getFeedback();
	}
	
	/**
	 * Toggles settings for novice or advanced view.
	 * 
	 * @param isNovice boolean for novice or advanced view
	 */
	public void changeNoviceSettings(boolean isNovice) {
		_logic.setNoviceSettings(isNovice);
	}
	
	/**
	 * Retrieves text for help menu.
	 * 
	 * @return array containing help text
	 */
	public ArrayList<String> getHelpText() {
		return _logic.displayHelp();
	}

	/**
	 * Retrieves text to be displayed at each step during demo mode.
	 * 
	 * @return array of text strings for each step during demo mode
	 */
	public ArrayList<String> getDemoText() {
		return _logic.getDemoText();
	}

	/**
	 * Retrieves the reference for which task to be selected at each step during
	 * demo mode.
	 * 
	 * @return array of indexes of tasks to be selected at each step during demo
	 *         mode
	 */
	public ArrayList<Integer> getDemoSelectionIndexes() {
		return _logic.getDemoSelectionIndexes();
	}

	/**
	 * Retrieves data file at current save directory.
	 * 
	 * @return File object referenced to the current directory of stored data
	 */
	public File getSaveDirectory() {
		File file = new File(_logic.getCurrentSaveDirectory());
		return file;
	}

	/**
	 * Retrieves input format suggestions according to current input command
	 * text by user.
	 * 
	 * @param text
	 *            current input command text by user
	 * @return SuggestFeedback object containing input format suggestions for
	 *         current input
	 */
	public SuggestFeedback retrieveSuggestions(String text) {
		return _logic.getSuggestions(text);
	}

	/**
	 * Calculates bound coordinates of Urgenda window with respect to the screen
	 * at time of method call.
	 * 
	 * @return bounds of window
	 */
	public Bounds computeAllScreenBounds() {
		double minX = Double.POSITIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;
		for (Screen screen : Screen.getScreens()) {
			Rectangle2D screenBounds = screen.getBounds();
			if (screenBounds.getMinX() < minX) {
				minX = screenBounds.getMinX();
			}
			if (screenBounds.getMinY() < minY) {
				minY = screenBounds.getMinY();
			}
			if (screenBounds.getMaxX() > maxX) {
				maxX = screenBounds.getMaxX();
			}
			if (screenBounds.getMaxY() > maxY) {
				maxY = screenBounds.getMaxY();
			}
		}
		return new BoundingBox(minX, minY, maxX - minX, maxY - minY);
	}

	private void quit() {
		if (_mainController.getHelpController() != null) {
			_mainController.getHelpController().closeHelpWindow();
		}
		System.exit(0);
	}

	/**
	 * Returns MainController instance.
	 * 
	 * @return MainController object instance
	 */
	public MainController getController() {
		return _mainController;
	}

	/**
	 * Returns primaryStage instance.
	 * 
	 * @return primaryStage instance
	 */
	public Stage getPrimaryStage() {
		return _primaryStage;
	}

	/**
	 * Launches Urgenda.
	 * 
	 * @param args
	 *            directory of Urgenda.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}