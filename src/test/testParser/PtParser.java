package testParser;

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
	
	//TODO:Cannot parse DD/MM. Parses only MM/DD
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
	
	//TODO:Similarly no DD/MM/YYYY. only MM/DD/YYYY
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
	
	//TODO:Cant parse description properly. returns as "test001 by 3/23/16 seven am"
	//TODO:seven-thirty am is parsed as 18:00, even though I want 0730
	//TODO: seven thirty am is parsed as 0700, even though I want 0730
	//TODO: ten/eleven/twelve pm/am is parsed as 0100, even though I want 2200
	//time is parsed correctly though
	@Test
	public void test010DeadlineTimeSpeltAm() {
		String phrase = "add test001 by 3/23/16 nine am";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String endTime = "2016-03-23T09:00";
		CommandParser.parseCommand(phrase, 5);
//		assertEquals("test001", PublicVariables.taskDescription);
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
//		assertEquals("test001", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(null, PublicVariables.taskStartTime);
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
	}
	
	//TODO: cnt write 23 march 16 2359 cnt parse.
	//TODO: cnt parse "add test001 by 23 Mar 2359", returns time as time.now() delimiter ":" works, "." doesn't. 
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
	
	//TODO: Can't parse without time
//	@Test
//	public void test014DeadlineDateSpeltNoTime() {
//		String phrase = "add test001 by Jan 23";
//		ArrayList<String> testHashtags = new ArrayList<String>();
//		String endTime = "2016-01-23T23:59";
//		CommandParser.parseCommand(phrase, 5);
//		assertEquals("test001", PublicVariables.taskDescription);
//		assertEquals("",PublicVariables.taskLocation);
//		assertEquals(null, PublicVariables.taskStartTime);
//		assertEquals(endTime, PublicVariables.taskEndTime.toString());
//		assertEquals(testHashtags, PublicVariables.taskHashtags);
//		assertEquals(TASKTYPE_DEADLINE, PublicVariables.taskType.toString());
//	}
	
	//TODO: Same can't parse without time given
	@Test
	public void test015DeadlineDay() {
		String phrase = "add test001 by Friday at 2359";
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
	public void test016NoAddKeyWord() {
		String phrase = "test001 by Friday at 2359";
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
	public void test017DeadlineSeparateDateTime() {
		String phrase = "Friday test001 by 23:59";
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
		String phrase = "03/25 test from 22:59 to 23:59";
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
	public void test020EventSeparateDateTimeSingleDate2() {
		String phrase = "next wed test 22:59 - 23:59";
		ArrayList<String> testHashtags = new ArrayList<String>();
		String startTime = "2016-03-30T22:59";
		String endTime = "2016-03-30T23:59";
		CommandParser.parseCommand(phrase, 5);
		assertEquals("test", PublicVariables.taskDescription);
		assertEquals("",PublicVariables.taskLocation);
		assertEquals(startTime, PublicVariables.taskStartTime.toString());
		assertEquals(endTime, PublicVariables.taskEndTime.toString());
		assertEquals(testHashtags, PublicVariables.taskHashtags);
		assertEquals(TASKTYPE_EVENT, PublicVariables.taskType.toString());
	}
	
	@Test
	public void test021EventSeparateDateTimeSingleDate3() {
		String phrase = "test next wed for abcd 22:59 - 23:59";
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
	public void test026InsertHashtag1() {
		String phrase = "test @headquarter 3/30 22:59 - 23:59 #impt";
		ArrayList<String> testHashtags = new ArrayList<String>();
		testHashtags.add("#impt");
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
	public void test027InsertHashtag2() {
		String phrase = "test @headquarter 3/30 22:59 - 23:59 #impt #now";
		ArrayList<String> testHashtags = new ArrayList<String>();
		testHashtags.add("#impt");
		testHashtags.add("#now");
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
		assertEquals(COMMAND_TYPE.DELETE, PublicVariables.commandType);
		assertEquals(0, PublicVariables.taskIndex);
		assertEquals("", PublicVariables.taskDescription);
	}
	
	@Test
	public void test029DeleteFunctionByPosition() {
		String phrase = "delete";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.DELETE, PublicVariables.commandType);
		assertEquals(5, PublicVariables.taskIndex);
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
		assertEquals(COMMAND_TYPE.PRIORITISE, PublicVariables.commandType);
		assertEquals(0, PublicVariables.taskIndex);
		assertEquals("", PublicVariables.taskDescription);
	}
	
	@Test
	public void test032PriFunctionByPosition() {
		String phrase = "pri";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.PRIORITISE, PublicVariables.commandType);
		assertEquals(5, PublicVariables.taskIndex);
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
		assertEquals(COMMAND_TYPE.COMPLETE, PublicVariables.commandType);
		assertEquals(0, PublicVariables.taskIndex);
		assertEquals("", PublicVariables.taskDescription);
	}
	
	@Test
	public void test035DoneFunctionByPosition() {
		String phrase = "done";
		CommandParser.parseCommand(phrase, 5);
		assertEquals(COMMAND_TYPE.COMPLETE, PublicVariables.commandType);
		assertEquals(5, PublicVariables.taskIndex);
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
}
