package data;

import classes.Size;
import utils.WorldGenerator;

import java.util.HashMap;

import org.newdawn.slick.TrueTypeFont;

import classes.Chunk;
import classes.Index;
import classes.Object;

public class WorldData {
	public static HashMap<String, Chunk> chunks = new HashMap<String, Chunk>();
	public static HashMap<Integer, TrueTypeFont> fonts = new HashMap<Integer, TrueTypeFont>();

	public static Chunk getChunk(int x, int z) {
		Chunk chunk = null;

		if (x > WorldGenerator.chunkIndex.getX() - (WorldGenerator.chunkRenderSize.getWidth() * 2)
				&& x < WorldGenerator.chunkIndex.getX() + (WorldGenerator.chunkRenderSize.getWidth() * 2)
				&& z > WorldGenerator.chunkIndex.getY() - (WorldGenerator.chunkRenderSize.getHeight() * 2)
				&& z < WorldGenerator.chunkIndex.getY() + (WorldGenerator.chunkRenderSize.getHeight() * 2)) {
			chunk = WorldData.chunks.get(x + "," + z);
		}

		return chunk;
	}
}
