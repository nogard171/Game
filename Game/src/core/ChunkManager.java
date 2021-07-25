package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

public class ChunkManager {
	public static HashMap<Point, Chunk> chunks = new HashMap<Point, Chunk>();
	public static ArrayList<Chunk> chunksInView = new ArrayList<Chunk>();

	public void setup() {
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				Chunk chunk = new Chunk(x, y);
				chunk.setup();
				chunks.put(new Point(x, y), chunk);
			}
		}
	}

	public static Point getIndexByType(TextureType type) {
		Point index = null;
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
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
		return index;
	}

	public static TextureType getTypeByIndexWithTiles(Point index) {
		TextureType type = null;
		/*
		 * for (int x = 0; x < 2; x++) { for (int y = 0; y < 2; y++) { Chunk chunk =
		 * chunks.get(new Point(x, y)); if (chunk != null) { Tile tile =
		 * chunk.getObjectAtIndex(index, true,false); if (tile != null) { type =
		 * tile.getType(); } } } }
		 */

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

	}

	public void render() {
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				Chunk chunk = chunks.get(new Point(x, y));
				if (chunk != null) {
					chunk.render();
				}
			}
		}
	}

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
						newChunk.build();
					}

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
					if (obj.getType() == TextureType.AIR) {
						chunk.objects.put(new Point(objX, objY), new Object(type));
						chunk.build();
					}
				}
			}
		}
	}

	public static void removeType(TextureType type) {
		for (int x = 0; x < 2; x++) {
			for (int y = 0; y < 2; y++) {
				Chunk chunk = chunks.get(new Point(x, y));
				if (chunk != null) {
					chunk.removeType(type);
				}
			}
		}
	}
}
