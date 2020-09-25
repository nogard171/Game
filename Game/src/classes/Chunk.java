package classes;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.Color;

import data.WorldData;
import utils.Renderer;

public class Chunk {
	public Index index;
	public int displayListID = -1;
	private boolean needsUpdating = false;
	public Size size = new Size(16, 16, 16);
	public int[][] data;
	public Object[][] groundObjects;
	public Object[][] entityObjects;
	public Object[][] maskObjects;
	public GroundItem[][] groundItems;

	public Chunk(int i, int j) {
		index = new Index(i, j);
	}

	public Index getIndex() {
		return index;
	}

	public void setup() {
		data = new int[size.getWidth()][size.getDepth()];
		groundObjects = new Object[size.getWidth()][size.getDepth()];
		maskObjects = new Object[size.getWidth()][size.getDepth()];
		entityObjects = new Object[size.getWidth()][size.getDepth()];
		groundItems = new GroundItem[size.getWidth()][size.getDepth()];

		for (int x = 0; x < size.getWidth(); x++) {
			for (int z = 0; z < size.getDepth(); z++) {
				int carX = x * 32;
				int carY = z * 32;
				int isoX = carX - carY;
				int isoY = (carY + carX) / 2;
				Object obj = new Object();
				obj.setX(isoX);
				obj.setY(isoY);
				if (x == 0 && z == 0 && index.getX() == 0 && index.getY() == 0) {
					obj.setMaterial("DIRT");
					Object newObj = new Object();
					newObj.setX(isoX);
					newObj.setY(isoY);
					newObj.setMaterial("PLAYER");

					entityObjects[x][z] = newObj;
				} else {
					entityObjects[x][z] = new Object();
				}

				if (x == 12 && z == 7) {

					Resource newObj = new Resource();
					newObj.name = "WHEAT";
					newObj.setX(isoX);
					newObj.setY(isoY);
					newObj.setMaterial("WHEAT");
					newObj.setModel("SQUARE");

					maskObjects[x][z] = newObj;
				} else if (x == 7 && z == 12) {

					Resource newObj = new Resource();
					newObj.name = "ORE";
					newObj.setX(isoX);
					newObj.setY(isoY);
					newObj.setMaterial("ORE");
					newObj.setModel("CUBE");

					maskObjects[x][z] = newObj;
				} else if (x == 7 && z == 7) {

					Resource newObj = new Resource();
					newObj.name = "TREE";
					newObj.setX(isoX);
					newObj.setY(isoY);
					newObj.setMaterial("TREE");
					newObj.setModel("TREE");

					maskObjects[x][z] = newObj;
				} else if (x == 13 && z == 13) {

					CraftingTable newObj = new CraftingTable();
					newObj.setX(isoX);
					newObj.setY(isoY);
					newObj.setMaterial("CRAFTING_TABLE");
					newObj.setModel("CRAFTING_TABLE");

					maskObjects[x][z] = newObj;
				} else {

					maskObjects[x][z] = new Object();
				}
				obj.setMaterial("GRASS");
				groundObjects[x][z] = obj;

				groundItems[x][z] = null;

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
				Object obj = groundObjects[x][z];
				if (obj != null) {
					Renderer.renderModel(this, x, z, obj);
				}

				Object itemObj = groundItems[x][z];
				if (itemObj != null) {
					if (itemObj.getMaterial() != "AIR") {
						Renderer.renderModel(this, x, z, itemObj);

						// System.out.println("Item: " + itemObj.getMaterial() + "/" + itemObj);
					}
				}

				Object entityObj = entityObjects[x][z];
				if (entityObj != null) {
					Renderer.renderModel(this, x, z, entityObj);
				}

				Object maskObj = maskObjects[x][z];
				if (maskObj != null) {
					Renderer.renderModel(this, x, z, maskObj);
				}

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

	public Object getData(int x, int y) {
		Object obj = null;
		if (x >= 0 && y >= 0 && x < size.getWidth() && y < size.getHeight()) {
			obj = groundObjects[x][y];
		}
		return obj;
	}

	public void setObject(int x, int y, Object obj) {
		groundObjects[x][y] = obj;
		needsUpdating();
	}

	public void needsUpdating() {
		this.needsUpdating = true;
	}

	public void setObjectColor(int x, int y, Color newColor) {
		groundObjects[x][y].setColor(newColor);
		needsUpdating();
	}
}
