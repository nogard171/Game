package core;

public enum TextureType implements Comparable<TextureType> {
	AIR(0, 0), CHARACTER(0, 0.75F, 1, 0.75f, 0, -0.25f), GRASS(1, 0, 1, 0.75f), GRASS0(1, 0.75f, 1, 0.75f),
	DIRT(2, 0, 1, 0.75f), TREE(3, 0, 1, 4, 0, -3.5f), BUSH(2, 0.75f, 1, 0.75f, 0, -0.75f),
	PATH_DURING(0, 0.75F * 2, 1, 0.75f, 0, 0.1f), PATH_FINISH(0, 0.75F * 3, 1, 0.75f, 0, 0.1f),
	ROCK(1, 0.75f * 2, 1, 0.75f, 0, -0.25f), ROCK_ITEM(0, 0, 1, 1), LOG_ITEM(1, 0, 1, 1);

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
		return this.name() + "(Vec->" + x + "f," + y + "f" + (w > 1 || h > 1 ? ",Size->" + w + "f," + h + "f" : "")
				+ (xOffset != 0 || yOffset != 0 ? ",Offset->" + xOffset + "f," + yOffset + "f" : "") + ")";
	}
}
