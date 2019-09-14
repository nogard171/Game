package utils;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

public class FPS {
	public static long lastFrame;
	public static int fps;
	public static long lastFPS;

	public static void setup() {
		getDelta();
		lastFPS = getTime();
	}

	public static int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
}
