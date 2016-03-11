package urgenda.command;

import urgenda.logic.LogicData;

/**
 * Command interface for implementation of subsequent command classes
 *
 */
public abstract class Command {

	// for execution of specific command
	public abstract String execute(LogicData data) throws Exception;
}

