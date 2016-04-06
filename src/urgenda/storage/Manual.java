package urgenda.storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;

import urgenda.util.UrgendaLogger;

/**
 * Manual Class is the class where all manuals are retrieved and read. Files
 * here cannot be edited like in the FileEditor class.
 *
 */
public class Manual {
	private static final String HELP_FILE_LOCATION = "resources/help.txt";
	private static final String DEMO_FILE_LOCATION = "resources/demo.txt";
	private static final String HELP_TYPE = "HELP";
	private static final String DEMO_TYPE = "DEMO";
	private static final String SEPARATOR_SECTION = "\n\n";
	private static final String SEPARATOR_LINES = "\n";
	private static final String DELIMITER_INDEXES = " ";

	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	private String _text;
	private ArrayList<String> _manual = new ArrayList<String>();
	private ArrayList<Integer> _demoIndexes = new ArrayList<Integer>();

	/**
	 * Constructor for a Manual. Sorts the manuals into different types before
	 * creating the manual, since each manual comes with their unique method of
	 * constructing.
	 * 
	 * @param type
	 *            String value of the manual type.
	 */
	public Manual(String type) {
		if (type.equalsIgnoreCase(HELP_TYPE)) {
			createHelpManual();
		} else if (type.equalsIgnoreCase(DEMO_TYPE)) {
			createDemoManual();
		}
	}

	/**
	 * Creates a demo manual. This is an add-on to the Help manual, with an
	 * additional String of integers as the first line for the selection
	 * indexes. These indexes are used for selection of tasks in the demo
	 * screen.
	 */
	@SuppressWarnings("unchecked")
	private void createDemoManual() {
		logger.getLogger().info("constructing Demo object");
		InputStream demoInput = this.getClass().getClassLoader().getResourceAsStream(DEMO_FILE_LOCATION);
		createManual(demoInput);
		String indexes = _manual.get(0).trim();
		_manual.remove(0);
		String[] demoIndex = indexes.split(DELIMITER_INDEXES);
		ArrayList<String> demoIndexesString = new ArrayList<String>(Arrays.asList(demoIndex));
		for (String index : demoIndexesString) {
			_demoIndexes.add(Integer.valueOf(index));
		}
		logger.getLogger().info("retrieved demofile.");

	}

	/**
	 * creates a manual with sections from the InputStream given.
	 * 
	 * @param is
	 *            InputStream from the resources of Urgenda to construct
	 *            manuals.
	 */
	public void createManual(InputStream is) {
		_text = readFileInOneString(is);
		String[] manual = _text.split(SEPARATOR_SECTION);
		for (int i = 0; i < manual.length; i++) {
			String textLine = manual[i].trim();
			_manual.add(textLine);
		}
	}

	/*
	 * Creates a help manual after extracting from resources.
	 */
	private void createHelpManual() {
		logger.getLogger().info("constructing Help object");
		InputStream helpInput = this.getClass().getClassLoader().getResourceAsStream(HELP_FILE_LOCATION);
		createManual(helpInput);
		logger.getLogger().info("retrieved helpfile.");
	}

	private String readFileInOneString(InputStream is) {

		BufferedReader breader = null;
		StringBuilder textString = new StringBuilder();

		String line;
		try {
			breader = new BufferedReader(new InputStreamReader(is));
			while ((line = breader.readLine()) != null) {
				textString.append(SEPARATOR_LINES);
				textString.append(line);
			}
			breader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return textString.toString();

	}

	/**
	 * Returns the required manual. Manual is in the form of an ArrayList, with
	 * each section being an element.
	 * 
	 * @return ArrayList of String as a manual.
	 */
	public ArrayList<String> getManual() {
		return _manual;
	}

	/**
	 * Returns the indexes of the demo selection for corresponding indicator to
	 * be displayed at a particular section of the manual.
	 * 
	 * @return ArrayList of Integers for selection purposes. 
	 */
	public ArrayList<Integer> getIndexes() {
		return _demoIndexes;
	}

}
