package core;

import java.awt.Point;
import java.awt.Polygon;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class ResourceDatabase {
	public static Texture texture;
	public static Vector2f textureSize;

	public static Texture uiTexture;

	public static HashMap<String, TrueTypeFont> fonts = new HashMap<String, TrueTypeFont>();

	public static void load() {
		try {
			InputStream input = ResourceLoader.getResourceAsStream("assets/textures/tileset.png");
			texture = TextureLoader.getTexture("PNG", input);
			input.close();

			textureSize = new Vector2f(texture.getImageHeight() / 64, texture.getImageHeight() / 64);
			
			
			input = ResourceLoader.getResourceAsStream("assets/textures/uiset.png");
			uiTexture = TextureLoader.getTexture("PNG", input);

			input.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
