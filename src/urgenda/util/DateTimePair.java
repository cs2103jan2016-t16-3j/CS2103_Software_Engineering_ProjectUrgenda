package urgenda.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;

public class DateTimePair {

	private static final String LOGGER_NULL_VALUE = "DateTimePair instance contains one or more null values";
	
	private LocalDateTime _dateTime1;
	private LocalDateTime _dateTime2;
	private boolean _firstIsBefore;

	public DateTimePair(LocalDateTime dateTime1, LocalDateTime dateTime2) {
		_dateTime1 = dateTime1;
		_dateTime2 = dateTime2;
		checkRelativeDateTimes();
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
		if (_dateTime1 != null && _dateTime2 != null) {
			if (_firstIsBefore) {
				_dateTime1 = _dateTime1.plus(number, chronoUnit);
			} else {
				_dateTime2 = _dateTime2.plus(number, chronoUnit);
			}
			checkRelativeDateTimes();
		} else {
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, LOGGER_NULL_VALUE);
		}
	}


	public void minusFromEarlierDateTime(int number, ChronoUnit chronoUnit) {
		if (_dateTime1 != null && _dateTime2 != null) {
			if (_firstIsBefore) {
				_dateTime1 = _dateTime1.minus(number, chronoUnit);
			} else {
				_dateTime2 = _dateTime2.minus(number, chronoUnit);
			}
			checkRelativeDateTimes();
		} else {
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, LOGGER_NULL_VALUE);
		}
	}

	public void addToLaterDateTime(int number, ChronoUnit chronoUnit) {
		if (_dateTime1 != null && _dateTime2 != null) {
			if (!_firstIsBefore) {
				_dateTime1 = _dateTime1.plus(number, chronoUnit);
			} else {
				_dateTime2 = _dateTime2.plus(number, chronoUnit);
			}
			checkRelativeDateTimes();
		} else {
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, LOGGER_NULL_VALUE);
		}
	}

	public void minusFromLaterDateTime(int number, ChronoUnit chronoUnit) {
		if (_dateTime1 != null && _dateTime2 != null) {
			if (!_firstIsBefore) {
				_dateTime1 = _dateTime1.minus(number, chronoUnit);
			} else {
				_dateTime2 = _dateTime2.minus(number, chronoUnit);
			}
			checkRelativeDateTimes();
		} else {
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, LOGGER_NULL_VALUE);
		}
	}
	public boolean equals(DateTimePair d) {
		if (_dateTime1 != null && _dateTime2 != null) {
			if(d.getEarlierDateTime().equals(this.getEarlierDateTime()) && d.getLaterDateTime().equals(this.getLaterDateTime())) {
				return true;
			}
		} else {
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, LOGGER_NULL_VALUE);
		}
		return false;
	}
	
	public int getRoundedDays() {
		LocalDate d1 = getEarlierDateTime().toLocalDate();
		LocalDate d2 = getLaterDateTime().toLocalDate();
		return (int) ChronoUnit.DAYS.between(d1, d2);
	}
	
	public boolean isSameDay() {
		if(_dateTime1.getYear() == _dateTime2.getYear() && _dateTime1.getDayOfYear() == _dateTime2.getDayOfYear()) {
			return true;
		}
		return false;
	}
	
	public boolean firstIsBefore() {
		return _firstIsBefore;
	}
	
	private void checkRelativeDateTimes() {
		if(_dateTime1 != null && _dateTime2 != null) {
			if(_dateTime1.isBefore(_dateTime2)) {
				_firstIsBefore = true;
			} else if(_dateTime1.isAfter(_dateTime2)) {
				_firstIsBefore = false;
			}
		} else {
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, LOGGER_NULL_VALUE);
		}
	}
}