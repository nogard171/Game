package core;

import java.awt.Polygon;

public class Object {
	private String sprite;
	public Polygon bounds;
	public boolean updated = false;

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
}
