package core;

import java.awt.Color;
import java.util.ArrayList;

public enum TextureType implements Comparable<TextureType> {
	// x,y,w,h,ox,oy
	AIR(0, 0,0,0),  
	CHARACTER(0, 0,32,64,16,-40), 
	GRASS(64, 0, 64,64), 
	GRASS0(64, 64, 64,64),
	DIRT(128, 0, 64,64), 
	SAND(0, 192, 64,64), 
	TREE(192, 0, 130, 127, 0, -96), 
	MAPLE_TREE(322, 0, 130, 127, 0, -96), 
	BUSH(192, 128, 128, 64, 0f, -32),
	ROCK_ORE(68,136, 87, 56, 4, -24), 
	DEEP_WATER(0, 256, 64, 64), 
	TILLED_DIRT(192, 192, 64, 64),

	ITEM(0, 0, 0, 0), 
	TIN_ORE(68,263, 87, 56, 4, -24), 
	COAL_ORE(200,263, 87, 56, 4, -24), 
	COPPER_ORE(68,198, 64, 64, 4, -24),
	SHALLOW_WATER(128, 64,64,64), 
	FISHING_SPOT(0, 5*64, 64,64), 
	FISHING_SPOT1(0, 6*64, 64,64), 
	FISHING_SPOT2(0, 7*64, 64,64),
	FISHING_SPOT3(64, 5*64, 64,64), 
	FISHING_SPOT4(64, 6*64, 64,64),
	PATH_DURING(32, 0, 20,10,22,10), 
	PATH_FINISH(32,10, 20,10,22,10),
	//PATH_DURING(0, 0.75F * 2, 1, 0.75f, 0, 0.1f), 
	//PATH_FINISH(0, 0.75F * 3, 1, 0.75f, 0, 0.1f)

	WOOD_WALL_E(322,0,36,78,0,-60);

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
		case ROCK_ORE:
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
		case ROCK_ORE:
			ci = colorToInt(175, 161, 142, 255);
			break;
		case GRASS0:
			ci = colorToInt(114, 162, 70, 255);
			break;
		case GRASS:
			ci = colorToInt(142, 202, 88, 255);
			break;
		case TREE:
		case MAPLE_TREE:
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
