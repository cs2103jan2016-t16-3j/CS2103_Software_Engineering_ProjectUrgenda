package urgenda.storage;


import java.util.ArrayList;

import urgenda.util.*;

public class AStorage implements StorageInterface{
	private FileEditor _file;
	private FileEditor _settings;
	private ArrayList<String> _fileDataStringArr;
	private Decryptor _decryptor;
	private Encryptor _encryptor;
	
	public AStorage(){
		_file = new FileEditor();
		_fileDataStringArr = new ArrayList<String>();
		_file.retrieveFromFile(_fileDataStringArr);
		_decryptor = new Decryptor();
		_encryptor = new Encryptor();
		
	}
	
	public int updateArrayLists(ArrayList<Task> tasks, ArrayList<Task> archive){
		int taskId = _decryptor.decrypt(tasks, _fileDataStringArr);
//		int archiveId = _decryptor.decrypt(archive, _fileDataStringArr);
		return taskId;
	}

	public void save(ArrayList<Task> tasks, ArrayList<Task> archive){
		_encryptor.encrypt(tasks, _fileDataStringArr);
//		_encryptor.encrypt(archive, _fileDataStringArr);
		_file.writeToFile(_fileDataStringArr);
	}
}
