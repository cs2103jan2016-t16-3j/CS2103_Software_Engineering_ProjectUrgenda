package urgenda.util;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

	private static FileHandler fileTxt;
	private static SimpleFormatter formatterTxt;

	public void setup() throws IOException {
		Logger logger = Logger.getLogger(MyLogger.class.getName());

		Logger consoleLogger = Logger.getLogger("");
		Handler[] handlers = consoleLogger.getHandlers();
		if (handlers[0] instanceof ConsoleHandler) {
			consoleLogger.removeHandler(handlers[0]);
		}
		logger.setLevel(Level.INFO);
		fileTxt = new FileHandler("UrgendaLogging.log");

		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);
	}
}
