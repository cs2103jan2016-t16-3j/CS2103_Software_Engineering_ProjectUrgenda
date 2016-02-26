package urgenda.logic;

import urgenda.command.Command;
import urgenda.parser.Parser;
import urgenda.util.StateFeedback;

public class Logic {

	private static final String MESSAGE_WELCOME = "Welcome to Urgenda. Your task manager is ready for use.";

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
		_logicData.addUndo(currCmd);
		_logicData.saveContents();
		StateFeedback state = _logicData.getState();
		state.setFeedback(feedback);
		return state;
	}
}
