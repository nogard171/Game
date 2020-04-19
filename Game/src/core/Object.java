package core;

import java.awt.Polygon;

import org.lwjgl.util.vector.Vector3f;

public class Object {
	private String sprite;
	public Polygon bounds;
	public boolean updated = false;
	private Vector3f index;

	public Object(Vector3f newIndex) {
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
