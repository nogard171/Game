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

	private static void processBind() {
		if (textureBound) {
			bindTexture();
		} else {
			unbindTexture();
		}
	}

	public static void renderTexture(Rectangle bound, Color color) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(color.r, color.g, color.b, color.a);

		GL11.glBegin(GL11.GL_QUADS);
		
		
		float tempxStep = bound.x / Database.textureSize.width;
		float tempyStep = bound.y / Database.textureSize.height;
		float tempwStep = (bound.x + bound.width) / Database.textureSize.width;
		float temphStep = (bound.y + bound.height) / Database.textureSize.height;
		GL11.glTexCoord2f(tempxStep, tempyStep);
		GL11.glVertex2f(0, 0);
		GL11.glTexCoord2f(tempxStep + tempwStep, tempyStep);
		GL11.glVertex2f(bound.width, 0);
		GL11.glTexCoord2f(tempxStep + tempwStep, tempyStep + temphStep);
		GL11.glVertex2f(bound.width, bound.height);
		GL11.glTexCoord2f(tempxStep, tempyStep + temphStep);
		GL11.glVertex2f(0, bound.height);

		GL11.glEnd();
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
		processBind();
	}

	public static void renderText(Point position, String text, int fontSize, Color color) {
		renderText(position.x, position.y, text, fontSize, color);
	}

	public static void renderText(int x, int y, String text, int fontSize, Color color) {

		TrueTypeFont font = Database.fonts.get(fontSize);

		if (font == null) {
			Font awtFont = new Font("Courier", Font.PLAIN, fontSize);
			Database.fonts.put(fontSize, new TrueTypeFont(awtFont, false));
		}
		if (font != null) {
			TextureImpl.bindNone();
			font.drawString(x, y, text, color);
		}
		processBind();
	}
}
