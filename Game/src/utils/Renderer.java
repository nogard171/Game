package utils;

import java.awt.Font;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

import classes.Chunk;
import classes.Index;
import classes.TextureData;
import classes.TextureType;
import classes.Object;
import classes.MaterialData;
import classes.ModelData;
import data.WorldData;

public class Renderer {
	public static void renderModel(Chunk self, int x, int z, Object obj) {
		if (self != null) {
			if (obj != null) {
				if (obj.getMaterial() != "AIR") {
					int carX = (self.index.getX() * 32) * 16;
					int carY = (self.index.getY() * 32) * 16;
					int isoX = carX - carY;
					int isoY = (carY + carX) / 2;

					int selfX = isoX;
					int selfY = isoY;
					ModelData raw = WorldData.modelData.get(obj.getModel());
					if (raw != null) {

						MaterialData mat = WorldData.materialData.get(obj.getMaterial());
						if (mat != null) {

							GL11.glColor4f(obj.getColor().r, obj.getColor().g, obj.getColor().b, obj.getColor().a);
							for (int b = 0; b < raw.indices.length; b++) {

								byte i = raw.indices[b];
								byte ti = i;
								if (mat.indices.length > 0) {
									ti = mat.indices[b];
								}
								Vector2f textureVec = mat.vectors[ti];

								GL11.glTexCoord2f(textureVec.x / WorldData.texture.getImageWidth(),
										textureVec.y / WorldData.texture.getImageHeight());

								Vector2f vec = raw.vectors[i];

								int objX = (x * 32) - (z * 32);
								int objY = ((z * 32) + (x * 32)) / 2;
								GL11.glVertex2f(vec.x + selfX + objX + mat.offset.x,
										vec.y + selfY + objY + mat.offset.y);
							}
						}
					}
				}
			}
		}
	}

	public static void renderModel(int objX, int objY, String model, String material, Color c) {
		ModelData raw = WorldData.modelData.get(model);
		if (raw != null) {
			MaterialData mat = WorldData.materialData.get(material);
			if (mat != null) {
				GL11.glColor4f(c.r, c.g, c.b, c.a);
				for (int b = 0; b < raw.indices.length; b++) {

					byte i = raw.indices[b];
					byte ti = i;
					if (mat.indices.length > 0) {
						ti = mat.indices[b];
					}
					Vector2f textureVec = mat.vectors[ti];

					GL11.glTexCoord2f(textureVec.x / WorldData.texture.getImageWidth(),
							textureVec.y / WorldData.texture.getImageHeight());
					Vector2f vec = raw.vectors[i];
					GL11.glVertex2f(vec.x + objX + mat.offset.x, vec.y + objY + mat.offset.y);
				}
			}
		}

	}

	public static void renderGrid(int indexX, int indexY) {
		int cartX = indexX * 32;
		int cartZ = indexY * 32;

		int isoX = cartX - cartZ;
		int isoZ = (cartX + cartZ) / 2;

		GL11.glVertex2i(isoX, isoZ);
		isoX = (cartX + 32) - cartZ;
		isoZ = ((cartX + 32) + cartZ) / 2;
		GL11.glVertex2i(isoX, isoZ);

		isoX = (cartX + 32) - (cartZ + 32);
		isoZ = ((cartX + 32) + (cartZ + 32)) / 2;
		GL11.glVertex2i(isoX, isoZ);

		isoX = (cartX) - (cartZ + 32);
		isoZ = ((cartX) + (cartZ + 32)) / 2;
		GL11.glVertex2i(isoX, isoZ);
	}

	public static void renderRectangle(float x, float y, float width, float height, Color c) {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(c.r, c.g, c.b, c.a);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x + width, y);
		GL11.glVertex2f(x + width, y + height);
		GL11.glVertex2f(x, y + height);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	public static void renderRectangleWithoutBegin(int x, int y, int width, int height, Color c) {
		GL11.glColor4f(c.r, c.g, c.b, c.a);
		GL11.glVertex2i(x, y);
		GL11.glVertex2i(x + width, y);
		GL11.glVertex2i(x + width, y + height);
		GL11.glVertex2i(x, y + height);
	}

	public static void renderText(Vector2f position, String text, int fontSize, Color color) {

		TrueTypeFont font = WorldData.fonts.get(fontSize);

		if (font == null) {
			Font awtFont = new Font("Courier", Font.PLAIN, fontSize);
			WorldData.fonts.put(fontSize, new TrueTypeFont(awtFont, false));
		}
		if (font != null) {
			TextureImpl.bindNone();
			font.drawString(position.x, position.y, text, color);
		}
		WorldData.texture.bind();
	}

}
