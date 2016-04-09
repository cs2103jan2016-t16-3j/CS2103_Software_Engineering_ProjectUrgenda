package test.testCommand;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.ShowArchive;
import urgenda.logic.LogicData;

public class ShowArchiveTest {

	// this fn is for testing if correct feedback msg is return for ShowArchive
	// command
	@Test
	public void test() {
		LogicData _data = LogicData.getInstance(true);
		ShowArchive tester = new ShowArchive();
		assertEquals("Showing all archived tasks", tester.execute());
		_data.clearTasks();
	}

}
