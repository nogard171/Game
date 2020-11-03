package ui;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import data.UIData;
import utils.Renderer;
import utils.Window;

public class ListView {

	Rectangle bounds;

	int startIndex = 0;
	int maxCount = 0;
	public ArrayList<MenuItem> items = new ArrayList<MenuItem>();

	public ListView(int x, int y, int w, int h) {
		bounds = new Rectangle(x, y, w, h);
		maxCount = h / 12;
	}

	public void setup() {

	}

	public void update() {
		if (bounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
			for (MenuItem item : items) {
				item.hovered = false;
				if (item.bounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
					item.hovered = true;
					if (Window.isMainAction()) {
						handleClick(item);
					} else {
						handleUnClick(item);
					}
				}
			}
		}
	}

	public void handleClick(MenuItem item) {
		item.click();
	}

	public void handleUnClick(MenuItem item) {
		item.unclick();
	}

	public void render(int x, int y) {
		bounds.x = x;
		bounds.y = y;
		Renderer.renderRectangle(bounds.x, bounds.y, bounds.width, bounds.height, new Color(1, 1, 1, 0.5f));
		int i = 0;
		for (MenuItem item : items) {
			item.bounds = new Rectangle(bounds.x, bounds.y + (i * 12) + 2, bounds.width, 12);
			if (item.hovered) {
				Renderer.renderRectangle(item.bounds.x, item.bounds.y, item.bounds.width, item.bounds.height,
						new Color(1, 0, 0, 0.5f));
			}
			Renderer.renderText(new Vector2f(bounds.x, bounds.y + (i * 12)), item.text, 12, Color.white);
			i++;

		}
	}
}
