package urgenda.logic;

import urgenda.command.Command;
import urgenda.parser.Parser;
import urgenda.util.StateFeedback;

public class Logic {

	private static final String MESSAGE_WELCOME = "Welcome to Urgenda! Your task manager is ready for use.";
	private static final String MESSAGE_HEADER_ALL = "Showing ALL TASKS.";

	private LogicData _logicData;
	
	// Default constructor for Logic Data if no path is given
	public Logic() {
		_logicData = new LogicData();
	}
	
	// Constructor for specifying path for Data to be saved/extracted
	public Logic(String path) {
		_logicData = new LogicData(path);
	}
	
	public StateFeedback executeCommand(String command) {
		// parser take in a string and return it in its corresponding class obj
		Command currCmd = Parser.parseCommand(command); 
		String feedback = currCmd.execute(_logicData);
		String displayHeader = "Showing BLABLABLA";	//TODO *pass string to be shown at the top of display panel
		_logicData.addUndo(currCmd);
		_logicData.saveContents();
		StateFeedback state = _logicData.getState(false);	//TODO *boolean passed to getState method indicates whether to show completed tasks or not
		state.setFeedback(feedback);
//		if() {	//TODO: *indicate whether there has been error processing command or not
//			state.setIsError(true);
//		} else {
//			state.setIsError(false);
//		}
		state.setDisplayHeader(displayHeader);
		return state;
	}
	
	public StateFeedback retrieveStartupState() {
		StateFeedback state = _logicData.getState(false); //TODO *boolean passed to getstate method indicates whether to show completed tasks or not
		state.setFeedback(MESSAGE_WELCOME);
		state.setDisplayHeader(MESSAGE_HEADER_ALL);
		return state;
	}
}
