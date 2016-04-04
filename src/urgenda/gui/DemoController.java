package urgenda.gui;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class DemoController {
	
	@FXML
	private TextFlow demoTextArea;
	
	private ArrayList<String> _demoText = new ArrayList<String>();
	private MainController _mainController;
	private int _demoIndex = 0;
	
	public DemoController(MainController mainController, ArrayList<String> demoText) {
		_mainController = mainController;
		if (demoText != null) {
			_demoText.addAll(demoText);
		}
	}
	
	public void init() {
		showDemoPart(_demoIndex);
	}
	
	private void showDemoPart(int index) {
		demoTextArea.getChildren().clear();
		demoTextArea.getChildren().add(new Text(_demoText.get(index)));
	}

	public void nextPart() {
			if (_demoIndex < _demoText.size() - 1) {
				_demoIndex++;
				showDemoPart(_demoIndex);
			} else if (_demoIndex >= _demoText.size() - 1) {
				_mainController.setDemo(false);
			}
	}

	public void prevPart() {
			if (_demoIndex > 0) {
				_demoIndex--;
				showDemoPart(_demoIndex);
			}
	}
	
	public int getDemoIndex() {
		return _demoIndex;
	}
	
	public void setDemoIndex(int _demoIndex) {
		this._demoIndex = _demoIndex;
	}
}
