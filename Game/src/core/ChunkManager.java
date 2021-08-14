package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import game.Base;

public class ChunkManager {
	public static HashMap<Point, Chunk> chunks = new HashMap<Point, Chunk>();
	public static ArrayList<Chunk> chunksInView = new ArrayList<Chunk>();

	static Point chunkDim = new Point(3, 3);
	public static Point viewRange = new Point(3, 3);

	public void setup() {
		for (int x = 0; x < chunkDim.x; x++) {
			for (int y = 0; y < chunkDim.y; y++) {
				Chunk chunk = new Chunk(x, y);
				chunk.setup();
				chunk.build();
				chunks.put(new Point(x, y), chunk);
			}
		}
	}

	public static Point getIndexByType(TextureType type) {
		Point index = null;

		for (Chunk chunk : chunksInView) {
			if (chunk != null) {
				index = chunk.getIndexByType(type);
			}
			if (index != null) {
				break;
			}
		}
		if (index == null) {

			for (int x = 0; x < chunkDim.x; x++) {
				for (int y = 0; y < chunkDim.y; y++) {
					Chunk chunk = chunks.get(new Point(x, y));
					if (chunk != null) {
						index = chunk.getIndexByType(type);
					}
					if (index != null) {
						break;
					}
				}
				if (index != null) {
					break;
				}
			}
		}
		return index;
	}

	public static void dropItem(GroundItem item) {
		Point test = ChunkManager.getIndexByType(TextureType.CHARACTER);
		if (test != null) {
			int chunkX = (int) (test.x / Chunk.size.width);
			int chunkY = (int) (test.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (test.x % 16);
				int objY = (int) (test.y % 16);

				chunk.droppedItems.put(new Point(objX, objY), item);
				chunk.build();
			}
		}
	}

	public static Point findIndexAroundIndex(Point index) {
		Point newIndex = null;
		if (index != null) {
			System.out.println("check..." + index);
			for (int x = -1; x < 2; x++) {
				for (int y = -1; y < 2; y++) {
					Point tempIndex = new Point(index.x + x, index.y + y);
					TextureType type = getTypeByIndexWithTiles(tempIndex);
					if (type != null) {
						System.out.println("looking..." + tempIndex + "->" + type);
						if (type == TextureType.AIR || type == TextureType.GRASS || type == TextureType.GRASS0) {
							newIndex = tempIndex;
							break;
						}
					}
				}
			}
		}
		return newIndex;
	}

	public static TextureType getTypeByIndexWithTiles(Point index) {
		TextureType type = null;
		/*
		 * for (int x = 0; x < 2; x++) { for (int y = 0; y < 2; y++) { Chunk chunk =
		 * chunks.get(new Point(x, y)); if (chunk != null) { Tile tile =
		 * chunk.getObjectAtIndex(index, true,false); if (tile != null) { type =
		 * tile.getType(); } } } }
		 */
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Tile tile = chunk.getObjectAtIndex(new Point(objX, objY), true, false);
				if (tile != null) {
					type = tile.getType();
				}
			}
		}
		return type;
	}

	public static TextureType getTypeByIndex(Point index) {
		TextureType type = null;
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				Chunk chunk = chunks.get(new Point(x, y));
				if (chunk != null) {
					Tile tile = chunk.getObjectAtIndex(index, true, true);
					if (tile != null) {
						type = tile.getType();
					}
				}
			}
		}
		return type;
	}

	public void update() {
		chunksInView.clear();
		Point center = Base.playerIndex;
		//System.out.println("Key: " + center);
		if (center != null) {
			int chunkX = (int) (center.x / Chunk.size.width);
			int chunkY = (int) (center.y / Chunk.size.height);
			for (int x = (chunkX - viewRange.x); x < (chunkX + viewRange.x); x++) {
				for (int y = (chunkY - viewRange.y); y < (chunkY + viewRange.y); y++) {
					Point key = new Point(x, y);
					Chunk chunk = chunks.get(key);
					if (chunk != null) {
						chunksInView.add(chunk);
					}
				}
			}
		}
	}

	public void render() {
		for (Chunk chunk : chunksInView) {
			chunk.render();
		}
	}

	public static LinkedList<Point> chunksToBuild = new LinkedList<Point>();

	public static void move(Point index, Point newIndex) {
		if (index != null && newIndex != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Entity ent = chunk.entities.remove(new Point(objX, objY));
				if (ent != null) {
					int newChunkX = (int) (newIndex.x / Chunk.size.width);
					int newChunkY = (int) (newIndex.y / Chunk.size.height);
					Chunk newChunk = ChunkManager.chunks.get(new Point(newChunkX, newChunkY));
					if (newChunk != null) {
						int newObjX = (int) (newIndex.x % 16);
						int newObjY = (int) (newIndex.y % 16);
						newChunk.entities.put(new Point(newObjX, newObjY), ent);

						// chunksToBuild.add(new Point(newChunkX, newChunkY));
						newChunk.build();
					}
					// chunksToBuild.add(new Point(chunkX, chunkY));
					chunk.build();
				}
			}
		}
	}

	public static void setItemAtIndex(Point index, TextureType type) {
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Object obj = chunk.objects.get(new Point(objX, objY));
				if (obj != null) {
					chunk.droppedItems.put(new Point(objX, objY), new GroundItem(type));
					chunk.build();

				}
			}
		}
	}

	public static void setObjectAtIndex(Point index, TextureType type) {
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Object obj = chunk.objects.get(new Point(objX, objY));
				if (obj != null) {
					chunk.objects.put(new Point(objX, objY), new Object(type));
					chunk.build();

				}
			}
		}
	}

	public static void removeType(TextureType type) {
		for (int x = 0; x < chunkDim.x; x++) {
			for (int y = 0; y < chunkDim.y; y++) {
				Chunk chunk = chunks.get(new Point(x, y));
				if (chunk != null) {
					chunk.removeType(type);
				}
			}
		}
	}

	public static boolean isResource(Point index) {
		boolean isRes = false;
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Tile tile = chunk.getObjectAtIndex(new Point(objX, objY), true);
				if (tile != null) {
					isRes = (tile instanceof Resource);
				}
			}
		}

		return isRes;
	}

	public static boolean isItem(Point index) {
		boolean isItem = false;
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Tile tile = chunk.getObjectAtIndex(new Point(objX, objY), true);
				if (tile != null) {
					isItem = (tile instanceof GroundItem);
				}
			}
		}

		return isItem;
	}

	public static GroundItem getItem(Point index) {
		GroundItem item = null;
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				item = chunk.getItemAtIndex(new Point(objX, objY));
			}
		}

		return item;
	}

	public static Tile getTile(Point index) {
		
		Tile tile = null;
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				tile = chunk.getObjectAtIndex(new Point(objX, objY), true);
			}
		}

		return tile;
	}

	public static Resource getResource(Point index) {
		Resource res = null;
		if (index != null) {
			int chunkX = (int) (index.x / Chunk.size.width);
			int chunkY = (int) (index.y / Chunk.size.height);
			Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
			if (chunk != null) {
				int objX = (int) (index.x % 16);
				int objY = (int) (index.y % 16);
				Tile tile = chunk.getObjectAtIndex(new Point(objX, objY), true);
				if (tile != null) {
					if (tile instanceof Resource) {
						res = (Resource) tile;
					} else if (tile instanceof GroundItem) {

					}
				}
			}
		}

		return res;
	}
}
