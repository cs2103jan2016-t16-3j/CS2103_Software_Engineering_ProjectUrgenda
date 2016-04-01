package urgenda.gui;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import urgenda.util.SuggestFeedback;

public class InputSuggestionsPopupController extends BorderPane {

	private static final String PATH_INPUT_SUGGESTIONS_CSS = "InputSuggestions.css";
	
	@FXML
	private BorderPane typeSuggestionsPane;
	@FXML
	private Text commandWordText;
	@FXML
	private FlowPane suggestionsArea;
	@FXML
	private Text userSuggestionText;
	
	public InputSuggestionsPopupController() {
		this.getStylesheets().addAll(getClass().getResource(PATH_INPUT_SUGGESTIONS_CSS).toExternalForm());
	}

	public void updateSuggestions(SuggestFeedback retriveSuggestions) {
		System.out.println("update suggestions");
		// TODO Auto-generated method stub
	}
}
