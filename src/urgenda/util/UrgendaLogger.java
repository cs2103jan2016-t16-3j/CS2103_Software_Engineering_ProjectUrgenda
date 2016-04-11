//@@author A0127358Y
package urgenda.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * UrgendaLogger a the class under Utils component. It is used for logging and tracking of 
 * performance of Urgenda. It is global to all other components.
 *
 */
public class UrgendaLogger {
	
	private final static Logger myLogger = Logger.getLogger(UrgendaLogger.class.getName());
	private static UrgendaLogger _loggerInstance = null;
	
	private static File _parentDir;

	/**
	 * Singleton constructor of UrgendaLogger so that all classes uses the same logger and 
	 * logging could be appended to a single log file.
	 * @return _loggerInstance
	 * The instance of Singleton pattern UrgendaLogger.
	 */
	public static UrgendaLogger getInstance() {
		if (_loggerInstance == null) {
			setUpLogger();
			_loggerInstance = new UrgendaLogger();
		}
		return _loggerInstance;
	}

	/**
	 * This method is for configuration and setting up of logger.
	 */
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

	/**
	 * Getter for the private attribute of UrgendaLogger class.
	 * @return myLogger
	 * The logger used for logging.
	 */
	public Logger getLogger() {
		return myLogger;
	}

}
