package utils;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import data.Settings;

public class Window {

	public static void setup() {

		try {
			Display.setDisplayMode(new DisplayMode(Settings._Width, Settings._Height));
			Display.setResizable(Settings._resizable);
			Display.create();

			setupGL();
		} catch (LWJGLException e) {
			ErrorHandler.handle(e, Severity.HIGH);
		}
	}

	private static void setupGL() {
		setupViewport();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);

	}

	private static void setupViewport() {
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective((float) Settings._Fov, ((float) Settings._Width / (float) Settings._Height), 0.1f, 100);
		GL11.glLoadIdentity();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public static void update() {
		sync();

	}

	public static void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public static boolean initilizedClosing() {
		return Display.isCloseRequested();
	}

	public static void sync() {
		Display.update();
		if (Settings._Vsync) {
			Display.sync(Settings._Fps);
		}
	}

	public static void clean() {

	}
}
