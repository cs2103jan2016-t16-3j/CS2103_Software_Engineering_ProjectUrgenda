//@@author A0080436J
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
import urgenda.util.LogicException;
import urgenda.util.Task;

/**
 * FindFree command for Urgenda. Responsible for finding all possible free time
 * between a given set of timings.
 *
 */
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
	private static final String MESSAGE_24_HOURS = "24 hours";
	private static final int SINGLE_LIMITER = 1;

	private LogicData _data;

	private LocalDateTime _startOfRange;
	private LocalDateTime _endOfRange;

	/**
	 * Default Constructor for creating a FindFree command object.
	 */
	public FindFree() {

	}

	/**
	 * Constructor for creating a FindFree command object with a start and end
	 * timing.
	 * 
	 * @param start
	 *            LocalDateTime of the starting timing.
	 * @param end
	 *            LocalDateTime of the ending timing.
	 */
	public FindFree(LocalDateTime start, LocalDateTime end) {
		_startOfRange = start;
		_endOfRange = end;
	}

	/**
	 * Execute method for Finding free timings.
	 * 
	 * @throws LogicException
	 *             Throws exception when the end is before start or end time has
	 *             already passed.
	 */
	public String execute() throws LogicException {
		_data = LogicData.getInstance();
		checkAndUpdateRange();
		_data.clearShowMoreTasks();
		return findFreeTimings();
	}

	/*
	 * Finds all matching timings with the given timings Returns the feedback to
	 * user.
	 */
	private String findFreeTimings() {
		Task timeRange = createTimeTask(_startOfRange, _endOfRange);
		ArrayList<Task> matches = _data.overlappingTasks(timeRange);
		Collections.sort(matches, comparator);
		return generateFreeTimings(matches);
	}

	/*
	 * Generates all possible timings given the matches. Returns the generated
	 * feedback.
	 */
	private String generateFreeTimings(ArrayList<Task> matches) {
		Deque<LocalDateTime> freeTimes = new ArrayDeque<LocalDateTime>();
		if (matches.isEmpty()) {
			return setEmptyFindFreeState(freeTimes);
		} else {
			generateStartSlot(matches, freeTimes);
			addFreeTimeSlots(matches, freeTimes);
			generateEndSlot(freeTimes);
			return setFindFreeState(freeTimes);
		}
	}

	/*
	 * Uses the given timings to set display if there are no tasks given the
	 * time range.
	 */
	private String setEmptyFindFreeState(Deque<LocalDateTime> freeTimes) {
		_data.clearDisplays();
		freeTimes.addFirst(_startOfRange);
		freeTimes.addFirst(_endOfRange);
		ArrayList<Task> forDisplay = generateDisplayList(freeTimes);
		_data.setDisplays(forDisplay);
		_data.setCurrState(LogicData.DisplayState.FIND_FREE);
		return formatFeedback(MESSAGE_FREE_TIME);
	}

	/*
	 * Uses the free timings gathered to generate the display for setting.
	 */
	private String setFindFreeState(Deque<LocalDateTime> freeTimes) {
		// assert to ensure that the number of timings are even
		assert (freeTimes.size() % 2 == 0);
		if (freeTimes.isEmpty()) {
			_data.clearDisplays();
			_data.setCurrState(LogicData.DisplayState.FIND_FREE);
			return formatFeedback(MESSAGE_NO_FREE_TIME);
		} else {
			ArrayList<Task> forDisplay = generateDisplayList(freeTimes);
			_data.clearDisplays();
			_data.setDisplays(forDisplay);
			_data.setCurrState(LogicData.DisplayState.FIND_FREE);
			return formatFeedback(MESSAGE_FREE_TIME);
		}
	}

	private void generateEndSlot(Deque<LocalDateTime> freeTimes) {
		if (freeTimes.peekFirst().isBefore(_endOfRange)) {
			freeTimes.addFirst(_endOfRange);
		} else {
			freeTimes.removeFirst();
		}
	}

	/*
	 * Goes through all matches and checks whether the current timeslot in the
	 * deque overlaps/continues/creates a new empty slot.
	 */
	private void addFreeTimeSlots(ArrayList<Task> matches, Deque<LocalDateTime> freeTimes) {
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
	}

	private void generateStartSlot(ArrayList<Task> matches, Deque<LocalDateTime> freeTimes) {
		if (matches.get(0).getStartTime().isAfter(_startOfRange)) {
			freeTimes.addFirst(_startOfRange);
			freeTimes.addFirst(matches.get(0).getStartTime());
		}
		freeTimes.addFirst(matches.get(0).getEndTime());
		matches.remove(0);
	}

	/*
	 * Checker method to ensure that the time range given is valid. Update if
	 * start time is before current time.
	 */
	private void checkAndUpdateRange() throws LogicException {
		LocalDateTime now = LocalDateTime.now();
		if (!_startOfRange.isBefore(_endOfRange) || _endOfRange.isBefore(now)) {
			_data.setCurrState(LogicData.DisplayState.ALL_TASKS);
			throw new LogicException(MESSAGE_INVALID_TIME_RANGE);
		} else if (_startOfRange.isBefore(now)) {
			_startOfRange = LocalDateTime.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth(),
					now.getHour(), now.getMinute());
		}
	}

	private String formatFeedback(String feedback) {
		return String.format(feedback, _startOfRange.getDayOfMonth(), _startOfRange.getMonthValue(),
				_startOfRange.getHour(), _startOfRange.getMinute(), _endOfRange.getDayOfMonth(),
				_endOfRange.getMonthValue(), _endOfRange.getHour(), _endOfRange.getMinute());
	}

	/*
	 * Creates the Task list for display given the different free timings given
	 * in the deque.
	 */
	private ArrayList<Task> generateDisplayList(Deque<LocalDateTime> freeTimes) {
		ArrayList<Task> forDisplay = new ArrayList<Task>();
		while (!freeTimes.isEmpty()) {
			LocalDateTime start = freeTimes.removeLast();
			LocalDateTime end = freeTimes.removeLast();
			if (!start.toLocalDate().equals(end.toLocalDate()) && 
					!end.toLocalTime().equals(LocalTime.of(0, 0))) {
				LocalDateTime split = LocalDateTime.of(start.toLocalDate().plusDays(1), LocalTime.of(0, 0));
				forDisplay.add(createTimeTask(start, split));
				freeTimes.addLast(end);
				freeTimes.addLast(split);
			} else {
				forDisplay.add(createTimeTask(start, end));
			}
		}
		return forDisplay;
	}

	/*
	 * Creates the Task object to match the start and end timing given, sets
	 * desc as the duration of timing for display.
	 */
	private Task createTimeTask(LocalDateTime start, LocalDateTime end) {
		Task temp = new Task();
		setDurationDesc(start, end, temp);
		temp.setStartTime(start);
		temp.setEndTime(end);
		temp.setTaskType(Task.Type.EVENT);
		return temp;
	}

	/*
	 * Sets the duration according to the difference in timing.
	 */
	private void setDurationDesc(LocalDateTime start, LocalDateTime end, Task temp) {
		if (start.toLocalTime().equals(end.toLocalTime()) && start.toLocalDate().isBefore(end.toLocalDate())) {
			temp.setDesc(MESSAGE_24_HOURS);
		} else {
			temp.setDesc(timeDiff(start.toLocalTime(), end.toLocalTime()));
		}
	}

	/*
	 * Calculates the difference in time from the start to end.
	 */
	private String timeDiff(LocalTime start, LocalTime end) {
		Duration diff = generateDurationDiff(start, end);
		long hourDiff = diff.toHours();
		long minuteDiff = diff.toMinutes() - 60 * hourDiff;
		long secondDiff = diff.getSeconds() - 60 * minuteDiff - 3600 * hourDiff;
		return generateFeedbackMessage(hourDiff, minuteDiff, secondDiff);
	}

	private String generateFeedbackMessage(long hourDiff, long minuteDiff, long secondDiff) {
		String duration = "";
		if (hourDiff > 0) {
			duration += addTimeMessage(hourDiff, MESSAGE_HOUR, MESSAGE_HOURS);
		}
		if (minuteDiff > 0) {
			duration += addTimeMessage(minuteDiff, MESSAGE_MINUTE, MESSAGE_MINUTES);
		}
		if (secondDiff > 0) {
			duration += addTimeMessage(secondDiff, MESSAGE_SECOND, MESSAGE_SECONDS);
		}
		return duration;
	}

	private String addTimeMessage(long hourDiff, String singular, String plural) {
		String duration = "";
		duration += hourDiff;
		if (hourDiff == SINGLE_LIMITER) {
			duration += singular;
		} else {
			duration += plural;
		}
		return duration;
	}

	private Duration generateDurationDiff(LocalTime start, LocalTime end) {
		Duration diff;
		if (end.equals(LocalTime.of(0, 0))) {
			diff = Duration.between(start, LocalTime.of(23, 59, 59));
			diff = diff.plusSeconds(1);
		} else {
			diff = Duration.between(start, end);
		}
		return diff;
	}

	/**
	 * Setter method for the start timing of FindFree.
	 * 
	 * @param start
	 *            LocalDateTime of the starting time.
	 */
	public void setStartOfRange(LocalDateTime start) {
		_startOfRange = start;
	}

	/**
	 * Setter method for the end timing of FindFree.
	 * 
	 * @param end
	 *            LocalDateTime of the ending time.
	 */
	public void setEndOfRange(LocalDateTime end) {
		_endOfRange = end;
	}

	/*
	 * Comparator for comparing of the matches to sort them according to their
	 * start times.
	 */
	static Comparator<Task> comparator = new Comparator<Task>() {
		public int compare(final Task o1, final Task o2) {
			return o1.getStartTime().compareTo(o2.getStartTime());
		}
	};

}
