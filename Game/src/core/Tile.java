package core;

import java.awt.Polygon;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

public class Tile {
	private TextureType type = TextureType.AIR;
	private Vector2f position;

	public Tile(TextureType newType) {
		setType(newType);
	}

	public TextureType getType() {
		return type;
	}

	public void setType(TextureType type) {
		this.type = type;
	}

	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}
}
