package test.testUtils;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import urgenda.util.SuggestCommand;

public class SuggestCommandTest {

	@Test
	public void testConstructor() {
		ArrayList<String> commands = new ArrayList<String>();
		String currCmd = "test";
		SuggestCommand suggCmd = new SuggestCommand(SuggestCommand.Command.ADD, commands, currCmd);
		assertEquals(SuggestCommand.Command.ADD, suggCmd.getConfirmedCommand());
		assertEquals(commands, suggCmd.getPossibleCommands());
		assertEquals(currCmd, suggCmd.getCurrCmd());
		
		suggCmd = new SuggestCommand(null, null, null);
		assertEquals(null, suggCmd.getConfirmedCommand());
		assertEquals(null, suggCmd.getPossibleCommands());
		assertEquals(null, suggCmd.getCurrCmd());
	}

}
