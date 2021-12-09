package game;

import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import core.Index;
import core.Region;
import core.Size;
import core.TextureData;
import core.View;

public class Database {
	// window data
	public static View viewFrame;
	public static Point mousePosition;

	// world data
	public static Size regionSize = new Size(16, 16, 16);
	public static HashMap<Index, Region> regions = new HashMap<Index, Region>();
	public static ArrayList<Object> hoveredObjects = new ArrayList<Object>();

	// renderer data
	public static String textureFile = "assets/textures/tileset.png";
	public static Texture texture;
	public static Dimension textureSize;
	public static HashMap<String, Integer> textures = new HashMap<String, Integer>();
	public static HashMap<String, TextureData> textureData = new HashMap<String, TextureData>();
	public static HashMap<Integer, TrueTypeFont> fonts = new HashMap<Integer, TrueTypeFont>();

	// telemetry data
	public static boolean showFPS = true;
	public static boolean showRegsionCount = true;
	public static boolean showTextureCount = true;

	public static void build() {
		if (!loadTexture()) {
			System.out.println("Failed to load Texture");
		} else {
			if (!buildTextures()) {
				System.out.println("Failed to load Textures");
			} else {

			}
		}
	}

	private static boolean loadTexture() {
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(textureFile));
			textureSize = new Dimension(texture.getImageWidth(), texture.getImageHeight());
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private static boolean buildTextures() {
		if (texture != null) {
			for (Map.Entry<String, TextureData> set : textureData.entrySet()) {
				// System.out.println(set.getKey() + " = " + set.getValue());

			}
		} else {
			return false;
		}
		return true;
	}

}