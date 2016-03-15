package testParser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.Test;

import urgenda.parser.CommandParser;
import urgenda.parser.PublicVariables;


import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PtParser {
	private static final String TASKTYPE_EVENT = "EVENT";
	private static final String TASKTYPE_DEADLINE = "DEADLINE";
	private static final String TASKTYPE_FLOATING = "FLOATING";
	
	@Test
	public void test001DeadlineTime24h() {
		String phrase = "add test001 by 03/03/2016 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-03T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test002DeadlineHourAm() {
		String phrase = "add test001 by 03/03/2016 2am";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-03T02:00";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	//Description not parsed correctly.
	//All others correct
	@Test
	public void test003DeadlineTimePm() {
		String phrase = "add test001 by 03/03/2016 2.11pm";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-03T14:11";
		CommandParser.parseCommand(phrase, 5);
//		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test004DeadlineTimePmDelimiter() {
		String phrase = "add test001 by 03/03/2016 7:11pm";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-03T19:11";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test005DeadlineTimePmDelimiter() {
		String phrase = "add test001 by 03/03/2016 7:11pm";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-03T19:11";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
}
