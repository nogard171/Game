package classes;

public class Index {
	private int x;
	private int y;

	public Index(int newX, int newY) {
		x = newX;
		y = newY;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public String getString() {
		return x + "," + y;
	}
}
