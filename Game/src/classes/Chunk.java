package classes;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import data.WorldData;
import utils.Renderer;

public class Chunk {
	public Index index;
	public int displayListID = -1;
	private boolean needsUpdating = true;
	public Size size = new Size(16, 16, 16);
	public int[][] data;
	public Object[][] objects;

	public Chunk(int i, int j) {
		index = new Index(i, j);
	}

	public Index getIndex() {
		return index;
	}

	public void setup() {
		data = new int[size.getWidth()][size.getDepth()];
		objects = new Object[size.getWidth()][size.getDepth()];
		for (int x = 0; x < size.getWidth(); x++) {
			for (int z = 0; z < size.getDepth(); z++) {
				int carX = x * 32;
				int carY = z * 32;
				int isoX = carX - carY;
				int isoY = (carY + carX) / 2;
				Object obj = new Object();
				obj.setX(isoX);
				obj.setY(isoY);
				if (x == 1 && z == 1) {
					obj.setMaterial("DIRT");
				}
				objects[x][z] = obj;

			}
		}
		this.build();
	}

	private void build() {
		displayListID = GL11.glGenLists(1);
		GL11.glNewList(displayListID, GL11.GL_COMPILE);
		GL11.glColor3f(1, 1, 1);
		GL11.glBegin(GL11.GL_TRIANGLES);
		for (int x = 0; x < size.getWidth(); x++) {
			for (int z = 0; z < size.getDepth(); z++) {
				Renderer.renderModel(this, x, z);
			}
		}
		GL11.glEnd();
		GL11.glEndList();
	}

	public void update() {
		if (needsUpdating) {
			this.build();
			needsUpdating = false;
		}
	}

	public void render() {
		if (displayListID != -1) {
			GL11.glCallList(displayListID);
		}
	}

	public void destroy() {

	}

	public void refresh() {
		this.needsUpdating = true;
	}
}
