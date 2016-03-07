package urgenda.gui;

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
import urgenda.util.Task;
import urgenda.util.TaskList;

public class Main extends Application {
	private BorderPane _rootLayout;
	private Scene _scene;
	private MainController _mainController;
	private DisplayController _displayController;
	private Logic _logic;

	
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
	

	@Override
	public void start(Stage primaryStage) {
		initLogicComponent();
		initRootLayout();
		initDisplay();
		initStage(primaryStage);
	}

	private void initLogicComponent() {
		_logic = new Logic();
	}

	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(PATH_GUI_FXML));
			_rootLayout = loader.load();
			_mainController = loader.getController();
			_mainController.setUIMain(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initDisplay() {
		_displayController = _mainController.getDisplayController();
		StateFeedback state = retrieveStartupState();
		_displayController.setDisplay(state.getAllTasks(), createDisplayHeader(state), state.getDetailedIndexes());
	}

	private void initStage(Stage primaryStage) {
		_scene = new Scene(_rootLayout);
		primaryStage.initStyle(StageStyle.DECORATED);
		Image ico = new Image(getClass().getResourceAsStream(PATH_ICON));
		primaryStage.getIcons().add(ico);
		primaryStage.setTitle(APP_NAME);
		primaryStage.setResizable(false);
		primaryStage.setScene(_scene);
		primaryStage.sizeToScene();
		primaryStage.show();
	}

	public StateFeedback retrieveStartupState() {
		//StateFeedback state = dummyState(); //TODO change for dummy list
		StateFeedback state = _logic.retrieveStartupState();
		_mainController.displayFeedback(state.getFeedback(), false);
		return state;
	}
	
	public String handleCommandLine(String commandLine) {
		StateFeedback state = _logic.executeCommand(commandLine, _displayController.getSelectedTaskIndex());
		_displayController.setDisplay(state.getAllTasks(), createDisplayHeader(state), state.getDetailedIndexes());
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
			//case DISPLAY:
			//	break;
		case ERROR:
			break;
		case ALL_TASKS: //fall-through
		default:
			display = HEADER_ALL_TASKS;
			break;
		}
		return display;
	}
	
	public void invokeHelpSplash() {
		// TODO create help splash menu
	}

	public void invokeUrgendaSplash() {
		// TODO create about urgenda splash menu
	}

	public MainController getController() {
		return _mainController;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	// dummy method to create dummy state
	private StateFeedback dummyState() {
		Task taskO = new Task("Overdue task", "O location", LocalDateTime.now(), LocalDateTime.now(),
				new ArrayList<String>(), false);
		Task taskTI = new Task("Today Important task", "TI location", LocalDateTime.now(), LocalDateTime.now(),
				new ArrayList<String>(), true);
		Task taskT = new Task("Today task", "T location", LocalDateTime.now(), LocalDateTime.now(),
				new ArrayList<String>(), false);
		Task taskI = new Task("Important task", "I location", LocalDateTime.now(), LocalDateTime.now(),
				new ArrayList<String>(), true);
		Task taskD = new Task("Detailed task", "Detailed location", LocalDateTime.now(), LocalDateTime.now(),
				new ArrayList<String>(), false);
		Task task1 = new Task("1 task", "1 location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(),
				false);
		Task task2 = new Task("2 task", "2 location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(),
				false);
		Task task3 = new Task("3 task", "3 location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(),
				false);
		Task taskC = new Task("Completed task", "C location", LocalDateTime.now(), LocalDateTime.now(),
				new ArrayList<String>(), false);
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		ArrayList<Task> archives = new ArrayList<Task>();
		tasks.add(taskO);
		tasks.add(taskTI);
		tasks.add(taskT);
		tasks.add(taskI);
		tasks.add(taskD);
		tasks.add(task1);
		tasks.add(task2);
		tasks.add(task3);
		archives.add(taskC);
		StateFeedback state = new StateFeedback();
		state.setAllTasks(new TaskList(tasks, archives, 1, 2, 0, 5, 1));
		state.setFeedback("feedback from dummylist");
		state.addDetailedTaskIdx(4);
		state.setState(StateFeedback.State.ALL_TASKS);
		return state;
	}
}
