package utils;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

public class FPS {
	// store the lastframe
	private static long lastFrame;
	// store the current fps in the cycle
	private static int cycleFps;
	// store the current average fps
	private static int fps;
	// store the last fps to be used to determine fps
	private static long lastFPS;

	// setup the fps counter
	public static void setup() {
		getDelta();
		lastFPS = getTime();
	}

	// return the delta time
	public static int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	// get the time in seconds
	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static int getFPS() {
		return fps;
	}

	// poll the fps counter
	public static void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			// Display.setTitle("FPS: " + fps);
			fps = cycleFps;
			cycleFps = 0;
			lastFPS += 1000;
		}
		cycleFps++;
	}
}
