package test.testCommand;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.Undo;
import urgenda.logic.LogicData;

public class UndoTest {

	@Test
	public void testExecute() {
		LogicData _data = LogicData.getInstance(true);
		Undo tester = new Undo();
		assertEquals("Undo: ", tester.execute());
		_data.clearTasks();
	}

}
