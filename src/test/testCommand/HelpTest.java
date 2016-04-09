package test.testCommand;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.command.Help;
import urgenda.logic.LogicData;

public class HelpTest {

	@Test
	public void test() {
		LogicData _data = LogicData.getInstance(true);
		Help tester = new Help();
		assertEquals("", tester.execute());
		_data.clearTasks();
	}

}
