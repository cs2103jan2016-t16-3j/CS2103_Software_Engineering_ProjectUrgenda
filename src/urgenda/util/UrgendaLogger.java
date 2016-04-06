//@@author A0127358Y
package urgenda.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class UrgendaLogger {
	
	private final static Logger myLogger = Logger.getLogger(UrgendaLogger.class.getName());
	private static UrgendaLogger _loggerInstance = null;
	
	private static File _parentDir;

	public static UrgendaLogger getInstance() {
		if (_loggerInstance == null) {
			setUpLogger();
			_loggerInstance = new UrgendaLogger();
		}
		return _loggerInstance;
	}

	private static void setUpLogger() {
		try {
			createFileSettings();
			FileHandler fh = new FileHandler("settings/UrgendaLog.txt");
			fh.setFormatter(new SimpleFormatter());
			myLogger.addHandler(fh);
			myLogger.setUseParentHandlers(false);
			myLogger.setLevel(Level.FINEST);
		} catch (IOException e) {
			myLogger.log(Level.SEVERE, "Error occur in FileHandler.", e);
		}
	}
	
	private static void createFileSettings() {
		_parentDir = new File("settings");
		_parentDir.mkdir();
		
	}

	public Logger getLogger() {
		return myLogger;
	}

}
