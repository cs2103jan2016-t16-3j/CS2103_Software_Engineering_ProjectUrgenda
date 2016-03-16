package urgenda.logic;

import urgenda.command.Command;
import urgenda.parser.Parser;
import urgenda.util.MyLogger;
import urgenda.util.StateFeedback;

public class Logic {
	
	private static final String MESSAGE_WELCOME = "Welcome to Urgenda! Your task manager is ready for use. \nPress ALT + F1 if you need help.";

	private LogicData _logicData;
	private LogicCommand _logicCommand;
	
	// Default constructor for Logic
	public Logic() {
		_logicData = LogicData.getInstance();
		_logicCommand = new LogicCommand();
	}
	
	
	@SuppressWarnings("static-access")
	public StateFeedback executeCommand(String command, int index) {
		
		MyLogger logger = MyLogger.getInstance();
		logger.myLogger.info("executing user input: " + command);
		
		assert (index >= -1); // asserts that given index is non-negative OR -1(case when there is no tasks)
		
		// parser take in a string and return it in its corresponding class obj
		Command currCmd = Parser.parseCommand(command,index);
		assert (currCmd != null); // asserts that parser returns a command object
		
		String feedback;
		// To update if there are any deadlines that turned overdue
		_logicData.updateState();
		feedback = _logicCommand.processCommand(currCmd);
		
		StateFeedback state = _logicData.getState();
		state.setFeedback(feedback);
		_logicData.saveContents();
		return state;
	}
	
	@SuppressWarnings("static-access")
	public String displayHelp() {
		
		MyLogger logger = MyLogger.getInstance();
		logger.myLogger.info("Help fn has been called");
		
		return _logicData.generateHelpManual();
	}
	
	@SuppressWarnings("static-access")
	public StateFeedback retrieveStartupState() {
		MyLogger logger = MyLogger.getInstance();
		logger.myLogger.info("Retrieving prev launched info");
		
		StateFeedback state = _logicData.getState();
		state.setFeedback(MESSAGE_WELCOME);
		return state;
	}


	public String getCurrentSaveDirectory() {
		return _logicData.retrieveCurrentDirectory();
	}
}
