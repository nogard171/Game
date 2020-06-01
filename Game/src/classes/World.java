package classes;

import data.WorldData;
import utils.View;
import utils.WorldGenerator;

public class World {

	public Index playerIndex;

	public void setup() {
		WorldGenerator.generate();
		for (int x = 0; x < 2; x++) {
			for (int z = 0; z < 2; z++) {
				Chunk chunk = new Chunk(x, z);
				chunk.setup();
				WorldData.chunks.put(chunk.index.getString(), chunk);
			}
		}
	}

	public void updateRenderedChunks() {
		for (int x = WorldGenerator.chunkIndex.getX() - WorldGenerator.chunkRenderSize.getWidth()
				+ 1; x < WorldGenerator.chunkIndex.getX() + WorldGenerator.chunkRenderSize.getWidth(); x++) {
			for (int z = WorldGenerator.chunkIndex.getY() - WorldGenerator.chunkRenderSize.getHeight()
					+ 1; z < WorldGenerator.chunkIndex.getY() + WorldGenerator.chunkRenderSize.getHeight(); z++) {

			}
		}
	}

	public void update() {
		for (Chunk chunk : WorldData.chunks.values()) {
			chunk.update();
		}

		if (View.moved) {
			WorldGenerator.generate();
		}
	}

	public void render() {
		/*
		 * for (Chunk chunk : WorldData.chunks.values()) { chunk.render(); }
		 */

		for (int x = WorldGenerator.chunkIndex.getX()
				- WorldGenerator.chunkRenderSize.getWidth(); x < WorldGenerator.chunkIndex.getX()
						+ WorldGenerator.chunkRenderSize.getWidth(); x++) {
			for (int z = WorldGenerator.chunkIndex.getY()
					- WorldGenerator.chunkRenderSize.getHeight(); z < WorldGenerator.chunkIndex.getY()
							+ WorldGenerator.chunkRenderSize.getHeight(); z++) {
				Chunk chunk = WorldData.chunks.get(x + "," + z);
				if (chunk != null) {
					chunk.render();
				}
			}
		}
	}

	public void destroy() {

	}

}
