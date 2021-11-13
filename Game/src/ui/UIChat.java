package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import core.Input;
import core.Renderer;
import core.ResourceDatabase;
import core.Window;
import game.PlayerDatabase;
import utils.Ticker;

public class UIChat {
	public static boolean show = true;

	private static Rectangle inputBounds;
	public static Rectangle chatBounds;
	public Vector2f position = new Vector2f(0, 0);
	Point size = new Point(11, 5);

	private static LinkedList<String> messages = new LinkedList<String>();

	Ticker cursorTick;

	public static void addMessage(String newMessage) {
		messages.add(newMessage);
	}

	public void setup() {
		position = new Vector2f(0, Window.height - (((size.y) * 32) + 32));
		messages.add("Admin:Hello world0");
		messages.add("Admin:Hello world1");
		messages.add("Admin:Hello world2");
		messages.add("Admin:Hello world3");
		messages.add("Admin:Hello world4");
		messages.add("Admin:Hello world5");
		messages.add("Admin:Hello world6");
		messages.add("Admin:Hello world7");
		messages.add("Admin:Hello world8");
		messages.add("Admin:Hello world9");
		messages.add("Admin:Hello world10");
		messages.add("Admin:Hello world11");

		position = new Vector2f(0, Window.height - (((size.y) * 32) + 32));
		chatBounds = new Rectangle((int) position.x, (int) position.y - 24, ((size.x) * 32), (size.y) * 32);
		inputBounds = new Rectangle((int) position.x, (int) position.y + ((size.y) * 32) - 20, ((size.x) * 32), 20);
		cursorTick = new Ticker();
	}

	public static boolean isPanelHovered() {
		boolean isHovered = false;
		if (show) {

			if (chatBounds.contains(Input.getMousePoint()) || inputHovered) {
				isHovered = true;
			}
		}
		return isHovered;
	}

	static boolean inputHovered = false;

	public void update() {
		if (Window.wasResized) {
			position = new Vector2f(0, Window.height - (((size.y) * 32) + 32));

			chatBounds = new Rectangle((int) position.x, (int) position.y - 24, ((size.x) * 32), (size.y) * 32);
			inputBounds = new Rectangle((int) position.x, (int) position.y + ((size.y) * 32) - 20, ((size.x) * 32),
					20);
		}
		if (show) {
			cursorTick.poll(500);
			
			inputHovered = inputBounds.contains(Input.getMousePoint());
			if (Input.isMousePressed(0) && inputHovered) {
				textSelected = true;
			} else if (Input.isMousePressed(0) && !inputHovered) {
				textSelected = false;
			}
			if (cursorTick.ticked()) {
				if (cursorText.length() > 0) {
					cursorText = "";
				} else {
					cursorText = "|";
				}
			}
			if (textSelected) {
				handleInput();
			}
		}
	}

	boolean capLock = false;

	public void handleInput() {
		int[] k = Input.getKey();
		if (k.length > 0) {
			if (k[0] >= 0 && (k.length == 2 && k[1] >= 0 ? true : false)) {
				String firstKey = Keyboard.getKeyName(k[0]);
				String secondKey = (k.length == 2 ? Keyboard.getKeyName(k[1]) : "");
				String keyVal = "";
				System.out.println("Key:" + firstKey + "/" + secondKey);
				if (secondKey.equals("LSHIFT")) {
					keyVal = firstKey.toUpperCase();
				} else if (firstKey.equals("RSHIFT")) {
					// keyVal = secondKey.toUpperCase();
				} else if (firstKey.length() == 1) {
					keyVal = (capLock ? firstKey.toUpperCase() : firstKey.toLowerCase());
				}
				if (firstKey.equals("CAPITAL") || secondKey.equals("CAPITAL")) {
					capLock = !capLock;
				}

				if (firstKey.equals("BACK") || secondKey.equals("BACK")) {
					if (inputText.length() > 0) {
						inputText = inputText.substring(0, inputText.length() - 1);
					}
				}
				if (firstKey.equals("SPACE") || secondKey.equals("SPACE")) {
					inputText += " ";
				}
				if (firstKey.equals("RETURN") || secondKey.equals("RETURN")) {
					addMessage(PlayerDatabase.name + ":" + inputText);
					inputText = "";
				}
				inputText += keyVal;
			}
		}
	}

	String inputText = "";
	String cursorText = "|";
	static boolean textSelected = false;

	public static boolean isAllowedKeyboard() {
		return !textSelected;
	}

	public void render() {
		if (show) {
			Renderer.bindTexture(ResourceDatabase.uiTexture);
//		Renderer.renderQuad(chatBounds, new Color(0, 0, 0, 0.5f));
			GL11.glBegin(GL11.GL_QUADS);
			UIPanel.renderPanel((int) chatBounds.x, (int) chatBounds.y, chatBounds.width / 32, chatBounds.height / 32);
			GL11.glEnd();
			int c = 11;
			int maxLength = 55;
			int endIndex = (messages.size() - c);
			int startIndex = (messages.size() - 1 >= 0 ? messages.size() - 1 : 0);
			// Renderer.renderText(new Vector2f(position.x, position.y), "Start:" +
			// (messages.size() - c), 12, Color.white);
			int yOffset = 0;
			int currentC = 0;
			for (int i = startIndex; i > endIndex; i--) {
				int lineNumber = i - endIndex;

				String lineString = messages.get(i);

				int temp = (int) (Math.ceil((float) lineString.length() / (float) maxLength));
				for (int y = temp - 1; y >= 0; y--) {
					int endLine = ((y + 1) * maxLength > lineString.length() ? lineString.length()
							: (y + 1) * maxLength);

					String line = lineString.substring(y * maxLength, endLine);

					Renderer.renderText(
							new Vector2f(position.x + 5, ((position.y - 29 + (c * 12))) - ((yOffset - 1) * 12)), line,
							12, Color.white);
					yOffset++;
					currentC++;
				}
				if (currentC > c) {
					break;
				}
			}

			Renderer.renderQuad(new Rectangle(inputBounds.x, inputBounds.y, inputBounds.width, inputBounds.height),
					new Color(0, 0, 0, 0.5f));
			String refinedText = inputText;
			if (textSelected) {
				refinedText = inputText + cursorText;
			}

			Renderer.renderText(new Vector2f(position.x + 5, position.y - 20 + ((size.y) * 32)),
					PlayerDatabase.name + ":" + refinedText, 12, Color.white);
		}
	}
}
