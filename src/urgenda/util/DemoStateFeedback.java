package urgenda.util;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DemoStateFeedback extends StateFeedback{
	
	public DemoStateFeedback() {
		super();
		Task taskOverdue = new Task("Complete tutorial", null, null, LocalDateTime.now().minusDays(1).withHour(23).withMinute(59),
				new ArrayList<String>(), false);
		Task taskTodayImportant = new Task("Breakfast with mum and dad", null, LocalDateTime.now().withHour(7).withMinute(0), LocalDateTime.now().withHour(8).withMinute(0),
				new ArrayList<String>(), false);
		taskTodayImportant.setIsCompleted(true);
		Task taskToday1 = new Task("NUS Overseas Colleges Workshop", null, LocalDateTime.now().minusDays(1).withHour(10).withMinute(0), LocalDateTime.now().plusDays(1).withHour(22).withMinute(0),
				new ArrayList<String>(), false);
		Task taskImportant = new Task("Internship interview", null, LocalDateTime.now().plusDays(6).withHour(10).withMinute(0), LocalDateTime.now().plusDays(6).withHour(11).withMinute(0),
				new ArrayList<String>(), true);
		taskImportant.setSlot(new MultipleSlot());
		taskImportant.getSlot().addTimeSlot(LocalDateTime.now().plusDays(6).withHour(11).withMinute(0), LocalDateTime.now().plusDays(6).withHour(12).withMinute(0));
		taskImportant.getSlot().addTimeSlot(LocalDateTime.now().plusDays(6).withHour(12).withMinute(0), LocalDateTime.now().plusDays(6).withHour(13).withMinute(0));
		Task taskOverrun = new Task("Get groceries", "Supermarket", LocalDateTime.now().plusDays(2).withHour(17).withMinute(0), LocalDateTime.now().plusDays(2).withHour(18).withMinute(0), new ArrayList<String>(),
				false);
		taskOverrun.setSlot(new MultipleSlot());
		taskOverrun.getSlot().addTimeSlot(LocalDateTime.now().plusDays(3).withHour(17).withMinute(0), LocalDateTime.now().plusDays(3).withHour(18).withMinute(0));
		Task taskDetailedLong = new Task("Success is the sum of small efforts, repeated day in and day out. - Robert Collier", null, null, null,
				new ArrayList<String>(), false);
		Task taskCompleted = new Task("Dental Appointment", "Hougang Polyclinic", LocalDateTime.now().minusDays(8).withHour(10).withMinute(30), LocalDateTime.now().minusDays(8).withHour(12).withMinute(00), new ArrayList<String>(),
				false);
		
		ArrayList<Task> tasks = new ArrayList<Task>();
		ArrayList<Task> archives = new ArrayList<Task>();
		tasks.add(taskOverdue);
		tasks.add(taskTodayImportant);
		tasks.add(taskToday1);
		tasks.add(taskImportant);
		tasks.add(taskOverrun);
		tasks.add(taskDetailedLong);
		archives.add(taskCompleted);
		this.setAllTasks(new TaskList(tasks, archives, 1, 2, 3, 1));
		this.setFeedback("Welcome to Urgenda's demo view!\n Type \"home\" to exit.");
		this.addDetailedTaskIdx(4);
		this.addDetailedTaskIdx(6);
		this.setOverdueCount(1);
		this.setState(StateFeedback.State.ALL_TASKS);
	}
}
