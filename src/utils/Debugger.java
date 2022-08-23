package utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Debugger {
//class for handling exceptions and everything that may give an error.
	// it will log before closing, and evenually log every possible variable in the
	// game to a custom data file for storing all the variables.
	private static String logFile = "game.log";
	private static LinkedList<String> logData = new LinkedList<String>();

	// gets current date and time in desiresd format
	private static String getDateTime() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}

	// log an exception to the log file
	public static void logException(String message) {
		System.out.println("Date:" + getDateTime());
		String data = getDateTime() + "\tException:" + message + System.getProperty("line.separator");
		logData.add(data);
		FileHandler.writeFile(logFile, data, true);
	}

//log an error to the log file
	public static void logError(String message) {
		String data = getDateTime() + "\tError:" + message + System.getProperty("line.separator");
		logData.add(data);
		FileHandler.writeFile(logFile, data, true);
	}

//log a general message
	public static void log(String message) {
		System.out.println("Date:" + getDateTime());
		String data = getDateTime() + "\tGeneral:" + message + System.getProperty("line.separator");
		logData.add(data);
		FileHandler.writeFile(logFile, data, true);
	}

//produce a data dump for uplaod
	public static void getDataDump() {

	}
}
