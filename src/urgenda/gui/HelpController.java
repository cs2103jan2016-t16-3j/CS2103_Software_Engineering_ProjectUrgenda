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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class HelpController implements Initializable {
	
	static Stage stage = new Stage();
	static String _helpText;
	  
	@FXML
	private TextArea helpContentPane;
	
	@FXML
	public void handleEscPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE || event.getCode() == KeyCode.ENTER) {
			stage.close();
		}
	}
	
	@FXML
	public void handleOkAction(ActionEvent e) {
		stage.close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		helpContentPane.setText(_helpText);
	}
	
	public void setupHelpStage() throws IOException {		
		Parent help = FXMLLoader.load(Main.class.getResource("HelpSplash.fxml"));
		stage.setScene(new Scene(help));
		stage.show();	
	}
	
	public void setHelpText(String text) {
		_helpText = text;
	}
}