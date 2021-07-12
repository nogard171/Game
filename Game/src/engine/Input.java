package engine;

import java.awt.Point;

import org.lwjgl.input.Mouse;

public class Input {
	private static int[] buttons;
	private static int[] buttonCount;
	private static boolean[] buttonDown;

	int[] keys;
	boolean[] keysDown;

	public static void setup() {
		int bCount = Mouse.getButtonCount();
		buttons = new int[bCount];
		buttonCount = new int[bCount];
		buttonDown = new boolean[bCount];
		for (int b = 0; b < bCount; b++) {
			buttons[b] = b;
			buttonCount[b] = 0;
		}
	}

	public static void poll() {
		for (int b = 0; b < buttons.length; b++) {
			buttonCount[b] = (buttonDown[b] == true ? 1 : 0);
			buttonDown[b] = Mouse.isButtonDown(buttons[b]);
		}
	}

	public static boolean isMousePressed(int index) {
		boolean isPressed = (buttonDown[index] == true && buttonCount[index] == 0 ? true : false);
		return isPressed;
	}

	public static boolean isMouseDown(int index) {
		return buttonDown[index];
	}

	public static Point getMousePoint() {
		return new Point(Mouse.getX(), Window.height-Mouse.getY());
	}
}
