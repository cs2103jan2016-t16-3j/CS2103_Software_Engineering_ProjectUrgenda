//@@author A0127358Y
package test.testLogic;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.Home;
import urgenda.command.Prioritise;
import urgenda.logic.LogicCommand;

public class LogicCommandTest {

	/*
	 * This method is a simple test for testing if processCommand in
	 * LogicCommand executes properly and correct feedback is returned.
	 */
	@Test
	public void testProcessCommand() {
		LogicCommand test = new LogicCommand();
		Home cmd = new Home();
		assertEquals("Showing all tasks", test.processCommand(cmd));
		Prioritise cmd2 = new Prioritise();
		assertEquals("No matches found to prioritise", test.processCommand(cmd2));
	}

}
