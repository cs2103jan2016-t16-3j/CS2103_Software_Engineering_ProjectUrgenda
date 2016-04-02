package urgenda.gui;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import urgenda.util.SuggestFeedback;

public class InputSuggestionsPopupController extends BorderPane {

	private static final String PATH_INPUT_SUGGESTIONS_CSS = "InputSuggestions.css";

	private static final String SUBSTRING_SELECTED_TASK = "selected task";
	private static final String SUBSTRING_INDEX = "task no";
	private static final String SUBSTRING_DESC = "desc";
	private static final String SUBSTRING_DESC_EDIT = "new desc";
	private static final String SUBSTRING_DATE_TIME_START = "start";
	private static final String SUBSTRING_DATE_TIME_END = "end";
	private static final String SUBSTRING_DATE_TIME_DEADLINE = "deadline";
	private static final String SUBSTRING_DATE_TIME_SEARCH = "date/day/time";
	private static final String SUBSTRING_TASK_TYPE_SEARCH = "task type";
	private static final String SUBSTRING_LOCATION = "location";
	private static final String SUBSTRING_PATH_DIRECTORY = "path directory";

	private static final Color COLOR_DEFAULT = Color.web("#FFFFFF"); //white
	private static final Color COLOR_SELECTED_TASK = Color.web("#559BFF"); //blue
	private static final Color COLOR_INDEX = Color.web("#FFAF4B"); //orange
	private static final Color COLOR_DESC = Color.web("#FF7FB6"); //pink
	private static final Color COLOR_DATE_TIME = Color.web("#86E086"); //green
	private static final Color COLOR_TASK_TYPE = Color.web("#86E086"); //green
	private static final Color COLOR_LOCATION = Color.web("#000000"); //black
	private static final Color COLOR_PATH_DIRECTORY = Color.web("#B5B5B5"); //grey

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

	public void updateSuggestions(SuggestFeedback suggestFeedback) {
		suggestionsArea.getChildren().clear();
		// System.out.println("update suggestions");
		if (suggestFeedback.isCommand()) {
			commandWordText.setText(suggestFeedback.getCurrCmd() + " ");
			commandWordText.setVisible(true);
		} else {
			commandWordText.setText("");
			commandWordText.setVisible(false);
		}
		for (String suggestionString : suggestFeedback.getSuggestions()) {
			suggestionsArea.getChildren().add(formatSingleSuggestion(suggestionString));
		}
		// remove last delimiter
		if (suggestionsArea.getChildren().size() > 0) {
			HBox lastItem = (HBox) suggestionsArea.getChildren().get(suggestionsArea.getChildren().size() - 1);
			lastItem.getChildren().remove(lastItem.getChildren().size() - 1);
		}
		userSuggestionText.setText(suggestFeedback.getUserInstructionsPrompt());
	}

	private HBox formatSingleSuggestion(String suggestionString) {
		HBox singleSuggestion = new HBox();
		ArrayList<Text> suggestionsTokenisedList = new ArrayList<Text>();
		StringTokenizer suggestionsTokeniser = new StringTokenizer(suggestionString, "[]<>");
		while (suggestionsTokeniser.hasMoreElements()) {
			String tokenized = suggestionsTokeniser.nextElement().toString();
			Text tokenizedText;
			switch (tokenized) {
			case SUBSTRING_SELECTED_TASK:
				tokenizedText = new Text("<" + tokenized + ">");
				tokenizedText.setFill(COLOR_SELECTED_TASK);
				break;
			case SUBSTRING_INDEX:
				tokenizedText = new Text("[" + tokenized + "]");
				tokenizedText.setFill(COLOR_INDEX);
				break;
			case SUBSTRING_DESC: // fall-through
			case SUBSTRING_DESC_EDIT:
				tokenizedText = new Text("[" + tokenized + "]");
				tokenizedText.setFill(COLOR_DESC);
				break;
			case SUBSTRING_DATE_TIME_START: // fall-through
			case SUBSTRING_DATE_TIME_END: // fall-through
			case SUBSTRING_DATE_TIME_DEADLINE: // fall-through
			case SUBSTRING_DATE_TIME_SEARCH:
				tokenizedText = new Text("[" + tokenized + "]");
				tokenizedText.setFill(COLOR_DATE_TIME);
				break;
			case SUBSTRING_TASK_TYPE_SEARCH:
				tokenizedText = new Text("[" + tokenized + "]");
				tokenizedText.setFill(COLOR_TASK_TYPE);
				break;
			case SUBSTRING_LOCATION:
				tokenizedText = new Text("[" + tokenized + "]");
				tokenizedText.setFill(COLOR_LOCATION);
				break;
			case SUBSTRING_PATH_DIRECTORY:
				tokenizedText = new Text("[" + tokenized + "]");
				tokenizedText.setFill(COLOR_PATH_DIRECTORY);
				break;
			default:
				tokenizedText = new Text(tokenized);
				tokenizedText.setFill(COLOR_DEFAULT);
				break;
			}
			suggestionsTokenisedList.add(tokenizedText);
		}
		singleSuggestion.getChildren().addAll(suggestionsTokenisedList);
		Text delimiter = new Text("|");
		delimiter.setFill(COLOR_DEFAULT);
		singleSuggestion.getChildren().add(delimiter);
		return singleSuggestion;
	}
}
