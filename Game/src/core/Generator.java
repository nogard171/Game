package core;

import java.awt.Point;
import java.util.Random;

import utils.ImprovedNoise;

public class Generator {
	private static int[][] map;
	private static Random r;

	public static int[][] generateHeightMap(Point chunkIndex,int width, int height, int scale) {
		r = new Random();
		int[][] tempMap = new int[width][height];
		int chunkX = chunkIndex.x*width;
		int chunkY = chunkIndex.y*height;
		for (int y = 0; y < width; y++) {
			for (int x = 0; x < height; x++) {
				tempMap[x][y] = (int) Math.round(
						ImprovedNoise.noise((double) (x+chunkX) / (double) 100, 0, (double) (y+chunkY) / (double) 100) * (double) 100);
			}
		}
		/*
		 * // Setup points in the 4 corners of the map. map[0][0] = scale; map[width -
		 * 1][0] = scale; map[width - 1][height - 1] = scale; map[0][height - 1] =
		 * scale; // Do the midpoint midpoint(0, 0, width - 1, height - 1);
		 */
		return tempMap;
	}

	public static int[][] generateHeightMapWithMap(int width, int height, int[][] tempMap, int scale) {
		r = new Random();
		map = tempMap;
		// Do the midpoint
		midpoint(0, 0, width - 1, height - 1);
		return map;
	}

	public static int[][] buildMapWithChunks(int width, int height, int[][] xChunk, int[][] chunkX, int[][] yChunk,
			int[][] chunkY, int scale) {
		int[][] newMap = new int[width][height];
		boolean changed = false;
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				newMap[0][y] = 0;
			}
		}
		if (xChunk != null) {
			for (int y = 0; y < height; y++) {
				newMap[0][y] = xChunk[width - 1][y];
			}
		} else {
			newMap[0][0] = scale;
			newMap[width - 1][0] = scale;
		}
		if (chunkX != null) {
			for (int y = 0; y < height; y++) {
				newMap[width - 1][y] = chunkX[0][y];
			}
		} else {
			newMap[0][0] = scale;
			newMap[width - 1][0] = scale;
		}
		if (yChunk != null) {
			for (int x = 0; x < width; x++) {
				newMap[x][0] = yChunk[x][height - 1];
			}
		} else {
			newMap[0][height - 1] = scale;
			newMap[width - 1][height - 1] = scale;
		}
		if (chunkY != null) {
			for (int x = 0; x < width; x++) {
				newMap[x][height - 1] = chunkY[x][0];
			}
		} else {
			newMap[0][height - 1] = scale;
			newMap[width - 1][height - 1] = scale;
		}
		return newMap;
	}

	private static boolean midpoint(int x1, int y1, int x2, int y2) {
		// If this is pointing at just on pixel, Exit because
		// it doesn't need doing}
		if (x2 - x1 < 2 && y2 - y1 < 2)
			return false;

		// Find distance between points and
		// use when generating a random number.
		int dist = (x2 - x1 + y2 - y1);
		int hdist = dist / 2;
		// Find Middle Point
		int midx = (x1 + x2) / 2;
		int midy = (y1 + y2) / 2;
		// Get pixel colors of corners
		int c1 = map[x1][y1];
		int c2 = map[x2][y1];
		int c3 = map[x2][y2];
		int c4 = map[x1][y2];

		// If Not already defined, work out the midpoints of the corners of
		// the rectangle by means of an average plus a random number.
		if (map[midx][y1] == 0)
			map[midx][y1] = ((c1 + c2 + r.nextInt(dist) - hdist) / 2);
		if (map[midx][y2] == 0)
			map[midx][y2] = ((c4 + c3 + r.nextInt(dist) - hdist) / 2);
		if (map[x1][midy] == 0)
			map[x1][midy] = ((c1 + c4 + r.nextInt(dist) - hdist) / 2);
		if (map[x2][midy] == 0)
			map[x2][midy] = ((c2 + c3 + r.nextInt(dist) - hdist) / 2);

		// Work out the middle point...
		map[midx][midy] = ((c1 + c2 + c3 + c4 + r.nextInt(dist) - hdist) / 4);

		// Now divide this rectangle into 4, And call again For Each smaller
		// rectangle
		midpoint(x1, y1, midx, midy);
		midpoint(midx, y1, x2, midy);
		midpoint(x1, midy, midx, y2);
		midpoint(midx, midy, x2, y2);

		return true;
	}
}
