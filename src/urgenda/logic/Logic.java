package urgenda.logic;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import urgenda.command.Command;
import urgenda.parser.Parser;
import urgenda.util.StateFeedback;

public class Logic {
	
	private static final String MESSAGE_WELCOME = "Welcome to Urgenda! Your task manager is ready for use. \nPress ALT + F1 if you need help.";

	private LogicData _logicData;
	private LogicCommand _logicCommand;
	
	private static Logger myLogger = Logger.getLogger(Logic.class.getName());
	private static Handler consoleHandler = null;
	private static Handler fileHandler = null;

	// Default constructor for Logic
	public Logic() {
		_logicData = new LogicData();
		_logicCommand = new LogicCommand(_logicData);
	}
	
	
	public StateFeedback executeCommand(String command, int index) {
		/* try {
		consoleHandler = new ConsoleHandler();
		fileHandler  = new FileHandler("UrgendaLog.txt",true);
		myLogger.addHandler(consoleHandler);
		myLogger.addHandler(fileHandler);
		consoleHandler.setLevel(Level.ALL);
		fileHandler.setLevel(Level.ALL);
		myLogger.setLevel(Level.ALL);
		myLogger.config("Configuration done.");
		myLogger.removeHandler(consoleHandler);
		} catch (IOException e){
			myLogger.log(Level.SEVERE, "Error occur in FileHandler.", e);
		} */
		
		myLogger.info("executing userinput: " + command);
		
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
	
	public String displayHelp() {
		/* try {
			consoleHandler = new ConsoleHandler();
			fileHandler  = new FileHandler("UrgendaLog.txt",true);
			myLogger.addHandler(consoleHandler);
			myLogger.addHandler(fileHandler);
			consoleHandler.setLevel(Level.ALL);
			fileHandler.setLevel(Level.ALL);
			myLogger.setLevel(Level.ALL);
			myLogger.config("Configuration done.");
			myLogger.removeHandler(consoleHandler);
			} catch (IOException e){
				myLogger.log(Level.SEVERE, "Error occur in FileHandler.", e);
			} */
		
		myLogger.info("Help fn has been called");
		
		return _logicData.generateHelpManual();
	}
	
	public StateFeedback retrieveStartupState() {
		try {
			consoleHandler = new ConsoleHandler();
			fileHandler  = new FileHandler("UrgendaLog.txt",true);
			myLogger.addHandler(consoleHandler);
			myLogger.addHandler(fileHandler);
			consoleHandler.setLevel(Level.ALL);
			fileHandler.setLevel(Level.ALL);
			myLogger.setLevel(Level.ALL);
			myLogger.config("Configuration done.");
			myLogger.removeHandler(consoleHandler);
			} catch (IOException e){
				myLogger.log(Level.SEVERE, "Error occur in FileHandler.", e);
			}
		
		myLogger.info("retrieving prev launched info");
		
		StateFeedback state = _logicData.getState();
		state.setFeedback(MESSAGE_WELCOME);
		return state;
	}
}
