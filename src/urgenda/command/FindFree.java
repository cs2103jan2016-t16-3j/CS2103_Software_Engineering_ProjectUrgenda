package urgenda.command;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
	private static final String MESSAGE_INVALID_TIME_RANGE = "Invalid time range for finding available time";
	private static final String MESSAGE_HOURS = " hours ";
	private static final String MESSAGE_MINUTES = " minutes ";
	private static final String MESSAGE_SECONDS = " seconds ";
	private static final String MESSAGE_HOUR = " hour ";
	private static final String MESSAGE_MINUTE = " minute ";
	private static final String MESSAGE_SECOND = " second ";
	private static final int SINGLE_LIMITER = 1;
	
	private LocalDateTime _startOfRange;
	private LocalDateTime _endOfRange;

	public FindFree() {

	}

	public FindFree(LocalDateTime start, LocalDateTime end) {
		_startOfRange = start;
		_endOfRange = end;
	}

	public String execute() throws Exception {
		LogicData data = LogicData.getInstance();
		// TODO show warning then flip range
		LocalDateTime now = LocalDateTime.now();
		if (!_startOfRange.isBefore(_endOfRange) || _endOfRange.isBefore(now)) {
			data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			throw new Exception(MESSAGE_INVALID_TIME_RANGE);
		} else if (_startOfRange.isBefore(now)) {
			_startOfRange = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
					now.getHour(), now.getMinute());
			// TODO feedback for edited time
		}
		data.clearShowMoreTasks();
		Task timeRange = createTimeTask(_startOfRange, _endOfRange);
		ArrayList<Task> matches = data.overlappingTasks(timeRange);
		Collections.sort(matches, comparator);
		Deque<LocalDateTime> freeTimes = new ArrayDeque<LocalDateTime>();
		if (matches.isEmpty()) {
			matches.add(timeRange);
			data.clearDisplays();
			freeTimes.addFirst(_startOfRange);
			freeTimes.addFirst(_endOfRange);
			ArrayList<Task> forDisplay = generateDisplayList(freeTimes);
			data.setDisplays(forDisplay);
			data.setCurrState(LogicData.DisplayState.FIND_FREE);
			return formatFeedback(MESSAGE_FREE_TIME);
		}
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
			data.clearDisplays();
			data.setCurrState(LogicData.DisplayState.FIND_FREE);
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
			if (!start.toLocalDate().equals(end.toLocalDate()) && !end.toLocalTime().equals(LocalTime.of(0, 0))) {
				LocalDateTime split = LocalDateTime.of(end.toLocalDate(), LocalTime.of(0, 0));
				forDisplay.add(createTimeTask(start, split));
				forDisplay.add(createTimeTask(split, end));
			} else {
				forDisplay.add(createTimeTask(start, end));				
			}
		}
		return forDisplay;
	}

	private Task createTimeTask(LocalDateTime start, LocalDateTime end) {
		Task temp = new Task();
		temp.setDesc(timeDiff(start.toLocalTime(), end.toLocalTime()));
		temp.setStartTime(start);
		temp.setEndTime(end);
		temp.setTaskType(Task.Type.EVENT);
		return temp;
	}

	private String timeDiff(LocalTime start, LocalTime end) {
		Duration diff;
		if (start.isBefore(end)) {
			diff = Duration.between(start, end);			
		} else {
			diff = Duration.between(end, start);	
		}
		long hourDiff = diff.toHours();
		long minuteDiff = diff.toMinutes() - 60 * hourDiff;
		long secondDiff = diff.getSeconds() - 60 * minuteDiff - 3600 * hourDiff;
		
		String duration = "";
		if (hourDiff > 0) {
			duration += hourDiff;
			if (hourDiff == SINGLE_LIMITER) {
				duration += MESSAGE_HOUR;
			} else {
				duration += MESSAGE_HOURS;
			}
		}
		if (minuteDiff > 0) {
			duration += minuteDiff;
			if (minuteDiff == SINGLE_LIMITER) {
				duration += MESSAGE_MINUTE;
			} else {
				duration += MESSAGE_MINUTES;
			}
		}
		if (secondDiff > 0) {
			duration += secondDiff;
			if (secondDiff == SINGLE_LIMITER) {
				duration += MESSAGE_SECOND;
			} else {
				duration += MESSAGE_SECONDS;
			}
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
