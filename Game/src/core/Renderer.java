package core;

import java.awt.Polygon;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Renderer {

	public static Texture texture;

	public static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();

	public static void load() {
		try {
			// load texture from PNG file
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("assets/textures/tileset.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Sprite grass = new Sprite();
		Polygon newShape = new Polygon();
		newShape.addPoint(0, 0);
		newShape.addPoint(64, 0);
		newShape.addPoint(64, 64);
		newShape.addPoint(0, 64);
		grass.shape = newShape;

		Polygon grassTexture = new Polygon();
		grassTexture.addPoint(64, 0);
		grassTexture.addPoint(128, 0);
		grassTexture.addPoint(128, 64);
		grassTexture.addPoint(64, 64);
		grass.texture = grassTexture;

		sprites.put("grass", grass);

		Sprite dirt = new Sprite();
		dirt.shape = newShape;

		Polygon dirtTexture = new Polygon();
		dirtTexture.addPoint(128, 0);
		dirtTexture.addPoint(192, 0);
		dirtTexture.addPoint(192, 64);
		dirtTexture.addPoint(128, 64);
		dirt.texture = dirtTexture;

		sprites.put("dirt", dirt);
	}

	public static void renderSprite(String name, int x, int y) {
		Sprite sprite = sprites.get(name);
		if (sprite != null) {
			for (int i = 0; i < sprite.shape.npoints; i++) {
				GL11.glTexCoord2f((float) sprite.texture.xpoints[i] / (float) texture.getImageWidth(),
						(float) sprite.texture.ypoints[i] / (float) texture.getImageHeight());
				GL11.glVertex2i(x + sprite.shape.xpoints[i], y + sprite.shape.ypoints[i]);
			}
		}
	}
}
