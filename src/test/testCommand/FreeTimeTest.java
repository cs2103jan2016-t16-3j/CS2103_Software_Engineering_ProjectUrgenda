package testLogic;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.Test;

import org.junit.runners.MethodSorters;

import urgenda.command.FindFree;
import urgenda.logic.LogicData;
import urgenda.util.Task;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FreeTimeTest {
	
	//1 task comparing with range
	@Test
	public void test001FreeTimeOneTask() {
		//setting up the test
		LogicData data = LogicData.getInstance();
		data.clearTasks();
		Task task1 = new Task();
		LocalDateTime start1 = LocalDateTime.of(2016, 3, 23, 11, 0);
		LocalDateTime end1 = LocalDateTime.of(2016, 3, 23, 14, 0);
		task1.setStartTime(start1);
		task1.setEndTime(end1);
		task1.updateTaskType(start1, end1);
		data.addTask(task1);
		
		
		//configuring expected outputs
		String expectedPhrase = "Showing time slots where there are no events between 23/3, 12:00 to 23/3, 18:00";
		ArrayList<Task> expectedList = new ArrayList<Task>();
		Task exTask1 = new Task();
		LocalDateTime exStart1 = LocalDateTime.of(2016, 3, 23, 14, 0);
		LocalDateTime exEnd1 = LocalDateTime.of(2016, 3, 23, 18, 0);
		exTask1.setStartTime(exStart1);
		exTask1.setEndTime(exEnd1);
		expectedList.add(exTask1);
		
		//getting actual outputs
		LocalDateTime rangeStart = LocalDateTime.of(2016, 3, 23, 12, 0);
		LocalDateTime rangeEnd = LocalDateTime.of(2016, 3, 23, 18, 0);
		FindFree test = new FindFree(rangeStart, rangeEnd);
		String actualPhrase = test.execute();
		assertEquals(expectedPhrase, actualPhrase);
		ArrayList<Task> actualList = data.getDisplays();
		
		//comparing expected and actual free time range
		assertEquals(expectedList.size(), actualList.size());
		for (int i = 0; i < expectedList.size(); i++){
			Task actual = actualList.get(i);
			Task expected = expectedList.get(i);
			assertEquals(expected.getStartTime(), actual.getStartTime());
			assertEquals(expected.getEndTime(), actual.getEndTime());
		}
		
		

	}

}
