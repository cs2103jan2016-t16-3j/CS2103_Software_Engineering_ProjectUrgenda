package urgenda.storage;

import java.io.File;
import java.util.ArrayList;

import urgenda.util.*;

public class AStorage implements StorageInterface{
	private FileEditor _file;
	private FileEditor _settings;
	private ArrayList<String> _fileDataStringArr;
	
	public AStorage(){
		_file = new FileEditor();
		_file.retrieveFromFile(_fileDataStringArr);
		
	}
	
	public int updateArrayLists(ArrayList<Task> _tasks, ArrayList<Task> _archive){
		return 0;
	}

	public void save(ArrayList<Task> _tasks, ArrayList<Task> _archive){
		
	}
}
