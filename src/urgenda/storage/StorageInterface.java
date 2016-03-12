package urgenda.storage;

import java.util.ArrayList;
import urgenda.util.Task;

public interface StorageInterface {
	

	// logic will use this function to store all these arraylists in a txtfile
	// IMPT: DO NOT MODIFY CONTENTS INSIDE!
	public void save(ArrayList<Task> _tasks, ArrayList<Task> _archive);
	
	/**
	 * Changes the location of storing the files. 
	 * @param path is required to be a String in directory format
	 * 		e.g. "C:\\Users\\User\\Google Drive\\NUS\\Year2"
	 */
	public void changeFilePath(String path);
	
	/**
	 * Changes the name of the file that stores the ArrayList of tasks and archives
	 * @param name is required to be a String ending with .txt format
	 * 		e.g. "mylist.txt" or "my list.txt"
	 */
	public void changeFileName(String name);
	
	public void changeFileSettings(String path, String name);
}
