package core;

public enum TextureType {
	AIR(0, 0), GRASS(1, 0, 1, 0.75f), GRASS0(1, 0.75f, 1, 0.75f), DIRT(2, 0, 1, 0.75f), TREE(3, 0, 1, 4, 0, -3.5f),
	BUSH(2, 0.75f, 1, 0.75f,0,-0.75f);

	float x;
	float y;
	float w;
	float h;

	float xOffset;
	float yOffset;

	TextureType(float newX, float newY) {
		this(newX, newY, 1, 1);
	}

	TextureType(float newX, float newY, float newW, float newH) {
		this(newX, newY, newW, newH, 0, 0);
	}

	TextureType(float newX, float newY, float newW, float newH, float newXOffset, float newYOffset) {
		x = newX;
		y = newY;

		w = newW;
		h = newH;

		xOffset = newXOffset;
		yOffset = newYOffset;
	}

	@Override
	public String toString() {
		return this.name() + "(" + x + "," + y + ")";
	}
}
