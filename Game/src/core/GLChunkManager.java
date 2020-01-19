package core;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Mouse;

import classes.GLIndex;
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

	public void setup() {
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < height; z++) {
				GLChunk chunk = new GLChunk(x, 0, z);
				chunk.setupChunk();
				chunks.put(new GLIndex(x, 0, z), chunk);
			}
		}
	}

	public void update() {
		mouseGLIndex.clear();
		totalRenderCount = 0;
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < height; z++) {
				GLChunk chunk = chunks.get(new GLIndex(x, 0, z));
				if (chunk != null) {
					chunk.update();
					if (chunk.inView(Main.view)) {
						chunk.setLevel(currentLevel);
					}
				}
			}
		}
		
		/*

		int mouseWheel = Mouse.getDWheel();
		if (mouseWheel < 0 && currentLevel < mapMaxHeight - 1) {
			currentLevel++;
		}

		if (mouseWheel > 0 && currentLevel > 0) {
			currentLevel--;
		}
		*/
	}

	public void render() {
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < height; z++) {
				GLChunk chunk = chunks.get(new GLIndex(x, 0, z));
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
	
	

	public void destroy() {

	}
}
