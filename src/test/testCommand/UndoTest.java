package test.testCommand;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.Undo;

public class UndoTest {

	@Test
	public void testExecute() {
		Undo tester = new Undo();
		assertEquals("Undo: ", tester.execute());
	}

}
