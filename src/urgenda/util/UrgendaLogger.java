package urgenda.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class UrgendaLogger {

	private final static Logger myLogger = Logger.getLogger(UrgendaLogger.class.getName());
	private static UrgendaLogger _loggerInstance = null;

	public static UrgendaLogger getInstance() {
		if (_loggerInstance == null) {
			setUpLogger();
			_loggerInstance = new UrgendaLogger();
		}
		return _loggerInstance;
	}

	private static void setUpLogger() {
		try {
			FileHandler fh = new FileHandler("UrgendaLog.txt");
			fh.setFormatter(new SimpleFormatter());
			myLogger.addHandler(fh);
			myLogger.setUseParentHandlers(false);
			myLogger.setLevel(Level.FINEST);
		} catch (IOException e) {
			myLogger.log(Level.SEVERE, "Error occur in FileHandler.", e);
		}
	}
	
	public Logger getLogger() {
		return myLogger;
	}

}
