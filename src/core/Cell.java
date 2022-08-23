package core;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.UUID;

public class Cell {
	private Index index;
	private String texture;
	private Polygon bounds;
	private String hash;
	public TextureData data;

	public Cell() {
		hash = UUID.randomUUID().toString();

	}

	public String getHash() {
		return hash;
	}

	public Cell(Index newIndex, String newTexture) {
		index = newIndex;
		texture = newTexture;
		hash = UUID.randomUUID().toString();
	}

	public Index getIndex() {
		return index;
	}

	public void setIndex(Index index) {
		this.index = index;
	}

	public String getTexture() {
		return texture;
	}

	public void setTexture(String texture) {
		this.texture = texture;
	}

	public Polygon getBounds() {
		return bounds;
	}

	public void setBounds(Polygon bounds) {
		this.bounds = bounds;
	}

}
