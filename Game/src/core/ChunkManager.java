package core;

import java.awt.Point;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

public class ChunkManager {
	public static HashMap<Point, Chunk> chunks = new HashMap<Point, Chunk>();

	public void setup() {
		for (int x = 0; x < 1; x++) {
			for (int y = 0; y <1; y++) {
				Chunk chunk = new Chunk(x, y);
				chunk.setup();
				chunks.put(new Point(x, y), chunk);
			}
		}
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
}
