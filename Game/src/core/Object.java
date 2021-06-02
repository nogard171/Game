package core;

import java.awt.Point;
import java.awt.Polygon;
import java.util.UUID;

import org.lwjgl.util.vector.Vector3f;

public class Object {
	public UUID uuid;
	private String sprite;
	public Polygon bounds;
	public Point offset = new Point(0, 0);
	public boolean updated = false;
	private Vector3f index;

	public boolean known = false;

	public Object(Vector3f newIndex) {
		uuid = UUID.randomUUID();
		index = newIndex;
	}

	public void setSprite(String newSprite) {
		if (newSprite != sprite) {
			updated = true;
		}
		sprite = newSprite;
	}

	public void setBounds(Polygon newBounds) {
		if (newBounds != bounds) {
			updated = true;
		}
		bounds = newBounds;
	}

	public String getSprite() {
		return sprite;
	}

	public Polygon getBounds() {
		return bounds;
	}

	public Vector3f getIndex() {
		return index;
	}
}
