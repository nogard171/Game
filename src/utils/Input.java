package utils;

import java.awt.Point;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import core.Window;
import game.Database;

public class Input {
	private static int[] buttons;
	private static int[] buttonCount;
	private static boolean[] buttonDown;

	private static int[] keyCount;
	private static boolean[] keysDown;

	public static void setup() {
		Debugger.log("Input Setup Started");
		int bCount = Mouse.getButtonCount();
		Debugger.log("Input Mouse Button Count: " + bCount);
		buttons = new int[bCount];
		buttonCount = new int[bCount];
		buttonDown = new boolean[bCount];

		keyCount = new int[256];
		keysDown = new boolean[256];
		Debugger.log("Input Completed Variable Initlization");

		for (int b = 0; b < bCount; b++) {
			buttons[b] = b;
			buttonCount[b] = 0;
		}
		Debugger.log("Input Completed Zeroing Mouse Buttons");
		for (int k = 0; k < 256; k++) {
			keyCount[k] = 0;
			keysDown[k] = false;
		}
		Debugger.log("Input Completed Zeroing Keyboard Buttons");
	}

	public static void poll() {
		for (int b = 0; b < buttons.length; b++) {
			buttonCount[b] = (buttonDown[b] == true ? 1 : 0);
			buttonDown[b] = Mouse.isButtonDown(buttons[b]);
		}
		for (int k = 0; k < 256; k++) {
			keyCount[k] = (keysDown[k] == true ? 1 : 0);
			keysDown[k] = Keyboard.isKeyDown(k);
		}
	}

	public static boolean isMousePressed(int index) {
		boolean isPressed = (buttonDown[index] == true && buttonCount[index] == 0 ? true : false);
		return isPressed;
	}

	public static boolean isKeyDown(int index) {
		return keysDown[index];
	}

	public static boolean isKeyPressed(int index) {
		boolean isPressed = (keysDown[index] == true && keyCount[index] == 0 ? true : false);
		return isPressed;
	}

	public static boolean isMouseDown(int index) {
		return buttonDown[index];
	}

	public static Point getMousePoint() {
		return new Point(Mouse.getX(), Database.height - Mouse.getY());
	}

	public static Point getMousePointInWorld() {

		return new Point(Mouse.getX() + Database.viewFrame.x, (Database.height - Mouse.getY()) + Database.viewFrame.y);
	}
}
