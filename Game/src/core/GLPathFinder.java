package core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import classes.GLIndex;
import classes.GLObject;
import classes.GLType;

public class GLPathFinder {
	HashMap<GLIndex, GLIndex> parentList = new HashMap<GLIndex, GLIndex>();
	ArrayList<GLIndex> indexes = new ArrayList<GLIndex>();

	public GLPathFinder() {
		indexes.add(new GLIndex(-1, 0, 0));
		indexes.add(new GLIndex(1, 0, 0));
		indexes.add(new GLIndex(0, 0, -1));
		indexes.add(new GLIndex(0, 0, 1));
	}

	protected List<GLIndex> constructPath(GLIndex index) {
		LinkedList<GLIndex> path = new LinkedList();

		for (GLIndex p : parentList.values()) {
			if (p != null) {
				System.out.println("Index: " + p.x + "," + p.y + "," + p.z);
			}
		}

		while (parentList.get(index) != null) {
			path.addFirst(index);
			index = parentList.get(index);
		}
		parentList.clear();
		return path;
	}

	public List find(GLChunk chunk, GLIndex startIndex, GLIndex endIndex) {
		LinkedList openList = new LinkedList();
		LinkedList closedList = new LinkedList();

		openList.add(startIndex);
		parentList.put(startIndex, null);

		GLIndex test2 = new GLIndex(endIndex.x, 0, endIndex.z, 0, 0, 0);
		chunk.setObject(test2, new GLObject(GLType.IRON_ORE));

		while (!openList.isEmpty()) {
			GLIndex current = (GLIndex) openList.removeFirst();

			if (current.beside(endIndex)) {
				return constructPath(endIndex);
			} else {
				closedList.add(current);
			}
			if (current.x > chunk.size.x && current.z > chunk.size.z) {
				return null;
			}
			for (GLIndex index : indexes) {
				GLIndex neighborIndex = new GLIndex(current.x + index.x, 0, current.z + index.z);
				GLObject obj = chunk.objects.get(new GLIndex(neighborIndex.x, 0, neighborIndex.z));
				if (obj != null) {
					GLType type = obj.getType();
					if (!closedList.contains(neighborIndex) && !openList.contains(neighborIndex)
							&& type == GLType.AIR) {
						System.out.println("Closed: " + closedList.size() + "/" + openList.size());
						parentList.put(neighborIndex, current);
						openList.add(neighborIndex);
					}

				}
			}
		}
		return null;
	}
}
