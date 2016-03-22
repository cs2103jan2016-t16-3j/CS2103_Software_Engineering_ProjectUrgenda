package urgenda.util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Collections;

// MultipleSlot is an attribute for Tasks with multiple possible timings. Main timing displayed is shown in 
// _startTime and _endTime for Task. Alternatives that are later will be placed as multiple slots to be confirmed
public class MultipleSlot {

	private ArrayList<TimePair> _slots;
	
	public MultipleSlot() {
		_slots = new ArrayList<TimePair>();
	}
	
	public void addTimeSlot(LocalDateTime start, LocalDateTime end) {
		_slots.add(new TimePair(start,end));
	}
	
	public ArrayList<TimePair> getSlots() {
		return _slots;
	}
	
	// TODO sort for parser's return
	public void sortSlots() {
		Collections.sort(_slots, comparator);
	}
	
	static Comparator<TimePair> comparator = new Comparator<TimePair>() {
		public int compare(final TimePair p1, final TimePair p2) {
			if (p1.getStart().equals(p2.getStart())) {
				return p1.getEnd().compareTo(p2.getEnd());
			} else {
				return p1.getStart().compareTo(p2.getStart());
			}
		}
	};
	
	public TimePair getNextSlot() {
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
