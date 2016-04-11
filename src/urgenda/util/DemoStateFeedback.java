package urgenda.util;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Utility class to create a StateFeedback object used during demo.
 * 
 * @author KangSoon
 */
public class DemoStateFeedback extends StateFeedback {

	// Constants
	private static final String FEEDBACK_MESSAGE_DEMO = "Use TAB and SHIFT + TAB to navigate during the demo.\n"
			+ "Type \"home\" to exit.";

	/**
	 * Creates a DemoStateFeedback instance.
	 */
	public DemoStateFeedback() {
		super();
		ArrayList<Task> tasks = createTaskList();
		ArrayList<Task> archives = createArchiveList();
		this.setAllTasks(new TaskList(tasks, archives, 1, 2, 3, 1));
		this.setFeedback(FEEDBACK_MESSAGE_DEMO);
		this.addDetailedTaskIdx(4);
		this.addDetailedTaskIdx(6);
		this.setOverdueCount(1);
		this.setState(StateFeedback.State.ALL_TASKS);
	}

	private ArrayList<Task> createArchiveList() {
		ArrayList<Task> archives = new ArrayList<Task>();
		Task task7 = new Task("Dental Appointment", "Hougang Polyclinic",
				LocalDateTime.now().minusDays(8).withHour(10).withMinute(30),
				LocalDateTime.now().minusDays(8).withHour(12).withMinute(00), false);
		archives.add(task7);
		return archives;
	}

	private ArrayList<Task> createTaskList() {
		Task task1 = new Task("Complete tutorial", null, null,
				LocalDateTime.now().minusDays(1).withHour(23).withMinute(59), false);
		Task task2 = new Task("Breakfast with mum and dad", null,
				LocalDateTime.now().withHour(7).withMinute(0), LocalDateTime.now().withHour(8).withMinute(0),
				false);
		task2.setIsCompleted(true);
		Task task3 = new Task("NUS Overseas Colleges Workshop", null,
				LocalDateTime.now().minusDays(1).withHour(10).withMinute(0),
				LocalDateTime.now().plusDays(1).withHour(22).withMinute(0), false);
		Task task4 = new Task("Internship interview", null,
				LocalDateTime.now().plusDays(6).withHour(10).withMinute(0),
				LocalDateTime.now().plusDays(6).withHour(11).withMinute(0), true);
		task4.setSlot(new MultipleSlot());
		task4.getSlot().addTimeSlot(LocalDateTime.now().plusDays(6).withHour(11).withMinute(0),
				LocalDateTime.now().plusDays(6).withHour(12).withMinute(0));
		task4.getSlot().addTimeSlot(LocalDateTime.now().plusDays(6).withHour(12).withMinute(0),
				LocalDateTime.now().plusDays(6).withHour(13).withMinute(0));
		Task task5 = new Task("Get groceries", "Supermarket",
				LocalDateTime.now().plusDays(2).withHour(17).withMinute(0),
				LocalDateTime.now().plusDays(2).withHour(18).withMinute(0), false);
		task5.setSlot(new MultipleSlot());
		task5.getSlot().addTimeSlot(LocalDateTime.now().plusDays(3).withHour(17).withMinute(0),
				LocalDateTime.now().plusDays(3).withHour(18).withMinute(0));
		Task task6 = new Task(
				"Success is the sum of small efforts, repeated day in and day out. - Robert Collier", null,
				null, null, false);

		ArrayList<Task> tasks = new ArrayList<Task>();
		tasks.add(task1);
		tasks.add(task2);
		tasks.add(task3);
		tasks.add(task4);
		tasks.add(task5);
		tasks.add(task6);
		return tasks;
	}
}
