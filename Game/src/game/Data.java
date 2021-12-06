package game;

import core.TextureData;

public class Data {

	public static int failedCount = 0;

	public static void setup() {
		if (!setupTextures()) {
			failedCount++;
		} else {

		}
	}

	private static boolean setupTextures() {
		TextureData temp = new TextureData(64, 0, 64, 64, 64, 64);
		Database.textureData.put("grass", temp);
		return true;
	}

}
