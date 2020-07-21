package classes;

import org.newdawn.slick.Color;

public class Object {
	private int x = 0;
	private int y = 0;
	private int indexX = 0;
	private int indexY = 0;
	private int indexZ = 0;
	private String model = "TILE";
	private String material = "AIR";
	private Color color = Color.white;

	public boolean isResource = false;

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getIndexX() {
		return indexX;
	}

	public void setIndexX(int indexX) {
		this.indexX = indexX;
	}

	public int getIndexY() {
		return indexY;
	}

	public void setIndexY(int indexY) {
		this.indexY = indexY;
	}

	public int getIndexZ() {
		return indexZ;
	}

	public void setIndexZ(int indexZ) {
		this.indexZ = indexZ;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color newColor) {
		this.color = newColor;
	}

}
