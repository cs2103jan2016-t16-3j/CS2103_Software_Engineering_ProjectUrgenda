//@@author A0131857B
package urgenda.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;

/**
 * Utility class to store two LocalDateTime instances. Allows changing of values
 * referencing to earlier or later, and relative comparisons between the two
 * LocalDateTime instances.
 * 
 * @author KangSoon
 *
 */
public class DateTimePair {

	private static final String LOGGER_NULL_VALUE = "DateTimePair instance contains one or more null values";

	private LocalDateTime _dateTime1;
	private LocalDateTime _dateTime2;
	private boolean _firstIsBefore;

	/**
	 * Creates a DateTimePair object with two LocalDateTime instances.
	 * 
	 * @param dateTime1
	 *            the first LocalDateTime instance
	 * @param dateTime2
	 *            the second LocalDateTime instance
	 */
	public DateTimePair(LocalDateTime dateTime1, LocalDateTime dateTime2) {
		_dateTime1 = dateTime1;
		_dateTime2 = dateTime2;
		checkRelativeDateTimes();
	}

	/**
	 * Returns the first LocalDateTime instance set when instantiated.
	 * 
	 * @return the first LocalDateTime instance set when instantiated
	 */
	public LocalDateTime getDateTime1() {
		return _dateTime1;
	}

	/**
	 * Returns the second LocalDateTime instance set when instantiated.
	 * 
	 * @return the second LocalDateTime instance set when instantiated
	 */
	public LocalDateTime getDateTime2() {
		return _dateTime2;
	}

	/**
	 * Returns the earlier LocalDateTime instance.
	 * 
	 * @return the earlier LocalDateTime instance.
	 */
	public LocalDateTime getEarlierDateTime() {
		if (_firstIsBefore) {
			return _dateTime1;
		} else {
			return _dateTime2;
		}
	}

	/**
	 * Returns the later LocalDateTime instance.
	 * 
	 * @return the later LocalDateTime instance
	 */
	public LocalDateTime getLaterDateTime() {
		if (_firstIsBefore) {
			return _dateTime2;
		} else {
			return _dateTime1;
		}
	}

	/**
	 * Adds specified time period in chronounits to the earlier LocalDateTime
	 * instance.
	 * 
	 * @param number
	 *            number of specified chronounits to be added
	 * 
	 * @param chronoUnit
	 *            chronounits used
	 */
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

	/**
	 * Subtracts specified time period in chronounits from the earlier
	 * LocalDateTime instance.
	 * 
	 * @param number
	 *            number of specified chronounits to be subtracted
	 * 
	 * @param chronoUnit
	 *            chronounits used
	 */
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

	/**
	 * Adds specified time period in chronounits to the later LocalDateTime
	 * instance.
	 * 
	 * @param number
	 *            number of specified chronounits to be added
	 * 
	 * @param chronoUnit
	 *            chronounits used
	 */

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

	/**
	 * Subtracts specified time period in chronounits from the later
	 * LocalDateTime instance.
	 * 
	 * @param number
	 *            number of specified chronounits to be subtracted
	 * 
	 * @param chronoUnit
	 *            chronounits used
	 */
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

	/**
	 * Returns whether the passed DateTimePair matches this DateTimePair
	 * instance.
	 * 
	 * @param d
	 *            DateTimePair to compare
	 * 
	 * @return boolean whether passed instance matches this instance or not
	 */
	public boolean equals(DateTimePair d) {
		if (_dateTime1 != null && _dateTime2 != null) {
			if (d.getEarlierDateTime().equals(this.getEarlierDateTime())
					&& d.getLaterDateTime().equals(this.getLaterDateTime())) {
				return true;
			}
		} else {
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, LOGGER_NULL_VALUE);
		}
		return false;
	}

	/**
	 * Returns the difference between the two LocalDateTime instances rounded up
	 * to the nearest day.
	 * 
	 * @return difference in the number of days rounded up to the nearest day
	 */
	public int getRoundedDays() {
		LocalDate d1 = getEarlierDateTime().toLocalDate();
		LocalDate d2 = getLaterDateTime().toLocalDate();
		return (int) ChronoUnit.DAYS.between(d1, d2);
	}

	/**
	 * Returns whether the two LocalDateTime instances are on the same day.
	 * 
	 * @return boolean whether the two LocalDateTime instances are on the same
	 *         day
	 */
	public boolean isSameDay() {
		if (_dateTime1.getYear() == _dateTime2.getYear()
				&& _dateTime1.getDayOfYear() == _dateTime2.getDayOfYear()) {
			return true;
		}
		return false;
	}

	/**
	 * Returns whether the first LocalDateTime instance is before the second.
	 * 
	 * @return boolean whether the first LocalDateTime instance is before the
	 *         second
	 */
	public boolean firstIsBefore() {
		return _firstIsBefore;
	}

	private void checkRelativeDateTimes() {
		if (_dateTime1 != null && _dateTime2 != null) {
			if (_dateTime1.isBefore(_dateTime2)) {
				_firstIsBefore = true;
			} else if (_dateTime1.isAfter(_dateTime2)) {
				_firstIsBefore = false;
			}
		} else {
			UrgendaLogger.getInstance().getLogger().log(Level.SEVERE, LOGGER_NULL_VALUE);
		}
	}
}