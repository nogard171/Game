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
			if ((current.x > 100 && current.y > 100) || (current.x < -100 && current.y < -100)) {
				return null;
			}
			float shortestDistance = 100;
			for (ANode index : indexes) {
				ANode neighborIndex = new ANode(current.x + index.x, current.y + index.y, current.z + index.z);

				float distance = (float) Math.sqrt(Math.pow(neighborIndex.x - current.x, 2)
						+ Math.pow(neighborIndex.y - current.y, 2) + Math.pow(neighborIndex.z - current.z, 2));
				System.out.println("Finding Path..." + neighborIndex + "=>" + distance);
				// if (distance <= shortestDistance) {
				if (!closedList.contains(neighborIndex) && !openList.contains(neighborIndex)) {
					// System.out.println("Current: " + current);
					parentList.put(neighborIndex, current);
					openList.add(neighborIndex);
					shortestDistance = distance;
					// World.setCell(neighborIndex.toIndex(), "dirt");
				}
				// }
			}
		}
		return null;
	}
}
