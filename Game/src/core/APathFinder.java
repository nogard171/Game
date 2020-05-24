package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class APathFinder {
	HashMap<Vector3f, Vector3f> parentList = new HashMap<Vector3f, Vector3f>();
	ArrayList<Vector3f> indexes = new ArrayList<Vector3f>();

	public APathFinder() {
		indexes.add(new Vector3f(-1, 0, 0));
		indexes.add(new Vector3f(1, 0, 0));
		indexes.add(new Vector3f(0, 0, -1));
		indexes.add(new Vector3f(0, 0, 1));

		indexes.add(new Vector3f(-1, 1, 0));
		indexes.add(new Vector3f(1, 1, 0));
		indexes.add(new Vector3f(0, 1, -1));
		indexes.add(new Vector3f(0, 1, 1));

		indexes.add(new Vector3f(-1, -1, 0));
		indexes.add(new Vector3f(1, -1, 0));
		indexes.add(new Vector3f(0, -1, -1));
		indexes.add(new Vector3f(0, -1, 1));
	}

	protected List constructPath(Vector3f index) {
		LinkedList path = new LinkedList();
		System.out.println("obj:" + index);
		Chunk chunk = ChunkManager.chunks.get("0,0,0");
		if (chunk != null) {
			for (Vector3f pIndex : parentList.values()) {
				if (pIndex != null) {

					Object obj = chunk.objects.get((int) pIndex.x + "," + (int) pIndex.y + "," + (int) pIndex.z);

					if (obj != null && pIndex.y == 1) {

						obj.setSprite("dirt");
					}
				}
			}
			chunk.build();
		}
		while (parentList.get(index) != null) {

			path.addFirst(index);
			index = parentList.get(index);
		}
		parentList.clear();
		return path;
	}

	public List find(Vector3f startIndex, Vector3f endIndex) {
		LinkedList openList = new LinkedList();
		LinkedList closedList = new LinkedList();
		openList.add(startIndex);
		parentList.put(startIndex, null);

		while (!openList.isEmpty()) {
			Vector3f current = (Vector3f) openList.removeFirst();
			if (current.equals(endIndex)) {
				return constructPath(endIndex);
			} else {
				closedList.add(current);
			}
			if (current.x > ChunkManager.size.x && current.y > ChunkManager.size.y) {
				return null;
			}
			for (Vector3f index : indexes) {
				Vector3f neighborIndex = new Vector3f(current.x + index.x, current.y + index.y, current.z + index.z);

				int chunkX = (int) (neighborIndex.x / ChunkManager.size.x);
				int chunkY = (int) (neighborIndex.y / ChunkManager.size.y);
				Chunk chunk = ChunkManager.chunks.get(chunkX + ",0," + chunkY);
				if (chunk != null) {
					int objX = (int) (neighborIndex.x / 16);
					int objY = (int) (neighborIndex.y / 16);
					int objZ = (int) (neighborIndex.z / 16);
					Object data = chunk.getData(objX, objY, objZ);
					if (data != null) {
						if (!closedList.contains(neighborIndex) && !openList.contains(neighborIndex)) {
							parentList.put(neighborIndex, current);
							openList.add(neighborIndex);
						}
					}
				}
			}
		}
		return null;
	}
}
