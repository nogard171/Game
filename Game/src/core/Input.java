package core;

import java.awt.Point;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class Input {
	private static int[] buttons;
	private static int[] buttonCount;
	private static boolean[] buttonDown;

	private static int[] keysCount;
	private static boolean[] keysDown;

	public static void setup() {
		int bCount = Mouse.getButtonCount();
		buttons = new int[bCount];
		buttonCount = new int[bCount];
		buttonDown = new boolean[bCount];
		for (int b = 0; b < bCount; b++) {
			buttons[b] = b;
			buttonCount[b] = 0;
		}

		keysCount = new int[256];
		keysDown = new boolean[256];
		for (int k = 0; k < 256; k++) {
			keysCount[k] = 0;
			keysDown[k] = false;
		}
	}

	public static void poll() {

		if (buttons == null || keysDown == null) {
			setup();
		}
		for (int b = 0; b < buttons.length; b++) {
			buttonCount[b] = (buttonDown[b] == true ? 1 : 0);
			buttonDown[b] = Mouse.isButtonDown(buttons[b]);
		}
		for (int k = 0; k < 256; k++) {
			keysCount[k] = (keysDown[k] == true ? 1 : 0);
			keysDown[k] = Keyboard.isKeyDown(k);
		}
	}

	public static boolean isKeyPressed(int index) {
		boolean isPressed = (keysDown[index] == true && keysCount[index] == 0 ? true : false);
		return isPressed;
	}

	public static boolean isKeyDown(int index) {
		return keysDown[index];
	}

	public static boolean isMousePressed(int index) {
		boolean isPressed = (buttonDown[index] == true && buttonCount[index] == 0 ? true : false);
		return isPressed;
	}

	public static boolean isMouseDown(int index) {
		return buttonDown[index];
	}

	public static Point getMousePoint() {
		return new Point(Mouse.getX(), Window.height - Mouse.getY());
	}
}