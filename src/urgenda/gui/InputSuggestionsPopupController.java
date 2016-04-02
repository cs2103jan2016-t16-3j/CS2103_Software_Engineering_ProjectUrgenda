package urgenda.gui;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import urgenda.util.SuggestFeedback;

public class InputSuggestionsPopupController extends BorderPane {

	private static final String PATH_INPUT_SUGGESTIONS_CSS = "InputSuggestions.css";
	//private static final Color COLOR_INDEX = Color.web("");
	
	
	@FXML
	private BorderPane typeSuggestionsPane;
	@FXML
	private Text commandWordText;
	@FXML
	private FlowPane suggestionsArea;
	@FXML
	private Text userSuggestionText;
	
	private SuggestFeedback _currSuggestions;
	
	public InputSuggestionsPopupController() {
		this.getStylesheets().addAll(getClass().getResource(PATH_INPUT_SUGGESTIONS_CSS).toExternalForm());
	}

	public void updateSuggestions(SuggestFeedback retrieveSuggestions) {
		System.out.println("update suggestions");
		_currSuggestions = retrieveSuggestions;
//		if (_currSuggestions.isCommand()) {
//			commandWordText.setText(_currSuggestions.getCurrCmd());
//		} else {
//			commandWordText.setText("");
//		}
		
	}
}
