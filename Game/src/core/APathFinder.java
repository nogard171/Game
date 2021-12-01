package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class APathFinder {
	public static HashMap<ANode, ANode> parentList = new HashMap<ANode, ANode>();
	public static LinkedList<ANode> indexes = new LinkedList<>(Arrays.asList(new ANode(0, -1, 0), new ANode(0, 1, 0),

			new ANode(-1, 0, 0), new ANode(1, 0, 0), new ANode(0, 0, -1), new ANode(0, 0, 1),

			new ANode(-1, 1, 0), new ANode(1, 1, 0), new ANode(0, 1, -1), new ANode(0, 1, 1),

			new ANode(-1, -1, 0), new ANode(1, -1, 0), new ANode(0, -1, -1), new ANode(0, -1, 1)));

	public static LinkedList<ANode> constructPath(ANode index) {
		LinkedList<ANode> path = new LinkedList<ANode>();
		while (parentList.get(index) != null) {

			path.addFirst(index);
			index = parentList.get(index);
		}
		parentList.clear();
		return path;
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
			if (current.x > 100 && current.y > 100) {
				return null;
			}
			for (ANode index : indexes) {
				ANode neighborIndex = new ANode(current.x + index.x, current.y + index.y, current.z + index.z);
				if (!closedList.contains(neighborIndex) && !openList.contains(neighborIndex)) {
					System.out.println("Current: " + current);
					parentList.put(neighborIndex, current);
					openList.add(neighborIndex);

				}
			}
		}
		return null;
	}
}
