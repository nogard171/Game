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

	public static void renderSprite(TextureType type, int x, int y) {
		Vector2f[] vectors = { new Vector2f(0, 0), new Vector2f(64 * type.w, 0), new Vector2f(64 * type.w, 64 * type.h),
				new Vector2f(0, 64 * type.h) };
		Vector2f[] textureVectors = { new Vector2f(type.x, type.y), new Vector2f(type.x + type.w, type.y),
				new Vector2f(type.x + type.w, type.y + type.h), new Vector2f(type.x, type.y + type.h)

		};
		System.out.println("Vec: " + (64 * type.h));
		int i = 0;
		for (Vector2f vec : vectors) {
			Vector2f textureVec = textureVectors[i];
			GL11.glTexCoord2f((textureVec.x * 64) / ResourceDatabase.texture.getImageWidth(),
					(textureVec.y * 64) / ResourceDatabase.texture.getImageHeight());
			System.out.println("Vec: " + textureVec);
			GL11.glVertex2f(((vec.x) + x) + (64 * type.xOffset), ((vec.y) + y) + (64 * type.yOffset));
			i++;
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

	public static void renderText(float x, float y, String text, int fontSize, Color color) {

		TrueTypeFont font = ResourceDatabase.fonts.get(fontSize);

		if (font == null) {
			Font awtFont = new Font("Courier", Font.PLAIN, fontSize);
			ResourceDatabase.fonts.put(fontSize, new TrueTypeFont(awtFont, false));
		}
		if (font != null) {
			TextureImpl.bindNone();
			font.drawString(x, y, text, color);
		}
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
