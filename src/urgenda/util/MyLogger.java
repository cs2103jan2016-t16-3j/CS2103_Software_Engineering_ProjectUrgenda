package urgenda.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

	private final static Logger myLogger = Logger.getLogger(MyLogger.class.getName());
	private static MyLogger instance = null;

	public static MyLogger getInstance() {
		if (instance == null) {
			setUpLogger();
			instance = new MyLogger();
		}
		return instance;
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
