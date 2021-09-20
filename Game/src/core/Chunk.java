package core;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import utils.Ticker;

public class Chunk {

	private int tileID = -1;
	private int itemID = -1;
	private int otherID = -1;
	Point index;

	HashMap<Point, Tile> tiles = new HashMap<Point, Tile>();
	HashMap<Point, GroundItem> droppedItems = new HashMap<Point, GroundItem>();
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
				boolean isSolid = true;
				Tile tile = new Tile(TextureType.GRASS);
				tile.setPosition(position);

				Point tileIndex = new Point(x, y);

				Random r = new Random();
				float t = r.nextFloat();
				if (t < 0.5f) {
					tile.setType(TextureType.GRASS0);
				}

				t = r.nextFloat();
				if (t < 0.1f) {
					tile.setType(TextureType.DIRT);
				}

				if (x < 5 && x < 9 && y > 5 && y < 9) {
					//tile.setType(TextureType.SHALLOW_WATER);
					isSolid = false;
				}

				tiles.put(tileIndex, tile);

				Resource res = new Resource(TextureType.AIR);
				res.setPosition(position);

				if (x == 3 && y == 6) {
					//res.setType(TextureType.FISHING_SPOT);
				}

				if (isSolid) {

					t = r.nextFloat();
					if (t < 0.05f) {
						 res.setType(TextureType.TREE, 10);
					}
					t = r.nextFloat();
					if (t < 0.05f) {
						 res.setType(TextureType.BUSH, 10);
					}
					t = r.nextFloat();
					if (t < 0.1f) {
						 res.setType(TextureType.ROCK, 10);
					}

					t = r.nextFloat();
					if (t < 0.1f) {
						// res.setType(TextureType.TIN_ORE, 10);
					}
					t = r.nextFloat();
					if (t < 0.1f) {
						// res.setType(TextureType.COPPER_ORE, 10);
					}
				}
				objects.put(tileIndex, res);
				Entity ent = new Entity(TextureType.AIR);
				ent.setPosition(position);
				if (x == 12 && y == 7 && index.x == 1 && index.y == 0) {
					ent.setType(TextureType.CHARACTER);
				}
				entities.put(tileIndex, ent);
			}
		}
		// build();
	}

	public void build() {
		tileID = GL11.glGenLists(1);
		GL11.glNewList(tileID, GL11.GL_COMPILE);
		Renderer.bindTexture(ResourceDatabase.texture);
		GL11.glBegin(GL11.GL_QUADS);
		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {

				Vector2f position = getPosition(x, y);

				Point tempIndex = new Point(x, y);
				Tile tile = tiles.get(tempIndex);
				if (tile != null) {
					tile.setPosition(position);
					Renderer.renderTexture(tile.getType(), (int) tile.getPosition().x, (int) tile.getPosition().y);
				}
			}
		}
		GL11.glEnd();

		GL11.glEndList();

		itemID = GL11.glGenLists(1);
		GL11.glNewList(itemID, GL11.GL_COMPILE);
		Renderer.bindTexture(ResourceDatabase.uiTexture);
		GL11.glBegin(GL11.GL_QUADS);
		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {

				Vector2f position = getPosition(x, y);

				Point tempIndex = new Point(x, y);
				GroundItem tile = droppedItems.get(tempIndex);
				if (tile != null) {
					tile.setPosition(position);
					Renderer.renderUITexture(tile.type, (int) tile.getPosition().x + 16, (int) tile.getPosition().y - 6,
							32, 32);
				}
			}
		}
		GL11.glEnd();

		GL11.glEndList();

		otherID = GL11.glGenLists(1);
		GL11.glNewList(otherID, GL11.GL_COMPILE);
		Renderer.bindTexture(ResourceDatabase.texture);
		GL11.glBegin(GL11.GL_QUADS);
		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {

				Vector2f position = getPosition(x, y);

				Point tempIndex = new Point(x, y);

				Object obj = objects.get(tempIndex);
				if (obj != null) {
					obj.setPosition(position);
					Renderer.renderTexture(obj.getType(), (int) obj.getPosition().x, (int) obj.getPosition().y);
				}

				Entity ent = entities.get(tempIndex);
				if (ent != null) {
					ent.setPosition(position);
					Renderer.renderTexture(ent.getType(), (int) ent.getPosition().x, (int) ent.getPosition().y);
				}

			}
		}
		GL11.glEnd();

		GL11.glEndList();

	}

	public void update(boolean ticked) {
		if (ticked) {
			for (int x = 0; x < size.width; x++) {
				for (int y = 0; y < size.height; y++) {
					Point tempIndex = new Point(x, y);

					Object obj = objects.get(tempIndex);
					if (obj != null) {
						if (obj.getType() != TextureType.AIR) {
							if (obj instanceof Resource) {
								Resource res = (Resource) obj;
								if (res != null) {
									if(res.isAnimated())
									{
									ResourceData dat = GameDatabase.resources.get(res.getBaseType());
									if (dat != null) {
										res.cycleAnimation();
										TextureType newType = dat.animationTypes[res.getAnimationIndex()];
										
										res.setRawType(newType);
										build();
									}
									}
								}
							}
						}
					}
				}
			}

		}
	}
	public void renderTiles() {
		if (tileID == -1) {
			build();
		} else {
			GL11.glCallList(tileID);
		}
	}
	public void render() {
		if ( itemID == -1 || otherID == -1) {
			build();
		} else {
			
			GL11.glCallList(itemID);
			GL11.glCallList(otherID);
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

	public Tile getObjectAtIndex(Point point, boolean removeAIR) {
		return getObjectAtIndex(point, removeAIR, false);
	}

	public Tile getObjectAtIndex(Point point, boolean removeAIR, boolean removeTiles) {
		Tile newTile = null;

		Entity ent = entities.get(point);
		newTile = (ent != null && ((removeAIR && ent.getType() != TextureType.AIR) || (!removeAIR)) ? ent : null);

		Object obj = objects.get(point);
		newTile = (newTile == null || (newTile.getType() == TextureType.AIR)
				? (obj != null && ((removeAIR && obj.getType() != TextureType.AIR) || (!removeAIR)) ? obj : null)
				: newTile);

		GroundItem item = droppedItems.get(point);
		newTile = (newTile == null || (removeAIR && newTile.getType() == TextureType.AIR)
				? (item != null && ((removeAIR && item.getType() != TextureType.AIR) || (!removeAIR)) ? item : null)
				: newTile);

		// System.out.println("Dropped Item: " + newTile+"/"+
		// (item==null?"":item.getType()));
		if (!removeTiles) {
			Tile tile = tiles.get(point);
			newTile = (newTile == null || (newTile.getType() == TextureType.AIR)
					? (tile != null && ((removeAIR && tile.getType() != TextureType.AIR) || (!removeAIR)) ? tile : null)
					: newTile);
		}

		return newTile;
	}

	public GroundItem getItemAtIndex(Point point) {
		GroundItem newTile = null;

		GroundItem item = droppedItems.get(point);
		newTile = (item != null ? item : null);

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
