package urgenda.logic;

import urgenda.command.Command;
import urgenda.parser.CommandParser;
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
	
	/**
	 * Executes the command given in string format, taking the relevant position if required
	 * 
	 * @param command input string by the user
	 * @param index current index pointed at by the user
	 * @return StateFeedback which includes the current state of tasks as well as feedback line
	 */
	
	@SuppressWarnings("static-access")
	public StateFeedback executeCommand(String command, int index) {
		
		MyLogger logger = MyLogger.getInstance();
		logger.myLogger.info("executing user input: " + command);
		
		assert (index >= -1); // asserts that given index is non-negative OR -1(case when there is no tasks)
		logger.myLogger.info("Checking index: " + index + " >= -1 " );
		
		// parser take in a string and return it in its corresponding class obj
		Command currCmd = CommandParser.parseCommand(command,index);
		assert (currCmd != null); // asserts that parser returns a command object
		logger.myLogger.info("Checking cmd obj: " + currCmd + " is non null");
		
		String feedback;
		// To update if there are any deadlines that turned overdue
		_logicData.updateState();
		feedback = _logicCommand.processCommand(currCmd);
		
		StateFeedback state = _logicData.getState();
		state.setFeedback(feedback);
		_logicData.saveContents();
		return state;
	}
	
	/**
	 * Generates the help manual when requested by the user
	 * 
	 * @return String containing the help manual
	 */
	
	@SuppressWarnings("static-access")
	public String displayHelp() {
		
		MyLogger logger = MyLogger.getInstance();
		logger.myLogger.info("Help fn has been called");
		
		return _logicData.generateHelpManual();
	}
	
	/**
	 * Initialization of Logic upon launch of the program
	 * 
	 * @return StateFeedback containing the previously stored state
	 */
	
	@SuppressWarnings("static-access")
	public StateFeedback retrieveStartupState() {
		MyLogger logger = MyLogger.getInstance();
		logger.myLogger.info("Retrieving prev launched info");
		
		StateFeedback state = _logicData.getState();
		state.setFeedback(MESSAGE_WELCOME);
		return state;
	}

	/**
	 * Retrieval of current directory where the data is stored
	 * 
	 * @return String of location of current directory
	 */
	@SuppressWarnings("static-access")
	public String getCurrentSaveDirectory() {
		MyLogger logger = MyLogger.getInstance();
		logger.myLogger.info("Retrieving current help directory");
		return _logicData.retrieveCurrentDirectory();
	}
}
