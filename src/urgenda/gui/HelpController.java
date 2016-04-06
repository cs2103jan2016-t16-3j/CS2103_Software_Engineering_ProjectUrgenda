//@@author A0131857B
package urgenda.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * UI components, controls the elements in help window.
 * 
 * @author KangSoon
 */
public class HelpController implements Initializable {
	
	// Constants
	private static final String HELP_NAME = "Help";
	
	// File paths
	private static final String PATH_HELP_SPLASH_FXML = "fxml/HelpSplash.fxml";
	private static final String PATH_ICON = "../../resources/urgenda_icon.png";
	
	// Private attributes
	static Stage _helpStage;
	static Scene _helpScene;
	private static ArrayList<String> _helpText;
	private int _helpTextPos;
	
	// Elements loaded using FXML
	@FXML
	private TextArea helpContentPane;
	@FXML
	private Button helpPrev;
	@FXML
	private Button helpNext;
	
	@FXML
	private void handleEscPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE) {
			_helpStage.close();
		}
	}
	
	@FXML
	private void handleOkAction(ActionEvent e) {
		_helpStage.close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		_helpTextPos = 0;
		helpContentPane.setText(_helpText.get(_helpTextPos));
		helpContentPane.setEditable(false);
	}
	
	/**
	 * Sets up the help window.
	 * @param helpText list of help text to display
	 * @throws IOException 
	 */
	public void setupHelpStage(ArrayList<String> helpText) throws IOException {		
		_helpText = helpText;
		Parent help = FXMLLoader.load(Main.class.getResource(PATH_HELP_SPLASH_FXML));
		_helpStage = new Stage();
		_helpScene = new Scene(help);
		_helpStage.setScene(_helpScene);
		_helpStage.initStyle(StageStyle.DECORATED);
		_helpStage.getIcons().add(new Image(getClass().getResourceAsStream(PATH_ICON)));
		_helpStage.setTitle(HELP_NAME);
		showHelpStage();
		_helpScene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if(event.getCode().equals(KeyCode.LEFT)) {
					helpPrev.fire();
				}
				if(event.getCode().equals(KeyCode.RIGHT)) {
					helpNext.fire();
				}
			}	
		});
	}
	
	@FXML
	private void helpPrevListener(ActionEvent e) {
		helpPrev();
	}

	private void helpPrev() {
		if(_helpTextPos > 0) {
			_helpTextPos--;
			helpContentPane.setText(_helpText.get(_helpTextPos));
		}
	}

	private void helpNext() {
		if(_helpTextPos < _helpText.size() - 1) {
			_helpTextPos++;
			helpContentPane.setText(_helpText.get(_helpTextPos));
		}
	}
	
	@FXML
	private void helpNextListener(ActionEvent e) {
		helpNext();
	}
	
	/**
	 * Shows the help window.
	 */
	public void showHelpStage() {
		_helpStage.show();
	}
	
	/**
	 * Closes the help window.
	 */
	public void closeHelpWindow() {
		_helpStage.close();		
	}
	
	/**
	 * Gets the help stage used for the window.
	 * @return help stage used for the window
	 */
	public Stage getHelpStage() {
		return _helpStage;
	}
}