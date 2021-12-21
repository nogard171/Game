package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class APathFinder {
	public static HashMap<ANode, ANode> parentList = new HashMap<ANode, ANode>();
	public static LinkedList<ANode> nodeSearchList = new LinkedList<ANode>();
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

	private static Long startTime;
	private static Long endTime;

	public static LinkedList<ANode> find(ANode startIndex, ANode endIndex) {
		startTime = System.currentTimeMillis();
		System.out.println("Finding Path...");
		nodeSearchList.clear();
		parentList.clear();
		LinkedList<ANode> openList = new LinkedList<ANode>();
		LinkedList<ANode> closedList = new LinkedList<ANode>();
		openList.add(startIndex);
		parentList.put(startIndex, null);

		while (!openList.isEmpty()) {
			ANode current = (ANode) openList.removeFirst();
			if (current.equals(endIndex)) {
				endTime = System.currentTimeMillis();

				long temp = (endTime - startTime);
				System.out.println("Search Size:" + nodeSearchList.size());
				System.out.println("Search Time:" + temp+"ms");

				return constructPath(endIndex);
			} else {
				closedList.add(current);
			}
			if ((current.x > 100 && current.y > 100) || (current.x < -100 && current.y < -100)) {
				return null;
			}
			for (ANode index : indexes) {
				ANode neighborIndex = new ANode(current.x + index.x, current.y + index.y, current.z + index.z);
				if (!nodeSearchList.contains(neighborIndex)) {
					nodeSearchList.add(neighborIndex);
					if (!closedList.contains(neighborIndex) && !openList.contains(neighborIndex)) {
						parentList.put(neighborIndex, current);
						openList.add(neighborIndex);
					}
				}
			}
		}

		return null;
	}
}
