package engine;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import game.Base;

public class ChunkManager {

	public static Point size = new Point(16, 16);
	public static LinkedHashMap<Point, Chunk> chunks = new LinkedHashMap<Point, Chunk>();

	public static ArrayList<Chunk> chunksInView = new ArrayList<Chunk>();

	public static int layer = 0;

	public void setup() {

		for (int x = 0; x < 5; x++) {
			for (int y = 0; y < 1; y++) {
				Point index = new Point(x, y);
				Chunk chunk = new Chunk(index);
				chunk.load();
				chunks.put(index, chunk);
			}
		}

	}

	public void update() {
		chunksInView.clear();
		for (Chunk chunk : chunks.values()) {
			if (Base.view.getRect().intersects(chunk.chunkBounds)) {
				chunksInView.add(chunk);
			}
			chunk.update();
		}
	}

	public void render() {

		for (Chunk chunk : chunksInView) {
			chunk.render();
		}
	}

	public void destroy() {

	}

}
