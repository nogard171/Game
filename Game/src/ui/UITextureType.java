package ui;

public enum UITextureType {
	BLANK(0, 0, 0, 0), CURSOR(3, 1, 1, 1),ROCK_ITEM(0, 3, 1, 1), LOG_ITEM(1, 3, 1, 1),

	PANEL_TL(0, 0, 1, 1), PANEL_TC(1, 0, 1, 1), 
	PANEL_TR(2, 0, 1, 1), PANEL_ML(0, 1, 1, 1), 
	PANEL_MC(1, 1, 1, 1),	PANEL_MR(2, 1, 1, 1), 
	PANEL_BL(0, 2, 1, 1), PANEL_BC(1, 2, 1, 1), 
	PANEL_BR(2, 2,  1, 1);

	public float x;
	public float y;
	public float w;
	public float h;

	public float xOffset;
	public float yOffset;

	UITextureType(float newX, float newY) {
		this(newX, newY, 1, 1);
	}

	UITextureType(float newX, float newY, float newW, float newH) {
		this(newX, newY, newW, newH, 0, 0);
	}

	UITextureType(float newX, float newY, float newW, float newH, float newXOffset, float newYOffset) {
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
