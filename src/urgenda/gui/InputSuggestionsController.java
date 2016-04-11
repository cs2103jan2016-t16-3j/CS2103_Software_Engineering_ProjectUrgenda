//@@author A0131857B
package urgenda.gui;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import urgenda.util.SuggestFeedback;

/**
 * UI component, controls the elements that are used for the suggestions popup and styles keywords in suggestions for better readability.
 * 
 * @author KangSoon
 */
public class InputSuggestionsController extends BorderPane {

	// File paths
	private static final String PATH_INPUT_SUGGESTIONS_CSS = "styles/InputSuggestions.css";
	
	// Delimiters to identify by and for replacement during formatting
	private static final String DELIMITER_CHAR = "[]<>";
	private static final String DELIMITER_NON_SUGGESTION = "or";
	private static final String DELIMITER_SUGGESTION = " / ";
	
	// Strings to identify by for formatting
	private static final String SUBSTRING_INDEX = "task no";
	private static final String SUBSTRING_DESC = "desc";
	private static final String SUBSTRING_DESC_EDIT = "new desc";
	private static final String SUBSTRING_DATE_TIME_NEW = "new timing(s)";	
	private static final String SUBSTRING_DATE_TIME_REMOVE = "-r";	
	private static final String SUBSTRING_DATE_TIME_START = "start time";
	private static final String SUBSTRING_DATE_TIME_END = "end time";
	private static final String SUBSTRING_DATE_TIME_DURATION = "duration";
	private static final String SUBSTRING_DATE_TIME_DEADLINE = "deadline";
	private static final String SUBSTRING_DATE_TIME_SEARCH = "date/month/day/time";
	private static final String SUBSTRING_TASK_TYPE_SEARCH = "task type";
	private static final String SUBSTRING_LOCATION = "location";
	private static final String SUBSTRING_PATH_DIRECTORY = "path directory";
	private static final String SUBSTRING_INFO_REMOVE_TIME = "removes a timing";
	private static final String SUBSTRING_INFO_TIMING = "use at/from/on/by for timings";
	private static final String SUBSTRING_INFO_OPTIONAL = "optional";
	private static final String SUBSTRING_INFO_MULTIPLE = "multiple";
	
	// Colors to use for formatting
	private static final Color COLOR_DEFAULT = Color.web("#FFFFFF"); //white
	private static final Color COLOR_INDEX = Color.web("#FFAF4B"); //orange
	private static final Color COLOR_DESC = Color.web("#FFA3A3"); //pink
	private static final Color COLOR_DATE_TIME = Color.web("#86E086"); //green
	private static final Color COLOR_TASK_TYPE = Color.web("#559BFF"); //blue
	private static final Color COLOR_LOCATION = Color.web("#559BFF"); //blue
	private static final Color COLOR_PATH_DIRECTORY = Color.web("#559BFF"); //blue
	private static final Color COLOR_SUGGESTED_COMMANDS = Color.web("#B5B5B5"); //grey
	private static final Paint COLOR_INFO = Color.web("#B5B5B5"); //grey

	// Elements loaded using FXML
	@FXML
	private BorderPane typeSuggestionsPane;
	@FXML
	private Text commandWordText;
	@FXML
	private FlowPane suggestionsArea;
	@FXML
	private Text userSuggestionText;
	
	/**
	 * Creates a InputSuggestionsPopupController instance.
	 */
	public InputSuggestionsController() {
		this.getStylesheets().addAll(getClass().getResource(PATH_INPUT_SUGGESTIONS_CSS).toExternalForm());
	}
	
	/**
	 * updates the suggested commands or input formats according to the current user input.
	 * @param suggestFeedback suggestFeedback object containing suggested command words or suggested format
	 */
	public void updateSuggestions(SuggestFeedback suggestFeedback) {
		suggestionsArea.getChildren().clear();
		if (suggestFeedback.isCommand()) {
			commandWordText.setText(suggestFeedback.getCurrCmd() + " ");
			commandWordText.setVisible(true);
		} else {
			commandWordText.setText("");
			commandWordText.setVisible(false);
		}
		for (String suggestionString : suggestFeedback.getSuggestions()) {
			suggestionsArea.getChildren().add(formatSingleSuggestion(suggestionString, suggestFeedback.isSuggestion()));
		}
		// remove last delimiter
		if (suggestionsArea.getChildren().size() > 0) {
			HBox lastItem = (HBox) suggestionsArea.getChildren().get(suggestionsArea.getChildren().size() - 1);
			lastItem.getChildren().remove(lastItem.getChildren().size() - 1);
		}
		userSuggestionText.setText(suggestFeedback.getUserInstructionsPrompt());
	}

	//identify and apply styles to keywords accordingly
	private HBox formatSingleSuggestion(String suggestionString, boolean isSuggestion) {
		HBox singleSuggestion = new HBox();
		ArrayList<Text> suggestionsTokenisedList = new ArrayList<Text>();
		StringTokenizer suggestionsTokeniser = new StringTokenizer(suggestionString, DELIMITER_CHAR);
		while (suggestionsTokeniser.hasMoreElements()) {
			String tokenized = suggestionsTokeniser.nextElement().toString();
			Text tokenizedText;
			switch (tokenized) {
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
			case SUBSTRING_DATE_TIME_DURATION: //fall-through
			case SUBSTRING_DATE_TIME_NEW: //fall-through
			case SUBSTRING_DATE_TIME_SEARCH:
				tokenizedText = new Text("[" + tokenized + "]");
				tokenizedText.setFill(COLOR_DATE_TIME);
				break;
			case SUBSTRING_DATE_TIME_REMOVE:
				tokenizedText = new Text(tokenized);
				tokenizedText.setFill(COLOR_TASK_TYPE);
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
			case SUBSTRING_INFO_REMOVE_TIME: //fall-through
			case SUBSTRING_INFO_OPTIONAL: //fall-through
			case SUBSTRING_INFO_MULTIPLE:
				tokenizedText = new Text("(" + tokenized + ")");
				tokenizedText.setFill(COLOR_INFO);
				break;
			case SUBSTRING_INFO_TIMING:
				tokenizedText = new Text("<" + tokenized + ">");
				tokenizedText.setFill(COLOR_INFO);
				break;
			default:
				tokenizedText = new Text(tokenized);
				if(isSuggestion) {
					tokenizedText.setFill(COLOR_SUGGESTED_COMMANDS);
				} else {
					tokenizedText.setFill(COLOR_DEFAULT);
				}
				break;
			}
			suggestionsTokenisedList.add(tokenizedText);
		}
		singleSuggestion.getChildren().addAll(suggestionsTokenisedList);
		Text delimiter = new Text();
		if(isSuggestion) {
			delimiter.setText(DELIMITER_SUGGESTION);
			delimiter.setFill(COLOR_DEFAULT);
		} else {
			delimiter.setText(DELIMITER_NON_SUGGESTION);
			delimiter.setFill(COLOR_INFO);
		}
		singleSuggestion.getChildren().add(delimiter);
		return singleSuggestion;
	}
}
