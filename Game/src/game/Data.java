package game;

import java.awt.Polygon;

import core.TextureData;
import utils.Debugger;

public class Data {

	public static int failedCount = 0;

	public static void setup() {
		Debugger.log("Data Setup Started");
		Debugger.log("Data Setting Up Textures");
		if (!setupTextures()) {
			failedCount++;
			Debugger.log("Data Textures Failed To Load");
		}
		Debugger.log("Data Textures Successfully Loaded");
	}

	private static boolean setupTextures() {
		Debugger.log("Data Textures Building Grass Texture");
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
		Debugger.log("Data Textures Building Dirt Texture");
		temp = new TextureData(128, 0, 64, 64, 64, 64);
		temp.bounds = b;
		Database.textureData.put("dirt", temp);
		Debugger.log("Data Textures Building Sand Texture");
		temp = new TextureData(192, 0, 64, 64, 64, 64);
		temp.bounds = b;
		Database.textureData.put("sand", temp);
		Debugger.log("Data Textures Building Stone Texture");
		temp = new TextureData(0, 64, 64, 64, 64, 64);
		temp.bounds = b;
		Database.textureData.put("stone", temp);
		Debugger.log("Data Textures Building Character Texture");
		temp = new TextureData(0, 169, 45, 22, 45, 22, 0, 0, 45 / 4, 32);
		temp.bounds = b;
		Database.textureData.put("character", temp);
		Debugger.log("Data Textures Building Tree Texture");
		temp = new TextureData(64, 64, 64, 148, 64, 148, 0, -96, 0, -96);
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
