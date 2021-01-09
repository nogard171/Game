import java.util.Arrays;

import org.lwjgl.input.Keyboard;

public class KeyboardInput {
	int keyCount = 255;
	KeyState[] keys = {};
	int[] keyPressed = {};

	public enum KeyState {

		RELEASED, // Not down

		PRESSED, // Down, but not the first time

		ONCE // Down for the first time

	}

	public void startPoll() {
		for (int i = 0; i < keyCount; i++) {
			if (Keyboard.isKeyDown(i) && keys[i] != KeyState.ONCE && keys[i] != KeyState.PRESSED) {
				keys[i] = KeyState.ONCE;
				currentKey = i;
				keyPressed[i]++;
			} else if (Keyboard.isKeyDown(i)) {
				keys[i] = KeyState.PRESSED;
				currentKey = i;
				keyPressed[i]++;
			}
		}
	}
	
	public void endPoll() {
		for (int i = 0; i < keyCount; i++) {
			if (!Keyboard.isKeyDown(i) && keys[i] != KeyState.RELEASED) {
				keys[i] = KeyState.RELEASED;
				currentKey = -1;
			}
		}
	}
	
	public boolean contains(final int[] array, final int key) {
		return Arrays.asList(array).contains(key);
	}

	int[] alphaKeys = { 30, 48, 46, 32, 18, 33, 34, 35, 23, 36, 37, 38, 50, 49, 24, 25, 16, 19, 31, 20, 22, 47, 17, 45,
			21, 44,57,2,3,4,5,6,7,8,9,11 };
	char[] alphaChars = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z',' ','1','2','3','4','5','6','7','8','9','0' };

	public int getIndex(int value) {
		int newIndex = -1;
		for (int i = 0; i < alphaKeys.length; i++) {
			if (alphaKeys[i] == value) {
				newIndex = i;
				break;
			}
		}
		return newIndex;
	}
	public boolean isAlpha(int key) {
		boolean isAlpha= false;
		for (int i = 0; i < this.alphaKeys.length; i++) {
			if (key == this.alphaKeys[i]) {
				isAlpha = true;
				break;
			} 
		}		
		return isAlpha;
	}
	public boolean keyPressed(int key) {
		return ((keys[key] == KeyState.PRESSED) ? true : false);
	}

	public boolean keyOnce(int key) {
		return ((keys[key] == KeyState.ONCE) ? true : false);
	}

	public KeyboardInput() {
		keys = new KeyState[keyCount];
		keyPressed = new int[keyCount];
		for (int i = 0; i < keyCount; i++) {
			keys[i] = KeyState.RELEASED;
			keyPressed[i] = 0;
		}
	}

	int currentKey = -1;
	public int getKeyCode() {
		for (int i = 0; i < keyCount; i++) {
			if (keys[i] == KeyState.ONCE) {
				currentKey = i;
				break;
			}
		}
		return currentKey;
	}
	public String getKeyChar(boolean shift)
	{
		String keyChar = "";
		for (int i = 0; i < keyCount; i++) {
			if (keys[i] == KeyState.ONCE&&this.isAlpha(i)) {
				keyChar = alphaChars[getIndex(i)]+"";
				break;
			}
		}
		if(shift)
		{
			return keyChar.toUpperCase();
		}
		else
		{
			return keyChar.toLowerCase();
		}
	}
}
