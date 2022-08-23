package core;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class View {
	public int x = 0;
	public int y = 0;
	public int w = 0;
	public int h = 0;
	private static boolean moved = false;

	public View(int newX, int newY, int newW, int newH) {
		x = newX;
		y = newY;
		w = newW;
		h = newH;
	}

	public void move(int forceX, int forceY) {
		if (forceX != 0 || forceY != 0) {
			moved = true;
		}

		if (moved) {
			x += forceX;
			y += forceY;
		}

		if (forceX == 0 && forceY == 0) {
			moved = false;
		}
	}

	public void finalizeMove() {
		if (moved) {
			moved = false;
		}
	}

	public Rectangle getRect() {
		return new Rectangle(x, y, w, h);
	}

	public static boolean Moved() {
		return moved;
	}
}
