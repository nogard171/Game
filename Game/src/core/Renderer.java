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

import ui.UITextureType;

public class Renderer {

	private static Texture previousTexture;
	private static Texture currentTexture;

	public static void bindTexture(Texture newTexture) {
		if (newTexture != null) {
			if (currentTexture != previousTexture) {
				previousTexture = currentTexture;
			}
			currentTexture = newTexture;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture.getTextureID());
			GL11.glColor3f(1, 1, 1);

		}
	}

	public static void rebind() {
		if (previousTexture != null) {
			currentTexture = previousTexture;
			previousTexture = null;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, currentTexture.getTextureID());
		}
	}

	public static void renderTexture(TextureType type, int x, int y) {
		renderTexture(type, x, y, 64, 64);
	}

	public static void renderTexture(TextureType type, int x, int y, int width, int height) {
		if (currentTexture != null) {
			if (type != TextureType.AIR) {
				Vector2f[] vectors = { new Vector2f(0, 0), new Vector2f(width * type.pW, 0),
						new Vector2f(width * type.pW, height * type.pH), new Vector2f(0, height * type.pH) };
				Vector2f[] textureVectors = { new Vector2f(type.x, type.y), new Vector2f(type.x + type.w, type.y),
						new Vector2f(type.x + type.w, type.y + type.h), new Vector2f(type.x, type.y + type.h)

				};
				int i = 0;
				for (Vector2f vec : vectors) {
					Vector2f textureVec = textureVectors[i];
					GL11.glTexCoord2f((textureVec.x * width) / currentTexture.getImageWidth(),
							(textureVec.y * height) / currentTexture.getImageHeight());
					GL11.glVertex2f(((vec.x) + x) + (width * type.xOffset), ((vec.y) + y) + (height * type.yOffset));
					i++;
				}
			}
		}
	}

	public static void renderUITexture(UITextureType type, int x, int y, int width, int height) {
		if (currentTexture != null) {
			if (type != UITextureType.BLANK) {
				Vector2f[] vectors = { new Vector2f(0, 0), new Vector2f(width * type.pW, 0),
						new Vector2f(width * type.pW, height * type.pH), new Vector2f(0, height * type.pH) };
				Vector2f[] textureVectors = { new Vector2f(type.x, type.y), new Vector2f(type.x + type.w, type.y),
						new Vector2f(type.x + type.w, type.y + type.h), new Vector2f(type.x, type.y + type.h)

				};
				int i = 0;
				for (Vector2f vec : vectors) {
					Vector2f textureVec = textureVectors[i];
					GL11.glTexCoord2f((textureVec.x * 32) / currentTexture.getImageWidth(),
							(textureVec.y * 32) / currentTexture.getImageHeight());
					GL11.glVertex2f(((vec.x) + x) + (width * type.xOffset), ((vec.y) + y) + (height * type.yOffset));
					i++;
				}
			}
		}
	}

	public static void renderUITexture(UITextureType type, int x, int y, int width, int height, int top, int left) {
		if (currentTexture != null) {
			if (type != UITextureType.BLANK) {
				float topTexture = (type.h / ((float) 32 / ((float) 32 - top + 1)));
				Vector2f[] vectors = { new Vector2f(0, 32 - top), new Vector2f(width * type.w, 32 - top),
						new Vector2f(width * type.w, height * type.h), new Vector2f(0, height * type.h) };
				Vector2f[] textureVectors = { new Vector2f(type.x, type.y + topTexture),
						new Vector2f(type.x + type.w, type.y + topTexture),
						new Vector2f(type.x + type.w, type.y + type.h), new Vector2f(type.x, type.y + type.h)

				};
				int i = 0;
				for (Vector2f vec : vectors) {
					Vector2f textureVec = textureVectors[i];
					GL11.glTexCoord2f((textureVec.x * width) / currentTexture.getImageWidth(),
							(textureVec.y * height) / currentTexture.getImageHeight());
					GL11.glVertex2f(((vec.x) + x) + (width * type.xOffset), ((vec.y) + y) + (height * type.yOffset));
					i++;
				}
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

		renderText(position.x, position.y, text, fontSize, color);
	}

	public static void renderText(Vector2f position, String text, int fontSize, Color color, int fontType) {
		renderText(position.x, position.y, text, fontSize, color, fontType);
	}

	public static int renderTextWithWidth(Vector2f position, String text, int fontSize, Color color, int fontType) {
		int fontWidth = 0;
		fontWidth = renderText(position.x, position.y, text, fontSize, color, fontType);
		return fontWidth;
	}

	public static void renderText(float x, float y, String text, int fontSize, Color color) {

		renderText(x, y, text, fontSize, color, Font.PLAIN);
	}

	public static int getTextWidth(String text, int fontSize) {

		return getTextWidth(text, fontSize, Font.PLAIN);
	}

	public static int getTextWidth(String text, int fontSize, int fontType) {
		int fontWidth = 0;
		String key = fontSize + "," + fontType;
		TrueTypeFont font = ResourceDatabase.fonts.get(key);

		if (font == null) {
			// Fixedsys
			Font awtFont = new Font("Fixedsys", fontType, fontSize);
			ResourceDatabase.fonts.put((fontSize + "," + fontType), new TrueTypeFont(awtFont, false));
		}
		if (font != null) {
			fontWidth = font.getWidth(text);
		}
		return fontWidth;
	}

	public static int getFontWidth(String text, int fontSize, int fontType) {
		int fontWidth = 0;
		String key = fontSize + "," + fontType;
		TrueTypeFont font = ResourceDatabase.fonts.get(key);

		if (font == null) {
			// Fixedsys
			Font awtFont = new Font("Fixedsys", fontType, fontSize);
			ResourceDatabase.fonts.put((fontSize + "," + fontType), new TrueTypeFont(awtFont, false));
		}
		if (font != null) {
			fontWidth = font.getWidth(text);
		}
		return fontWidth;
	}

	public static int renderText(float x, float y, String text, int fontSize, Color color, int fontType) {
		int fontWidth = 0;
		String key = fontSize + "," + fontType;
		TrueTypeFont font = ResourceDatabase.fonts.get(key);

		if (font == null) {
			// Fixedsys
			Font awtFont = new Font("Fixedsys", fontType, fontSize);
			ResourceDatabase.fonts.put((fontSize + "," + fontType), new TrueTypeFont(awtFont, false));
		}
		if (font != null) {
			TextureImpl.bindNone();
			font.drawString(x, y, text, color);
			fontWidth = font.getWidth(text);
		}
		return fontWidth;
	}

	public static void renderGrid(float x, float y) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		GL11.glColor4f(0, 0, 0, 0.5f);

		GL11.glBegin(GL11.GL_QUADS);
		float cartX = x * 32;
		float cartZ = y * 32;

		float isoX = cartX - cartZ;
		float isoZ = (cartX + cartZ) / 2;

		GL11.glVertex2f(isoX, isoZ);
		isoX = (cartX + 32) - cartZ;
		isoZ = ((cartX + 32) + cartZ) / 2;
		GL11.glVertex2f(isoX, isoZ);

		isoX = (cartX + 32) - (cartZ + 32);
		isoZ = ((cartX + 32) + (cartZ + 32)) / 2;
		GL11.glVertex2f(isoX, isoZ);

		isoX = (cartX) - (cartZ + 32);
		isoZ = ((cartX) + (cartZ + 32)) / 2;
		GL11.glVertex2f(isoX, isoZ);

		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
