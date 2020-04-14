package core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import game.Base;

public class Window {
	public static void setup() {
		try {
			Display.setDisplayMode(new DisplayMode(Integer.parseInt(Base.settings.getProperty("window.width")),
					Integer.parseInt(Base.settings.getProperty("window.height"))));

			Display.setResizable(true);
			Display.setFullscreen(Boolean.parseBoolean(Base.settings.getProperty("window.fullscreen")));
			Display.create();

			setupViewport();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			if (Base.settings.contains("window.highdpi")) {
				System.setProperty("org.lwjgl.opengl.Display.enableHighDPI",
						Base.settings.getProperty("window.highdpi"));
			}
		} catch (NumberFormatException | LWJGLException e) {

		}
	}

	private static void setupViewport() {
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	public static void update() {
		if (Display.wasResized()) {
			Base.settings.setProperty("window.width", String.valueOf(Display.getWidth()));
			Base.settings.setProperty("window.height", String.valueOf(Display.getHeight()));

			setupViewport();

		}
		Display.update();
		if (Base.settings.containsKey("window.vsync")) {
			if (Boolean.parseBoolean(Base.settings.getProperty("window.vsync"))) {
				if (Base.settings.containsKey("window.fps")) {
					int fps = Integer.parseInt(Base.settings.getProperty("window.fps"));
					if (fps > 999) {
						Display.sync(999);
					} else {
						Display.sync(fps);
					}
				} else {
					Display.sync(999);
				}
			}
		}
	}

	public static void clear() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
	}

	public static void destroy() {
		Display.destroy();
	}

	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}
}
