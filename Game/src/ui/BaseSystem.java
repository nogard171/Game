package ui;

import java.awt.Point;
import java.awt.Rectangle;

import utils.Window;

public class BaseSystem {

	public boolean showSystem = false;
	public Rectangle baseBounds = new Rectangle(0, 0, 0, 0);
	public boolean baseHovered = false;

	public void setup() {
		baseBounds = new Rectangle(0, 0, 0, 0);
	}

	public void update() {
		if (Window.wasResized()) {
			baseBounds.y = (Window.height - 32) - baseBounds.height;
		}
		if (showSystem) {
			if (baseBounds.contains(new Point(Window.getMouseX(), Window.getMouseY()))) {
				baseHovered = true;
			} else {
				baseHovered = false;
			}
		}
	}

	public void render() {

	}

	public void clean() {

	}
}
