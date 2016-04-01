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

public class HelpController implements Initializable {
	
	private static final String HELP_NAME = "Help";
	private static final String PATH_ICON = "../../resources/urgenda_icon.png";
	
	static Stage _helpStage;
	static Scene _helpScene;
	private static ArrayList<String> _helpText;
	private int _helpTextPos;
	
	@FXML
	private TextArea helpContentPane;
	@FXML
	private Button helpPrev;
	@FXML
	private Button helpNext;
	
	@FXML
	public void handleEscPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.ESCAPE) {
			_helpStage.close();
		}
	}
	
	@FXML
	public void handleOkAction(ActionEvent e) {
		_helpStage.close();
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		_helpTextPos = 0;
		helpContentPane.setText(_helpText.get(_helpTextPos));
		helpContentPane.setEditable(false);
	}
	
	public void setupHelpStage(ArrayList<String> arrayList) throws IOException {		
		_helpText = arrayList;
		Parent help = FXMLLoader.load(Main.class.getResource("HelpSplash.fxml"));
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
					helpPrev();
				}
				if(event.getCode().equals(KeyCode.RIGHT)) {
					helpNext();
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
			helpContentPane.setVisible(true);
			helpContentPane.setText(_helpText.get(_helpTextPos));
		}
	}

	private void helpNext() {
		if(_helpTextPos < _helpText.size() - 1) {
			_helpTextPos++;
			helpContentPane.setVisible(true);
			helpContentPane.setText(_helpText.get(_helpTextPos));
		}
	}
	
	@FXML
	private void helpNextListener(ActionEvent e) {
		helpNext();
	}
	
	public void showHelpStage() {
		_helpStage.show();
	}

	public void closeHelpWindow() {
		_helpStage.close();		
	}
	
	public Stage getHelpStage() {
		return _helpStage;
	}
}