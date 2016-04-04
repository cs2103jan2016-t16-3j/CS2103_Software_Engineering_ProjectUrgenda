package urgenda.storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import urgenda.util.UrgendaLogger;

public class Help {
	private static final String HELP_FILE_LOCATION = "resources/help.txt";
	private static final String SEPARATOR_HELP_SECTION = "\n\n";
	private static final String SEPARATOR_HELP_LINES = "\n";
	
	
	private static UrgendaLogger logger = UrgendaLogger.getInstance();

	private String _help;
	private ArrayList<String> _manual = new ArrayList<String>();

	public Help(boolean test) {
		logger.getLogger().info("constructing Help object");
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(HELP_FILE_LOCATION);
		_help = getStringFromInputStream(is);
		String[] manual = _help.split(SEPARATOR_HELP_SECTION);
		for (int i = 0; i < manual.length; i++){
			String help = manual[i].trim();
			_manual.add(help);
		}
		logger.getLogger().info("retrieved helpfile.");
	}

	private String getStringFromInputStream(InputStream is) {

		BufferedReader breader = null;
		StringBuilder helpString = new StringBuilder();

		String line;
		try {
			breader = new BufferedReader(new InputStreamReader(is));
			while ((line = breader.readLine()) != null) {
				helpString.append(SEPARATOR_HELP_LINES);
				helpString.append(line);
			}
			breader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return helpString.toString();
		
	}

	public ArrayList<String> getHelp() {
		return _manual;
	}


}
