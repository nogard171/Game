package core;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import utils.Renderer;

public class Chunk {
	private int id = -1;
	private int fringeID = -1;

	private HashMap<Point, Object> groundObjects = new HashMap<Point, Object>();
	private HashMap<Point, Object> maskObjects = new HashMap<Point, Object>();
	private HashMap<Point, Object> fringeObjects = new HashMap<Point, Object>();

	private Point size = new Point(16, 16);

	private boolean needsUpdating = false;

	public void setup() {
		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				Object obj = new Object();
				Point index = new Point(x, y);

				obj.sprite = "grass";

				groundObjects.put(index, obj);

			}
		}
	}

	public void build() {

		id = GL11.glGenLists(1);

		GL11.glNewList(id, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);

		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				Object groundObj = groundObjects.get(new Point(x, y));
				Object maskObj = maskObjects.get(new Point(x, y));

				if (groundObj != null) {
					Renderer.renderSprite(x * 32, y * 32, groundObj.sprite);
				}
				if (maskObj != null) {
					Renderer.renderSprite(x * 32, y * 32, maskObj.sprite);
				}

			}
		}
		GL11.glEnd();
		GL11.glEndList();
	}

	public void buildFringe() {

		fringeID = GL11.glGenLists(1);

		GL11.glNewList(fringeID, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);

		for (int x = 0; x < size.x; x++) {
			for (int y = 0; y < size.y; y++) {
				Object obj = fringeObjects.get(new Point(x, y));
				if (obj != null) {
					Renderer.renderSprite(x * 32, y * 32, obj.sprite);
				}
			}
		}
		GL11.glEnd();
		GL11.glEndList();
	}

	public void setGround(Point index, Object newObject) {
		groundObjects.put(index, newObject);
		needsUpdating = true;
	}

	public void update() {
		if (needsUpdating) {
			build();
			buildFringe();
			needsUpdating = false;
		}
	}

	public void render() {
		if (id == -1) {
			build();
		} else {
			GL11.glCallList(id);
		}
	}

	public void renderFringe() {
		if (fringeID == -1) {
			buildFringe();
		} else {
			GL11.glCallList(fringeID);
		}
	}
}
