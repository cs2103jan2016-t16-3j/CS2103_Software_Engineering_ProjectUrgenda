package test.testCommand;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.Exit;
import urgenda.logic.LogicData;

public class ExitTest {

	// this fn is for testing whether a correct null status feedback is return
	// when exit cmd is executed
	@Test
	public void test() {
		LogicData _data = LogicData.getInstance(true);
		Exit tester = new Exit();
		assertEquals(null, tester.execute());
		_data.clearTasks();
	}

}
