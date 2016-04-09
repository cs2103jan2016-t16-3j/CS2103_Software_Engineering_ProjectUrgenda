package test.testCommand;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.SetDirectory;
import urgenda.logic.LogicData;

public class SetDirectoryTest {

	@Test
	public void testExecute() {
		LogicData _data = LogicData.getInstance(true);
		SetDirectory test = new SetDirectory("");
		// test invalid case, valid case has been tested to be working by
		// dogfooding.
		// not testing valid using junit as dont want to change the directory of
		// the tests.
		assertEquals("Invalid file directory", test.execute());
		_data.clearTasks();

	}

}
