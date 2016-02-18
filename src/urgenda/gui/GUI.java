package urgenda.gui;

/**
 * color code for gui
 * main: 313745
 * side: 474E60
 */

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GUI extends Application {
	
	private static final int SCENE_WIDTH_DEFAULT = 450;
	private static final int SCENE_HEIGHT_DEFAULT = 650;
	private static final int STAGE_HEIGHT_INSET = 39;
	private static final int STAGE_WIDTH_INSET = 16;
	
	private Parent root;
	private Scene scene;

	@Override
	public void start(Stage primaryStage) {
		loadFonts();
		try {
			root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			scene = new Scene(root, SCENE_WIDTH_DEFAULT, SCENE_HEIGHT_DEFAULT);
			primaryStage.setMinHeight(SCENE_HEIGHT_DEFAULT + STAGE_HEIGHT_INSET);
			primaryStage.setMinWidth(SCENE_WIDTH_DEFAULT + STAGE_WIDTH_INSET);
			scene.getStylesheets().add(getClass().getResource("../../resources/urgendaStyle.css").toExternalForm());
			primaryStage.setTitle("Urgenda");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings("unused")
	private void loadFonts() {
		Font logoFont = Font.loadFont(GUI.class.getResourceAsStream("../../resources/Sacramento-Regular.ttf"), 10);
		Font RegularFont = Font.loadFont(GUI.class.getResourceAsStream("../../resources/Montserrat-Light.otf"), 10);
		Font BoldFont = Font.loadFont(GUI.class.getResourceAsStream("../../resources/Montserrat-Regular.otf"), 10);
	}
}