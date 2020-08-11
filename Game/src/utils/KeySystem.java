package utils;

import org.lwjgl.input.Keyboard;

public class KeySystem {
	private static boolean[] keys;

	public static void setup() {
		keys = new boolean[255];
		for (int k = 0; k < 255; k++) {
			keys[k] = false;
		}
	}

	public static void poll() {
		if (keys == null) {
			setup();
		}
		for (int k = 0; k < 255; k++) {
			keys[k] = false;
		}
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (!Keyboard.isRepeatEvent()) {
					int k = Keyboard.getEventKey();
					keys[k] = true;
				}
			}
		}
	}

	public static boolean keyPressed(int key) {
		if (keys != null) {
			return keys[key];
		} else {
			return false;
		}
	}
}
