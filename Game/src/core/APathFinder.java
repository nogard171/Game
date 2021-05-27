package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class APathFinder {
	HashMap<ANode, ANode> parentList = new HashMap<ANode, ANode>();
	ArrayList<ANode> indexes = new ArrayList<ANode>();

	public APathFinder() {
		indexes.add(new ANode(-1, 0, 0));
		indexes.add(new ANode(1, 0, 0));
		indexes.add(new ANode(0, 0, -1));
		indexes.add(new ANode(0, 0, 1));
		indexes.add(new ANode(0, -1, 0));
		indexes.add(new ANode(0, 1, 0));

		indexes.add(new ANode(-1, 1, 0));
		indexes.add(new ANode(1, 1, 0));
		indexes.add(new ANode(0, 1, -1));
		indexes.add(new ANode(0, 1, 1));

		indexes.add(new ANode(-1, -1, 0));
		indexes.add(new ANode(1, -1, 0));
		indexes.add(new ANode(0, -1, -1));
		indexes.add(new ANode(0, -1, 1));

	}

	protected List<ANode> constructPath(ANode index) {
		LinkedList<ANode> path = new LinkedList<ANode>();
		while (parentList.get(index) != null) {

			path.addFirst(index);
			index = parentList.get(index);
		}
		parentList.clear();
		return path;
	}

	public List<ANode> find(ANode startIndex, ANode endIndex) {
		LinkedList<ANode> openList = new LinkedList<ANode>();
		LinkedList<ANode> closedList = new LinkedList<ANode>();
		openList.add(startIndex);
		parentList.put(startIndex, null);

		while (!openList.isEmpty()) {
			ANode current = (ANode) openList.removeFirst();
			if (current.equals(endIndex)) {
				return constructPath(endIndex);
			} else {
				closedList.add(current);
			}
			if (current.x > ChunkManager.size.x && current.y > ChunkManager.size.y) {
				return null;
			}
			for (ANode index : indexes) {
				ANode neighborIndex = new ANode(current.x + index.x, current.y + index.y, current.z + index.z);
				int chunkX = (int) (neighborIndex.x / ChunkManager.size.x);
				int chunkZ = (int) (neighborIndex.z / ChunkManager.size.z);
				Chunk chunk = ChunkManager.chunks.get(chunkX + ",0," + chunkZ);
				if (chunk != null) {
					int objX = (int) (neighborIndex.x % 16);
					int objY = (int) (neighborIndex.y % 16);
					int objZ = (int) (neighborIndex.z % 16);
					Object data = chunk.getData(objX, objY, objZ);

					if (data != null) {
						if (data.getSprite() == "AIR") {
							if (!closedList.contains(neighborIndex) && !openList.contains(neighborIndex)) {
								parentList.put(neighborIndex, current);
								openList.add(neighborIndex);
							}
						}
					}
				}
			}
		}
		return null;
	}
}
