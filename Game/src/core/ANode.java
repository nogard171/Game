package core;

import java.awt.Point;

public class ANode {
	public int x = 0;
	public int y = 0;

	public ANode(int newX, int newY) {
		x = newX;
		y = newY;
	}

	public ANode(Point index) {
		x = index.x;
		y = index.y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) x;
		result = prime * result + (int) y;
		return result;
	}

	@Override
	public boolean equals(java.lang.Object obj) {
		if (this == obj) {
			// return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ANode other = (ANode) obj;
		if (x == other.x && y == other.y) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "ANode(" + x + "," + y + ")";
	}

	public Point toPoint() {
		return new Point(x, y);
	}
}
