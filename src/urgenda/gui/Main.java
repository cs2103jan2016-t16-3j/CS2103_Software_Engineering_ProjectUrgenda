package urgenda.gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import urgenda.logic.Logic;
import urgenda.util.StateFeedback;
import urgenda.util.StateFeedback.State;
import urgenda.util.SuggestFeedback;
import urgenda.util.UrgendaLogger;

public class Main extends Application {
	
	private static final String APP_NAME = "Urgenda";
	private static final String PATH_GUI_FXML = "fxml/MainView.fxml";
	public static final String PATH_REGULAR_FONT = new String("../../resources/Montserrat-Light.otf");
	public static final String PATH_BOLD_FONT = new String("../../resources/Montserrat-Regular.otf");
	public static final String PATH_LIGHT_FONT = new String("../../resources/Montserrat-UltraLight.ttf");
	
	private static final String HEADER_ALL_TASKS = "ALL TASKS";
	private static final String HEADER_ALL_WITH_COMPLETED_TASKS = "ALL TASKS WITH COMPLETED TASKS";
	private static final String HEADER_FREE_TIME = "AVAILABLE TIME PERIODS";
	private static final String HEADER_SEARCH_RESULTS = "SEARCH RESULTS";
	private static final String HEADER_ARCHIVE_TASKS = "ARCHIVE TASKS";
	private static final String HEADER_MULTIPLE_MATCHES = "MULTIPLE MATCHES";

	private static final int DEFAULT_REGULAR_FONT_SIZE = 20;
	private static final int DEFAULT_BOLD_FONT_SIZE = 20;
	private static final int DEFAULT_LIGHT_FONT_SIZE = 20;
	
	// Resources to load
	public static final Font REGULAR_FONT = Font.loadFont(Main.class.getResourceAsStream(PATH_REGULAR_FONT),
			DEFAULT_REGULAR_FONT_SIZE);
	public static final Font BOLD_FONT = Font.loadFont(Main.class.getResourceAsStream(PATH_BOLD_FONT),
			DEFAULT_BOLD_FONT_SIZE);
	public static final Font LIGHT_FONT = Font.loadFont(Main.class.getResourceAsStream(PATH_LIGHT_FONT),
			DEFAULT_LIGHT_FONT_SIZE);
	
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
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, "Initialisation of root layout failed!");
			e.printStackTrace();
		}
		UrgendaLogger.getInstance().getLogger().log(Level.INFO, "Successful initialisation of root layout");
	}

	private void initDisplay() {
		_displayController = _mainController.getDisplayController();
		_currState = retrieveStartupState();
		//TODO implement check settings for showing novice headers, change boolean below
		_displayController.initDisplay(_currState.getAllTasks(), createDisplayHeader(_currState), _currState.getDetailedIndexes(), _currState.getDisplayPosition(), true);
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
		UrgendaLogger.getInstance().getLogger().log(Level.INFO, "Successful initialisation of Urgenda window");
	}

	private void initFeatures() {
		_mainController.updateOverdueCount(_currState.getOverdueCount());
		_mainController.setupTypeSuggestions();
	}
	
	private StateFeedback retrieveStartupState() {
		StateFeedback state = _logic.retrieveStartupState();
		_mainController.displayFeedback(state.getFeedback());
		return state;
	}
	
	protected String handleCommandLine(String commandLine, boolean isDemo) {
		if(!isDemo || commandLine.equals(MainController.KEYWORD_SHOW_ALL)) {
			_currState = _logic.executeCommand(commandLine, _displayController.getSelectedTaskIndex());
			if(_currState.getState() == State.SHOW_HELP) {
				_mainController.showHelp();
			} else if(_currState.getState() == State.EXIT) {
				quit();
			}
			//TODO implement check settings for showing novice headers, change boolean below
			switch(_currState.getState()) {
			case FIND_FREE:
				_displayController.setDisplay(_currState.getAllTasks(), createDisplayHeader(_currState), _currState.getDetailedIndexes(), _currState.getDisplayPosition(), true, true);
				break;
			case HIDE:
				_primaryStage.setIconified(true);
				//fall-through
			default:
				_displayController.setDisplay(_currState.getAllTasks(), createDisplayHeader(_currState), _currState.getDetailedIndexes(), _currState.getDisplayPosition(), true, false);
				break;
			}
			_mainController.updateOverdueCount(_currState.getOverdueCount());
			if (commandLine.equals(MainController.KEYWORD_SHOW_ALL)) {
				_mainController.setDemo(false);
			}
			return _currState.getFeedback();
		} else {
			return null;
		}
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
			//TODO review
//		case DISPLAY:
//			break;
		case SHOW_HELP:
			display = null; //previous display header not changed
			break;
		case FIND_FREE:
			display = HEADER_FREE_TIME;
			break;
		case ARCHIVE:
			display = HEADER_ARCHIVE_TASKS;
			break;
		case ALL_TASKS: //fall-through
		default:
			display = HEADER_ALL_TASKS;
			break;
		}
		return display;
	}
	
	public ArrayList<String> getHelpText() {
		return _logic.displayHelp();
	}

	public MainController getController() {
		return _mainController;
	}
	
	public Stage getPrimaryStage() {
		return _primaryStage;
	}

	public String activateDemoScreen() {
		StateFeedback state = new DemoStateFeedback();
		_displayController.setDisplay(state.getAllTasks(), createDisplayHeader(state), state.getDetailedIndexes(), state.getDisplayPosition(), true, false);
		_mainController.updateOverdueCount(state.getOverdueCount());
		return state.getFeedback();
	}

	public File getSaveDirectory() {
		File file = new File(_logic.getCurrentSaveDirectory());
		return file;
	}
	
	public void setMultipleSlotMenuForSelected(boolean show) {
		_mainController.toggleMultipleSlotMenuOption(show);
	}

	private void quit() {
		if(_mainController.getHelpController() != null) {
			_mainController.getHelpController().closeHelpWindow();
		}
		System.exit(0);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public SuggestFeedback retrieveSuggestions(String text) {
		return _logic.getSuggestions(text);
	}

	public Bounds computeAllScreenBounds() {
        double minX = Double.POSITIVE_INFINITY ;
        double minY = Double.POSITIVE_INFINITY ;
        double maxX = Double.NEGATIVE_INFINITY ;
        double maxY = Double.NEGATIVE_INFINITY ;
        for (Screen screen : Screen.getScreens()) {
            Rectangle2D screenBounds = screen.getBounds();
            if (screenBounds.getMinX() < minX) {
                minX = screenBounds.getMinX();
            }
            if (screenBounds.getMinY() < minY) {
                minY = screenBounds.getMinY() ;
            }
            if (screenBounds.getMaxX() > maxX) {
                maxX = screenBounds.getMaxX();
            }
            if (screenBounds.getMaxY() > maxY) {
                maxY = screenBounds.getMaxY() ;
            }
        }
        return new BoundingBox(minX, minY, maxX-minX, maxY-minY);
    }
}