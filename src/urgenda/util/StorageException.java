//@@author A0126888L
package urgenda.util;

public class StorageException extends Exception {
	/**
	 * System Generated serial number for StorageException.
	 */
	private static final long serialVersionUID = 1L;

	private static final String ERROR_MESSAGE = "%1$s already exist in %2$s. \nLoading tasks from existing file";

	private String _dir;
	private String _name;

	/**
	 * Default constructor for Storage Exception.
	 * 
	 * @param dir
	 *            directory containing the existing file.
	 * @param name
	 *            name of the existing file.
	 */
	public StorageException(String dir, String name) {
		_dir = dir;
		_name = name;
	}

	/**
	 * Returns the directory containing the existing file.
	 * 
	 * @return String of the directory.
	 */
	public String getDir() {
		return _dir;
	}

	/**
	 * Returns the existing file name.
	 * 
	 * @return String of the file name.
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Returns the error message containing the name and folder of the existing file.
	 * 
	 * @return String of the error message
	 */
	@Override
	public String getMessage() {
		String error = String.format(ERROR_MESSAGE, _name, _dir.toUpperCase());
		return error;
	}
}
