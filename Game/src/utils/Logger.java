package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import data.Settings;

public class Logger {
	private static File logFile;
	private static FileWriter logWriter;

	public static void log(String message) {
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		try {
			if (logFile == null) {
				logFile = new File(Settings.logFilename);
			}

			logFile.createNewFile();
			if (logWriter == null) {
				logWriter = new FileWriter(logFile, true);
			}
			logWriter.append(timeStamp + '\t' + "|" + '\t' + message);
			logWriter.append('\n');
			logWriter.flush();

		} catch (IOException e) {
			System.out.println("An Error Has Occured.");
			System.exit(0);
		}

	}

	public static void close() {

		try {
			logWriter.close();
		} catch (IOException e) {
			System.out.println("An Error Has Occured.");
			System.exit(0);
		}
	}
}
