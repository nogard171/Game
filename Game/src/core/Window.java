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

	public static void start() {
		try {
			// DisplayMode dm = new DisplayMode(width, height);
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setResizable(true);
			Display.create();

//			Mouse.setGrabbed(true);
			hideCursor();

		} catch (LWJGLException e) {
			// e.printStackTrace();
			System.out.println("failed");
		}
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

	public static void update() {
		resize();
		Display.update();
		if (Display.isActive()) {
			Display.sync(999);
		} else {
			Display.sync(30);
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