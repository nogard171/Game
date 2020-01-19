package core;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import classes.GLObject;
import classes.GLSpriteData;
import classes.GLType;
import classes.GLView;
import classes.GLIndex;
import game.Main;
import utils.GLGenerator;
import utils.GLLogger;

public class GLChunk {
	// the chunks display list id
	private int dlId = -1;
	// the chunks position in the world
	public Point position;
	// the chunks index
	public GLIndex index;
	// the chunks sizing for the object data
	public Vector3f size = new Vector3f(16, 2, 16);
	// the objects for the map
	public HashMap<GLIndex, GLObject> objects = new HashMap<GLIndex, GLObject>();
	// the list of the rendered objects
	private ArrayList<GLObject> renderedObjects = new ArrayList<GLObject>();
	// the bounds of the chunk
	private Polygon bounds;
	// the current level
	int currentLevel = 0;
	// the previous level
	int previousLevel = 0;

	int lowestRender = 0;
	// var for updating the display list
	boolean needsUpdating = true;
	// var for determining if a chunk is empty
	boolean isEmpty = false;
	// var for checking the total object count on the screen
	int renderCount = 0;

	public GLChunk(int x, int y, int z) {
		index = new GLIndex(0, 0, 0, x, y, z);
		position = new Point((int) ((x - z) * (size.x * 32)), (int) (((z + x) * (size.x * 16)) + (y * (size.y * 32))));
	}

	public void setupChunk() {

		this.updateBounds();

		int xCount = (int) size.x;
		int zCount = (int) size.z;
		int yCount = (int) size.y;

		for (int y = 0; y < yCount; y++) {
			for (int x = 0; x < xCount; x++) {
				for (int z = 0; z < zCount; z++) {
					GLObject obj = new GLObject(GLType.GRASS);

					if (y == 0) {
						obj = new GLObject(GLType.AIR);
					}
					if (x == 0 && z == 0 && y == 0) {
						obj = new GLObject(GLType.SAND);
					}
					if (x == 5 && z == 5 && y == 0) {
						obj = new GLObject(GLType.BUSH);
					}

					if (x == 7 && z == 5 && y == 0) {
						obj = new GLObject(GLType.COPPER_ORE);
					}
					if (x == 7 && z == 6 && y == 0) {
						obj = new GLObject(GLType.IRON_ORE);
					}
					if (x == 8 && z == 6 && y == 0) {
						obj = new GLObject(GLType.TIN_ORE);
					}

					int posX = position.x + (x - z) * 32;
					int posY = position.y + (y - 1) * 32;
					int posZ = ((z + x) * 16) + posY;

					Polygon poly = new Polygon();

					poly.addPoint(posX + 32, posZ);

					poly.addPoint(posX + 64, posZ + 16);

					poly.addPoint(posX + 64, posZ + 48);

					poly.addPoint(posX + 32, posZ + 64);

					poly.addPoint(posX, posZ + 48);

					poly.addPoint(posX, posZ + 16);

					obj.bounds = poly;

					obj.setPositionGLIndex(x, y, z, index.chunkX, index.chunkY, index.chunkZ);
					objects.put(obj.getPositionGLIndex(), obj);
				}
			}
		}
		lowestRender = (int) size.y - 1;
		updateDisplayList();
	}

	public void updateBounds() {
		bounds = new Polygon();

		bounds.addPoint(position.x + 32, position.y + (0 + ((this.currentLevel - 1) * 32)));

		bounds.addPoint((int) (position.x + ((size.x + 1) * 32)),
				(int) (position.y + (size.x * 16 + ((this.currentLevel - 1) * 32))));

		bounds.addPoint((int) (position.x + ((size.x + 1) * 32)), (int) (position.y
				+ (size.x * 16 + ((this.currentLevel - 1) * 32)) + ((size.y - this.currentLevel) * 32)));

		bounds.addPoint((int) (position.x + (size.x - size.z + 1) * 32), (int) (position.y
				+ ((size.z + size.x) * 16 + ((this.currentLevel - 1) * 32) + ((size.y - this.currentLevel) * 32))));

		bounds.addPoint((int) (position.x + ((1 - size.z) * 32)), (int) (position.y
				+ (size.z * 16 + (((this.currentLevel - 1)) * 32)) + ((size.y - this.currentLevel) * 32)));

		bounds.addPoint((int) (position.x + ((1 - size.z) * 32)),
				(int) (position.y + (size.z * 16 + ((this.currentLevel - 1) * 32))));

		bounds.addPoint(position.x + 32, position.y + (0 + (((this.currentLevel - 1)) * 32)));

	}

