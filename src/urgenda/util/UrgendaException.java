package urgenda.util;

public class UrgendaException extends Exception{
	private static final String ERROR_MESSAGE = "file already exist, will not replace. Loading tasks from new location";
	
	private String _dir;
	private String _name;
	
	public UrgendaException(String dir, String name){
		_dir = dir;
		_name = name;
	}
	
	public String getDir(){
		return _dir;
	}
	
	public String getName(){
		return _name;
	}
	
	public String getMessage(){
		return ERROR_MESSAGE;
	}
}
