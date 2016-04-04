package urgenda.gui;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class DemoController {
	
	@FXML
	private TextFlow demoTextArea;
	
	private ArrayList<String> _demoText = new ArrayList<String>();
	private ArrayList<Integer> _demoSelectionIndexes = new ArrayList<Integer>();
	private MainController _mainController;
	private int _demoIndex = 0;
	
	public DemoController(MainController mainController, ArrayList<String> demoText, ArrayList<Integer> demoSelectionIndexes) {
		_mainController = mainController;
		_demoText.addAll(demoText);
		_demoSelectionIndexes.addAll(demoSelectionIndexes);
	}
	
	public void init() {
		showDemoPart(_demoIndex);
	}
	
	private void showDemoPart(int index) {
		demoTextArea.getChildren().clear();
		demoTextArea.getChildren().add(new Text(_demoText.get(index)));
		_mainController.getDisplayController().setSelectedTaskByCall(_demoSelectionIndexes.get(_demoIndex));
	}

	@FXML
	private void nextPartListener(MouseEvent e) {
		nextPart();
	}
	
	@FXML
	private void prevPartListener(MouseEvent e) {
		prevPart();
	}
	
	public void nextPart() {
		if (_demoIndex < _demoText.size()) {
			_demoIndex++;
		}
		if (_demoIndex >= _demoText.size()) {
			_mainController.setDemo(false);
		} else {
			showDemoPart(_demoIndex);				
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
