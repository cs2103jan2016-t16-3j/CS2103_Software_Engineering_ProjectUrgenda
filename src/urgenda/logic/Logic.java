package urgenda.logic;

import urgenda.command.Command;
import urgenda.parser.Parser;

public class Logic {

	private static final String MESSAGE_WELCOME = "Welcome to Urgenda. Your task manager is ready for use.";

	private LogicData _logicData; //attributes of logic?? may include more as we code
	
	public Logic() {
		_logicData = new LogicData();//constructor of logic data, every new launch will empty data in logic data
	}
	
	public static String launchProg() {
		_logicData.retrieveData();
		return MESSAGE_WELCOME;	
	}
	
	public static String executeCommand(String command) {
		Command currCmd = Parser.parseCommand(command); //parser take in a string and return it in its corresponding class obj
		return currCmd.execute();
	}
}
