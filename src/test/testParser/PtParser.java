//@@author A0127764X
package test.testParser;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.Test;

import urgenda.parser.CommandParser;
import urgenda.parser.PublicVariables;
import urgenda.parser.PublicVariables.COMMAND_TYPE;

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
	
	//TODO:Description not parsed correctly.
	//All others correct
	@Test
	public void test003DeadlineTimePm() {
		String phrase = "add test001 by 03/03/2016 2:11pm";
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
	
	@Test
	public void test006DeadlineDateNoYear() {
		String phrase = "add test001 by 03/03 7:11am";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-03T07:11";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test007DeadlineDateNoYear() {
		String phrase = "add test001 by 3/3 7:11am";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-03T07:11";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test008DeadlineDateLeapYear() {
		String phrase = "add test001 by 2/29 7:11am";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-02-29T07:11";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test009DeadlineDateYY() {
		String phrase = "add test001 by 3/23/16 7:11am";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-23T07:11";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}

	@Test
	public void test010DeadlineTimeSpeltAm() {
		String phrase = "add test001 by 3/23/16 nine am";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-23T09:00";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	
	@Test
	public void test011DeadlineTimeSpeltPm() {
		String phrase = "add test001 by 3/23/16 nine pm";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-23T21:00";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
 
	@Test
	public void test012DeadlineDateSpeltYear() {
		String phrase = "add test001 by 23 MaR 2016 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-23T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test013DeadlineDateSpeltNoYear() {
		String phrase = "add test001 by Jan 23 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-01-23T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test015DeadlineDay() {
		String phrase = "add test001 by 15/4 at 2359";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-04-15T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test016NoAddKeyWord() {
		String phrase = "test001 by 15/4 at 2359";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-04-15T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test017DeadlineSeparateDateTime() {
		String phrase = "15/4 test001 abcd by 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-04-15T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001 abcd", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test017DeadlineSeparateDateTime2() {
		String phrase = "3/25 test001 by 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-25T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test017EventDefault() {
		String phrase = "test from 03/25 22:59 to 03/25 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String startTime = "2016-03-25T22:59";
		String endTime = "2016-03-25T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(startTime, PublicVariables.taskStartTime.toString());
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_EVENT, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test018EventSingleDate() {
		String phrase = "test from 03/25 22:59 to 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String startTime = "2016-03-25T22:59";
		String endTime = "2016-03-25T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(startTime, PublicVariables.taskStartTime.toString());
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_EVENT, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test019EventSeparateDateTimeSingleDate() {
		String phrase = "03/25 test abcd from 22:59 to 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String startTime = "2016-03-25T22:59";
		String endTime = "2016-03-25T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test abcd", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(startTime, PublicVariables.taskStartTime.toString());
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_EVENT, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test020EventSeparateDateTimeSingleDate2() {
		String phrase = "test 13/4 abcd 22:59 - 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String startTime = "2016-04-13T22:59";
		String endTime = "2016-04-13T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test abcd", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(startTime, PublicVariables.taskStartTime.toString());
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_EVENT, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test021EventSeparateDateTimeSingleDate3() {
		String phrase = "test 13/4 for abcd 22:59 - 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String startTime = "2016-04-13T22:59";
		String endTime = "2016-04-13T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test for abcd", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(startTime, PublicVariables.taskStartTime.toString());
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_EVENT, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test022EventSeparateDateTimeSingleDate4() {
		String phrase = "test on 3/30 for abcd 22:59 - 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String startTime = "2016-03-30T22:59";
		String endTime = "2016-03-30T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test for abcd", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(startTime, PublicVariables.taskStartTime.toString());
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_EVENT, PublicVariables.taskType.toString());	
	}
	
	@Test
	public void test023InsertLocation() {
		String phrase = "test at headquarter 3/30 22:59 - 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String startTime = "2016-03-30T22:59";
		String endTime = "2016-03-30T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test", PublicVariables.taskDescription);
		assertEquals("headquarter", PublicVariables.taskLocation);
		assertEquals(startTime, PublicVariables.taskStartTime.toString());
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_EVENT, PublicVariables.taskType.toString());	
	}
	
	@Test
	public void test024InsertLocation2() {
		String phrase = "test 3/30 22:59 - 23:59 at headquarter";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String startTime = "2016-03-30T22:59";
		String endTime = "2016-03-30T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test", PublicVariables.taskDescription);
		assertEquals("headquarter", PublicVariables.taskLocation);
		assertEquals(startTime, PublicVariables.taskStartTime.toString());
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_EVENT, PublicVariables.taskType.toString());	
	}
	
	@Test
	public void test025InsertLocation3() {
		String phrase = "test @headquarter 3/30 22:59 - 23:59 ";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String startTime = "2016-03-30T22:59";
		String endTime = "2016-03-30T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test", PublicVariables.taskDescription);
		assertEquals("headquarter", PublicVariables.taskLocation);
		assertEquals(startTime, PublicVariables.taskStartTime.toString());
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_EVENT, PublicVariables.taskType.toString());	
	}
	
	@Test
	public void test028DeleteFunctionByIndex() {
		String phrase = "delete 1";
		CommandParser.parseCommand(phrase, 5);
		Integer expectedIndex = 0;
		assertEquals(COMMAND_TYPE.DELETE, PublicVariables.commandType);
		assertEquals(expectedIndex, PublicVariables.positions.get(0));
		assertEquals("", PublicVariables.taskDescription);
	}
	
	@Test
	public void test029DeleteFunctionByPosition() {
		String phrase = "delete";
		CommandParser.parseCommand(phrase, 5);
		Integer expectedIndex = 5;
		assertEquals(COMMAND_TYPE.DELETE, PublicVariables.commandType);
		assertEquals(expectedIndex, PublicVariables.positions.get(0));
		assertEquals("", PublicVariables.taskDescription);
	}
	
	@Test
	public void test030DeleteFunctionByDesc() {
		String phrase = "delete abc";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.DELETE, PublicVariables.commandType);
		assertEquals(-10, PublicVariables.taskIndex);
		assertEquals("abc", PublicVariables.taskDescription);
	}
	
	@Test
	public void test031PriFunctionByIndex() {
		String phrase = "pri 1";
		CommandParser.parseCommand(phrase, 5);
		Integer expectedIndex = 0;
		assertEquals(COMMAND_TYPE.PRIORITISE, PublicVariables.commandType);
		assertEquals(expectedIndex, PublicVariables.positions.get(0));
		assertEquals("", PublicVariables.taskDescription);
	}
	
	@Test
	public void test032PriFunctionByPosition() {
		String phrase = "pri";
		CommandParser.parseCommand(phrase, 5);
		Integer expectedIndex = 5;
		assertEquals(COMMAND_TYPE.PRIORITISE, PublicVariables.commandType);
		assertEquals(expectedIndex, PublicVariables.positions.get(0));
		assertEquals("", PublicVariables.taskDescription);
	}
	
	@Test
	public void test033PriFunctionByDesc() {
		String phrase = "pri abc";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.PRIORITISE, PublicVariables.commandType);
		assertEquals(-10, PublicVariables.taskIndex);
		assertEquals("abc", PublicVariables.taskDescription);
	}
	
	@Test
	public void test034DoneFunctionByIndex() {
		String phrase = "done 1";
		CommandParser.parseCommand(phrase, 5);
		Integer expectedIndex = 0;
		assertEquals(COMMAND_TYPE.COMPLETE, PublicVariables.commandType);
		assertEquals(expectedIndex, PublicVariables.positions.get(0));
		assertEquals("", PublicVariables.taskDescription);
	}
	
	@Test
	public void test035DoneFunctionByPosition() {
		String phrase = "done";
		CommandParser.parseCommand(phrase, 5);
		Integer expectedIndex = 5;
		assertEquals(COMMAND_TYPE.COMPLETE, PublicVariables.commandType);
		assertEquals(expectedIndex, PublicVariables.positions.get(0));
		assertEquals("", PublicVariables.taskDescription);
	}
	
	@Test
	public void test036DoneFunctionByDesc() {
		String phrase = "done abc";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.COMPLETE, PublicVariables.commandType);
		assertEquals(-10, PublicVariables.taskIndex);
		assertEquals("abc", PublicVariables.taskDescription);
	}
	
	@Test
	public void test037ExitFunction() {
		String phrase = "exit";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.EXIT, PublicVariables.commandType);
	}
	
	@Test
	public void test038UndoFunction() {
		String phrase = "undo";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.UNDO, PublicVariables.commandType);
	}
	
	@Test
	public void test039RedoFunction() {
		String phrase = "redo";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.REDO, PublicVariables.commandType);
	}
	
	@Test
	public void test040ShowArchiveFunction() {
		String phrase = "archive";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.SHOW_ARCHIVE, PublicVariables.commandType);
	}
	
	@Test
	public void test041HomeFunction() {
		String phrase = "home";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.HOME, PublicVariables.commandType);
	}
	
	@Test
	public void test042HideFunction() {
		String phrase = "hide";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.HIDE, PublicVariables.commandType);
	}
	
	@Test
	public void test043HelpFunction() {
		String phrase = "help";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.HELP, PublicVariables.commandType);
	}
	
	@Test
	public void test044DemoFunction() {
		String phrase = "demo";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.DEMO, PublicVariables.commandType);
	}
	
	@Test
	public void test045SetDirectoryFunction() {
		String phrase = "saveto abcd";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.SET_DIRECTORY, PublicVariables.commandType);
	}
}
