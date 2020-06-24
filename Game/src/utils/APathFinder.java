package utils;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import classes.Chunk;
import data.WorldData;

public class APathFinder {
	HashMap<Point, Point> parentList = new HashMap<Point, Point>();
	ArrayList<Point> indexes = new ArrayList<Point>();

	public APathFinder() {
		indexes.add(new Point(-1, 0));
		indexes.add(new Point(1, 0));
		indexes.add(new Point(0, -1));
		indexes.add(new Point(0, 1));

		indexes.add(new Point(-1, -1));
		indexes.add(new Point(-1, 1));
		indexes.add(new Point(1, 1));
		indexes.add(new Point(1, -1));
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

	public List find(Point startIndex, Point endIndex) {
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
			int maxIndexX = WorldGenerator.chunkIndex.getX();
			int maxIndexY = WorldGenerator.chunkIndex.getX();

			if (maxIndexX == 0) {
				maxIndexX++;
			}
			if (maxIndexY == 0) {
				maxIndexY++;
			}
			if ((current.x > (maxIndexX * 16) + (16 * WorldGenerator.chunkRenderSize.getWidth())
					&& current.y > (maxIndexY * 16) + (16 * WorldGenerator.chunkRenderSize.getHeight()))
					|| (current.x < (maxIndexX * 16) + (-16 * WorldGenerator.chunkRenderSize.getWidth())
							&& current.y < (maxIndexY * 16) + (-16 * WorldGenerator.chunkRenderSize.getHeight()))) {
				return null;
			}
			for (Point index : indexes) {
				Point neighborIndex = new Point(current.x + index.x, current.y + index.y);
				int chunkX = neighborIndex.x / 16;
				int chunkY = neighborIndex.y / 16;
				Chunk chunk = WorldData.getChunk(chunkX, chunkY);
				if (chunk != null) {
					int objX = neighborIndex.x % 16;
					int objY = neighborIndex.y % 16;
					if (current.x != 0 && current.y != 0) {
						if (objX < 0) {
							objX = 16 + objX;
							neighborIndex.x = objX;
						}
						if (objY < 0) {
							objY = 16 + objY;
							neighborIndex.y = objY;
						}
					}

					Object data = chunk.getData(objX, objY);
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
