package urgenda.util;

import java.time.LocalDateTime;

public class MultipleSlot {

	private String _desc;
	// serves as unique id for MultipleSlot attribute
	private LocalDateTime _dateAdded;
	
	public MultipleSlot() {
		
	}
	
	public MultipleSlot(String desc, LocalDateTime dateAdded) {
		_desc = desc;
		_dateAdded = dateAdded;
	}

	public String getDesc() {
		return _desc;
	}

	public LocalDateTime getDateAdded() {
		return _dateAdded;
	}

	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setDateAdded(LocalDateTime dateAdded) {
		_dateAdded = dateAdded;
	}

}
