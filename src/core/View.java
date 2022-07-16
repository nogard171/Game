package core;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class View {
	public float x = 0;
	public float y = 0;
	public int w = 0;
	public int h = 0;
	public boolean moved = false;

	public View(int newX, int newY, int newW, int newH) {
		x = newX;
		y = newY;
		w = newW;
		h = newH;
	}

	public void move(float forceX, float forceY) {
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
		return new Rectangle((int) x, (int) y, w, h);
	}
}
