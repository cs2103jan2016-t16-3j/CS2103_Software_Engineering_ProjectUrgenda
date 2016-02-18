package urgenda.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
public class MainController {
	
	@FXML
	private TextField inputBar;
	@FXML
	private TextArea feedbackField;
	@FXML
	private FlowPane display;
	
	public void commandListener(KeyEvent event) {
		KeyCode code = event.getCode();
		if(code == KeyCode.ENTER) {
			testCommand(inputBar.getText());
		}
	}
	public void testCommand(String command) {
		feedbackField.setText(command);
	}
}
