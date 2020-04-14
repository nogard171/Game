package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import game.Base;

public class Logger {
	public static void writeLog(String message) {
		String logFile = Base.settings.getProperty("system.log");
		if (logFile != null) {
			try {

				BufferedReader reader = new BufferedReader(new FileReader(Base.settings.getProperty("system.log")));
				int lines = 0;
				while (reader.readLine() != null) {
					lines++;
				}
				reader.close();

				if (lines > 1000) {
					File f = new File(Base.settings.getProperty("system.log"));
					if (f.delete()) {
					} else {
						System.out.println("Failed to remove log");
					}
				}

				System.out.println("Message: " + message);
				BufferedWriter writer = new BufferedWriter(
						new FileWriter(Base.settings.getProperty("system.log"), true));
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
				LocalDateTime now = LocalDateTime.now();
				String dateTime = dtf.format(now);
				String finalMessage = dateTime + '\t' + message;

				writer.newLine();
				writer.write(finalMessage);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
