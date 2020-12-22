package classes;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.Color;

import com.sun.scenario.effect.Color4f;

import data.UIData;
import data.WorldData;
import utils.Renderer;

public class Chunk {
	public Index index;
	public int activeID = -1;
	public int passiveID = -1;
	private boolean needsUpdating = false;
	public Size size = new Size(16, 16, 16);
	public int[][] data;
	public Object[][] groundObjects;
	public Object[][] entityObjects;
	public Object[][] maskObjects;
	public Object[][] buildingObjects;
	public GroundItem[][] groundItems;

	public Chunk(int i, int j) {
		index = new Index(i, j);
	}

	public Index getIndex() {
		return index;
	}

	Random r = new Random();

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
					newObj.name = "BRONZE_ORE";
					newObj.setX(isoX);
					newObj.setY(isoY);
					newObj.setMaterial("BRONZE_ORE");
					newObj.setModel("CUBE");

					maskObjects[x][z] = newObj;
				} else if (x == 7 && z == 13) {

					Resource newObj = new Resource();
					newObj.name = "STONE";
					newObj.setX(isoX);
					newObj.setY(isoY);
					newObj.setMaterial("STONE");
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

					Furnace newObj = new Furnace();
					newObj.setX(isoX);
					newObj.setY(isoY);
					newObj.setMaterial("FURNACE");
					newObj.setModel("FURNACE");

					maskObjects[x][z] = newObj;
				} else if (x == 13 && z == 14) {

					CraftingTable newObj = new CraftingTable();
					newObj.setX(isoX);
					newObj.setY(isoY);
					newObj.setMaterial("CRAFTING_TABLE");
					newObj.setModel("CRAFTING_TABLE");

					maskObjects[x][z] = newObj;
				} else if (x == 13 && z == 15) {

					Building newObj = new Building();
					newObj.name = "WALL_SOUTH";
					maskObjects[x][z] = newObj;
				} else if (x == 14 && z == 15) {

					Building newObj = new Building();
					newObj.name = "WALL_WEST";
					maskObjects[x][z] = newObj;
				} else if (x == 15 && z == 15) {

					Building newObj = new Building();
					newObj.name = "WALL_NORTH";
					maskObjects[x][z] = newObj;
				} else {

					maskObjects[x][z] = new Object();
				}
				obj.setMaterial("GRASS");
				groundObjects[x][z] = obj;

				int randItems = r.nextInt(100 - 0);

				if (randItems == 0) {
					GroundItem item = new GroundItem();
					item.count = 1;
					item.isItem = true;
					item.name = "STICK_ITEM";
					item.setMaterial("STICK_ITEM");
					item.setModel("TILE");
					groundItems[x][z] = item;
				} else if (randItems == 1) {
					GroundItem item = new GroundItem();
					item.count = 1;
					item.isItem = true;
					item.name = "STONE_ITEM";
					item.setMaterial("STONE_ITEM");
					item.setModel("TILE");
					groundItems[x][z] = item;
				} else {
					groundItems[x][z] = null;
				}

			}
		}
		this.build();
	}

	private void build() {
		passiveID = GL11.glGenLists(1);
		GL11.glNewList(passiveID, GL11.GL_COMPILE);
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
					}
				}

				Object entityObj = entityObjects[x][z];
				if (entityObj != null) {
					Renderer.renderModel(this, x, z, entityObj);
				}

				Object maskObj = maskObjects[x][z];
				if (maskObj != null) {
					if (maskObj.type.equals(ObjectType.BUILDING)) {
						String buildingName = maskObj.name;
						BuildingData data = UIData.buildingData.get(buildingName);
						if (data != null) {
							for (int b = 0; b < data.materials.size(); b++) {
								BuildingMaterial mat = data.materials.get(b);
								if (mat != null) {
									int carX = (index.getX() * 32) * 16;
									int carY = (index.getY() * 32) * 16;
									int isoX = carX - carY;
									int isoY = (carY + carX) / 2;

									int selfX = isoX;
									int selfY = isoY;
									int objX = (x * 32) - (z * 32);
									int objY = ((z * 32) + (x * 32)) / 2;
									System.out.println("dir: " + data.type);
									if (data.type.equals("DIRECTIONAL")) {
										String model = modifyModel(buildingName, x, z);
										System.out.println("Wall: " + model);
										Renderer.renderModel(objX + selfX + mat.offset.x, objY + selfY + mat.offset.y,
												model, mat.name, maskObj.getColor());
									} else {
										Renderer.renderModel(objX + selfX + mat.offset.x, objY + selfY + mat.offset.y,
												data.model, mat.name, maskObj.getColor());
									}
								}
							}
						}
					} else {
						Renderer.renderModel(this, x, z, maskObj);
					}
				}

			}
		}
		GL11.glEnd();
		GL11.glEndList();

		activeID = GL11.glGenLists(1);
		GL11.glNewList(activeID, GL11.GL_COMPILE);
		GL11.glColor3f(1, 1, 1);
		GL11.glBegin(GL11.GL_TRIANGLES);
		for (int x = 0; x < size.getWidth(); x++) {
			for (int z = 0; z < size.getDepth(); z++) {
				Object itemObj = groundItems[x][z];
				if (itemObj != null) {
					if (itemObj.getMaterial() != "AIR") {
						Renderer.renderModel(this, x, z, itemObj);
					}
				}

				Object entityObj = entityObjects[x][z];
				if (entityObj != null) {
					Renderer.renderModel(this, x, z, entityObj);
				}

				Object maskObj = maskObjects[x][z];
				if (maskObj != null) {
					if (maskObj.type.equals(ObjectType.BUILDING)) {
						String buildingName = maskObj.name;
						BuildingData data = UIData.buildingData.get(buildingName);
						if (data != null) {
							for (int b = 0; b < data.materials.size(); b++) {
								BuildingMaterial mat = data.materials.get(b);
								if (mat != null) {
									int carX = (index.getX() * 32) * 16;
									int carY = (index.getY() * 32) * 16;
									int isoX = carX - carY;
									int isoY = (carY + carX) / 2;

									int selfX = isoX;
									int selfY = isoY;
									int objX = (x * 32) - (z * 32);
									int objY = ((z * 32) + (x * 32)) / 2;
									System.out.println("dir: " + data.type);
									if (data.type.equals("DIRECTIONAL")) {
										String model = modifyModel(buildingName, x, z);
										System.out.println("Wall: " + model);
										Renderer.renderModel(objX + selfX + mat.offset.x, objY + selfY + mat.offset.y,
												model, mat.name, maskObj.getColor());
									} else {
										Renderer.renderModel(objX + selfX + mat.offset.x, objY + selfY + mat.offset.y,
												data.model, mat.name, maskObj.getColor());
									}
								}
							}
						}
					} else {
						Renderer.renderModel(this, x, z, maskObj);
					}
				}

			}
		}
		GL11.glEnd();
		GL11.glEndList();
	}

	public boolean isObjectBuilding(Object obj) {
		boolean isBuilding = false;

		if (obj.type == ObjectType.BUILDING) {
			String buildingName = obj.name;
			BuildingData data = UIData.buildingData.get(buildingName);
			if (data != null && data.type.equals("DIRECTIONAL")) {
				isBuilding = true;
			}
		}

		return isBuilding;
	}

	public String modifyModel(String name, int x, int z) {
		String newModel = name + "_CENTER";
		if (x > 0 && z > 0 && x < this.size.getWidth() - 1 && z < this.size.getDepth() - 1) {
			Object westObj = maskObjects[x - 1][z];
			Object eastObj = maskObjects[x + 1][z];
			Object northObj = maskObjects[x][z - 1];
			Object southObj = maskObjects[x][z + 1];
			if (westObj != null && isObjectBuilding(westObj) && northObj != null && isObjectBuilding(northObj)) {
				String tempName = name + "_NORTH_WEST";
				if (UIData.modelData.containsKey(tempName)) {
					newModel = tempName;
				}
			} else if (eastObj != null && isObjectBuilding(eastObj) && northObj != null && isObjectBuilding(northObj)) {
				String tempName = name + "_NORTH_EAST";
				if (UIData.modelData.containsKey(tempName)) {
					newModel = tempName;
				}
			} else if (eastObj != null && isObjectBuilding(eastObj) && southObj != null && isObjectBuilding(southObj)) {
				String tempName = name + "_SOUTH_EAST";
				if (UIData.modelData.containsKey(tempName)) {
					newModel = tempName;
				}
			} else if (westObj != null && isObjectBuilding(westObj) && southObj != null && isObjectBuilding(southObj)) {
				String tempName = name + "_SOUTH_WEST";
				if (UIData.modelData.containsKey(tempName)) {
					newModel = tempName;
				}
			} else if (westObj != null && isObjectBuilding(westObj) && eastObj != null && isObjectBuilding(eastObj)) {
				String tempName = name + "_HORIZONTAL";
				if (UIData.modelData.containsKey(tempName)) {
					newModel = tempName;
				}
			} else if (westObj != null && isObjectBuilding(westObj)) {
				String tempName = name + "_WEST";
				if (UIData.modelData.containsKey(tempName)) {
					newModel = tempName;
				}
			} else if (eastObj != null && isObjectBuilding(eastObj)) {
				String tempName = name + "_EAST";
				if (UIData.modelData.containsKey(tempName)) {
					newModel = tempName;
				}
			} else if (northObj != null && isObjectBuilding(northObj) && southObj != null
					&& isObjectBuilding(southObj)) {
				String tempName = name + "_VERTICAL";
				if (UIData.modelData.containsKey(tempName)) {
					newModel = tempName;
				}
			} else if (northObj != null && isObjectBuilding(northObj)) {
				String tempName = name + "_NORTH";
				if (UIData.modelData.containsKey(tempName)) {
					newModel = tempName;
				}
			} else if (southObj != null && isObjectBuilding(southObj)) {
				String tempName = name + "_SOUTH";
				if (UIData.modelData.containsKey(tempName)) {
					newModel = tempName;
				}
			}
		}
		return newModel;
	}

	public void update() {

		if (needsUpdating) {
			this.build();
			needsUpdating = false;
		}
	}

	public void renderPassive() {
		if (passiveID != -1) {
			GL11.glCallList(passiveID);
		}
	}

	public void renderActive() {
		if (activeID != -1) {
			GL11.glCallList(activeID);
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
