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

	public static void renderSprite(String name, int x, int y) {
		Sprite sprite = ResourceDatabase.sprites.get(name);
		if (sprite != null) {
			for (int i = 0; i < sprite.shape.npoints; i++) {
				GL11.glTexCoord2f((float) sprite.texture.xpoints[i] / (float) ResourceDatabase.texture.getImageWidth(),
						(float) sprite.texture.ypoints[i] / (float) ResourceDatabase.texture.getImageHeight());
				GL11.glVertex2i(x + sprite.shape.xpoints[i] + sprite.offset.x,
						y + sprite.shape.ypoints[i] + sprite.offset.y);
			}
		}
	}

	public static void renderSprite(String name, int x, int y, int[] heights) {
		// if (heights.length == 4) {
		int posX = ((x - y) * 32);
		int posY = ((0 - heights[0]) * 32);
		int posZ = (((y + x) * 16) - posY);

		GL11.glVertex2f(posX, posZ);

		posX = (((x + 1) - y) * 32);
		posY = ((0 - heights[1]) * 32);
		posZ = (((y + (x + 1)) * 16) - posY);
		GL11.glVertex2f(posX, posZ);

		posX = (((x + 1) - (y + 1)) * 32);
		posY = ((0 - heights[2]) * 32);
		posZ = ((((y + 1) + (x + 1)) * 16) - posY);
		GL11.glVertex2f(posX, posZ);

		posX = ((x - (y + 1)) * 32);
		posY = ((0 - heights[3]) * 32);
		posZ = ((((y + 1) + x) * 16) - posY);
		GL11.glVertex2f(posX, posZ);

		/*
		 * GL11.glTexCoord2f((float) sprite.texture.xpoints[i] / (float)
		 * ResourceDatabase.texture.getImageWidth(), (float) sprite.texture.ypoints[i] /
		 * (float) ResourceDatabase.texture.getImageHeight());
		 */
		// }
		/*
		 * Sprite sprite = ResourceDatabase.sprites.get(name); if (sprite != null) { for
		 * (int i = 0; i < sprite.shape.npoints; i++) { GL11.glTexCoord2f((float)
		 * sprite.texture.xpoints[i] / (float) ResourceDatabase.texture.getImageWidth(),
		 * (float) sprite.texture.ypoints[i] / (float)
		 * ResourceDatabase.texture.getImageHeight()); GL11.glVertex2i(x +
		 * sprite.shape.xpoints[i] + sprite.offset.x, y + sprite.shape.ypoints[i] +
		 * sprite.offset.y); } }
		 */
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
}
