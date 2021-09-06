package core;

import java.util.ArrayList;

public enum TextureType implements Comparable<TextureType> {
	AIR(0, 0), COINS(7, 0, 1, 1), CHARACTER(0, 0.75F, 1, 0.75f, 0, -0.25f), GRASS(1, 0, 1, 0.75f),
	GRASS0(1, 0.75f, 1, 0.75f), DIRT(2, 0, 1, 0.75f), TREE(3, 0, 1, 4, 0, -3.5f), BUSH(2, 0.75f, 1, 0.75f, 0, -0.75f),
	PATH_DURING(0, 0.75F * 2, 1, 0.75f, 0, 0.1f), PATH_FINISH(0, 0.75F * 3, 1, 0.75f, 0, 0.1f),
	ROCK(1, 0.75f * 2, 1, 0.75f, 0, -0.25f), ROCK_ITEM(0, 0, 1, 1), LOG_ITEM(1, 0, 1, 1), ITEM(0, 0, 0, 0),
	TIN_ORE(1, 0.75F * 4, 1, 0.75f, 0, -0.25f), COPPER_ORE(1, 0.75F * 3, 1, 0.75f, 0, -0.25f),
	SHALLOW_WATER(2, 0.75f * 2, 1, 0.75f), FISHING_SPOT(2, 0.75f * 3, 1, 0.75f), FISHING_SPOT1(2, 0.75f * 4, 1, 0.75f),
	FISHING_SPOT2(2, 0.75f * 5, 1, 0.75f), FISHING_SPOT3(2, 0.75f * 6, 1, 0.75f), FISHING_SPOT4(2, 0.75f * 7, 1, 0.75f);

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
			//System.out.println("Type: " + this.toString() + "=>" + passable);
		}
		return passable;
	}

}
