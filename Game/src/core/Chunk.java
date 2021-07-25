package core;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

public class Chunk {

	private int id = -1;
	Point index;

	HashMap<Point, Tile> tiles = new HashMap<Point, Tile>();
	HashMap<Point, Object> objects = new HashMap<Point, Object>();
	HashMap<Point, Entity> entities = new HashMap<Point, Entity>();
	public static Dimension size = new Dimension(16, 16);

	public Chunk(int x, int y) {
		index = new Point(x, y);
	}

	public Tile getTile(Point index) {
		return tiles.get(index);
	}

	public Object getObject(Point index) {
		return objects.get(index);
	}

	public Point getIndexByType(TextureType type) {
		Point tempIndex = null;
		for (Point key : entities.keySet()) {
			Entity ent = entities.get(key);
			if (ent != null) {
				if (ent.getType() == type) {
					tempIndex = key;
					break;
				}
			}
		}

		if (tempIndex == null) {
			for (Point key : objects.keySet()) {
				Object obj = objects.get(key);
				if (obj != null) {
					if (obj.getType() == type) {
						tempIndex = key;
						break;
					}
				}
			}
		}

		if (tempIndex == null) {
			for (Point key : tiles.keySet()) {
				Tile tile = tiles.get(key);
				if (tile != null) {
					if (tile.getType() == type) {
						tempIndex = key;
						break;
					}
				}
			}
		}

		if (tempIndex != null) {
			tempIndex = new Point(tempIndex.x + (index.x * size.width), tempIndex.y + (index.y * size.height));
		}

		return tempIndex;
	}

	public Vector2f getPosition(Point index) {
		return getPosition(index.x, index.y);
	}

	public Vector2f getPosition(int x, int y) {
		float chunkPosX = (((index.x - index.y) * 32) * size.width);
		float chunkPosY = (((index.y + index.x) * 16) * size.height);

		float posX = chunkPosX + (((x - y) * 32) - 32);
		float posY = chunkPosY + ((y + x) * 16);

		return new Vector2f(posX, posY);
	}

	public void setup() {
		float[][] heightMap = Generator.generateHeightMap(size.width, size.height);

		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {

				Vector2f position = getPosition(x, y);

				Tile tile = new Tile(TextureType.GRASS);
				tile.setPosition(position);

				Point tileIndex = new Point(x, y);

				Random r = new Random();
				float t = r.nextFloat();
				if (t < 0.5f) {
					tile.setType(TextureType.GRASS0);
				}

				tiles.put(tileIndex, tile);

				Object obj = new Object(TextureType.AIR);
				obj.setPosition(position);
				if (x == 5 && y == 5) {
					obj.setType(TextureType.TREE);

				}
				if (y == 2 && index.x == 1 && index.y == 1) {
					obj.setType(TextureType.TREE);

				}
				objects.put(tileIndex, obj);
				Entity ent = new Entity(TextureType.AIR);
				ent.setPosition(position);
				if (x == 12 && y == 7 && index.x == 1 && index.y == 0) {
					ent.setType(TextureType.CHARACTER);
				}
				entities.put(tileIndex, ent);
			}
		}
		build();
	}

	public void build() {
		System.out.println("Build..." + index);
		id = GL11.glGenLists(1);
		GL11.glNewList(id, GL11.GL_COMPILE);

		GL11.glBegin(GL11.GL_QUADS);
		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {

				Vector2f position = getPosition(x, y);

				Point tempIndex = new Point(x, y);
				Tile tile = tiles.get(tempIndex);
				if (tile != null) {
					tile.setPosition(position);
					Renderer.renderSprite(tile.getType(), (int) tile.getPosition().x, (int) tile.getPosition().y);
				}

				Object obj = objects.get(tempIndex);
				if (obj != null) {
					obj.setPosition(position);
					Renderer.renderSprite(obj.getType(), (int) obj.getPosition().x, (int) obj.getPosition().y);
				}

				Entity ent = entities.get(tempIndex);
				if (ent != null) {
					ent.setPosition(position);
					Renderer.renderSprite(ent.getType(), (int) ent.getPosition().x, (int) ent.getPosition().y);
				}
			}
		}
		GL11.glEnd();

		GL11.glEndList();
	}

	public void update() {

	}

	public void render() {
		if (id == -1) {
			build();
		} else {
			GL11.glCallList(id);
		}
	}

	public Tile getAtIndex(Point point) {
		Tile newTile = (Object) getObjectAtIndex(point);

		Tile tile = objects.get(point);
		newTile = (newTile == null ? (tile != null ? tile : null) : newTile);

		System.out.println("Geting..." + newTile);

		return newTile;
	}

	public Tile getObjectAtIndex(Point point) {
		return getObjectAtIndex(point, false, true);
	}

	public Tile getObjectAtIndex(Point point, boolean removeAIR, boolean removeTiles) {
		Tile newTile = null;

		Entity ent = entities.get(point);
		newTile = (ent != null && ((removeAIR && ent.getType() != TextureType.AIR) || (!removeAIR)) ? ent : null);

		Object obj = objects.get(point);
		newTile = (newTile == null || (newTile.getType() == TextureType.AIR)
				? (obj != null && ((removeAIR && obj.getType() != TextureType.AIR) || (!removeAIR)) ? obj : null)
				: newTile);
		if (!removeTiles) {
			Tile tile = tiles.get(point);
			newTile = (newTile == null || (newTile.getType() == TextureType.AIR)
					? (tile != null && ((removeAIR && tile.getType() != TextureType.AIR) || (!removeAIR)) ? tile : null)
					: newTile);
		}
		return newTile;
	}

	public void removeType(TextureType type) {
		boolean rebuild = false;
		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {
				Point index = new Point(x, y);
				Tile tile = tiles.get(index);
				if (tile != null) {
					if (tile.getType() == type) {
						tiles.put(index, new Tile(TextureType.AIR));
						rebuild = true;
					}
				}
				tile = (Tile) objects.get(index);
				if (tile != null) {
					System.out.println("Check:" + (tile.getType() == type));
					if (tile.getType() == type) {
						objects.put(index, new Object(TextureType.AIR));
						rebuild = true;
					}
				}
				tile = (Tile) entities.get(index);
				if (tile != null) {
					if (tile.getType() == type) {
						entities.put(index, new Entity(TextureType.AIR));
						rebuild = true;
					}
				}
			}
		}
		if (rebuild) {
			build();
		}
	}
}
