package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import classes.GLIndex;
import classes.GLObject;
import classes.GLType;

public class APathFinder {
	HashMap<Point, Point> parentList = new HashMap<Point, Point>();
	ArrayList<Point> indexes = new ArrayList<Point>();
	

	public APathFinder() {
		indexes.add(new Point(-1, 0));
		indexes.add(new Point(1, 0));
		indexes.add(new Point(0, -1));
		indexes.add(new Point(0, 1));
	}

	protected List constructPath(Point index) {
		LinkedList path = new LinkedList();
		while (parentList.get(index) != null) {
			path.addFirst(index);
			index = parentList.get(index);
		}
		parentList.clear();
		return path;
	}

	public List find(GLChunk chunk, Point startIndex, Point endIndex) {
		LinkedList openList = new LinkedList();
		LinkedList closedList = new LinkedList();
		openList.add(startIndex);
		parentList.put(startIndex, null);

		while (!openList.isEmpty()) {
			Point current = (Point) openList.removeFirst();
			if (current.equals(endIndex)) {
				return constructPath(endIndex);
			} else {
				closedList.add(current);
			}
			if (current.x > chunk.size.x && current.y > chunk.size.z) {
				return null;
			}
			for (Point index : indexes) {
				Point neighborIndex = new Point(current.x + index.x, current.y + index.y);

				GLObject obj = chunk.objects.get(new GLIndex(neighborIndex.x, 0, neighborIndex.y, 0, 0, 0));
				if (obj != null) {
					if (!closedList.contains(neighborIndex) && !openList.contains(neighborIndex)
							&& obj.getType() == GLType.AIR) {
						parentList.put(neighborIndex, current);
						openList.add(neighborIndex);
					}
				}
			}
		}
		return null;
	}
}
