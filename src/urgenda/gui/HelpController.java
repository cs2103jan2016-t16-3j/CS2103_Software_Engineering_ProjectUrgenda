package urgenda.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HelpController implements Initializable {
	
	private static final String HELP_NAME = "Help";
	private static final String PATH_ICON = "../../resources/urgenda_icon.png";
	
	static Stage stage = new Stage();
	static String _helpText;
	
	@FXML
	private TextArea helpContentPane;
	
	@FXML
	public void handleEscPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER) {
			stage.hide();
		}
	}
	
	@FXML
	public void handleOkAction(ActionEvent e) {
		stage.hide();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		helpContentPane.setText(_helpText);
		helpContentPane.setEditable(false);
	}
	
	public void setupHelpStage() throws IOException {		
		Parent help = FXMLLoader.load(Main.class.getResource("HelpSplash.fxml"));
		stage.setScene(new Scene(help));
		stage.initStyle(StageStyle.DECORATED);
		stage.getIcons().add(new Image(getClass().getResourceAsStream(PATH_ICON)));
		stage.setTitle(HELP_NAME);
		showHelpStage();	
	}
	
	public void setHelpText(String text) {
		_helpText = text;
	}

	public void showHelpStage() {
		stage.show();
	}

	public void closeHelpWindow() {
		stage.close();		
	}
}