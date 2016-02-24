package urgenda.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
//import urgenda.logic.Logic;

public class Main extends Application {
	private AnchorPane rootLayout;
	private Scene scene;
	private MainController urgendaController = new MainController();
	//private Logic logic;
	
	//default set window size values
	private static final int SCENE_WIDTH_DEFAULT = 600;
	private static final int SCENE_HEIGHT_DEFAULT = 600;

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Urgenda");
		initGuiLayout(primaryStage);
	}

	private void initGuiLayout(Stage primaryStage) {
		try {
			rootLayout = FXMLLoader.load(getClass().getResource("Main.fxml"));
			scene = new Scene(rootLayout, SCENE_WIDTH_DEFAULT, SCENE_HEIGHT_DEFAULT);
			primaryStage.initStyle(StageStyle.DECORATED);
			scene.getStylesheets().add(getClass().getResource("../../resources/urgendaStyle.css").toExternalForm());
			scene.setFill(Color.TRANSPARENT);
			//TODO: set icon
			primaryStage.setResizable(false);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	public MainController getController() {
		return urgendaController;
	}
}