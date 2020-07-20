package utils;

import java.awt.Point;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import data.Settings;

public class Input {
	// keyboard variables
	private static boolean[] keys;
	private static int key_count = 255;
	// mouse variables
	private static boolean[] buttons;

	public static void setup() {
		keys = new boolean[key_count];
		for (int k = 0; k < keys.length; k++) {
			keys[k] = false;
		}

		buttons = new boolean[3];
		for (int m = 0; m < buttons.length; m++) {
			buttons[m] = false;
		}
	}

	public static void poll() {
		for (int k = 0; k < keys.length; k++) {
			keys[k] = Keyboard.isKeyDown(k);
		}

		for (int m = 0; m < buttons.length; m++) {
			buttons[m] = Mouse.isButtonDown(m);
		}
	}

	public static boolean isKeyDown(int key) {
		return keys[key];
	}

	public static boolean isMouseDown(int button) {
		return buttons[button];
	}

	public static Point getMousePoint() {
		return new Point(Mouse.getX(), Settings._Height - Mouse.getY());
	}

	public static void clean() {
		keys = null;
		buttons = null;
	}
}
