package utils;

public class GLTicker {
	public static long tickCount = 0;
	private static long previousTick = 0;
	private static boolean ticked = false;
	private static long currentTime = 0;
	private static long endTime = -1;
	private static long tickTime = 1;

	public static void update() {
		currentTime = System.currentTimeMillis();
		if (endTime <= -1) {
			endTime = currentTime + (tickTime * 1000);
		}

		if (tickCount > previousTick) {
			previousTick = tickCount;
			ticked = true;
		} else {
			ticked = false;
		}

		if (currentTime > endTime) {
			tickCount++;
			endTime = currentTime + (tickTime * 1000);
		}
	}
}
