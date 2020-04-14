package core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class Window {

	public static void start() {
		try {
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.setResizable(true);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static boolean close() {
		return Display.isCloseRequested();
	}

	public static void setup() {

	}

	public static void update() {
		Display.update();
		Display.sync(100);
	}

	public static void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
	}

	public static void destroy() {
		Display.destroy();
	}

}
