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

	//Resources to load
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
		displayController.setDisplay(state.getTasks(),state.getDisplayHeader());
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
		displayController.setDisplay(state.getTasks(),state.getDisplayHeader());
		return state.getFeedback();
	}
	
	public StateFeedback retrieveStartupState(){
		StateFeedback state = dummyState(); //remove dummy when logic returns a legit feedbackstate
		//StateFeedback state = logic.retrieveStartupState();
		mainController.displayFeedback(state.getFeedback(), false);
		return state;
	}

	private StateFeedback dummyState() {
		Task taskU = new Task("Urgent task", "U location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), true);
		Task task1 = new Task("A task", "A location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), false);
		Task task2 = new Task("B task", "B location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), false);
		Task task3 = new Task("C task", "C location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), false);
		Task task4 = new Task("D task", "D location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), false);
		Task task5 = new Task("E task", "E location", LocalDateTime.now(), LocalDateTime.now(), new ArrayList<String>(), false);
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(taskU);
		tasks.add(task1);
		tasks.add(task2);
		tasks.add(task3);
		tasks.add(task4);
		tasks.add(task5);
		
		StateFeedback state = new StateFeedback();
		state.setTasks(new TaskList(tasks,0,1,4,0,0));
		state.setDisplayHeader("Showing dummylist");
		state.setFeedback("feedback from dummylist");
		return state;
	}

	public String callUndo() {
		//TODO call undo from logic
		return null;
	}
	
	public String callRedo() {
		//TODO call redo from logic
		return null;
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