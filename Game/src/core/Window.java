package core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Window {

	public static int width = 800;
	public static int height = 600;
	public static int targetFPS = -1;
	public static int inactiveFPS = 30;
	public static boolean wasResized = false;

	public static void start() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setResizable(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
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
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
	}

	public static void update() {
		resize();
		Display.update();
		
		if (!Display.isActive()) {
			Display.sync(inactiveFPS);
		} else if (targetFPS != -1) {
			Display.sync(targetFPS);
		}
		else
		{
			Display.sync(60);
		}
	}

	public static void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public static void destroy() {
		Display.destroy();
	}
}
