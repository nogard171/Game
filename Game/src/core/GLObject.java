package core;

import java.awt.Point;
import java.awt.Polygon;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class GLObject {
	private GLType type = GLType.AIR;
	public Polygon bounds = new Polygon();
	private Vector3f positionIndex = new Vector3f(0, 0, 0);
	private boolean isVisible = false;
	private boolean isKnown = false;

	public GLObject(GLType newType) {
		this.type = newType;
	}

	public GLType getType() {
		return this.type;
	}

	public void setType(GLType newType) {
		this.type = newType;
	}

	public boolean isVisible() {
		// TODO Auto-generated method stub
		return isVisible;
	}

	public void setVisible(boolean b) {
		this.isVisible = b;

	}

	public boolean isKnown() {
		// TODO Auto-generated method stub
		return isKnown;
	}

	public void setKnown(boolean b) {
		this.isKnown = b;

	}

	public void setPositionIndex(int x, int y, int z) {
		this.positionIndex = new Vector3f(x, y, z);
	}

	public Vector3f getPositionIndex() {
		return this.positionIndex;
	}
}
