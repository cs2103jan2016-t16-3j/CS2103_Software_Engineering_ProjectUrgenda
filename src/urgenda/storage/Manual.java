package urgenda.storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.ocpsoft.prettytime.shade.edu.emory.mathcs.backport.java.util.Arrays;

import urgenda.util.UrgendaLogger;

public class Manual {
	private static final String HELP_FILE_LOCATION = "resources/help.txt";
	private static final String DEMO_FILE_LOCATION = "resources/demo.txt";
	private static final String HELP_TYPE = "HELP";
	private static final String DEMO_TYPE = "DEMO";
	private static final String SEPARATOR_SECTION = "\n\n";
	private static final String SEPARATOR_LINES = "\n";
	
	
	
	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	private String _text;
	private ArrayList<String> _manual = new ArrayList<String>();
	private ArrayList<Integer> _demoIndexes = new ArrayList<Integer>();
	
	public Manual(String type){
		if (type.equalsIgnoreCase(HELP_TYPE)) {
			createHelpManual();
		} else if (type.equalsIgnoreCase(DEMO_TYPE)){
			createDemoManual();
		}
	}

	@SuppressWarnings("unchecked")
	private void createDemoManual() {
		logger.getLogger().info("constructing Demo object");
		InputStream demoInput = this.getClass().getClassLoader().getResourceAsStream(DEMO_FILE_LOCATION);
		createManual(demoInput);
		String indexes = _manual.get(0).trim();
		_manual.remove(0);
		String[] demoIndex = indexes.split(" ");
		ArrayList<String> demoIndexesString = new ArrayList<String>(Arrays.asList(demoIndex));
		for (String index: demoIndexesString) {
			_demoIndexes.add(Integer.valueOf(index));
		}
		logger.getLogger().info("retrieved demofile.");
		
	}

	public void createManual(InputStream is) {
		_text = readFileInOneString(is);
		String[] manual = _text.split(SEPARATOR_SECTION);
		for (int i = 0; i < manual.length; i++){
			String textLine = manual[i].trim();
			_manual.add(textLine);
		}
	}

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

	public ArrayList<String> getManual() {
		return _manual;
	}
	
	public ArrayList<Integer> getIndexes(){
		return _demoIndexes;
	}


}
