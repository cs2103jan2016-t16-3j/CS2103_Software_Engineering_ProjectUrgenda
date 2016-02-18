package urgenda.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;


public class MainController {
	
	public KeyCombination keyUndo = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
	public KeyCombination keyRedo = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN);
	
	@FXML
	private TextField inputBar;
	
	//tester method for detecting input in inputBar
	public void commandListener(KeyEvent event) {
		KeyCode code = event.getCode();
		if(code == KeyCode.ENTER) {
			inputBar.setText(("entered:" + inputBar.getText()));
		}
	}
	public void executeUndo(ActionEvent e){
		//run undo method
		inputBar.setText("undo!");
	}
	
	public void executeRedo(ActionEvent e){
		//run redo method
		inputBar.setText("redo!");
	}
	
	public void openHelp(ActionEvent e) {
		//open help menu
		inputBar.setText("help!");
	}
	
	public void exitByMenu(ActionEvent e){
		//save all edits
		Platform.exit();
        System.exit(0);
	}
	
	//TODO: create resize method for rounded rect and display anchor panel
	
}
