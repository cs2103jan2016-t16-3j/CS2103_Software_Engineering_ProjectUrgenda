package urgenda.util;

import java.util.UUID;

public class MultipleSlot {

	private String _desc;
	// serves as unique id for MultipleSlot attribute
	private String _uniqueId;
	
	// default constructor
	public MultipleSlot() {
		_uniqueId = UUID.randomUUID().toString(); 
	}
	
	// constructor with description given
	public MultipleSlot(String desc) {
		_desc = desc;
		_uniqueId = UUID.randomUUID().toString(); 
	}
	
	public MultipleSlot(String desc, String uniqueId) {
		_desc = desc;
		_uniqueId = uniqueId;
	}
	
	public boolean equals(MultipleSlot slot) {
		if (slot.getDesc().equals(_desc) && slot.getuniqueID().equals(_uniqueId)) {
			return true;
		} else {
			return false;
		}
	}

	public String getDesc() {
		return _desc;
	}

	public String getuniqueID() {
		return _uniqueId;
	}

	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setuniqueID(String uniqueId) {
		_uniqueId = uniqueId;
	}

}
