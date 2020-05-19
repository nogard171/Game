package utils;

import java.awt.Font;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import core.Sprite;
import game.Base;

public class Renderer {
	public static Texture texture;
	public static Texture uiTexture;

	private static HashMap<String, Sprite> rawSprites = new HashMap<String, Sprite>();

	static HashMap<Integer, TrueTypeFont> fonts = new HashMap<Integer, TrueTypeFont>();

	public static void loadSprites() {
		rawSprites = Loader.loadSprites();
		if (rawSprites != null) {
			try {
				String textureFile = Base.settings.getProperty("assets.texture");
				texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(textureFile));
				String uiTextureFile = Base.settings.getProperty("assets.ui.texture");
				uiTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(uiTextureFile));
			} catch (IOException e) {
				Logger.writeLog(e.getMessage());
			}
		}
	}

	public static void bindTexture(int textureId) {
		if (textureId == 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, Renderer.texture.getTextureID());
		} else if (textureId == 1) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, Renderer.uiTexture.getTextureID());
		}
	}

	public static void renderSquare(int x, int y, int w, int h) {
		renderSquare(x, y, w, h, Color.black);
	}

	public static void renderSprite(float x, float y, String spriteName) {

		Sprite raw = rawSprites.get(spriteName);

		if (raw != null) {
			LinkedList<Vector2f> shape = raw.shape;
			if (raw.inheritShape != "") {
				Sprite inheritShapeSprite = rawSprites.get(raw.inheritShape);

				if (inheritShapeSprite != null) {
					shape = inheritShapeSprite.shape;
				}
			}

			for (int nPoint = 0; nPoint < shape.size(); nPoint++) {
				Vector2f vec = shape.get(nPoint);
				Vector2f tex = raw.texture.get(nPoint);

				float texX = (float) tex.x / (float) texture.getImageWidth();
				float texY = (float) tex.y / (float) texture.getImageHeight();
				GL11.glTexCoord2f(texX, texY);
				GL11.glVertex2f(vec.x + x, vec.y + y);
			}
		}
	}

	public static void renderSquare(int x, int y, int w, int h, Color c) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glColor4f((float) c.getRed() / 255, (float) c.getGreen() / 255, (float) c.getBlue() / 255,
				(float) c.getAlpha() / 255);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2i(x, y);
		GL11.glVertex2i(x + w, y);
		GL11.glVertex2i(x + w, y + h);
		GL11.glVertex2i(x, y + h);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void renderRectangle(Rectangle rec, Color c, int glLineLoop) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glColor4f((float) c.getRed() / 255, (float) c.getGreen() / 255, (float) c.getBlue() / 255,
				(float) c.getAlpha() / 255);
		GL11.glBegin(glLineLoop);
		GL11.glVertex2i(rec.x, rec.y);
		GL11.glVertex2i(rec.x + rec.width, rec.y);
		GL11.glVertex2i(rec.x + rec.width, rec.y + rec.height);
		GL11.glVertex2i(rec.x, rec.y + rec.height);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void renderRectangle(Rectangle rec, Color c) {
		renderRectangle(rec, c, GL11.GL_QUADS);
	}

	public static void renderText(int x, int y, String text, int fontSize, Color color) {
		renderText(x, y, text, fontSize, color, "Courier");
	}

	public static void renderText(int x, int y, String text, int fontSize, Color color, String fontName) {
		TrueTypeFont font = fonts.get(fontSize);

		if (font == null) {
			Font awtFont = new Font(fontName, Font.PLAIN, fontSize);
			fonts.put(fontSize, new TrueTypeFont(awtFont, false));
		}
		if (font != null) {
			TextureImpl.bindNone();
			font.drawString(x, y - 4, text, color);
		}
	}

}
