package core;

import java.util.Random;

public class GLGenerator {
	// In this array the drawing will be made and stored
	public int[][] map = new int[16][16];
	// use r.nextInt() in the code for random numbers
	Random r = new Random();

	public void init() {
		// When starting up do the midpoint drawing one time
		domidpoint();
	}

	public void setupMultiChunk(int numXChunks, int numYChunks) {
		map = new int[16 * numXChunks][16 * numYChunks];
		//domidpoint();
	}

	public int[][] getMultiChunkMap(int chunkX, int chunkY) {
		int[][] tempMap = new int[16][16];

		for (int y = 0; y < tempMap.length ; y++) {
			for (int x = 0; x < tempMap[0].length; x++) {
				tempMap[x][y] = map[((chunkX) * 15) + x][((chunkY) * 15) + y];
			}
		}

		return tempMap;
	}

	// Here the midpoint code begins.
	public void domidpoint() {
		// Erase the old map array..
		for (int x = 0; x < map.length - 1; x++) {
			for (int y = 0; y < map[0].length - 1; y++) {
				map[x][y] = 0;
			}
		}
		// Setup points in the 4 corners of the map.
		map[0][0] = 2;
		map[map.length - 1][0] = 3;
		map[map.length - 1][map[0].length - 1] = 4;
		map[0][map[0].length - 1] = 4;
		// Do the midpoint
		midpoint(0, 0, map.length - 1, map[0].length - 1);
	}

	// This is the actual mid point displacement code.
	public boolean midpoint(int x1, int y1, int x2, int y2) {
		// If this is pointing at just on pixel, Exit because
		// it doesn't need doing}
		if (x2 - x1 < 2 && y2 - y1 < 2)
			return false;

		// Find distance between points and
		// use when generating a random number.
		int dist = (x2 - x1 + y2 - y1);
		int hdist = dist /2;
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
