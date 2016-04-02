package urgenda.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;

import urgenda.util.UrgendaLogger;

public class Help {
	private static final String SETTINGS_HELP = "help.txt";
	private static final String SETTINGS_DIRECTORY = "settings";

	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	private String _help;
	private File _parentDir;
	private File _file;
	private ArrayList<String> _manual = new ArrayList<String>();

	public Help() {
		_parentDir = new File(SETTINGS_DIRECTORY);
		_parentDir.mkdir();
		_file = new File(_parentDir, SETTINGS_HELP);
		if (_file.exists()) {
			_help = retrieveFromFile();
		} else {
			ArrayList<String> helpList = new ArrayList<String>();
			addToHelp(helpList);
			writeToFile(helpList);
			_help = retrieveFromFile();
		}
	}

	public Help(boolean test) {
		logger.getLogger().info("constructing Help object");
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("resources/help.txt");
		String result = getStringFromInputStream(is);
//		System.out.println(result);
		logger.getLogger().info(result);
		String[] manual = result.split("\n\n");
//		System.out.println(manual.length);
//		for (int i = 0; i < manual.length; i++){
//			System.out.println("index number: " + i);
//			System.out.println(manual[i]);
//		}
		for (int i = 0; i < manual.length; i++){
//			System.out.println("from array" + manual[i]);
			String help = manual[i].trim();
			_manual.add(help);
//			System.out.println("from arraylist" + _manual.get(i));
		}
		
//		addToManual(is);
		for (int i = 0; i < _manual.size(); i++) {
//			System.out.println("index number: " + i);
//			System.out.println(_manual.get(i));
		}
		logger.getLogger().info("retrieved helpfile.");
	}

	private void addToManual(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = br.readLine()) != null) {
				_manual.add(line);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append("\n");
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	public ArrayList<String> getHelp() {
		return _manual;
	}

	private void writeToFile(ArrayList<String> fileDataStringArr) {
		try {
			PrintWriter writer = new PrintWriter(_file);
			for (String phrase : fileDataStringArr) {
				writer.println(phrase);
			}
			writer.close();
		} catch (FileNotFoundException e) {
			logger.getLogger().info("no such file found");
		}
	}

	private void addToHelp(ArrayList<String> a) {
		a.add("Adding Tasks:");
		a.add("Simply type your task description with or without dates and times. A task will then be separated into 3 types:");
		a.add("Event: A task with start and end time.");
		a.add("Deadline: A task with only an end time.");
		a.add("Untimed: A task with no start or end time.\n");
		a.add("Deleting Tasks:");
		a.add("Command words - Delete / Del / Erase / Remove");
		a.add("NOTE: deleting a task is not completing a task");
		a.add("Specify the task number, task description or using the selector or a range");
		a.add("Example: \"Delete Dinner with Mum\", \"Del 4\"\n");
		a.add("Marking as Completed:");
		a.add("Command words - Done / Completed / Do / Mark / Finish / Fin");
		a.add("Specify the task number, task description or using the selector or a range");
		a.add("Example: \"Done Dinner with Mum\", \"Mark 4\"\n");
		a.add("Editing a Task:");
		a.add("Command words - Edit / Update / Change / Mod");
		a.add("Specify the task number, task description or using the selector or a range");
		a.add("Example: \"Change Dinner with Mum at Nex\", \"Edit 4 Dinner with Mum and Dad\"\n");
		a.add("Search:");
		a.add("Command words - Search / Find / Show / View / List");
		a.add("When searching a description, all tasks containing that description will be displayed");
		a.add("When searching for a date, all tasks with this date wil be displayed.");
		a.add("When searching for a month, all tasks during that month will be displayed.\n");
		a.add("Home:");
		a.add("returns you to the home display to show all tasks\n");
		a.add("Exit:");
		a.add("click the top right exit button, or type \"exit\"\n");
		a.add("Showmore:");
		a.add("Shows more details of a task");
		a.add("Specify the task number, using the selector or a range");
		a.add("Example: \"showmore 3\", \"showmore 1-4\"\n");
		a.add("Archive:");
		a.add("Shows the list of tasks that has been marked completed by the user during the last 30 days\n");
		a.add("Undo/Redo:");
		a.add("Allows you to undo the previous action taken. Only commands that affect the task list can be undone.\n");
		a.add("Prioritising Tasks:");
		a.add("Command words - Prioritise / Pri");
		a.add("This feature allows you to mark a task as important. Any task marked as important will be at the top of the list, for overdue, today or other tasks");
		a.add("Specify the task number, task description or using the selector or a range");
		a.add("Prioritising a range will result in all the tasks inside the range to be marked as important. If the whole range is already marked as important, then all the tasks in the range will be marked as unimportant");
		a.add("Example: \"Pri 1-5\"\n");
		a.add("Finding free time:");
		a.add("Command word - Findfree");
		a.add("This allows you to find free time that has no events. Simply key in the desired time range to search for free time");
		a.add("Example: \"findfree today 12pm - 9pm\"\n");
		a.add("Postponing a task:");
		a.add("Command words - Postpone / Delay / Move");
		a.add("This feature allows you to push back a task by the time entered by you");
		a.add("Specify the task number, task description or using the selector");
		a.add("Example: \"postpone 3 by 2 days\"\n");
		a.add("Multiple timeslots for a single task:");
		a.add("Command word - Block / Confirm(confirms a timeslot)");
		a.add("This feature allows you to add more than one timeslot to a task, and when you want to confirm what which timeslot it is, just type confirm");
		a.add("Example: \"Block Dinner with Mum at Monday 6pm to 7pm, Wednesday 7pm to 9pm\"\n");

	}

	private String retrieveFromFile() {
		String phrase = null;
		try {
			FileReader reader = new FileReader(_file);
			BufferedReader breader = new BufferedReader(reader);
			StringBuffer stringBuffer = new StringBuffer();
			phrase = readFileToString(breader, stringBuffer);
			phrase = stringBuffer.toString().trim();
			breader.close();
			reader.close();
			logger.getLogger().info("successful retrieval of data");
		} catch (FileNotFoundException e) {
			logger.getLogger().info("no such file found" + e);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return phrase;
	}

	private String readFileToString(BufferedReader breader, StringBuffer stringBuffer) throws IOException {
		String phrase;
		while ((phrase = breader.readLine()) != null) {
			stringBuffer.append(phrase).append("\n");
		}
		return phrase;
	}

}
