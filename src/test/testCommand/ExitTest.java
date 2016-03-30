package testCommand;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.Exit;

public class ExitTest {

	//this fn is for testing whether a correct null status feedback is return when exit cmd is executed
	@Test
	public void test() {
		Exit tester = new Exit();
		assertEquals(null, tester.execute());
	}

}
