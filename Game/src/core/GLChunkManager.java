package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import classes.GLIndex;
import classes.GLObject;
import classes.GLSize;
import game.Main;

public class GLChunkManager {
	// store the chunks in an index based hash map for easy retrieval
	public static HashMap<GLIndex, GLChunk> chunks = new HashMap<GLIndex, GLChunk>();
	// the total render count for the screen
	public static int totalRenderCount = 0;
	// the list of all hovered objects in the game
	public static ArrayList<GLIndex> mouseGLIndex = new ArrayList<GLIndex>();
	// the current level for the game
	public static int currentLevel = 0;

	public static int previousLevel = -1;
	// the max map height
	public static int mapMaxHeight = 16;

	public static int width = 1;
	public static int height = 1;

	public static Vector3f chunkSize = new Vector3f(16, 2, 16);

	public void setup() {
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < height; z++) {

				GLChunk chunk = new GLChunk(x, 0, z);
				chunk.setSize(chunkSize);
				chunk.setupChunk();

				System.out.println("test: " + chunk.index.x + "," + chunk.index.y + "," + chunk.index.z + ","
						+ (chunk.index.chunkX * 2) + "," + chunk.index.chunkY + "," + chunk.index.chunkZ + " = "
						+ (chunk.index.x + chunk.index.y + chunk.index.z + (chunk.index.chunkX * 2) + chunk.index.chunkY
								+ chunk.index.chunkZ));
				System.out.println("index: " + x + "," + z + "=" + chunk.index.hashCode());

				chunks.put(chunk.index, chunk);
			}
		}
		for (GLIndex key : chunks.keySet()) {
			System.out.println("index2: " + key.chunkX + "," + key.chunkZ);
		}

	}

	public void update() {
		mouseGLIndex.clear();
		totalRenderCount = 0;
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < height; z++) {
				GLChunk chunk = chunks.get(new GLIndex(0, 0, 0, x, 0, z));
				if (chunk != null) {
					chunk.update();
					if (chunk.inView(Main.view)) {
						chunk.setLevel(currentLevel);
					}
				}
			}
		}

		/*
		 * 
		 * int mouseWheel = Mouse.getDWheel(); if (mouseWheel < 0 && currentLevel <
		 * mapMaxHeight - 1) { currentLevel++; }
		 * 
		 * if (mouseWheel > 0 && currentLevel > 0) { currentLevel--; }
		 */
	}

	public void render() {
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < height; z++) {
				GLChunk chunk = chunks.get(new GLIndex(0, 0, 0, x, 0, z));
				if (chunk != null) {

					if (chunk.inView(Main.view)) {
						if (!chunk.isEmpty()) {

							chunk.render();
							totalRenderCount += chunk.renderCount;
						}
					}
				}
			}
		}
	}

	public static GLObject getObjectForPathFinding(Point objectIndex) {
		GLObject getObj = null;

		int cx = (int) Math.floor(objectIndex.x / chunkSize.x);
		int cy = (int) Math.floor(objectIndex.y / chunkSize.z);
		

		int nx = (int) (objectIndex.x % chunkSize.x);
		int ny = (int) (objectIndex.y % chunkSize.z);

		GLChunk chunk = GLChunkManager.chunks.get(new GLIndex(0, 0, 0, cx, 0, cy));
		if (chunk != null) {
			GLObject obj = chunk.objects.get(new GLIndex(nx, 0, ny, cx, 0, cy));
			if (obj != null) {
				getObj = obj;
			}
		}
		return getObj;
	}

	public void destroy() {

	}
}
