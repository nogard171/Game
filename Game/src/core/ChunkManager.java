package core;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import game.Base;

public class ChunkManager {

	// Chunk chunk;
	// Chunk chunk2;
	LinkedHashMap<Vector3f, Chunk> chunks = new LinkedHashMap<Vector3f, Chunk>();

	int layer = 0;

	public void setup() {
		Chunk chunk = new Chunk(new Vector3f(0, 0, 0));
		chunk.load();
		chunks.put(chunk.getIndex(), chunk);

		Chunk chunk2 = new Chunk(new Vector3f(1, 0, 0));
		chunk2.load();
		chunks.put(chunk2.getIndex(), chunk2);

		System.out.println("test:" + chunks.size());
	}

	public void update() {
		int mouseWheel = Mouse.getDWheel();
		if (mouseWheel > 0) {
			if (layer >= 0) {
				layer--;
			}
		} else if (mouseWheel < 0) {
			if (layer <= 15) {
				layer++;
			}
		}
		// chunk.setLayer(layer);
		// chunk.update();

		// chunk2.setLayer(layer);
		// chunk2.update();
		Base.hoveredObjects.clear();
		for (Chunk chunk : chunks.values()) {

			chunk.setLayer(layer);
			chunk.update();

			if (chunk.chunkBounds.contains(Base.mousePosition)) {
				chunk.handleHover();
			}
		}
	}

	public void render() {
		// chunk.render();

		// chunk2.render();
		for (Chunk chunk : chunks.values()) {
			chunk.render();
		}
	}

	public void destroy() {

	}
}
