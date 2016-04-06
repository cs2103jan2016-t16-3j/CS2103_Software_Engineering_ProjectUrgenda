//@@author A0080436J
package urgenda.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * MultipleSlot is an attribute for Tasks with multiple possible timings. Main
 * timing displayed is shown in start and end time for Task. Alternative timings
 * are placed as multiple slots to be confirmed.
 *
 */
public class MultipleSlot {

	private ArrayList<DateTimePair> _slots;

	/**
	 * Default constructor for MultipleSlot which creates the arraylist of
	 * datetimepair for storing.
	 */
	public MultipleSlot() {
		_slots = new ArrayList<DateTimePair>();
	}

	/**
	 * Constructor for replicating the given multiple slot.
	 * 
	 * @param original
	 *            Original multiple slot for replication.
	 */
	public MultipleSlot(MultipleSlot original) {
		_slots = new ArrayList<DateTimePair>(original.getSlots());
	}

	/**
	 * Adds a new time slot pair to the slot.
	 * 
	 * @param start
	 *            LocalDateTime of the start time.
	 * @param end
	 *            LocalDateTime of the end time.
	 */
	public void addTimeSlot(LocalDateTime start, LocalDateTime end) {
		_slots.add(new DateTimePair(start, end));
	}

	/**
	 * Getter method to retrieve the slots.
	 * 
	 * @return All the DateTimePair of the slot.
	 */
	public ArrayList<DateTimePair> getSlots() {
		return _slots;
	}

	/**
	 * Sorts the slots by the timing of the slots based on the comparator for
	 * start time followed by end time.
	 */
	public void sortSlots() {
		Collections.sort(_slots, comparator);
	}

	/*
	 * Comparator for sorting of datetimepair where the start time is compared
	 * followed by the end time.
	 */
	static Comparator<DateTimePair> comparator = new Comparator<DateTimePair>() {
		public int compare(final DateTimePair p1, final DateTimePair p2) {
			if (p1.getEarlierDateTime().equals(p2.getEarlierDateTime())) {
				return p1.getLaterDateTime().compareTo(p2.getLaterDateTime());
			} else {
				return p1.getEarlierDateTime().compareTo(p2.getEarlierDateTime());
			}
		}
	};

	/**
	 * Getter of the next slot present.
	 * 
	 * @return DateTimePair of next pair, else null if invalid.
	 */
	public DateTimePair getNextSlot() {
		if (_slots.isEmpty()) {
			return null;
		} else {
			return _slots.get(0);
		}
	}

	/**
	 * Removes the next slot present in the ArrayList of DateTimePair
	 */
	public void removeNextSlot() {
		if (!_slots.isEmpty()) {
			_slots.remove(0);
		}
	}

	/**
	 * Checks if the slots is empty.
	 * 
	 * @return boolean of whether the slot is empty.
	 */
	public boolean isEmpty() {
		return _slots.isEmpty();
	}

}
