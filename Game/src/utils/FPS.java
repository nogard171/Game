package utils;

import org.lwjgl.Sys;

public class FPS {
	private static long lastFrame;
	private static int fps;
	private static int currentFPS;
	private static long lastFPS = 0;

	public static void setup() {
		Debugger.log("FPS Util Setup Started");
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer
		Debugger.log("FPS Util Completed Setup");
	}

	public static int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			fps = currentFPS;
			currentFPS = 0;
			lastFPS += 1000;
		}
		currentFPS++;
	}

	public static int getFPS() {
		return fps;
	}
}
