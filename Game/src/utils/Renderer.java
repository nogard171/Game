package utils;

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

import core.Index;
import core.TextureData;
import game.Database;

public class Renderer {
	private static int textureID = -1;
	private static boolean textureBound = false;

	public static void bindTexture() {
		if (textureID == -1) {
			textureID = Database.texture.getTextureID();
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		textureBound = true;
	}

	public static void unbindTexture() {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		textureBound = false;
	}

	public static void renderTexture(Point position, TextureData data) {
		GL11.glColor4f(1, 1, 1, 1);
		float tempxStep = (float) data.textureX / (float) Database.textureSize.width;
		float tempyStep = (float) data.textureY / (float) Database.textureSize.height;
		float tempwStep = ((float) data.textureX + (float) data.textureWidth) / (float) Database.textureSize.width;
		float temphStep = ((float) data.textureY + (float) data.textureHeight) / (float) Database.textureSize.height;
		GL11.glTexCoord2f(tempxStep, tempyStep);
		GL11.glVertex2f(position.x + data.spriteX, position.y + data.spriteY);
		GL11.glTexCoord2f(tempwStep, tempyStep);
		GL11.glVertex2f(position.x + data.width + data.spriteX, position.y + data.spriteY);
		GL11.glTexCoord2f(tempwStep, temphStep);
		GL11.glVertex2f(position.x + data.width + data.spriteX, position.y + data.height + data.spriteY);
		GL11.glTexCoord2f(tempxStep, temphStep);
		GL11.glVertex2f(position.x + data.spriteX, position.y + data.height + data.spriteY);
	}

	public static void renderQuad(Rectangle bound, Color color) {
		unbindTexture();
		GL11.glColor4f(color.r, color.g, color.b, color.a);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(bound.x, bound.y);
		GL11.glVertex2f(bound.x + bound.width, bound.y);
		GL11.glVertex2f(bound.x + bound.width, bound.y + bound.height);
		GL11.glVertex2f(bound.x, bound.y + bound.height);

		GL11.glEnd();
		bindTexture();
	}

	public static void renderText(Point position, String text, int fontSize, Color color) {
		renderText(position.x, position.y, text, fontSize, color);
	}

	public static void renderText(int x, int y, String text, int fontSize, Color color) {
		unbindTexture();
		TrueTypeFont font = Database.fonts.get(fontSize);

		if (font == null) {
			Font awtFont = new Font("Courier", Font.PLAIN, fontSize);
			Database.fonts.put(fontSize, new TrueTypeFont(awtFont, false));
		}
		if (font != null) {
			font.drawString(x, y, text, color);
			TextureImpl.bindNone();
		}
		bindTexture();
	}

	public static void bindColor(Color c) {
		GL11.glColor4f(c.r, c.g, c.b, c.a);

	}

	public static void begin() {
		GL11.glBegin(GL11.GL_QUADS);
	}

	public static void end() {
		GL11.glEnd();
	}

	public static void renderBounds(Polygon polygon, Color newColor) {

		if (polygon != null) {
			unbindTexture();
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glColor4f(newColor.r, newColor.g, newColor.b, newColor.a);

			GL11.glBegin(GL11.GL_POLYGON);
			for (int p = 0; p < polygon.npoints; p++) {
				GL11.glVertex2f(polygon.xpoints[p], polygon.ypoints[p]);
			}
			GL11.glEnd();
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			bindTexture();
		}
	}
}
