package testCommand;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.Help;

public class HelpTest {

	@Test
	public void test() {
		Help tester = new Help();
		assertEquals("", tester.execute());
	}

}
