package test.testCommand;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.Redo;
import urgenda.logic.LogicData;

public class RedoTest {

	@Test
	public void testExecute() {
		LogicData _data = LogicData.getInstance(true);
		Redo tester = new Redo();
		assertEquals("Redo: ", tester.execute());
		_data.clearTasks();
	}

}
