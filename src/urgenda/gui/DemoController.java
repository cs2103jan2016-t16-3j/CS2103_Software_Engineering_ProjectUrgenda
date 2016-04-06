//@@author A0131857B
package urgenda.gui;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

/**
 * UI component that controls the features demo of Urgenda.
 * 
 * @author KangSoon
 */
public class DemoController {

	// Elements loaded using FXML
	@FXML
	private TextFlow demoTextArea;

	// Private attributes
	private ArrayList<String> _demoText = new ArrayList<String>();
	private ArrayList<Integer> _demoSelectionIndexes = new ArrayList<Integer>();
	private MainController _mainController;
	private int _demoIndex = 0;

	/**
	 * Creates a DemoController.
	 * 
	 * @param mainController
	 *            MainController object that invoked this method
	 * @param demoText
	 *            array of text to be shown during demo
	 * @param demoSelectionIndexes
	 *            array of indexes for selection of tasks during steps of demo
	 */
	public DemoController(MainController mainController, ArrayList<String> demoText,
			ArrayList<Integer> demoSelectionIndexes) {
		_mainController = mainController;
		_demoText.addAll(demoText);
		_demoSelectionIndexes.addAll(demoSelectionIndexes);
	}

	/**
	 * Initialise attributes for demo.
	 */
	public void init() {
		_demoIndex = 0;
		showDemoPart(_demoIndex);
	}

	private void showDemoPart(int index) {
		demoTextArea.getChildren().clear();
		demoTextArea.getChildren().add(new Text(_demoText.get(index)));
		_mainController.getDisplayController().setSelectedTaskByCall(_demoSelectionIndexes.get(_demoIndex),
				false);
	}

	@FXML
	private void nextPartListener(MouseEvent e) {
		nextPart();
	}

	@FXML
	private void prevPartListener(MouseEvent e) {
		prevPart();
	}

	/**
	 * Shows the next part of the demo, or returns to all tasks view if demo has
	 * finished.
	 */
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

	/**
	 * Returns to the previous part of the demo if it is not on the first part.
	 */
	public void prevPart() {
		if (_demoIndex > 0) {
			_demoIndex--;
			showDemoPart(_demoIndex);
		}
	}

	/**
	 * Returns the index of the part demo is at.
	 * 
	 * @return part the demo is at
	 */
	public int getDemoIndex() {
		return _demoIndex;
	}

	/**
	 * Sets the part demo is at by index.
	 * 
	 * @param _demoIndex
	 *            part to set the demo for
	 */
	public void setDemoIndex(int _demoIndex) {
		this._demoIndex = _demoIndex;
	}
}
