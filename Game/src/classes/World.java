package classes;

import data.WorldData;

public class World {

	public void setup() {
		for (int x = 0; x < 2; x++) {
			for (int z = 0; z < 2; z++) {
				Chunk chunk = new Chunk(x, z);
				chunk.setup();
				WorldData.chunks.put(chunk.index.getString(), chunk);
			}
		}
	}

	public void update() {
		for (Chunk chunk : WorldData.chunks.values()) {
			chunk.update();
		}
	}

	public void render() {
		for (Chunk chunk : WorldData.chunks.values()) {
			chunk.render();
		}
	}

	public void destroy() {

	}
}