	public void updateDisplayList() {
		this.isEmpty = false;
		this.updateBounds();

		HashMap<String, GLSpriteData> sprites = Main.sprites;

		renderCount = 0;
		renderedObjects.clear();

		dlId = GL11.glGenLists(1);

		GL11.glNewList(dlId, GL11.GL_COMPILE);

		GL11.glBegin(GL11.GL_QUADS);
		int xCount = (int) this.size.x;
		int zCount = (int) this.size.z;
		int yCount = (int) this.size.y;

		for (int y = yCount - 1; y >= this.currentLevel; y--) {
			for (int x = 0; x < xCount; x++) {
				for (int z = 0; z < zCount; z++) {
					GLObject obj = objects.get(new GLIndex(x, y, z));

					if (obj != null) {
						obj.setVisible(false);
						if (obj.getType() != GLType.AIR) {

							Color c = (Color) Color.WHITE;

							checkObjectVisibility(obj);
							GLSpriteData sprite = null;

							if (obj.isVisible() || y == this.currentLevel) {
								if (obj.isKnown()) {
									sprite = sprites.get(obj.getType().toString());
								} else {
									sprite = sprites.get("UNKNOWN");
								}
							}

							if (sprite != null) {
								renderObject(obj, sprite, c);
								renderedObjects.add(obj);
								renderCount++;
								if (lowestRender > y && obj.isKnown()) {
									lowestRender = y;
								}
							}
						}
					}
				}
			}
		}
		GL11.glEnd();
		GL11.glEndList();
		if (renderCount == 0) {
			this.isEmpty = true;
		}

		if (GLChunkManager.mapMaxHeight > lowestRender) {
			// GLChunkManager.mapMaxHeight = lowestRender;
		}

	}

	private void checkObjectVisibility(GLObject obj) {

		boolean visible = obj.isVisible();

		int x = (int) obj.getPositionGLIndex().x;
		int y = (int) obj.getPositionGLIndex().y;
		int z = (int) obj.getPositionGLIndex().z;

		if (x + 1 < size.x) {
			GLObject right = objects.get(new GLIndex(x + 1, y, z));
			if (right != null) {
				if (right.getType().isMask()) {
					obj.setKnown(true);
					visible = true;
				}
			}
		} else {
			GLChunk chunkX = GLChunkManager.chunks.get(new GLIndex(index.chunkX + 1, index.chunkY, index.chunkZ));
			if (chunkX != null) {
				GLObject objectX = chunkX.objects.get(new GLIndex(0, y, z));
				if (objectX != null) {
					if (objectX.getType().isMask()) {
						obj.setKnown(true);
						visible = true;
					}
				}
			} else {
				visible = true;
			}
		}
		if (z + 1 < size.z) {
			GLObject left = objects.get(new GLIndex(x, y, z + 1));
			if (left != null) {
				if (left.getType().isMask()) {
					obj.setKnown(true);
					visible = true;
				}
			}
		} else {
			GLChunk chunkZ = GLChunkManager.chunks.get(new GLIndex(index.chunkX, index.chunkY, index.chunkZ + 1));
			if (chunkZ != null) {
				GLObject objectZ = chunkZ.objects.get(new GLIndex(x, y, 0));
				if (objectZ != null) {
					if (objectZ.getType().isMask()) {
						obj.setKnown(true);
						visible = true;
					}
				}
			} else {
				visible = true;
			}
		}

		if (y - 1 >= 0) {
			GLObject top = objects.get(new GLIndex(x, y - 1, z));
			if (top != null) {
				if (top.getType().isMask()) {
					obj.setKnown(true);
					visible = true;
				}
			}
		} else {
			obj.setKnown(true);
			visible = true;
		}

		obj.setVisible(visible);
	}

