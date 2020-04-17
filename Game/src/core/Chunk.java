package core;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import game.Base;

public class Chunk {
	private int id = -1;
	public boolean updateID = false;

	public HashMap<String, Object> objects = new HashMap<String, Object>();
	private Vector3f size = new Vector3f(16, 16, 16);

	public static ArrayList<Object> rendererObjects = new ArrayList<Object>();

	public void load() {
		for (int y = (int) (size.y); y >= 0; y--) {
			for (int x = 0; x < size.x; x++) {
				for (int z = 0; z < size.x; z++) {
					Object obj = new Object();

					int posX = (x - z) * 33;
					int posY = (1 - y) * 33;
					int posZ = ((z + x) * 17) - posY;

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
								int posX = (x - z) * 33;
								int posY = (1 - y) * 33;
								int posZ = ((z + x) * 17) - posY;
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

		if (Base.mousePosition != null) {
			for (Object obj : rendererObjects) {
				if (obj.bounds.contains(Base.mousePosition)) {
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
			GL11.glCallList(id);
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
