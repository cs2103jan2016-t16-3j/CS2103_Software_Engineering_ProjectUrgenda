package urgenda.logic;

import urgenda.command.Command;
import urgenda.parser.Parser;
import urgenda.util.StateFeedback;

public class Logic {

	private static final String MESSAGE_WELCOME = "Welcome to Urgenda! Your task manager is ready for use.";

	private LogicData _logicData;
	
	// Default constructor for Logic Data if no path is given
	public Logic() {
		_logicData = new LogicData();
	}
	
	// Constructor for specifying path for Data to be saved/extracted
	public Logic(String path) {
		_logicData = new LogicData(path);
	}
	
	public StateFeedback executeCommand(String command, int position) {
		// parser take in a string and return it in its corresponding class obj
		Command currCmd = Parser.parseCommand(command, position);
		String feedback;
		// To update if there are any deadlines that turned overdue
		_logicData.updateState();
		try {
			feedback = currCmd.execute(_logicData);
			_logicData.addUndo(currCmd);
			_logicData.saveContents();
		} catch (Exception e) { // TODO might need to upgrade exceptions without affecting the command execute header
			feedback = e.getMessage();
		}
		StateFeedback state = _logicData.getState();
		state.setFeedback(feedback);
		return state;
	}
	
	public StateFeedback retrieveStartupState() {
		StateFeedback state = _logicData.getState();
		state.setFeedback(MESSAGE_WELCOME);
		return state;
	}
}
