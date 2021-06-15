package core;

import java.awt.Font;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Renderer {

	public static Texture texture;

	public static HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	public static HashMap<Integer, TrueTypeFont> fonts = new HashMap<Integer, TrueTypeFont>();

	public static void load() {
		try {
			// load texture from PNG file
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("assets/textures/tileset.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		Sprite character = new Sprite();
		Polygon characterShape = new Polygon();
		characterShape.addPoint(0, 0);
		characterShape.addPoint(45, 0);
		characterShape.addPoint(45, 22);
		characterShape.addPoint(0, 22);
		character.shape = characterShape;

		Polygon characterTexture = new Polygon();
		characterTexture.addPoint(0, 169);
		characterTexture.addPoint(45, 169);
		characterTexture.addPoint(45, 191);
		characterTexture.addPoint(0, 191);
		character.texture = characterTexture;
		character.offset = new Point(10, 32);

		sprites.put("character", character);

		Sprite unknown = new Sprite();
		Polygon newShape = new Polygon();
		newShape.addPoint(0, 0);
		newShape.addPoint(64, 0);
		newShape.addPoint(64, 64);
		newShape.addPoint(0, 64);
		unknown.shape = newShape;

		Polygon unknownTexture = new Polygon();
		unknownTexture.addPoint(0, 0);
		unknownTexture.addPoint(64, 0);
		unknownTexture.addPoint(64, 64);
		unknownTexture.addPoint(0, 64);
		unknown.texture = unknownTexture;

		sprites.put("unknown", unknown);

		Sprite grass = new Sprite();
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

		Sprite stone = new Sprite();
		stone.shape = newShape;

		Polygon stoneTexture = new Polygon();
		stoneTexture.addPoint(0, 64);
		stoneTexture.addPoint(64, 64);
		stoneTexture.addPoint(64, 128);
		stoneTexture.addPoint(0, 128);
		stone.texture = stoneTexture;

		sprites.put("stone", stone);
		
		Sprite hover = new Sprite();
		Polygon hoverShape = new Polygon();
		hoverShape.addPoint(0, 0);
		hoverShape.addPoint(64, 0);
		hoverShape.addPoint(64, 32);
		hoverShape.addPoint(0, 32);
		hover.shape = hoverShape;

		Polygon hoverTexture = new Polygon();
		hoverTexture.addPoint(0, 191);
		hoverTexture.addPoint(64, 191);
		hoverTexture.addPoint(64, 223);
		hoverTexture.addPoint(0, 223);
		hover.texture = hoverTexture;

		sprites.put("hover", hover);

		Sprite hover3d = new Sprite();
		hover3d.shape = newShape;

		Polygon hover3dTexture = new Polygon();
		hover3dTexture.addPoint(128, 192);
		hover3dTexture.addPoint(192, 192);
		hover3dTexture.addPoint(192, 256);
		hover3dTexture.addPoint(128, 256);
		hover3d.texture = hover3dTexture;

		sprites.put("hover3d", hover3d);
		
		

		Sprite tree = new Sprite();
		Polygon treeShape = new Polygon();
		treeShape.addPoint(0, 0);
		treeShape.addPoint(64, 0);
		treeShape.addPoint(64, 148);
		treeShape.addPoint(0, 148);
		tree.shape = treeShape;

		Polygon treeTexture = new Polygon();
		treeTexture.addPoint(64,64);
		treeTexture.addPoint(128, 64);
		treeTexture.addPoint(128, 212);
		treeTexture.addPoint(64, 212);
		tree.texture = treeTexture;
		tree.offset = new Point(0, -96);

		sprites.put("tree", tree);
	}

	public static void renderSprite(String name, int x, int y) {
		Sprite sprite = sprites.get(name);
		if (sprite != null) {
			for (int i = 0; i < sprite.shape.npoints; i++) {
				GL11.glTexCoord2f((float) sprite.texture.xpoints[i] / (float) texture.getImageWidth(),
						(float) sprite.texture.ypoints[i] / (float) texture.getImageHeight());
				GL11.glVertex2i(x + sprite.shape.xpoints[i]+sprite.offset.x, y + sprite.shape.ypoints[i]+sprite.offset.y);
			}
		}
	}

	public static void renderQuad(Rectangle bound, Color color) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glColor4f(color.r, color.g, color.b, color.a);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glVertex2f(bound.x, bound.y);
		GL11.glVertex2f(bound.x + bound.width, bound.y);
		GL11.glVertex2f(bound.x + bound.width, bound.y + bound.height);
		GL11.glVertex2f(bound.x, bound.y + bound.height);

		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void renderText(Vector2f position, String text, int fontSize, Color color) {

		TrueTypeFont font = fonts.get(fontSize);

		if (font == null) {
			Font awtFont = new Font("Courier", Font.PLAIN, fontSize);
			fonts.put(fontSize, new TrueTypeFont(awtFont, false));
		}
		if (font != null) {
			TextureImpl.bindNone();
			font.drawString(position.x, position.y, text, color);
		}
	}

	public static void renderText(int x, int y, String text, int fontSize, Color color) {

		TrueTypeFont font = fonts.get(fontSize);

		if (font == null) {
			Font awtFont = new Font("Courier", Font.PLAIN, fontSize);
			fonts.put(fontSize, new TrueTypeFont(awtFont, false));
		}
		if (font != null) {
			TextureImpl.bindNone();
			font.drawString(x, y, text, color);
		}
	}
}
