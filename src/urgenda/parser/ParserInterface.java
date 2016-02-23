package urgenda.parser;

import urgenda.command.Command;

public interface ParserInterface {

	// method called from logic
	Command parseCommand(String command);

}
