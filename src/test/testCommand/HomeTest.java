package test.testCommand;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.Home;
import urgenda.logic.LogicData;

public class HomeTest {

	// this fn is for testing if correct feedback msg is return for home command
	@Test
	public void test() {
		LogicData _data = LogicData.getInstance(true);
		Home tester = new Home();
		assertEquals("Showing all tasks", tester.execute());
		_data.clearTasks();
	}

}
