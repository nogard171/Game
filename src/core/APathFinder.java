package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import game.Base;
import ui.UIManager;

public class APathFinder {
	public static HashMap<ANode, ANode> parentList = new HashMap<ANode, ANode>();
	public static LinkedList<ANode> indexes = new LinkedList<>(Arrays.asList(new ANode(0, -1), new ANode(0, 1),

			new ANode(-1, 0), new ANode(1, 0),

			new ANode(-1, 1), new ANode(1, 1),

			new ANode(-1, -1), new ANode(1, -1)));

	public static LinkedList<ANode> constructPath(ANode index) {
		LinkedList<ANode> path = new LinkedList<ANode>();
		while (parentList.get(index) != null) {

			path.addFirst(index);
			index = parentList.get(index);
		}
		parentList.clear();
		return path;
	}
	public static LinkedList<ANode> findTest(ANode startIndex, ANode endIndex) {
		parentList.clear();

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
			if (current.x > UIManager.playerIndex.x + 100
					|| current.y > UIManager.playerIndex.y + 100) {
				return null;
			}
			for (ANode index : indexes) {
				ANode neighborIndex = new ANode(current.x + index.x, current.y + index.y);
				int chunkX = (int) (neighborIndex.x / Chunk.size.width);
				int chunkY = (int) (neighborIndex.y / Chunk.size.height);
				Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
				if (chunk != null) {
					int objX = (int) (neighborIndex.x % 16);
					int objY = (int) (neighborIndex.y % 16);
					Tile data = chunk.getObjectAtIndex(new Point(objX, objY), false, false);
					if (data != null) {
						if (data.getType().isPassable()||neighborIndex.equals(endIndex)) {
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
	public static LinkedList<ANode> find(ANode startIndex, ANode endIndex) {
		parentList.clear();

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
			if (current.x > UIManager.playerIndex.x + 100
					|| current.y > UIManager.playerIndex.y + 100) {
				return null;
			}
			for (ANode index : indexes) {
				ANode neighborIndex = new ANode(current.x + index.x, current.y + index.y);
				int chunkX = (int) (neighborIndex.x / Chunk.size.width);
				int chunkY = (int) (neighborIndex.y / Chunk.size.height);
				Chunk chunk = ChunkManager.chunks.get(new Point(chunkX, chunkY));
				if (chunk != null) {
					int objX = (int) (neighborIndex.x % 16);
					int objY = (int) (neighborIndex.y % 16);
					Tile data = chunk.getObjectAtIndex(new Point(objX, objY), false, false);
					if (data != null) {
						if (data.getType().isPassable()) {
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
