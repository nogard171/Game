package game;

import java.awt.Polygon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import engine.Sprite;

public class ResourceDatabase {
	private static Texture texture;
	private static HashMap<String, Texture> textures = new HashMap<String, Texture>();
	private static ArrayList<String> boundTexture = new ArrayList<String>();
	private static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	private static HashMap<Integer, TrueTypeFont> fonts = new HashMap<Integer, TrueTypeFont>();

	public static void addTexture(Texture newTexture) {
		texture = newTexture;
	}

	public static void addTexture(String name, Texture texture) {
		textures.put(name, texture);
	}

	public static void addSprite(String name, Sprite sprite) {
		sprites.put(name, sprite);
	}

	public static void addFont(Integer fontSize, TrueTypeFont font) {
		fonts.put(fontSize, font);
	}

	public static void addBoundTexture(String texture) {
		boundTexture.add(texture);
	}

	public static Texture getTexture() {
		return texture;
	}

	public static Texture getTexture(String name) {
		return textures.get(name);
	}

	public static Sprite getSprite(String name) {
		return sprites.get(name);
	}

	public static TrueTypeFont getFont(Integer fontSize) {
		return fonts.get(fontSize);
	}

	public static boolean isBoundTexture(String texture) {
		return (boundTexture.contains(texture) ? true : false);
	}

	public static void removeBoundTexture(String texture) {
		boundTexture.remove(texture);
	}

	public static void clearBoundTextures() {
		boundTexture.clear();
	}

	public static void clean() {
		texture = null;
		textures.clear();
		sprites.clear();
		fonts.clear();
	}

	public static void load() {
		try {
			Texture texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("assets/textures/tileset.png"));
			ResourceDatabase.addTexture(texture);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Polygon newShape = new Polygon();
		newShape.addPoint(0, 0);
		newShape.addPoint(32, 0);
		newShape.addPoint(32, 32);
		newShape.addPoint(0, 32);

		Sprite grass = new Sprite();
		grass.shape = newShape;

		Polygon grassTexture = new Polygon();
		grassTexture.addPoint(32, 0);
		grassTexture.addPoint(64, 0);
		grassTexture.addPoint(64, 32);
		grassTexture.addPoint(32, 32);
		grass.texture = grassTexture;

		ResourceDatabase.addSprite("grass", grass);

		Sprite unknown = new Sprite();
		unknown.shape = newShape;

		Polygon unknownTexture = new Polygon();
		unknownTexture.addPoint(0, 32);
		unknownTexture.addPoint(32, 32);
		unknownTexture.addPoint(32, 64);
		unknownTexture.addPoint(0, 64);
		unknown.texture = unknownTexture;

		ResourceDatabase.addSprite("unknown", unknown);

		Sprite character = new Sprite();
		character.shape = newShape;

		Polygon characterTexture = new Polygon();
		characterTexture.addPoint(96, 0);
		characterTexture.addPoint(128, 0);
		characterTexture.addPoint(128, 32);
		characterTexture.addPoint(96, 32);
		character.texture = characterTexture;

		ResourceDatabase.addSprite("character", character);
	}
}
