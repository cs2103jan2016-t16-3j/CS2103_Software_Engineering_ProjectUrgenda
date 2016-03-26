package urgenda.command;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;

import urgenda.logic.LogicData;
import urgenda.util.Task;

public class FindFree extends Command {

	private static final String MESSAGE_FREE_TIME = "Showing available time slots between "
			+ "%1$d/%2$d, %3$02d:%4$02d to %5$d/%6$d, %7$02d:%8$02d";
	private static final String MESSAGE_NO_FREE_TIME = "There are no available time between "
			+ "%1$d/%2$d, %3$02d:%4$02d to %5$d/%6$d, %7$02d:%8$02d";
	private static final String MESSAGE_HOURS = " hour(s) ";
	private static final String MESSAGE_MINUTES = " minute(s) ";
	private static final String MESSAGE_SECONDS = " second(s) ";
	
	private LocalDateTime _startOfRange;
	private LocalDateTime _endOfRange;

	public FindFree() {

	}

	public FindFree(LocalDateTime start, LocalDateTime end) {
		_startOfRange = start;
		_endOfRange = end;
	}

	public String execute() {
		LogicData data = LogicData.getInstance();
		data.clearShowMoreTasks();
		Task timeRange = createTimeTask(_startOfRange, _endOfRange);
		ArrayList<Task> matches = data.overlappingTasks(timeRange);
		Collections.sort(matches, comparator);
		if (matches.isEmpty()) {
			matches.add(timeRange);
			data.clearDisplays();
			data.setDisplays(matches);
			data.setCurrState(LogicData.DisplayState.FIND_FREE);
			return formatFeedback(MESSAGE_FREE_TIME);
		}
		Deque<LocalDateTime> freeTimes = new ArrayDeque<LocalDateTime>();
		if (matches.get(0).getStartTime().isAfter(_startOfRange)) {
			freeTimes.addFirst(_startOfRange);
			freeTimes.addFirst(matches.get(0).getStartTime());
		}
		freeTimes.addFirst(matches.get(0).getEndTime());
		matches.remove(0);
		while (!matches.isEmpty()) {
			LocalDateTime start = matches.get(0).getStartTime();
			LocalDateTime end = matches.get(0).getEndTime();
			if (start.isAfter(freeTimes.peekFirst())) {
				freeTimes.addFirst(start);
				freeTimes.addFirst(end);
			} else if (end.isAfter(freeTimes.peekFirst())) {
				freeTimes.removeFirst();
				freeTimes.addFirst(end);
			}
			matches.remove(0);
		}
		if (freeTimes.peekFirst().isBefore(_endOfRange)) {
			freeTimes.addFirst(_endOfRange);
		} else {
			freeTimes.removeFirst();
		}
		// assert to ensure that the number of timings are even
		assert (freeTimes.size() % 2 == 0);
		
		if (freeTimes.isEmpty()) {
			return formatFeedback(MESSAGE_NO_FREE_TIME);
		}
		
		ArrayList<Task> forDisplay = generateDisplayList(freeTimes);
		data.clearDisplays();
		data.setDisplays(forDisplay);
		data.setCurrState(LogicData.DisplayState.FIND_FREE);
		
		return formatFeedback(MESSAGE_FREE_TIME);
	}
	
	private String formatFeedback(String feedback) {
		return String.format(feedback, _startOfRange.getDayOfMonth(), _startOfRange.getMonthValue(),
				_startOfRange.getHour(), _startOfRange.getMinute(), _endOfRange.getDayOfMonth(),
				_endOfRange.getMonthValue(), _endOfRange.getHour(), _endOfRange.getMinute());
	}

	private ArrayList<Task> generateDisplayList(Deque<LocalDateTime> freeTimes) {
		ArrayList<Task> forDisplay = new ArrayList<Task>();
		while (!freeTimes.isEmpty()) {
			LocalDateTime start = freeTimes.removeLast();
			LocalDateTime end = freeTimes.removeLast();
			if (!start.toLocalDate().equals(end.toLocalDate())) {
				LocalDateTime split = LocalDateTime.of(end.toLocalDate(), LocalTime.of(0, 0));
				forDisplay.add(createTimeTask(start, split.minusSeconds(1)));
				forDisplay.add(createTimeTask(split, end));
			} else {
				forDisplay.add(createTimeTask(start, end));				
			}
		}
		return forDisplay;
	}

	private Task createTimeTask(LocalDateTime start, LocalDateTime end) {
		Task temp = new Task();
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yy");
//		temp.setDesc(start.format(formatter));
		
		temp.setDesc(timeDiff(start.toLocalTime(), end.toLocalTime()));
		temp.setStartTime(start);
		temp.setEndTime(end);
		temp.setTaskType(Task.Type.EVENT);
		return temp;
	}

	private String timeDiff(LocalTime start, LocalTime end) {
		int hourDiff = end.getHour() - start.getHour();
		int minuteDiff = end.getMinute() - start.getMinute();
		int secondDiff = end.getSecond() - start.getSecond();
		
		String duration = "";
		if (hourDiff > 0) {
			duration += hourDiff + MESSAGE_HOURS;
		}
		if (minuteDiff > 0) {
			duration += minuteDiff + MESSAGE_MINUTES;
		}
		if (secondDiff > 0) {
			duration += secondDiff + MESSAGE_SECONDS;
		}
		return duration;
	}

	public void setStartOfRange(LocalDateTime start) {
		_startOfRange = start;
	}

	public void setEndOfRange(LocalDateTime end) {
		_endOfRange = end;
	}


	// TODO: to be edited if the list for sorting consists of new structure and
	// not tasks
	static Comparator<Task> comparator = new Comparator<Task>() {
		public int compare(final Task o1, final Task o2) {
			return o1.getStartTime().compareTo(o2.getStartTime());
		}
	};

}
