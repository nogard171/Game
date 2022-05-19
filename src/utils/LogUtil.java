package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class LogUtil {
	private static FileOutputStream exceptionLogFile = null;
	private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void addLog(String message) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			if (exceptionLogFile == null) {
				exceptionLogFile = new FileOutputStream("exception.log", true);
			}
			String logMessage = sdf3.format(timestamp) + "\tException:\t" + (message + "\n");
			exceptionLogFile.write(logMessage.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void closeLog() {
		try {
			if (exceptionLogFile != null) {
				exceptionLogFile.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
