package core;

public enum TextureType {
	AIR(0, 0), GRASS(1, 0);

	int x;
	int y;

	TextureType(int newX, int newY) {
		x = newX;
		y = newY;
	}

	@Override
	public String toString() {
		return  this.name() + "(" + x + "," + y + ")";
	}
}
