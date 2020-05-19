package utils;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

public class FPS {
	/** time at last frame */
	private static long lastFrame;

	/** frames per second */
	private static int fps;
	private static int currentFPS;
	/** last fps time */
	private static long lastFPS = 0;
	
	public static float delta = 0;

	public static void setup() {
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer
	}

	/**
	 * Calculate how many milliseconds have passed since last frame.
	 * 
	 * @return milliseconds passed since last frame
	 */
	public static float getDelta() {
		long time = getTime();
		float delta = (float) (time - lastFrame);
		lastFrame = time;
		return delta;
	}

	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public static void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			fps = currentFPS;
			currentFPS = 0;
			lastFPS += 1000;
		}
		currentFPS++;
		
		delta = getDelta();
	}

	public static int getFPS() {
		// TODO Auto-generated method stub
		return fps;
	}
}
