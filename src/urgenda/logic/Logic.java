package urgenda.logic;

import java.util.ArrayList;

import urgenda.command.AddTask;
import urgenda.command.BlockSlots;
import urgenda.command.Command;
import urgenda.command.Complete;
import urgenda.command.Demo;
import urgenda.command.Exit;
import urgenda.command.Home;
import urgenda.command.Invalid;
import urgenda.command.ShowDetails;
import urgenda.command.TaskCommand;
import urgenda.parser.CommandParser;
import urgenda.util.StateFeedback;
import urgenda.util.SuggestCommand;
import urgenda.util.SuggestFeedback;
import urgenda.util.UrgendaLogger;

public class Logic {

	private static final String MESSAGE_WELCOME = "Welcome to Urgenda!\nType \"demo\" for a simple demo, type \"help\" for the help menu.";
	private static UrgendaLogger logger = UrgendaLogger.getInstance();
	private static Logic _logic;
	private LogicData _logicData;
	private LogicCommand _logicCommand;
	private LogicSuggester _logicSuggestion;

	// Default constructor for Logic
	private Logic() {
		_logicData = LogicData.getInstance();
		_logicCommand = new LogicCommand();
		_logicSuggestion = new LogicSuggester();
	}

	private Logic(boolean isTest) {
		_logicData = LogicData.getInstance(isTest);
		_logicCommand = new LogicCommand();
		_logicSuggestion = new LogicSuggester();
	}

	// Implementation of Singleton pattern for Logic
	public static Logic getInstance() {
		if (_logic == null) {
			logger.getLogger().info("creating instance of logic");
			_logic = new Logic();
		}
		logger.getLogger().info("retrieving prev instance of logic");
		return _logic;
	}

	// alternative constructor for testing purposes
	public static Logic getInstance(boolean isTest) {
		if (_logic == null) {
			_logic = new Logic(isTest);
		}
		return _logic;
	}

	/**
	 * Executes the command given in string format, taking the relevant position
	 * if required
	 * 
	 * @param command input string by the user
	 * @param index current index pointed at by the user
	 * @return StateFeedback which includes the current state of tasks as well
	 *         as feedback line
	 */
	public StateFeedback executeCommand(String command, int index) {

		logger.getLogger().info("executing user input: " + command);

		assert (index >= -1); // asserts that given index is non-negative OR -1(case when there is no tasks)
		logger.getLogger().info("Checking index: " + index + " >= -1 ");

		// parser take in a string and return it in its corresponding class obj
		Command currCmd = CommandParser.parseCommand(command, index);
		assert (currCmd != null); // asserts that parser returns a command object
		logger.getLogger().info("Checking cmd obj: " + currCmd + " is non null");

		// To ensure that the command is applicable to the state
		currCmd = checkAndFilterCommand(currCmd);

		String feedback;
		// To update if there are any deadlines that turned overdue
		_logicData.updateState();
		feedback = _logicCommand.processCommand(currCmd);

		// To update after the command has been processed to ensure that the
		// newly edited tasks are updated
		_logicData.updateState();

		// To check and change accordingly if the pointer is pointed towards an
		// archived task after the state is updated
		_logicData.checkPointer();
		StateFeedback state = _logicData.getState();
		state.setFeedback(feedback);
		_logicData.saveContents();
		return state;
	}

	private Command checkAndFilterCommand(Command currCmd) {
		if (_logicData.getCurrState() == LogicData.DisplayState.FIND_FREE) {
			if (currCmd instanceof TaskCommand || currCmd instanceof ShowDetails) {
				if (currCmd instanceof AddTask || currCmd instanceof BlockSlots) {
					// allow the addition of tasks
				} else {
					currCmd = new Invalid(LogicData.DisplayState.FIND_FREE);
				}

			}
		} else if (_logicData.getCurrState() == LogicData.DisplayState.ARCHIVE) {
			// TODO settle archive cases
			if (currCmd instanceof Complete) {
				currCmd = new Invalid(LogicData.DisplayState.ARCHIVE);
			}
		} else if (_logicData.getCurrState() == LogicData.DisplayState.DEMO) {
			if (currCmd instanceof Home || currCmd instanceof Exit) {
				// allow home
			} else {
				// continues as demo
				currCmd = new Demo();
			}
		}

		return currCmd;
	}

	/**
	 * Generates the help manual when requested by the user
	 * 
	 * @return String containing the help manual
	 */
	public ArrayList<String> displayHelp() {
		logger.getLogger().info("Help fn has been called");

		return _logicData.generateHelpManual();
	}
	
	/**
	 * Retrieves the demo text for Urgenda for demo mode
	 * 
	 * @return ArrayList of text containing instructions for demo mode
	 */
	public ArrayList<String> getDemoText() {
		return _logicData.generateDemoText();
	}
	
	/** 
	 * Retrieves the corresponding selector index for demonstration purposes in demo mode
	 * 
	 * @return ArrayList of indexes that is selected for each state in demo modes
	 */
	public ArrayList<Integer> getDemoSelectionIndexes() {
		return _logicData.generateDemoSelectionIndexes();
	}
	
	/**
	 * Initialization of Logic upon launch of the program
	 * 
	 * @return StateFeedback containing the previously stored state
	 */
	public StateFeedback retrieveStartupState() {
		logger.getLogger().info("Retrieving prev launched info");
		_logicData.clearOldArchive();
		StateFeedback state = _logicData.getState();
		state.setFeedback(MESSAGE_WELCOME);
		return state;
	}

	/**
	 * Retrieval of current directory where the data is stored
	 * 
	 * @return String of location of current directory
	 */
	public String getCurrentSaveDirectory() {
		logger.getLogger().info("Retrieving current help directory");
		return _logicData.retrieveCurrentDirectory();
	}

	/**
	 * Enable suggestion of the command while user is typing
	 * 
	 * @param currCmd current string that the user is typing
	 * @return SuggestFeedback including strings of suggestions to user
	 */
	public SuggestFeedback getSuggestions(String currCmd) {
		SuggestCommand suggCmd = CommandParser.parseRuntimeInput(currCmd);
		return _logicSuggestion.processSuggestions(suggCmd);
	}

	// clear storage for testing purposes
	public void clearStorageTester() {
		_logicData.reinitialiseStorageTester();
	}
}
