package urgenda.logic;

import urgenda.command.Command;
import urgenda.parser.Parser;

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
	
	public String executeCommand(String command) {
		Command currCmd = Parser.parseCommand(command); //parser take in a string and return it in its corresponding class obj
		String feedback = currCmd.execute(_logicData); 
		// TODO to be confirmed when KS decides if he can access directly or we need to return state
		_logicData.addUndo(currCmd);
		_logicData.saveContents();
	}
}
