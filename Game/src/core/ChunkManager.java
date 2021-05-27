package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import game.Base;

public class ChunkManager {

	public static Vector3f size = new Vector3f(100, 16, 100);
	public static LinkedHashMap<String, Chunk> chunks = new LinkedHashMap<String, Chunk>();

	public static ArrayList<Chunk> chunksInView = new ArrayList<Chunk>();

	int layer = 0;
	APathFinder pathFinder;

	public void setup() {
		pathFinder = new APathFinder();

		for (int x = 0; x < 1; x++) {
			for (int z = 0; z < 1; z++) {
				Chunk chunk = new Chunk(new Vector3f(x, 0, z));
				chunk.load();
				chunks.put(chunk.getIndex(), chunk);
			}
		}
	}

	int leftCount = 0;

	public void update() {
		int mouseWheel = Mouse.getDWheel();
		if (mouseWheel > 0) {
			if (layer >= 0) {
				layer--;
			}
		} else if (mouseWheel < 0) {
			if (layer <= size.y - 2) {
				layer++;
			}
		}
		Base.hoveredObjects.clear();
		chunksInView.clear();
		for (Chunk chunk : chunks.values()) {

			chunk.setLayer(layer);
			chunk.update();

			if (chunk.chunkBounds.contains(Base.mousePosition)) {
				chunk.handleHover();
			}
			if (chunk.chunkBounds.intersects(Base.view)) {
				chunksInView.add(chunk);
			}
		}
		if (Base.hoveredObjects.size() > 0 && Mouse.isButtonDown(0) && leftCount == 0) {
			System.out.println("end: " + (int) Base.hoveredObjects.get(0).getIndex().x + ","
					+ (int) Base.hoveredObjects.get(0).getIndex().y + ","
					+ (int) Base.hoveredObjects.get(0).getIndex().z);

			List test = pathFinder.find(new ANode(0, 0, 0),
					new ANode((int) Base.hoveredObjects.get(0).getIndex().x, (int) 0, // Base.hoveredObjects.get(0).getIndex().y,
							(int) Base.hoveredObjects.get(0).getIndex().z));
			if (test != null) {
				Chunk chunk = ChunkManager.chunks.get("0,0,0");
				if (chunk != null) {
					for (int i = 0; i < test.size(); i++) {
						ANode pIndex = (ANode) test.get(i);
						if (pIndex != null) {

							Object obj = chunk.objects
									.get((int) pIndex.x + "," + (int) pIndex.y + "," + (int) pIndex.z);

							if (obj != null) {

								obj.setSprite("dirt");
							}
						}
					}
					chunk.build();
				}
				System.out.println("LIST: " + test);
				if (test != null) {
					System.out.println("Count: " + test.size());
				}
			}
			leftCount++;
		} else if (!Mouse.isButtonDown(0)) {
			leftCount = 0;
		}
	}

	public void render() {
		for (Chunk chunk : chunksInView) {
			chunk.render();
		}
	}

	public void destroy() {

	}

	public static int getRenderCount() {
		int count = 0;
		for (Chunk chunk : chunksInView) {
			count += chunk.renderedObjects.size();
		}
		return count;
	}
}
