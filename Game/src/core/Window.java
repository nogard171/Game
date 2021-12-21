package core;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import game.Database;
import utils.Debugger;

public class Window {
	public static boolean close = false;
	public static boolean wasResized = false;

	public static void start() {
		try {
			Debugger.log("Looking for Desired Display Mode");
			DisplayMode dm = getTargetDispalyMode();

			Debugger.log("Found Desired Display Mode(" + dm.getWidth() + "px by " + dm.getHeight() + " @ "
					+ dm.getFrequency() + "Hz");
			// if (Database.resizable) {
			Debugger.log("Display Set Resizable Mode to: " + Database.resizable);
			Display.setResizable(Database.resizable);
			// }
			// if (Database.fullscreen) {
			Debugger.log("Display Set Fullscreen Mode to: " + Database.fullscreen);
			Display.setFullscreen(Database.fullscreen);
			// }
			Display.setDisplayMode(dm);
			Display.create();
			Debugger.log("Display created");
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	private static DisplayMode getTargetDispalyMode() {
		DisplayMode targetDisplayMode = new DisplayMode(Database.width, Database.height);
		try {
			for (DisplayMode dm : Display.getAvailableDisplayModes()) {
				if (Database.resolutionMax && (dm.getWidth() > targetDisplayMode.getWidth()
						|| dm.getHeight() > targetDisplayMode.getHeight())) {
					targetDisplayMode = dm;
				} else if (!Database.resolutionMax) {
					if (dm.isFullscreenCapable() && dm.getWidth() == Database.width
							&& dm.getHeight() == Database.height) {
						targetDisplayMode = dm;
						break;
					}
				}
			}
		} catch (LWJGLException e) {
			Debugger.logException(e.getMessage());
		}
		return targetDisplayMode;
	}

	public static void close() {
		close = true;
	}

	public static boolean isClose() {
		close = Display.isCloseRequested();
		return close;
	}

	public static void resize() {
		if (Display.wasResized()) {
			setupViewport();
			wasResized = true;
		}
	}

	private static void setupViewport() {
		Database.width = Display.getWidth();
		Database.height = Display.getHeight();
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Database.width, Database.height, 0, 1, -1);
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
		if (!Display.isActive()) {
			Display.sync(Database.inactiveFPS);
		} else if (Database.targetFPS != -1) {
			Display.sync(Database.targetFPS);
		} else {
			Display.sync(60);
		}
	}

	public static void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
	}

	public static void destroy() {
		Display.destroy();
	}
}
