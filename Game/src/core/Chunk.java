package core;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import game.Base;

public class Chunk {
	private int id = -1;
	public boolean updateID = false;
	public Polygon chunkBounds;

	public HashMap<String, Object> objects = new HashMap<String, Object>();

	public ArrayList<Object> renderedObjects = new ArrayList<Object>();

	private Point position = new Point(0, 0);
	private Vector3f index;

	public Chunk(Vector3f newIndex) {
		index = newIndex;
		int posX = (int) (((index.x - index.z) * 32) * ChunkManager.size.x);
		int posY = (int) (((0 - index.y) * 32) * ChunkManager.size.y);
		int posZ = (int) ((((index.z + index.x) * 16) - posY) * ChunkManager.size.z);

		position = new Point(posX, posZ);
	}

	public String getIndex() {
		return (int) index.x + "," + (int) index.y + "," + (int) index.z;
	}

	public void load() {

		chunkBounds = new Polygon();

		chunkBounds.addPoint(position.x + 32, position.y);

		chunkBounds.addPoint(((int) (ChunkManager.size.x + 1) * 32) + position.x,
				position.y + ((int) (ChunkManager.size.z) * 16));

		chunkBounds.addPoint((((int) (ChunkManager.size.x + 1) * 32)) + position.x,
				(((int) (ChunkManager.size.y) * 32)) + position.y + ((int) (ChunkManager.size.z) * 16));

		chunkBounds.addPoint(32 + position.x,
				(((int) (ChunkManager.size.y) * 32)) + position.y + ((int) (ChunkManager.size.z) * 32));

		chunkBounds.addPoint((32 - ((int) (ChunkManager.size.x) * 32)) + position.x,
				(((int) (ChunkManager.size.y) * 32)) + position.y + ((int) (ChunkManager.size.z) * 16));

		chunkBounds.addPoint((32 - ((int) (ChunkManager.size.x) * 32)) + position.x,
				position.y + ((int) (ChunkManager.size.z) * 16));

		chunkBounds.addPoint(position.x + 32, position.y);

		int offset = 0;
		if (index.x != 0 || index.y != 0 || index.z != 0) {
			offset = 1;
		}
		for (int y = (int) (ChunkManager.size.y) - 1; y >= 0; y--) {
			for (int x = 0; x < ChunkManager.size.x; x++) {
				for (int z = 0; z < ChunkManager.size.x; z++) {
					Object obj = new Object(new Vector3f(x + (index.x * ChunkManager.size.x),
							y + (index.y * ChunkManager.size.y), z + (index.z * ChunkManager.size.z)));

					int posX = position.x + ((x - z) * 32);
					int posY = ((0 - y) * 32);
					int posZ = position.y + (((z + x) * 16) - posY);

					Polygon newBounds = new Polygon();
					newBounds.addPoint(posX + 32, posZ);

					newBounds.addPoint(posX + 64, posZ + 16);
					newBounds.addPoint(posX + 64, posZ + 48);

					newBounds.addPoint(posX + 32, posZ + 64);

					newBounds.addPoint(posX, posZ + 48);
					newBounds.addPoint(posX, posZ + 16);

					newBounds.addPoint(posX + 32, posZ);

					obj.setBounds(newBounds);

					obj.setSprite("grass");

					if (index.x == 0 && index.y == 0 && index.z == 0 && x == 0 && y == 0 && z == 0) {

						obj.setSprite("character");
						obj.offset = new Point(10, 32);
						obj.known = true;
						objects.put(x + "," + y + "," + z, obj);
					}

					if (y > offset) {
						objects.put(x + "," + y + "," + z, obj);
					}
				}
			}
		}

	}

	public boolean isKnown(int x, int y, int z) {
		boolean known = false;
		if (y - 1 > 0) {
			Object testObj = objects.get(x + "," + (y - 1) + "," + z);
			if (testObj == null) {
				known = true;
			}
		} else {

			known = true;
		}
		return known;
	}

	public void build() {
		renderedObjects.clear();
		id = GL11.glGenLists(1);
		GL11.glNewList(id, GL11.GL_COMPILE_AND_EXECUTE);
		GL11.glBegin(GL11.GL_QUADS);
		for (int y = (int) ChunkManager.size.y - 1; y >= layer; y--) {
			for (int x = 0; x < ChunkManager.size.x; x++) {
				for (int z = 0; z < ChunkManager.size.x; z++) {
					Object obj = objects.get(x + "," + y + "," + z);
					if (obj != null) {

						boolean visible = isVisible(x, y, z);
						if (visible) {

							obj.known = isKnown(x, y, z);

							String sprite = obj.getSprite();
							if (sprite != null) {
								int posX = position.x + ((x - z) * 32);
								int posY = ((0 - y) * 32);
								int posZ = position.y + (((z + x) * 16) - posY);
								if (!obj.known) {
									sprite = "unknown";
								}
								Renderer.renderSprite(sprite, posX + obj.offset.x, posZ + obj.offset.y);
								renderedObjects.add(obj);
							}
						}
					}
				}
			}
		}
		GL11.glEnd();
		GL11.glEndList();
	}

	ArrayList<Vector3f> directions = new ArrayList<Vector3f>();

	public boolean isVisible(int x, int y, int z) {
		boolean isVisible = false;

		if (directions.size() == 0) {
			// directions.add(new Vector3f(-1, 0, 0));
			directions.add(new Vector3f(1, 0, 0));

			directions.add(new Vector3f(0, -1, 0));
			// directions.add(new Vector3f(0, 1, 0));

			// directions.add(new Vector3f(0, 0, -1));
			directions.add(new Vector3f(0, 0, 1));
		}
		int visibleCount = 0;
		for (Vector3f vec : directions) {
			Object testObj = objects.get((int) (x + vec.x) + "," + (int) (y + vec.y) + "," + (int) (z + vec.z));
			if (testObj != null && testObj.getSprite() != "character") {
				visibleCount++;
			}
		}
		if (visibleCount < directions.size() || y == layer) {
			isVisible = true;
		}

		return isVisible;
	}

	public void update() {
	}

	public void handleHover() {

		for (Object obj : this.renderedObjects) {
			if (obj.bounds != null) {
				if (obj.bounds.contains(Base.mousePosition) && !Base.hoveredObjects.contains(obj)) {
					Base.hoveredObjects.add(obj);
				}
			}
		}
	}

	public void render() {
		if (id == -1 || updateID) {
			build();
			updateID = false;
		} else {
			GL11.glColor3f(1, 1, 1);
			GL11.glCallList(id);

			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(1, 0, 0);
			GL11.glBegin(GL11.GL_POLYGON);
			for (int i = 0; i < chunkBounds.xpoints.length - 1; i++) {
				GL11.glVertex2i(chunkBounds.xpoints[i], chunkBounds.ypoints[i]);
			}
			GL11.glEnd();
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

	}

	int layer = 0;

	public void setLayer(int newLayer) {
		if (layer != newLayer) {
			for (int x = 0; x < ChunkManager.size.x; x++) {
				for (int z = 0; z < ChunkManager.size.x; z++) {
					Object obj = objects.get(x + "," + newLayer + "," + z);
					if (obj != null) {
						System.out.println("Build");
						updateID = true;
					}
					if (updateID) {
						break;
					}
				}
				if (updateID) {
					break;
				}
			}
			layer = newLayer;
		}
	}

	public Object getData(int x, int y, int z) {
		return objects.get(x + "," + y + "," + z);
	}
}
