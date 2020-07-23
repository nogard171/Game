package utils;

import classes.Chunk;
import classes.Index;
import classes.Size;
import data.WorldData;
import ui.MouseIndex;

public class WorldGenerator {
	public static Index centerIndex;
	public static Index chunkIndex;
	public static Size chunkRenderSize = new Size(3, 3);

	public static void generate() {
		pollCenterIndex();
		checkSurroundingIndexes();
	}

	public static void checkSurroundingIndexes() {
		for (int x = chunkIndex.getX() - chunkRenderSize.getWidth() + 1; x < chunkIndex.getX()
				+ chunkRenderSize.getWidth(); x++) {
			for (int z = chunkIndex.getY() - chunkRenderSize.getHeight() + 1; z < chunkIndex.getY()
					+ chunkRenderSize.getHeight(); z++) {
				if (!WorldData.chunks.containsKey(x + "," + z) && x >= 0 && z >= 0) {
					generateChunk(x, z);
				}
			}
		}
	}

	public static void generateChunk(int x, int z) {
		Chunk newChunk = new Chunk(x, z);
		newChunk.setup();
		WorldData.chunks.put(newChunk.getIndex().getString(), newChunk);
	}

	private static void pollCenterIndex() {
		int cartX = Window.width / 2 - View.x;
		int cartY = Window.height / 2 - View.y;
		int isoX = cartX / 2 + (cartY);
		int isoY = cartY - cartX / 2;
		int indexX = (int) Math.floor((float) isoX / (float) 32);
		int indexY = (int) Math.floor((float) isoY / (float) 32);

		int chunkX = (int) Math.floor(indexX / 16);
		int chunkY = (int) Math.floor(indexY / 16);
		if (indexX < 0) {
			chunkX--;
		}
		if (indexY < 0) {
			chunkY--;
		}
		chunkIndex = new Index(chunkX, chunkY);
		centerIndex = new Index(indexX, indexY);
	}
}
