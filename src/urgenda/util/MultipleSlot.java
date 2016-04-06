//@@author A0080436
package urgenda.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Collections;

// MultipleSlot is an attribute for Tasks with multiple possible timings. Main timing displayed is shown in 
// _startTime and _endTime for Task. Alternatives that are later will be placed as multiple slots to be confirmed
public class MultipleSlot {

	private ArrayList<DateTimePair> _slots;
	
	public MultipleSlot() {
		_slots = new ArrayList<DateTimePair>();
	}
	
	public MultipleSlot(MultipleSlot original) {
		_slots = new ArrayList<DateTimePair>(original.getSlots());
	}
	
	public void addTimeSlot(LocalDateTime start, LocalDateTime end) {
		_slots.add(new DateTimePair(start,end));
	}
	
	public ArrayList<DateTimePair> getSlots() {
		return _slots;
	}
	
	// TODO sort for parser's return
	public void sortSlots() {
		Collections.sort(_slots, comparator);
	}
	
	static Comparator<DateTimePair> comparator = new Comparator<DateTimePair>() {
		public int compare(final DateTimePair p1, final DateTimePair p2) {
			if (p1.getEarlierDateTime().equals(p2.getEarlierDateTime())) {
				return p1.getLaterDateTime().compareTo(p2.getLaterDateTime());
			} else {
				return p1.getEarlierDateTime().compareTo(p2.getEarlierDateTime());
			}
		}
	};
	
	public DateTimePair getNextSlot() {
		if (_slots.isEmpty()) {
			return null;
		} else {
			return _slots.get(0);
		}
	}
	
	public void removeNextSlot() {
		if (!_slots.isEmpty()) {
			_slots.remove(0);
		} 
	}
	
	public boolean isEmpty() {
		return _slots.isEmpty();
	}

}
