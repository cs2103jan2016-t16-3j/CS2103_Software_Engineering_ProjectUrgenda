package urgenda.util;

public class UrgendaException extends Exception{
	private static final String ERROR_MESSAGE = "File already exist. \n Loading tasks from existing file";
	
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
