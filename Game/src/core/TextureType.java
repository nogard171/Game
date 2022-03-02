package core;

import java.awt.Color;
import java.util.ArrayList;

public enum TextureType implements Comparable<TextureType> {
	// x,y,w,h,ox,oy
	AIR(0, 0), COINS(7, 0, 1, 1), CHARACTER(0, 0.75F, 1, 0.75f, 0, -0.25f), GRASS(1, 0, 1, 1), GRASS0(1, 1f, 1, 1f),
	DIRT(2, 0, 1, 1f), SAND(0, 3, 1, 1f), TREE(3, 0, 2, 2, 0, -1.5f), BUSH(3, 2, 2, 1, 0f, -0.5f),
	ROCK(1, 2, 2, 1, 0, -0.5f), DEEP_WATER(0, 4, 1, 1f), TILLED_DIRT(3, 3, 1, 1f),

	PATH_DURING(0, 0.75F * 2, 1, 0.75f, 0, 0.1f), PATH_FINISH(0, 0.75F * 3, 1, 0.75f, 0, 0.1f), ROCK_ITEM(0, 0, 1, 1),
	LOG_ITEM(1, 0, 1, 1), ITEM(0, 0, 0, 0), TIN_ORE(1, 1 * 4, 1, 1, 0, -0.5f), COPPER_ORE(1, 1 * 3, 1, 1, 0, -0.5f),
	SHALLOW_WATER(2, 1, 1, 1), FISHING_SPOT(0, 5, 1, 1f), FISHING_SPOT1(0, 6, 1, 1f), FISHING_SPOT2(0, 7, 1, 1f),
	FISHING_SPOT3(1, 5, 1, 1f), FISHING_SPOT4(1, 6, 1, 1f);

	float x;
	float y;
	float w;
	float h;

	float xOffset;
	float yOffset;

	public float pW;
	public float pH;

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

		pW = newW;
		pH = newH;

	}

	TextureType(float newX, float newY, float newW, float newH, float newXOffset, float newYOffset, float newPW,
			float newPH) {
		x = newX;
		y = newY;

		w = newW;
		h = newH;

		xOffset = newXOffset;
		yOffset = newYOffset;

		pW = newPW;
		pH = newPH;
	}

	@Override
	public String toString() {
		return this.name();
	}

	public boolean isPassable() {
		boolean passable = false;
		switch (this) {
		case SHALLOW_WATER:
		case TREE:
		case ROCK:
		case COPPER_ORE:
		case TIN_ORE:
		case BUSH:
		case FISHING_SPOT:
		case FISHING_SPOT1:
		case FISHING_SPOT2:
		case FISHING_SPOT3:
		case FISHING_SPOT4:
			break;
		default:
			passable = true;
			break;
		}
		if (this != AIR) {
			// System.out.println("Type: " + this.toString() + "=>" + passable);
		}
		return passable;
	}

	public int toColorInt() {
		int ci = 0;
		switch (this) {
		case SAND:
			ci = colorToInt(255, 233, 127, 255);
			break;
		case DIRT:
			ci = colorToInt(178, 135, 78, 255);
			break;
		case ROCK:
			ci = colorToInt(175, 161, 142, 255);
			break;
		case GRASS0:
			ci = colorToInt(114, 162, 70, 255);
			break;
		case GRASS:
			ci = colorToInt(142, 202, 88, 255);
			break;
		case TREE:
		case BUSH:
			ci = colorToInt(142, 202, 88, 128);
			break;
		case SHALLOW_WATER:
		case FISHING_SPOT:
		case FISHING_SPOT1:
		case FISHING_SPOT2:
		case FISHING_SPOT3:
		case FISHING_SPOT4:
			ci = colorToInt(31, 161, 255, 255);
			break;
		case DEEP_WATER:
			ci = colorToInt(0, 132, 239, 255);
			break;
		default:
			ci = colorToInt(255, 255, 255, 255);
			break;
		}
		return ci;
	}

	private int colorToInt(int r, int g, int b, int a) {
		return (a << 24) | (r << 16) | (g << 8) | b;
	}
}
