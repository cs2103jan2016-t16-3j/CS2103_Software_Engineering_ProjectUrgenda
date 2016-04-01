package test.testCommand;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.Home;

public class HomeTest {

	//this fn is for testing if correct feedback msg is return for home command
	@Test
	public void test() {
		Home tester = new Home();
		assertEquals("Showing all tasks", tester.execute());
	}

}
