package urgenda.logic;

import urgenda.command.Command;
import urgenda.parser.Parser;
import urgenda.util.StateFeedback;

public class Logic {

	private static final String MESSAGE_WELCOME = "Welcome to Urgenda! Your task manager is ready for use.";

	private LogicData _logicData;
	
	// Default constructor for Logic
	public Logic() {
		_logicData = new LogicData();
	}
	
	public StateFeedback executeCommand(String command, int index) {
		assert (index >= -1); // asserts that given index is non-negative OR -1(case when there is no tasks)
		
		// parser take in a string and return it in its corresponding class obj
		Command currCmd = Parser.parseCommand(command,index);
		assert (currCmd != null); // asserts that parser returns a command object
		
		String feedback;
		// To update if there are any deadlines that turned overdue
		_logicData.updateState();
		try {
			feedback = currCmd.execute(_logicData);
			_logicData.addUndo(currCmd);
		} catch (Exception e) { // TODO might need to upgrade exceptions without affecting the command execute header
			feedback = e.getMessage();
		}
		StateFeedback state = _logicData.getState();
		state.setFeedback(feedback);
		_logicData.saveContents();
		return state;
	}
	
	public String displayHelp() {
		return _logicData.generateHelpManual();
	}
	
	public StateFeedback retrieveStartupState() {
		StateFeedback state = _logicData.getState();
		state.setFeedback(MESSAGE_WELCOME);
		return state;
	}
}
