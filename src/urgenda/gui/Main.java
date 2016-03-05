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
//import urgenda.logic.Logic;
import urgenda.logic.Logic;
import urgenda.util.StateFeedback;
import urgenda.util.Task;
import urgenda.util.TaskList;

public class Main extends Application {
	private BorderPane rootLayout;
	private Scene scene;
	private MainController mainController;
	private DisplayController displayController;
	private Logic logic;

	//
	private static final String APP_NAME = "Urgenda";
	private static final String PATH_GUI_FXML = "Main.fxml";
	private static final String PATH_ICON = "../../resources/urgenda_icon.png";
	private static final String PATH_STYLESHEET_CSS = "../../resources/urgendaStyle.css";
	private static final String REGULAR_FONT_PATH = new String("../../resources/Montserrat-Light.otf");
	private static final String BOLD_FONT_PATH = new String("../../resources/Montserrat-Regular.otf");

	private static final int DEFAULT_SCENE_WIDTH = 600;
	private static final int DEFAULT_SCENE_HEIGHT = 600;
	private static final int DEFAULT_REGULAR_FONT_SIZE = 10;
	private static final int DEFAULT_BOLD_FONT_SIZE = 10;

	// Resources to load
	@SuppressWarnings("unused")
	private static final Font REGULAR_FONT = Font.loadFont(Main.class.getResourceAsStream(REGULAR_FONT_PATH),
			DEFAULT_REGULAR_FONT_SIZE);
	@SuppressWarnings("unused")
	private static final Font BOLD_FONT = Font.loadFont(Main.class.getResourceAsStream(BOLD_FONT_PATH),
			DEFAULT_BOLD_FONT_SIZE);

	@Override
	public void start(Stage primaryStage) {
		initLogicComponent();
		initRootLayout();
		initDisplay();
		initStage(primaryStage);
	}

	private void initLogicComponent() {
		logic = new Logic();
	}

	private void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(PATH_GUI_FXML));
			rootLayout = loader.load();
			mainController = loader.getController();
			mainController.setUIMain(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initDisplay() {
		displayController = mainController.getDisplayController();
		StateFeedback state = retrieveStartupState();
		displayController.setDisplay(state.getAllTasks(), state.getDisplayHeader(),state.getDetailedIndexes());
	}

	private void initStage(Stage primaryStage) {
		scene = new Scene(rootLayout, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
		scene.getStylesheets().add(getClass().getResource(PATH_STYLESHEET_CSS).toExternalForm());
		primaryStage.initStyle(StageStyle.DECORATED);
		Image ico = new Image(getClass().getResourceAsStream(PATH_ICON));
		primaryStage.getIcons().add(ico);
		primaryStage.setTitle(APP_NAME);
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public String handleCommandLine(String commandLine) {
		StateFeedback state = logic.executeCommand(commandLine);
		displayController.setDisplay(state.getAllTasks(), state.getDisplayHeader(), state.getDetailedIndexes());
		return state.getFeedback();
	}

	public StateFeedback retrieveStartupState() {
		StateFeedback state = dummyState(); // remove dummy when logic returns a
											// legit feedbackstate
		// StateFeedback state = logic.retrieveStartupState();
		mainController.displayFeedback(state.getFeedback(), false);
		return state;
	}

	//dummy method to create dummy state
	private StateFeedback dummyState() {
		Task taskO = new Task("Overdue task", "O location", LocalDateTime.now(), LocalDateTime.now(),
				new ArrayList<String>(), false);
		Task taskU = new Task("Urgent task", "U location", LocalDateTime.now(), LocalDateTime.now(),
				new ArrayList<String>(), true);
		Task taskT = new Task("Today task", "T location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(),
				false);
		Task taskD = new Task("Detailed task", "Detailed location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(),
				false);
		Task task1 = new Task("1 task", "1 location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(),
				false);
		Task task2 = new Task("2 task", "2 location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(),
				false);
		Task task3 = new Task("3 task", "3 location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(),
				false);
		Task task4 = new Task("4 task", "4 location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(),
				false);
		Task task5 = new Task("5 task", "5 location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(),
				false);
		Task task6 = new Task("6 task", "6 location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(),
				false);
		Task taskC = new Task("Completed task", "C location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(),
				false);

		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(taskO);
		tasks.add(taskU);
		tasks.add(taskT);
		tasks.add(taskD);
		tasks.add(task1);
		tasks.add(task2);
		tasks.add(task3);
		tasks.add(task4);
		tasks.add(task5);
		tasks.add(task6);
		tasks.add(taskC);
		StateFeedback state = new StateFeedback();
		state.setAllTasks(new TaskList(tasks, 1, 1, 1, 7, 1));
		state.setDisplayHeader("Showing dummylist");
		state.setFeedback("feedback from dummylist");
		state.addDetailedTaskIdx(3);
		return state;
	}

	public void callUndo() {
		handleCommandLine("undo");
	}

	public void callRedo() {
		handleCommandLine("redo");
	}

	public void invokeHelpSplash() {
		// TODO create help splash menu
	}

	public void invokeUrgendaSplash() {
		// TODO create about urgenda splash menu

	}

	public MainController getController() {
		return mainController;
	}
}