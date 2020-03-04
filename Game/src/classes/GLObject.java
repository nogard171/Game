package classes;

import java.awt.Point;
import java.awt.Polygon;
import java.util.UUID;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import core.GLChunk;
import core.GLChunkManager;

public class GLObject {
	private String hash = "";
	private GLType type = GLType.AIR;
	public Polygon bounds = new Polygon();
	private GLIndex positionGLIndex = new GLIndex(0, 0, 0);
	private boolean isVisible = false;
	private boolean isKnown = false;

	public GLObject(GLType newType) {
		this.type = newType;
		hash = UUID.randomUUID().toString();
	}

	public GLType getType() {
		return this.type;
	}

	public void setType(GLType newType) {
		GLChunk chunk = GLChunkManager.chunks
				.get(new GLIndex(positionGLIndex.chunkX, positionGLIndex.chunkY, positionGLIndex.chunkZ));
		if (chunk != null) {
			chunk.needsUpdating = true;
		}
		this.type = newType;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean b) {
		this.isVisible = b;

	}

	public boolean isKnown() {
		return isKnown;
	}

	public void setKnown(boolean b) {
		this.isKnown = b;

	}

	public void setPositionGLIndex(int x, int y, int z, int cx, int cy, int cz) {
		this.positionGLIndex = new GLIndex(x, y, z, cx, cy, cz);
	}

	public GLIndex getPositionGLIndex() {
		return this.positionGLIndex;
	}

	public String getHash() {
		return this.hash;
	}

}
