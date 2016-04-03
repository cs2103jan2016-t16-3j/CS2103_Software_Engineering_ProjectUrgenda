package urgenda.gui;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.text.TextFlow;

public class DemoController {
	
	@FXML
	private TextFlow demoTextArea;
	
	private ArrayList<String> demoText = new ArrayList<String>();
	private MainController _mainController;
	private int _demoIndex;
	
	public DemoController() {
		
	}

	public void nextPart() {
		// TODO Auto-generated method stub
	}

	public void prevPart() {
		// TODO Auto-generated method stub
	}

	public void setMainController(MainController mainController) {
		_mainController = mainController;
	}
	
	public int getDemoIndex() {
		return _demoIndex;
	}
	
	public void setDemoIndex(int _demoIndex) {
		this._demoIndex = _demoIndex;
	}
}
