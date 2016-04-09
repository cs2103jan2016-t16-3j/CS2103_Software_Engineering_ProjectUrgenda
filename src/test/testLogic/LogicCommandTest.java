package test.testLogic;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.Home;
import urgenda.command.Prioritise;
import urgenda.logic.LogicCommand;

public class LogicCommandTest {

	@Test
	public void testProcessCommand() {
		LogicCommand test = new LogicCommand();
		Home cmd = new Home();
		assertEquals("Showing all tasks", test.processCommand(cmd));
		Prioritise cmd2 = new Prioritise();
		assertEquals("No matches found to prioritise", test.processCommand(cmd2));
	}

}
