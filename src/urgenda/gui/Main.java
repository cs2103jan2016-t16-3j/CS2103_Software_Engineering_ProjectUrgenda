package urgenda.gui;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import urgenda.logic.Logic;
import urgenda.util.StateFeedback;
import urgenda.util.StateFeedback.State;
import urgenda.util.Task;
import urgenda.util.TaskList;

public class Main extends Application {
	
	private static final String APP_NAME = "Urgenda";
	private static final String PATH_GUI_FXML = "Main.fxml";
	private static final String PATH_ICON = "../../resources/urgenda_icon.png";
	private static final String PATH_REGULAR_FONT = new String("../../resources/Montserrat-Light.otf");
	private static final String PATH_BOLD_FONT = new String("../../resources/Montserrat-Regular.otf");
	private static final String PATH_LIGHT_FONT = new String("../../resources/Montserrat-UltraLight.ttf");
	
	private static final String HEADER_ALL_TASKS = "Showing ALL TASKS";

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

	@Override
	public void start(Stage primaryStage) {
		initLogicComponent();
		initRootLayout();
		initDisplay();
		initStage(primaryStage);
	}

	private void initLogicComponent() {
		_logic = Logic.getInstance();
	}

	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(PATH_GUI_FXML));
			_rootLayout = loader.load();
			_mainController = loader.getController();
			_mainController.setMain(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initDisplay() {
		_displayController = _mainController.getDisplayController();
		StateFeedback state = retrieveStartupState();
		//TODO implement check settings for showing novice headers, change boolean below
		_displayController.initDisplay(state.getAllTasks(), createDisplayHeader(state), state.getDetailedIndexes(), state.getDisplayPosition(), true);
		_displayController.setMain(this);
	}

	private void initStage(Stage primaryStage) {
		_scene = new Scene(_rootLayout);
		_primaryStage = primaryStage;
		primaryStage.initStyle(StageStyle.DECORATED);
		primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(PATH_ICON)));
		primaryStage.setTitle(APP_NAME);
		primaryStage.setResizable(false);
		primaryStage.setScene(_scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

	private StateFeedback retrieveStartupState() {
		StateFeedback state = _logic.retrieveStartupState();
		_mainController.displayFeedback(state.getFeedback());
		return state;
	}
	
	protected String handleCommandLine(String commandLine) {
		StateFeedback state = _logic.executeCommand(commandLine, _displayController.getSelectedTaskIndex());
		if(state.getState() == State.SHOW_HELP) {
			_mainController.showHelp();
		} else if(state.getState() == State.EXIT) {
			quit();
		}
		//TODO implement check settings for showing novice headers, change boolean below
		_displayController.setDisplay(state.getAllTasks(), createDisplayHeader(state), state.getDetailedIndexes(), state.getDisplayPosition(), true);
		return state.getFeedback();
	}
	

	private String createDisplayHeader(StateFeedback state) {
		String display = "";
		switch (state.getState()) {
		case MULTIPLE_MATCHES:
			display = "Showing MULTIPLE MATCHES";
			break;
		case SHOW_SEARCH:
			display = "Showing SEARCH RESULTS";
			break;
		case ALL_TASK_AND_COMPLETED:
			display = "Showing ALL TASKS WITH COMPLETED TASKS";
			break;
//		case DISPLAY:
//			break;
		case SHOW_HELP:
			display = null; //previous display header not changed
			break;
		case ERROR:
		case ALL_TASKS: //fall-through
		default:
			display = HEADER_ALL_TASKS;
			break;
		}
		return display;
	}
	
	public String getHelpText() {
		return _logic.displayHelp();
	}

	public MainController getController() {
		return _mainController;
	}
	
	public Stage getPrimaryStage() {
		return _primaryStage;
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
	
	//dummy method to create dummy list of tasks
	protected String setupDummyList() {
		Task taskOverdue = new Task("Complete tutorial", null, null, LocalDateTime.now().minusDays(1).withHour(23).withMinute(59),
				new ArrayList<String>(), false);
		Task taskTodayImportant = new Task("Dinner with mum", null, LocalDateTime.now().withHour(19).withMinute(0), LocalDateTime.now().withHour(20).withMinute(0),
				new ArrayList<String>(), true);
		Task taskToday1 = new Task("NUS Overseas Colleges Workshop", null, LocalDateTime.now().minusDays(1).withHour(10).withMinute(0), LocalDateTime.now().plusDays(1).withHour(22).withMinute(0),
				new ArrayList<String>(), false);
		Task taskTodayDetailed = new Task("Finish writing testimonial for scholarship application", "", null, LocalDateTime.now().withHour(23).withMinute(59),
				new ArrayList<String>(), false);
		Task taskImportant = new Task("Internship interview w/ Google", null, LocalDateTime.now().plusDays(6).withHour(10).withMinute(0), LocalDateTime.now().plusDays(6).withHour(11).withMinute(0),
				new ArrayList<String>(), true);
		Task taskOverrun = new Task("Buy milk, eggs, correction tape, eraser", "Supermarket", null, null, new ArrayList<String>(),
				false);
		Task taskDetailedLong = new Task("Success is the sum of small efforts, repeated day in and day out. - Robert Collier", null, null, null,
				new ArrayList<String>(), false);
		Task taskC = new Task("Submit Project Report", null, null, LocalDateTime.now().minusDays(4).withHour(23).withMinute(59),
				new ArrayList<String>(), false);
		Task taskC2 = new Task("Dental Appointment", null, LocalDateTime.now().minusDays(8).withHour(10).withMinute(30), LocalDateTime.now().minusDays(8).withHour(12).withMinute(00), new ArrayList<String>(),
				false);
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		ArrayList<Task> archives = new ArrayList<Task>();
		tasks.add(taskOverdue);
		tasks.add(taskTodayImportant);
		tasks.add(taskToday1);
		tasks.add(taskTodayDetailed);
		tasks.add(taskImportant);
		tasks.add(taskOverrun);
		tasks.add(taskDetailedLong);
		archives.add(taskC);
		archives.add(taskC2);
		StateFeedback state = new StateFeedback();
		state.setAllTasks(new TaskList(tasks, archives, 1, 3, 3, 2));
		state.setFeedback("Welcome to Urgenda! Your task manager is ready for use.\nPress ALT + F1 for help.");
		state.addDetailedTaskIdx(3);
		state.addDetailedTaskIdx(5);
		state.addDetailedTaskIdx(6);
		state.setState(StateFeedback.State.ALL_TASKS);
		_displayController.setDisplay(state.getAllTasks(), createDisplayHeader(state), state.getDetailedIndexes(), 0, true);
		return state.getFeedback();
	}

	public File getSaveDirectory() {
		File file = new File(_logic.getCurrentSaveDirectory());
		return file;
	}
}
