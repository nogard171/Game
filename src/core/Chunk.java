package core;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import ui.UITextureType;
import utils.Ticker;

public class Chunk {
	public int[][] heightMap;
	public int[][] treeMap;
	private int tileID = -1;
	private int itemID = -1;
	private int otherID = -1;
	Point index;

	public HashMap<Point, Tile> tiles = new HashMap<Point, Tile>();
	public HashMap<Point, Tile> pathMap = new HashMap<Point, Tile>();
	public HashMap<Point, GroundItem> droppedItems = new HashMap<Point, GroundItem>();
	HashMap<Point, LinkedList<GroundItem>> newdroppedItems = new HashMap<Point, LinkedList<GroundItem>>();
	public HashMap<Point, Object> objects = new HashMap<Point, Object>();

	public ArrayList<Object> animatedObjects = new ArrayList<Object>();
	public HashMap<Point, Entity> entities = new HashMap<Point, Entity>();
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
			for (int o = 0; o < animatedObjects.size(); o++) {
				Object obj = animatedObjects.get(o);
				if (obj != null) {
					if (obj.getType() == type) {
						tempIndex = obj.getIndex();
						break;
					}
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

	
	Random r = new Random();

	public void setup() {
		heightMap = Generator.generateHeightMap(index, size.width, size.height, 20);

		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {
				int t = heightMap[x][y];// r.nextFloat();
				Vector2f position = getPosition(x, y);
				boolean isSolid = true;
				Tile tile = new Tile(TextureType.DIRT);
				tile.setPosition(position);

				Point tileIndex = new Point(x, y);
				tile.setIndex(tileIndex);

				
				//t = 3;

				float g = 0.5f;// r.nextFloat();
				if (g < 0.5f) {
					tile.setType(TextureType.GRASS0);
				} else if (g >= 0.5f) {
					tile.setType(TextureType.GRASS);
				}
				g = 1;// r.nextFloat();
				if (g < 0.2f || (x % 2 == 0 && y % 2 == 0)) {
					tile.setType(TextureType.DIRT);
				}
				if (t <= 2) {
					tile.setType(TextureType.SAND);
				}
				if (t < 2) {
					tile.setType(TextureType.SHALLOW_WATER);
					isSolid = false;

				}
				if (t < -5) {
					tile.setType(TextureType.DEEP_WATER);
					isSolid = false;
				}

				tiles.put(tileIndex, tile);

				Resource res = new Resource(TextureType.AIR);
				
				res.setPosition(position);

				if (isSolid) {
					t = 0 + (int) (Math.random() * ((10 - 0) + 1));
					if (t == 1 && (tile.getType() == TextureType.GRASS || tile.getType() == TextureType.GRASS0)) {

						float tr = r.nextFloat();
						if (tr >= 0.5f) {
							res.setType(TextureType.TREE, 10);
						}
						if (tr < 0.5f) {
							res.setType(TextureType.MAPLE_TREE, 10);
						}
					}
					t = 0 + (int) (Math.random() * ((10 - 0) + 1));
					if (t == 1 && (tile.getType() == TextureType.GRASS || tile.getType() == TextureType.GRASS0)
							&& res.getType().equals(TextureType.AIR)) {
						res.setType(TextureType.BUSH, 10);
					}

					if ((tile.getType() == TextureType.DIRT)) {
						t = 0 + (int) (Math.random() * ((10 - 0) + 1));
						if (t == 1 && res.getType().equals(TextureType.AIR)) {
							res.setType(TextureType.ROCK_ORE, 10);
						}

						t = 0 + (int) (Math.random() * ((50 - 0) + 1));
						if (t == 1 && res.getType().equals(TextureType.AIR)) {
							res.setType(TextureType.TIN_ORE, 10);
						}
						t = 0 + (int) (Math.random() * ((50 - 0) + 1));
						if (t == 1 && res.getType().equals(TextureType.AIR)) {
							res.setType(TextureType.COPPER_ORE, 10);
						}
						t = 0 + (int) (Math.random() * ((100 - 0) + 1));
						if (t == 1 && res.getType().equals(TextureType.AIR)) {
							res.setType(TextureType.COAL_ORE, 10);
						}
					}
					if (res.getBaseType().equals(TextureType.AIR) && res.getType().equals(TextureType.AIR)) {
						t = 0 + (int) (Math.random() * ((20 - 0) + 1));
						if (t == 1) {
							GroundItem droppedItem = new GroundItem(TextureType.ITEM);
							droppedItem.count = 1;
							droppedItem.item = ItemType.ROCK;
							droppedItem.type = UITextureType.ROCK_ITEM;
							this.droppedItems.put(tileIndex, droppedItem);
						}
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
	}

	public void addFishingSpots() {

		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {
				Point tileIndex = new Point(x, y);

				Tile tile = tiles.get(tileIndex);
				if (tile.getType() == TextureType.SHALLOW_WATER) {
					float g = r.nextFloat();
					boolean beach = ChunkManager.checkSurroundingTilesFor(
							new Point(tileIndex.x + (index.x * size.width), tileIndex.y + (index.y * size.height)),
							TextureType.SAND);

					if (g < 0.3f && beach) {
						Vector2f position = getPosition(x, y);
						Resource animatedRes = new Resource(TextureType.AIR);
						animatedRes.setIndex(tileIndex);
						animatedRes.setPosition(position);
						animatedRes.setType(TextureType.FISHING_SPOT);
						animatedRes.setAnimated(true);
						animatedObjects.add(animatedRes);
					}
				}
			}
		}
	}

	public void build() {
		tileID = GL11.glGenLists(1);
		GL11.glNewList(tileID, GL11.GL_COMPILE);
		Renderer.bindTexture(ResourceDatabase.texture);
		GL11.glBegin(GL11.GL_QUADS);
		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {
				Point tempIndex = new Point(x, y);
				Tile tile = tiles.get(tempIndex);
				if (tile != null) {
					Renderer.renderTexture(tile.getType(), (int) tile.getPosition().x, (int) tile.getPosition().y);
				}
				Tile pathTile = pathMap.get(tempIndex);
				if (pathTile != null) {
					Vector2f temp = pathTile.getPosition();
					if(temp!=null)
					{
						Renderer.renderTexture(pathTile.getType(), (int) temp.x, (int) temp.y);						
					}
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
					Renderer.renderUITexture(tile.type, (int) tile.getPosition().x + 16, (int) tile.getPosition().y - 4,
							32, 16);
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

	int animationID = -1;

	public void buildAnimations() {
		for (int i = 0; i < animatedObjects.size(); i++) {
			Object obj = animatedObjects.get(i);
			if (obj != null) {
				// obj.setPosition(position);
				if (obj instanceof Resource) {
					Resource res = (Resource) obj;
					if (res != null) {
						ResourceData dat = GameDatabase.resources.get(res.getBaseType());
						if (dat != null) {
							int tempID = -1;
							for (int a = 0; a < res.animatedID.length; a++) {
								tempID = GL11.glGenLists(1);
								GL11.glNewList(tempID, GL11.GL_COMPILE);
								GL11.glBegin(GL11.GL_QUADS);
								TextureType newType = dat.animationTypes[a];
								Renderer.renderTexture(newType, (int) obj.getPosition().x, (int) obj.getPosition().y);
								GL11.glEnd();
								GL11.glEndList();
								res.animatedID[a] = tempID;
							}
						}

					}
				}
			}
		}
	}

	public void update(boolean ticked) {
		if (ticked) {
			for (int i = 0; i < animatedObjects.size(); i++) {
				Object obj = animatedObjects.get(i);
				if (obj != null) {
					if (obj instanceof Resource) {
						Resource res = (Resource) obj;
						if (res != null) {
							if (res.isAnimated()) {
								ResourceData dat = GameDatabase.resources.get(res.getBaseType());
								if (dat != null) {
									res.cycleAnimation();
									TextureType newType = dat.animationTypes[res.getAnimationIndex()];
									res.setRawType(newType);
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

	boolean isResourcesSetup = false;

	public void render() {
		if (!isResourcesSetup) {
			addFishingSpots();
			buildAnimations();
			isResourcesSetup = true;
		}

		if (itemID == -1 || otherID == -1) {
			build();
		} else {

			for (int i = 0; i < animatedObjects.size(); i++) {
				Object obj = animatedObjects.get(i);
				if (obj != null) {
					if (obj instanceof Resource) {
						Resource res = (Resource) obj;
						if (res != null) {
							if (res.isAnimated()) {
								GL11.glCallList(res.getAnimatedID());
							}
						}
					}
				}
			}

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
		return getObjectAtIndex(point, removeAIR, true);
	}

	public Tile getObjectAtIndex(Point point, boolean removeAIR, boolean removeTiles) {
		Tile newTile = null;

		Entity ent = entities.get(point);
		newTile = (ent != null && ((removeAIR && ent.getType() != TextureType.AIR) || (!removeAIR)) ? ent : null);

		Object obj = null;
		if (newTile == null || (newTile.getType() == TextureType.AIR)) {
			for (int o = 0; o < animatedObjects.size(); o++) {
				obj = animatedObjects.get(o);
				if (obj != null) {
					if (obj.getIndex().equals(point)) {
						if (obj != null && ((removeAIR && obj.getType() != TextureType.AIR) || (!removeAIR))) {
							newTile = obj;
							break;
						}
					}
				}
			}
		}

		obj = objects.get(point);
		newTile = (newTile == null || (newTile.getType() == TextureType.AIR)
				? (obj != null && ((removeAIR && obj.getType() != TextureType.AIR) || (!removeAIR)) ? obj : null)
				: newTile);

		GroundItem item = droppedItems.get(point);
		newTile = (newTile == null || (removeAIR && newTile.getType() == TextureType.AIR)
				? (item != null && ((removeAIR && item.type != UITextureType.BLANK) || (!removeAIR)) ? item : null)
				: newTile);
		if (!removeTiles) {
			Tile tile = tiles.get(point);
			newTile = (newTile == null || (newTile.getType() == TextureType.AIR)
					? (tile != null && ((removeAIR && tile.getType() != TextureType.AIR) || (!removeAIR)) ? tile : null)
					: newTile);
		}
		// System.out.println("Dropped Item1: " + newTile);

		return newTile;
	}

	public LinkedList<Tile> getObjectsAtIndex(Point point) {
		return getObjectsAtIndex(point, false, true);
	}

	public LinkedList<Tile> getObjectsAtIndex(Point point, boolean removeAIR) {
		return getObjectsAtIndex(point, removeAIR, false);
	}

	public LinkedList<Tile> getObjectsAtIndex(Point point, boolean removeAIR, boolean removeTiles) {
		LinkedList<Tile> newTiles = new LinkedList<Tile>();

		Entity ent = entities.get(point);
		if (ent != null) {
			if (!ent.getType().equals(TextureType.AIR)) {
				newTiles.add(ent);
			}
		}

		Object animObj = null;
		for (int o = 0; o < animatedObjects.size(); o++) {
			animObj = animatedObjects.get(o);
			if (animObj != null) {
				if (animObj.getIndex().equals(point)) {
					if (animObj != null && ((removeAIR && animObj.getType() != TextureType.AIR) || (!removeAIR))) {
						newTiles.add(animObj);
						break;
					}
				}
			}
		}

		Tile obj = objects.get(point);
		if (obj != null) {
			if (!obj.getType().equals(TextureType.AIR)) {
				newTiles.add(obj);
			}
		}

		GroundItem item = droppedItems.get(point);
		if (item != null) {
			if (!item.getType().equals(TextureType.AIR)) {
				newTiles.add(item);
			}
		}

		if (!removeTiles) {
			Tile tile = tiles.get(point);
			if (tile != null) {
				if (!tile.getType().equals(TextureType.AIR)) {
					newTiles.add(tile);
				}
			}
		}
		return newTiles;
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
