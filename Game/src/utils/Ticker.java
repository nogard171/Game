package utils;

import java.time.ZonedDateTime;

public class Ticker {
	private static long ticks = 0;
	private static long previousTickMilli = -1;
	private static long tickTime = 1000;
	private static boolean ticked = false;

	private static long getMilli() {
		return ZonedDateTime.now().toInstant().toEpochMilli();
	}

	public static void poll() {
		tickTime=100;
		ticked = false;
		long current = getMilli();

		if (current >= previousTickMilli) {
			previousTickMilli = current + tickTime;
			ticks = getTicks() + 1;
			ticked = true;
		}
	}

	public static boolean ticked() {
		return ticked;
	}

	public static long getTicks() {
		return ticks;
	}
}
