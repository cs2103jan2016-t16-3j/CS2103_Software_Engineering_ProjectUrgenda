package urgenda.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.Duration;

public class LocalDateTimeDifference {

	private static final Duration DURATION_SINGLEDAY = Duration.ofDays(1);
	private static final Duration DURATION_SINGLEHOUR = Duration.ofHours(1);
	private static final Duration DURATION_SINGLEMINUTE = Duration.ofMinutes(1);
	private static final Duration DURATION_SINGLESECOND = Duration.ofSeconds(1);

	private LocalDateTime _dateTime1;
	private LocalDateTime _dateTime2;
	private int _seconds;
	private int _minutes;
	private int _hours;
	private int _days;
	private boolean _firstIsBefore;

	public LocalDateTimeDifference(LocalDateTime dateTime1, LocalDateTime dateTime2) {
		_dateTime1 = dateTime1;
		_dateTime2 = dateTime2;
		updateUnits(dateTime1, dateTime2);
	}

	private void updateUnits(LocalDateTime dateTime1, LocalDateTime dateTime2) {
		Duration duration;
		if (dateTime1.isBefore(dateTime2)) {
			duration = Duration.between(dateTime1, dateTime2);
			_firstIsBefore = true;
		} else {
			duration = Duration.between(dateTime2, dateTime1);
			_firstIsBefore = false;
		}
		_days = generateChronoUnitCount(duration, DURATION_SINGLEDAY);
		_hours = generateChronoUnitCount(duration.minus(Duration.ofDays(_days)), DURATION_SINGLEHOUR);
		_minutes = generateChronoUnitCount(duration.minus(Duration.ofHours(_hours)), DURATION_SINGLEMINUTE);
		_seconds = generateChronoUnitCount(duration.minus(Duration.ofHours(_minutes)), DURATION_SINGLESECOND);
	}

	private int generateChronoUnitCount(Duration duration, Duration chronoUnit) {
		int chronoUnitCount = 0;
		while (duration.getSeconds() > chronoUnit.getSeconds()) {
			duration = duration.minus(chronoUnit);
			chronoUnitCount++;
		}
		return chronoUnitCount;
	}

	public LocalDateTime getDateTime1() {
		return _dateTime1;
	}

	public LocalDateTime getDateTime2() {
		return _dateTime2;
	}
	
	public LocalDateTime getEarlierDateTime() {
		if(_firstIsBefore) {
			return _dateTime1;
		} else {
			return _dateTime2;
		}
	}
	
	public LocalDateTime getLaterDateTime() {
		if(_firstIsBefore) {
			return _dateTime2;
		} else {
			return _dateTime1;
		}
	}
	
	public void addToEarlierDateTime(int number, ChronoUnit chronoUnit) {
		if (_dateTime1.isBefore(_dateTime2)) {
			_dateTime1.plus(number, chronoUnit);
		} else {
			_dateTime2.plus(number, chronoUnit);
		}
		updateUnits(_dateTime1, _dateTime2);
	}

	public void minusFromEarlierDateTime(int number, ChronoUnit chronoUnit) {
		if (_dateTime1.isBefore(_dateTime2)) {
			_dateTime1.minus(number, chronoUnit);
		} else {
			_dateTime2.minus(number, chronoUnit);
		}
		updateUnits(_dateTime1, _dateTime2);
	}

	public void addToLaterDateTime(int number, ChronoUnit chronoUnit) {
		if (_dateTime1.isAfter(_dateTime2)) {
			_dateTime1.plus(number, chronoUnit);
		} else {
			_dateTime2.plus(number, chronoUnit);
		}
		updateUnits(_dateTime1, _dateTime2);
	}

	public void minusFromLaterDateTime(int number, ChronoUnit chronoUnit) {
		if (_dateTime1.isAfter(_dateTime2)) {
			_dateTime1.minus(number, chronoUnit);
		} else {
			_dateTime2.minus(number, chronoUnit);
		}
		updateUnits(_dateTime1, _dateTime2);
	}

	public int getRoundedDays() {
		return getLaterDateTime().getDayOfYear() - getEarlierDateTime().getDayOfYear();		
	}
	
	public int getSeconds() {
		return _seconds;
	}

	public int getMinutes() {
		return _minutes;
	}

	public int getHours() {
		return _hours;
	}

	public int getDays() {
		return _days;
	}

	public boolean firstIsBefore() {
		return _firstIsBefore;
	}
}