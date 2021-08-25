package utils;

import java.time.ZonedDateTime;

public class Ticker {
	private long ticks = 0;
	private long previousTickMilli = -1;
	// private static long tickTime = 1000;
	private boolean ticked = false;

	private long getMilli() {
		return ZonedDateTime.now().toInstant().toEpochMilli();
	}

	public void poll(int tickTime) {
		// tickTime=100;
		ticked = false;
		long current = getMilli();

		if (current >= previousTickMilli) {
			previousTickMilli = current + tickTime;
			ticks = getTicks() + 1;
			ticked = true;
		}
	}

	public boolean ticked() {
		return ticked;
	}

	public long getTicks() {
		return ticks;
	}
}
