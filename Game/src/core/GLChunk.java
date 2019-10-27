package core;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import game.Main;

public class GLChunk {
	public Point position;
	public Vector3f size = new Vector3f(16, 16, 16);
	public Vector3f index = new Vector3f();
	private int dlId = -1;
	private GLObject[][][] objects;
	private Polygon bounds;
	int currentLevel = 0;
	boolean needsUpdating = true;
	boolean isEmpty = false;
	Vector2f hover;
	GLGenerator gen;

	public GLChunk(int x, int y, int z) {
		index = new Vector3f(x, y, z);
		position = new Point((int) ((x - z) * (size.x * 32)), (int) (((z + x) * (size.x * 16)) + (y * (size.y * 32))));
		// this.setupChunk();
	}

	public void setupChunk(boolean generate) {

		if (generate) {
			gen = new GLGenerator();
			gen.init();
		}

		objects = new GLObject[(int) size.x][(int) size.z][(int) size.y];
		int xCount = objects.length;
		int zCount = objects[0].length;
		int yCount = objects[0][0].length;

		this.updateBounds();

		for (int x = 0; x < xCount; x++) {
			for (int z = 0; z < zCount; z++) {
				for (int y = 0; y < yCount; y++) {
					int height = gen.map[x][z];
					GLObject obj = new GLObject(GLType.AIR);
					if (y > height) {
						obj.setType(GLType.GRASS);
					}
					if (y > height && y == 3) {
						obj.setType(GLType.SAND);
						// objects[x][z][y] = new GLObject(GLType.WATER);

					}
					if (y > 3) {
						obj.setType(GLType.SAND);
					}

					if (y == 3 && obj.getType() == GLType.AIR) {
						obj.setType(GLType.WATER);
					}

					if (y == 0) {
						obj.setKnown(true);
						obj.setVisible(true);
					}
					obj.setPositionIndex(x, y, z);
					if (y < 2) {
						obj.setType(GLType.AIR);
					}
					objects[x][z][y] = obj;
				}
			}
		}
	}

	public void updateBounds() {
		bounds = new Polygon();
		bounds.addPoint(position.x + 32, position.y + (0 + ((this.currentLevel - 1) * 32)));
		bounds.addPoint((int) (position.x + ((size.x + 1) * 32)),
				(int) (position.y + (size.x * 16 + ((this.currentLevel - 1) * 32))));
		bounds.addPoint((int) (position.x + (size.x - size.z + 1) * 32),
				(int) (position.y + ((size.z + size.x) * 16 + ((this.currentLevel - 1) * 32))));
		bounds.addPoint((int) (position.x + ((1 - size.z) * 32)),
				(int) (position.y + (size.z * 16 + ((this.currentLevel - 1) * 32))));
	}

	int renderCount = 0;

	public void updateDisplayList(HashMap<String, GLChunk> chunks) {
		this.isEmpty = false;
		this.updateBounds();

		HashMap<String, GLSpriteData> sprites = Main.sprites;

		renderCount = 0;
		dlId = GL11.glGenLists(1);
		GL11.glNewList(dlId, GL11.GL_COMPILE);

		GL11.glBegin(GL11.GL_QUADS);
		int xCount = objects.length;
		int zCount = objects[0].length;
		int yCount = objects[0][0].length;
		for (int x = 0; x < xCount; x++) {
			for (int z = 0; z < zCount; z++) {
				for (int y = yCount - 1; y >= this.currentLevel; y--) {
					GLObject obj = objects[x][z][y];
					if (obj != null) {
						if (obj.getType() != GLType.AIR) {
							Color c = (Color) Color.WHITE;
							if (y != this.currentLevel) {
								c = new Color(128, 128, 128);
							}
							checkObjectVisibility(obj);
							GLSpriteData sprite = null;
							if (obj.isVisible()) {
								if (obj.isKnown()) {
									sprite = sprites.get(obj.getType().toString());
								} else {
									// sprite = sprites.get("UNKNOWN");
								}
							}

							if (sprite != null) {
								renderObject(obj, sprite, c);
								renderCount++;
							}
						}
					}
				}
			}
		}

		// System.out.println("Count: " + renderCount);
		GL11.glEnd();
		GL11.glEndList();
		if (renderCount == 0) {
			this.isEmpty = true;
		}
	}

	private void checkObjectVisibility(GLObject obj) {

		boolean visible = obj.isVisible();

		int x = (int) obj.getPositionIndex().getX();
		int y = (int) obj.getPositionIndex().getY();
		int z = (int) obj.getPositionIndex().getZ();

		if (y - 1 >= 0) {
			GLObject top = objects[x][z][y - 1];
			if (top != null) {
				if (isMask(top.getType())) {
					obj.setKnown(true);
					visible = true;
				}
			}
		} else {
			visible = true;
		}

		if (x + 1 < objects.length) {
			GLObject right = objects[x + 1][z][y];
			if (right != null) {
				if (isMask(right.getType())) {
					obj.setKnown(true);
					visible = true;
				}
			}
		} else {
			visible = true;
		}

		if (x - 1 >= 0) {
			GLObject right = objects[x - 1][z][y];
			if (right != null) {
				if (isMask(right.getType())) {
					obj.setKnown(true);
					visible = true;
				}
			}
		}
		if (z + 1 < objects[0].length) {
			GLObject left = objects[x][z + 1][y];
			if (left != null) {
				if (isMask(left.getType())) {
					obj.setKnown(true);
					visible = true;
				}
			}
		} else {
			visible = true;
		}

		if (z - 1 >= 0) {
			GLObject left = objects[x][z - 1][y];
			if (left != null) {
				if (isMask(left.getType())) {
					obj.setKnown(true);
					visible = true;
				}
			}
		}
		obj.setVisible(visible);
	}

