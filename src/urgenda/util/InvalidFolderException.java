package urgenda.util;

public class InvalidFolderException extends Exception{

	/**
	 * System Generated serial number for InvalidNameException.
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERROR_MESSAGE = "%1$s is an invalid folder path. Please enter a valid folder path.";
	
	private String _path;
	
	public InvalidFolderException(String path){
		_path = path;
	}
	
	public String getPath(){
		return _path;
	}
	
	public String getMessage(){
		return String.format(ERROR_MESSAGE, _path.toUpperCase());
	}

}
