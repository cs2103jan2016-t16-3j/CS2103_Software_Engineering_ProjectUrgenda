package testUrgenda;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.logic.Logic;
import urgenda.logic.LogicData;

public class RunTest {

	@Test
	public void test() {
		Logic logic = Logic.getInstance(true);
		assertTrue(true);
	}

}