	public boolean isMask(GLType type) {
		boolean isMask = false;

		switch (type) {
		case AIR:
			isMask = true;
			break;
		case TREE:
			isMask = true;
			break;
		default:
			isMask = false;
			break;
		}

		return isMask;
	}

	private Vector2f isOutFacing(int x, int y, int z, GLChunk leftChunk, GLChunk rightChunk) {

		Vector2f visible = new Vector2f(0, 0);
		boolean topFacing = false;
		if (y - 1 > 0) {
			GLObject top = objects[x][z][y - 1];
			if (top != null) {
				if (top.getType() == GLType.AIR) {
					topFacing = true;
				}
			}
		} else {
			topFacing = true;
		}
		if (!topFacing) {
			if (x + 1 < objects.length) {
				GLObject right = objects[x + 1][z][y];
				if (right != null) {
					if (right.getType() == GLType.AIR) {
						visible.x = 1;
					}
				}
			} else {
				if (leftChunk != null) {
					GLObject leftChunkObject = leftChunk.objects[0][z][y];
					if (leftChunkObject != null) {
						if (leftChunkObject.getType() == GLType.AIR) {
							visible.x = 1;
						}
					}
				} else {
					visible.x = 1;
				}
			}

			if (z + 1 < objects[0].length) {
				GLObject top = objects[x][z + 1][y];
				if (top != null) {
					if (top.getType() == GLType.AIR) {
						visible.y = 1;
					}
				}
			} else {
				if (rightChunk != null) {
					GLObject rightChunkObject = rightChunk.objects[x][0][y];
					if (rightChunkObject != null) {
						if (rightChunkObject.getType() == GLType.AIR) {
							visible.y = 1;
						}
					}
				} else {
					visible.y = 1;
				}
			}
		} else {
			visible = new Vector2f(0, 0);
		}
		return visible;
	}

	private Vector3f isVisible(int x, int y, int z) {
		Vector3f visible = new Vector3f(0, 0, 0);

		GLObject top = objects[x][z][y - 1];
		if (top != null) {
			if (top.getType() == GLType.AIR || top.getType() == GLType.TREE) {
				visible.y = 1;
			}
		}

		return visible;
	}

	private void renderObject(GLObject obj, GLSpriteData spriteData, Color c) {
		int x = (int) obj.getPositionIndex().getX();
		int y = (int) obj.getPositionIndex().getY();
		int z = (int) obj.getPositionIndex().getZ();
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

	public void update(Vector2f camera, HashMap<String, GLChunk> chunks) {
		Point mousePoint = new Point(Mouse.getX() - (int) camera.x,
				Display.getHeight() - Mouse.getY() - (int) camera.y);
		boolean mouseInChunk = ((bounds == null) ? false : bounds.contains(mousePoint));
		boolean mouseIsDown = ((Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) ? true : false);

		if (mouseInChunk && mouseIsDown) {
			hover = null;

			int xCount = objects.length;
			int zCount = objects[0].length;
			int yCount = objects[0][0].length;
			for (int x = 0; x < xCount; x++) {
				for (int z = 0; z < zCount; z++) {
					GLObject obj = objects[x][z][this.currentLevel];
					if (obj != null) {
						int posX = position.x + (x - z) * 32;
						int posY = position.y + (this.currentLevel - 1) * 32;
						int posZ = ((z + x) * 16) + posY;

						Polygon poly = new Polygon();
						poly.addPoint(posX + 32, posZ);
						poly.addPoint(posX + 64, posZ + 16);

						poly.addPoint(posX + 32, posZ + 32);
						poly.addPoint(posX, posZ + 16);

						if (poly.contains(mousePoint)) {
							if (objects[x][z][this.currentLevel] != null) {
								hover = new Vector2f(x, z);
							}
							break;
						}
					}
				}
			}

			if (hover != null && Mouse.isButtonDown(0)) {
				GLObject obj = objects[(int) hover.x][(int) hover.y][this.currentLevel];
				obj.setKnown(true);
				obj.setType(GLType.TREE);
				needsUpdating = true;
			}
			if (hover != null && Mouse.isButtonDown(1)) {
				GLObject obj = objects[(int) hover.x][(int) hover.y][this.currentLevel];
				obj.setKnown(true);
				obj.setType(GLType.GRASS);
				needsUpdating = true;
			}
		} else {
			hover = null;
		}
		int mouseWheel = Mouse.getDWheel();
		if (mouseWheel < 0 && this.currentLevel < size.y - 1) {
			this.currentLevel++;
			needsUpdating = true;
		}

		if (mouseWheel > 0 && this.currentLevel > 0) {
			this.currentLevel--;
			needsUpdating = true;
		}
		if (needsUpdating) {
			updateDisplayList(chunks);
			needsUpdating = false;
		}
	}

	public boolean isEmpty() {
		return this.isEmpty;
	}

	public int getLevel() {
		return this.currentLevel;
	}

	public void changeLevel(int level) {
		this.currentLevel = level;
		needsUpdating = true;
	}

	public void render() {

		if (this.dlId == -1) {
			needsUpdating = true;
		}
		GL11.glCallList(this.dlId);

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glBegin(GL11.GL_LINE_LOOP);
		GL11.glColor3f(1, 0, 0);
		for (int x = 0; x < bounds.xpoints.length; x++) {
			float posX = bounds.xpoints[x];
			float posZ = bounds.ypoints[x];
			GL11.glVertex2f(posX, posZ);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}

	public void setData(int[][] multiChunkMap) {
		gen = new GLGenerator();
		gen.map = multiChunkMap;
	}
}
