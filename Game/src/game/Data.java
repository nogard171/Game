package game;

import java.awt.Polygon;

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
		Polygon b = new Polygon();
		b.addPoint(32, 0);
		b.addPoint(64, 16);
		b.addPoint(64, 48);
		b.addPoint(32, 64);
		b.addPoint(0, 48);
		b.addPoint(0, 16);
		temp.bounds = b;
		Database.textureData.put("grass", temp);
		temp = new TextureData(128, 0, 64, 64, 64, 64);
		temp.bounds = b;
		Database.textureData.put("dirt", temp);
		temp = new TextureData(192, 0, 64, 64, 64, 64);
		temp.bounds = b;
		Database.textureData.put("sand", temp);
		temp = new TextureData(0, 64, 64, 64, 64, 64);
		temp.bounds = b;
		Database.textureData.put("stone", temp);
		temp = new TextureData(64, 64, 64, 148, 64, 148, 0, -96);
		b = new Polygon();
		b.addPoint(32, 0);
		b.addPoint(64, 16);
		b.addPoint(64, 48);

		b.addPoint(40, 60);
		b.addPoint(40, 144);
		b.addPoint(32, 148);
		b.addPoint(24, 144);
		b.addPoint(24, 60);
		b.addPoint(0, 48);
		b.addPoint(0, 16);

		temp.bounds = b;

		Database.textureData.put("tree", temp);
		return true;
	}

}
