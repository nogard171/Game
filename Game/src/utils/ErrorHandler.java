package utils;

public class ErrorHandler {
	public static void handle(Exception e, Severity s) {
		switch (s) {
		case HIGH:
			Logger.log(e.getMessage());
			System.exit(0);
			break;
		default:
			Logger.log(e.getMessage());
		}
	}
}
