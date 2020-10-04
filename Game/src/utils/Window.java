package utils;

import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import data.Settings;
import threads.GameThread;

public class Window {

	public static int width = 800;
	public static int height = 600;
	public static boolean isResizable = true;

	public static void create() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setResizable(isResizable);
			Display.create();

			setup();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static boolean wasResized() {
		return Display.wasResized();
	}

	public static boolean differentResolution() {
		boolean diff = false;
		if (width != Display.getWidth() || height != Display.getHeight()) {
			diff = true;
		}
		return diff;
	}

	private static void setup() {
		width = Display.getWidth();
		height = Display.getHeight();

		GL11.glViewport(0, 0, width, height);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);

	}

	public static void update() {

		if (Display.wasResized()) {
			setup();
		}
		if (Display.isCloseRequested()) {
			GameThread.close();

		}

	}

	public static void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

	}

	public static void finalizeRender() {

		Display.update();
	}

	public static void destroy() {
		Display.destroy();
	}

	public static int getMouseX() {
		return Mouse.getX();
	}

	public static int getMouseY() {
		return height - Mouse.getY();
	}

	public static boolean isMousePressed(int button) {
		boolean isPressed = false;

		// while (Mouse.next()) {
		if (Mouse.getEventButtonState()) {
			if (Mouse.isButtonDown(button)) {
				isPressed = true;
			}
		}
		// }
		return isPressed;
	}

	public static boolean isKeyDown(int key) {
		if (!Settings.waitForKey) {
			return Keyboard.isKeyDown(key);
		} else {
			return false;
		}
	}

	public static int getKeyPressed() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (!Keyboard.isRepeatEvent()) {
					return Keyboard.getEventKey();
				}
			}
		}

		return -1;
	}

	public static String getKey(int keycode) {
		return Keyboard.getKeyName(keycode);
	}

	public static boolean isKeyPressed(int key) {
		if (!Settings.waitForKey) {
			boolean isPressed = false;
			while (Keyboard.next()) {
				if (key < 0) {
					continue;
				}
				if (Keyboard.getEventKeyState()) {
					if (!Keyboard.isRepeatEvent()) {
						if (Keyboard.isKeyDown(key)) {
							isPressed = true;
						}
					}
				}
			}
			return isPressed;
		} else {
			return false;
		}
	}

	public static boolean isMainAction() {
		return Mouse.isButtonDown(Settings.mainActionIndex);
	}

	public static boolean isSecondaryAction() {
		return Mouse.isButtonDown(Settings.secondaryActionIndex);
	}
}
