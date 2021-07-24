package core;

import java.util.Random;

public class Generator {

	public static float[][] generateHeightMap(int width, int height) {
		float[][] map = new float[width][height];
		Random r = new Random();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				map[x][y] = 0;// r.nextFloat();

				if (x == 2 && y == 7) {
					// map[x][y] = 0.5f;
				}
			}
		}
		return map;
	}
}
