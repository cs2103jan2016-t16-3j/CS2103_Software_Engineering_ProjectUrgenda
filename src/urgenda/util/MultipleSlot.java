package urgenda.util;

public class MultipleSlot {

	private String _desc;
	// serves as unique id for MultipleSlot attribute
	private String _uniqueID;
	
	public MultipleSlot() {
		
	}
	
	public MultipleSlot(String desc, String uniqueID) {
		_desc = desc;
		_uniqueID = uniqueID;
	}

	public String getDesc() {
		return _desc;
	}

	public String getuniqueID() {
		return _uniqueID;
	}

	public void setDesc(String desc) {
		_desc = desc;
	}

	public void setuniqueID(String uniqueID) {
		_uniqueID = uniqueID;
	}

}
