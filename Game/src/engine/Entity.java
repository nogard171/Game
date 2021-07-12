package engine;

import java.awt.Point;

public class Entity {
	public Point position;
	public boolean moved = false;

	public Entity(int x, int y) {
		position = new Point(x, y);
	}

	public void move(int forceX, int forceY) {
		if (forceX != 0 || forceY != 0) {
			moved = true;
		}

		if (moved) {
			position.x += forceX;
			position.y += forceY;
		}
	}

	public void finalizeMove() {
		if (moved) {
			moved = false;
		}
	}
}
