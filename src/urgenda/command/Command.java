package urgenda.command;

import urgenda.logic.LogicData;

/**
 * Command interface for implementation of subsequent command classes
 *
 */
public interface Command {
	
	// for execution of specific command
	String execute(LogicData data);

}
