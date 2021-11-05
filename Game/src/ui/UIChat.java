package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import core.Renderer;
import core.Window;

public class UIChat {
	public Vector2f position = new Vector2f(0, 0);
	Point size = new Point(11, 5);
	private static LinkedList<String> messages = new LinkedList<String>();

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

	}

	public void update() {
		if (Window.wasResized) {
			position = new Vector2f(0, Window.height - (((size.y) * 32) + 32));
		}
	}

	public void render() {
		Renderer.renderQuad(new Rectangle((int) position.x, (int) position.y, ((size.x) * 32), (size.y) * 32),
				new Color(0, 0, 0, 0.5f));
		int c = 13;
		int maxLength = 60;
		int yOffset = 11;
		for (int i = messages.size() - 1; i > messages.size() - 11; i--) {
			String tempMessage = messages.get(i);
			int mCount = (tempMessage.length() > maxLength ? maxLength : tempMessage.length());
			String tempLine = tempMessage.substring(0, mCount);
			
			if(mCount>=maxLength)
			{
				int lastSpace = tempMessage.lastIndexOf(" ");
				tempLine = tempMessage.substring(0,lastSpace);
			}

			Renderer.renderText(new Vector2f(position.x, position.y + (-(messages.size() - i) * 16)+160),
					 tempLine, 12, Color.white);

		}
	}
}
