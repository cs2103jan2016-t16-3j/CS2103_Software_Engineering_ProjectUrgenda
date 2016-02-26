package urgenda.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
//import urgenda.logic.Logic;
import urgenda.logic.Logic;
import urgenda.util.StateFeedback;

public class Main extends Application {
	private AnchorPane rootLayout;
	private Scene scene;
	private InputController inputController;
	private DisplayView displayView;
	private Logic logic;
	
	//
	private static final String PATH_GUI_FXML = "Main.fxml";
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
		primaryStage.setTitle("Urgenda");
		initGuiLayout(primaryStage);
		initDisplayArea();
		initLogicComponent();
	}

	private void initGuiLayout(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource(PATH_GUI_FXML));
			rootLayout = loader.load();
			scene = new Scene(rootLayout, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT);
			primaryStage.initStyle(StageStyle.DECORATED);
			scene.getStylesheets().add(getClass().getResource(PATH_STYLESHEET_CSS).toExternalForm());
			scene.setFill(Color.TRANSPARENT);
			Image ico = new Image(getClass().getResourceAsStream("../../resources/urgenda_icon.png")); 
			primaryStage.getIcons().add(ico);
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
			//setting up controller
			inputController = loader.getController();
			inputController.setUIMain(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initDisplayArea() {
		displayView = new DisplayView();
	}
	
	private void initLogicComponent() {
		logic = new Logic();	
	}

	public static void main(String[] args) {
		launch(args);
	}


	public String handleCommandLine(String commandLine) {
		StateFeedback state = logic.executeCommand(commandLine);
		//pass to displayview to update display
		return state.getFeedback();
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
	
	public InputController getController() {
		return inputController;
	}

}