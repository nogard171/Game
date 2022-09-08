package core;

import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class Window {

	public static int width = 800;
	public static int height = 600;
	public static boolean wasResized = false;
	public static boolean isFullscreen = false;
	public static boolean isResizable = true;
	public static boolean autoDisplayMode = false;

	public static void start() {
		try {
			DisplayMode dm = getDisplayMode();

			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setResizable(isResizable);
			Display.create();

			hideCursor();

		} catch (LWJGLException e) {
			// e.printStackTrace();
			System.out.println("failed");
		}
	}

	private static DisplayMode getDisplayMode() throws LWJGLException {
		DisplayMode newDisplayMode = new DisplayMode(width, height);
		int highestWidth = -1;
		int highestHeight = -1;
		int highestFreq = -1;
		// loop through all possible display modes based on the monitor data
		for (DisplayMode dm : Display.getAvailableDisplayModes()) {
			// setup variables for data needed
			int dmWidth = dm.getWidth();
			int dmHeight = dm.getHeight();
			int dmFreq = dm.getFrequency();
			if (autoDisplayMode) {

			} else {
				if (dmWidth == width && dmHeight == height && dmFreq > highestFreq) {
					highestFreq = dmFreq;
					newDisplayMode = dm;
				}
			}
		}
		return newDisplayMode;
	}

	public static boolean close() {
		return Display.isCloseRequested();
	}

	public static void resize() {
		if (Display.wasResized()) {
			setupViewport();
			wasResized = true;
		}
	}

	private static void setupViewport() {
		width = Display.getWidth();
		height = Display.getHeight();

		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public static void setup() {

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		setupViewport();
	}

	private static int targetFPS = 9999;
	private static int menuFPS = 60;
	private static int inactiveFPS = 60;

	public static void update() {
		resize();
		Display.update();
		if (!Display.isActive()) {
			Display.sync(inactiveFPS);
		} else if (true == false) {
			Display.sync(menuFPS);
		} else if (targetFPS < 0 || targetFPS > 0) {
			Display.sync(targetFPS);
		} else {
			Display.sync(120);
		}
	}

	public static void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);

	}

	public static void destroy() {
		Display.destroy();
	}

	private static void hideCursor() {
		Cursor emptyCursor;
		int min = org.lwjgl.input.Cursor.getMinCursorSize();
		IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
		try {
			emptyCursor = new org.lwjgl.input.Cursor(min, min, min / 2, min / 2, 1, tmp, null);
			Mouse.setNativeCursor(emptyCursor);
		} catch (LWJGLException e) {
			System.out.println("failed");
		}
	}
}
