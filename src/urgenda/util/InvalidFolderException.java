package urgenda.util;

public class InvalidFolderException extends Exception{

	/**
	 * System Generated serial number for InvalidFolderException.
	 */
	private static final long serialVersionUID = 1L;
	private static final String ERROR_MESSAGE = "%1$s is an invalid folder path. Please enter a valid folder path.";
	
	private String _path;
	
	/**
	 * Default constructor for InvalidFolderException
	 * 
	 * @param path absolute path of preferred file destination 
	 */
	public InvalidFolderException(String path){
		_path = path;
	}
	
	/**
	 * Returns the path of the file dest.
	 * @return String of the file dest.
	 */
	public String getPath(){
		return _path;
	}
	
	/**
	 * Returns 
	 */
	@Override
	public String getMessage(){
		return String.format(ERROR_MESSAGE, _path.toUpperCase());
	}

}
