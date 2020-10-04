package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import classes.InventoryItem;
import data.UIData;
import utils.Renderer;
import utils.Window;
import data.CharacterData;

public class ChatSystem extends BaseSystem {

	public ArrayList<String> messages = new ArrayList<String>();

	public void sendMessage(String message) {
		String characterName = CharacterData.name;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String newMessage = dtf.format(now) + " " + characterName + message;
		messages.add(newMessage);
	}

	@Override
	public void setup() {
		super.setup();
		baseBounds = new Rectangle(0, 0, 304, 333);
		baseBounds.y = (Window.height - 32) - baseBounds.height;

	}

	@Override
	public void update() {
		super.update();
		if (Window.wasResized()) {

		}
		if (showSystem) {

		}
	}

	@Override
	public void render() {
		super.render();
		if (showSystem) {
			Renderer.renderRectangle(baseBounds.x, baseBounds.y, baseBounds.width, baseBounds.height,
					new Color(0, 0, 0, 0.5f));
			int min = messages.size() - 16;
			if (min < 0) {
				min = 0;
			}
			int lineNumber = 0;
			for (int i = messages.size(); i >= min; i--) {
				if (messages.size() > i) {
					String message = messages.get(i);
					if (message != null) {
						Renderer.renderText(new Vector2f(baseBounds.x, baseBounds.y + (lineNumber * 20)), message, 12,
								Color.white);
						lineNumber++;
					}
				}
			}

		}
	}

	@Override
	public void clean() {
		super.clean();

	}
}
