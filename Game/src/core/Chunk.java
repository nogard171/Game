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
	private Vector3f size = new Vector3f(16, 16, 16);

	public static ArrayList<Object> rendererObjects = new ArrayList<Object>();

	private Point position = new Point(0, 0);
	private Vector3f index;

	public Chunk(Vector3f newIndex) {
		index = newIndex;
		int posX = (int) (((index.x - index.z) * 32) * size.x);
		int posY = (int) (((0 - index.y) * 32) * size.y);
		int posZ = (int) ((((index.z + index.x) * 16) - posY) * size.z);

		position = new Point(posX, posZ);

	}

	public Vector3f getIndex() {
		return index;
	}

	public void load() {

		chunkBounds = new Polygon();

		chunkBounds.addPoint(position.x + 32, position.y);
		chunkBounds.addPoint(((int) (size.x + 1) * 32) + position.x, ((int) (size.y) * 16) + position.y);

		chunkBounds.addPoint((((int) (size.x + 1) * 32)) + position.x, (((int) (size.y) * 48) + 32) + position.y);

		chunkBounds.addPoint(32 + position.x, (((int) (size.y) * 64) + 32) + position.y);

		chunkBounds.addPoint((32 - ((int) (size.x) * 32)) + position.x, (((int) (size.y) * 48) + 32) + position.y);

		chunkBounds.addPoint((32 - ((int) (size.x) * 32)) + position.x, ((int) (size.y) * 16) + position.y);

		chunkBounds.addPoint(position.x + 32, position.y);

		Object charc = new Object(new Vector3f(0, 0, 0));
		charc.setSprite("character");

		objects.put("0,0,0", charc);

		for (int y = (int) (size.y); y >= 0; y--) {
			for (int x = 0; x < size.x; x++) {
				for (int z = 0; z < size.x; z++) {
					Object obj = new Object(
							new Vector3f(x + (index.x * size.x), y + (index.y * size.y), z + (index.z * size.z)));

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
					if (x == 0 && z == 0 && y == 0) {
						obj.setSprite("dirt");
					}
					objects.put(x + "," + y + "," + z, obj);
				}
			}
		}
	}

	public void build() {
		rendererObjects.clear();
		id = GL11.glGenLists(1);
		GL11.glNewList(id, GL11.GL_COMPILE_AND_EXECUTE);
		GL11.glBegin(GL11.GL_QUADS);
		for (int y = (int) (size.y); y >= layer; y--) {
			for (int x = 0; x < size.x; x++) {
				for (int z = 0; z < size.x; z++) {
					boolean visible = isVisible(x, y, z);
					if (visible) {
						Object obj = objects.get(x + "," + y + "," + z);
						if (obj != null) {
							String sprite = obj.getSprite();
							if (sprite != null) {
								int posX = position.x + ((x - z) * 32);
								int posY = ((0 - y) * 32);
								int posZ = position.y + (((z + x) * 16) - posY);
								Renderer.renderSprite(sprite, posX, posZ);
								rendererObjects.add(obj);
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

			if (testObj != null) {
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
		for (Object obj : rendererObjects) {
			if (obj.bounds != null) {
				System.out.println("Chunk: " + getIndex());
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
			// GL11.glPushMatrix();
			// GL11.glTranslatef(position.x, position.y, 0);
			GL11.glCallList(id);
			// GL11.glPopMatrix();

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
			build();
		}
		layer = newLayer;
	}
}
