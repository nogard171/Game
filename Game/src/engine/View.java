package engine;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import java.awt.Point;

public class View {
	public static int x = 0;
	public static int y = 0;
	public int w = 0;
	public int h = 0;
	public boolean moved = false;

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
	}

	public void finalizeMove() {
		if (moved) {
			moved = false;
		}
	}

	public Rectangle getRect() {
		// TODO Auto-generated method stub
		return new Rectangle(x, y, w, h);
	}

	public static Point getIndexInWorld() {
		int indexX = (int) Math.floor(((float) Input.getMousePoint().x + (float) x) / (float) 32);
		int indexY = (int) Math.floor(((float) Input.getMousePoint().y + (float) y) / (float) 32);

		return new Point(indexX, indexY);
	}
}
