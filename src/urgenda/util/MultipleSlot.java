package urgenda.util;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
	
	public boolean isEmpty() {
		return _slots.isEmpty();
	}

}