	private void renderObject(GLObject obj, GLSpriteData spriteData, Color c) {
		int x = (int) obj.getPositionGLIndex().x;
		int y = (int) obj.getPositionGLIndex().y;
		int z = (int) obj.getPositionGLIndex().z;
		if (spriteData != null) {
			GL11.glColor3f((float) c.getRed() / 255, (float) c.getGreen() / 255, (float) c.getBlue() / 255);

			int posX = (int) (position.x + ((x - z) * 32) + spriteData.offset.x);
			int posY = position.y + ((y - 1) * 32);
			int posZ = (int) (((z + x) * 16) + posY + spriteData.offset.y);

			GL11.glTexCoord2f(spriteData.textureData.x, spriteData.textureData.y);
			GL11.glVertex2f(posX, posZ);
			GL11.glTexCoord2f(spriteData.textureData.x + spriteData.textureData.z, spriteData.textureData.y);
			GL11.glVertex2f(posX + spriteData.size.getWidth(), posZ);
			GL11.glTexCoord2f(spriteData.textureData.x + spriteData.textureData.z,
					spriteData.textureData.y + spriteData.textureData.w);
			GL11.glVertex2f(posX + spriteData.size.getWidth(), posZ + spriteData.size.getHeight());
			GL11.glTexCoord2f(spriteData.textureData.x, spriteData.textureData.y + spriteData.textureData.w);
			GL11.glVertex2f(posX, posZ + spriteData.size.getHeight());
		}
	}

	public void update() {
		Point mousePoint = new Point(Mouse.getX() + (int) Main.view.getPosition().x,
				Display.getHeight() - Mouse.getY() + (int) Main.view.getPosition().y);
		if (renderedObjects.size() > 0) {
			GLChunkManager.mouseGLIndex.clear();
			for (GLObject obj : renderedObjects) {
				if (obj.bounds.contains(mousePoint)) {
					GLChunkManager.mouseGLIndex.add(obj.getPositionGLIndex());
				}
			}
		}

		if (previousLevel != currentLevel) {
			previousLevel = currentLevel;
			this.needsUpdating = true;
		}

		if (this.needsUpdating) {
			this.updateDisplayList();
			this.needsUpdating = false;
		}
	}

	public boolean isEmpty() {
		return this.isEmpty;
	}

	public int getLevel() {
		return this.currentLevel;
	}

	public void render() {
		if (this.dlId == -1) {
			this.updateDisplayList();
		} else {
			GL11.glCallList(this.dlId);

			GL11.glDisable(GL11.GL_TEXTURE_2D);

			GL11.glBegin(GL11.GL_LINE_LOOP);
			GL11.glColor3f(1, 0, 0);
			for (int i = 0; i < bounds.xpoints.length - 1; i++) {
				float posX = bounds.xpoints[i];
				float posZ = bounds.ypoints[i];
				GL11.glVertex2f(posX, posZ);
			}
			GL11.glEnd();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
		}

	}

	public void setLevel(int newCurrentLevel) {
		this.currentLevel = newCurrentLevel;
	}

	public boolean inView(GLView view) {
		boolean inView = false;
		if (bounds.intersects(new Rectangle((int) view.getPosition().x, (int) view.getPosition().y,
				(int) view.getSize().getWidth(), (int) view.getSize().getHeight()))) {
			inView = true;
		}
		return inView;
	}

	public void moveObject(GLIndex index, GLIndex newIndex) {

	}

	public void setObject(GLIndex index, GLObject newObj) {
		GLObject obj = this.objects.get(index);
		if (obj != null) {

			int posX = position.x + (index.x - index.z) * 32;
			int posY = position.y + (index.y - 1) * 32;
			int posZ = ((index.z + index.x) * 16) + posY;
			Polygon poly = new Polygon();

			poly.addPoint(posX + 32, posZ);

			poly.addPoint(posX + 64, posZ + 16);

			poly.addPoint(posX + 64, posZ + 48);

			poly.addPoint(posX + 32, posZ + 64);

			poly.addPoint(posX, posZ + 48);

			poly.addPoint(posX, posZ + 16);

			newObj.bounds = poly;

			newObj.setPositionGLIndex(index.x, index.y, index.z, index.chunkX, index.chunkY, index.chunkZ);
			this.objects.put(index, newObj);
			updateDisplayList();

		} else {
			GLLogger.writeLog("Index(Chunk / Object): " + index.chunkX + "," + index.chunkY + "," + index.chunkZ + " / "
					+ index.x + "," + index.y + "," + index.z + " - Cannot place object");
		}
	}
}
