package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import urgenda.storage.Storage;

public class TestStorage {

	@Test
	public void testCheckUrgent() {
		Storage test = new Storage();
		assertFalse(test.checkUrgent());
	}
	
	@Test public void testGSONTester(){
		Storage test = new Storage();
		test.GSONTester();
	}

}
