package utils;

import java.awt.Point;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import classes.GLObject;
import classes.GLSpriteData;

public class GLRenderer {

	public static void renderObject(Point position, GLObject obj, GLSpriteData spriteData, Color c) {
		int x = (int) obj.getPositionGLIndex().x;
		int y = (int) obj.getPositionGLIndex().y;
		int z = (int) obj.getPositionGLIndex().z;
		if (spriteData != null) {
			GL11.glColor3f((float) c.getRed() / 255, (float) c.getGreen() / 255, (float) c.getBlue() / 255);

			int posX = (int) (position.x + ((x - z) * 32) + spriteData.offset.x);
			int posY = (int) (position.y + ((y - 1) * 32));
			int posZ = (int) (((z + x) * 16) + posY + spriteData.offset.y);

			GL11.glTexCoord2f(spriteData.textureData.x, spriteData.textureData.y);
			GL11.glVertex2f(posX, posZ);
			GL11.glTexCoord2f(spriteData.textureData.x + spriteData.textureData.z, spriteData.textureData.y);
			GL11.glVertex2f(posX + spriteData.size.getWidth(), posZ);
			GL11.glTexCoord2f(spriteData.textureData.x + spriteData.textureData.z,
					spriteData.textureData.y + spriteData.textureData.w);
			GL11.glVertex2f(posX + spriteData.size.getWidth(), posZ + spriteData.size.getHeight());
			GL11.glTexCoord2f(spriteData.textureData.x, spriteData.textureData.y + spriteData.textureData.w);
			GL11.glVertex2f(posX, posZ + spriteData.size.getHeight());
		}
	}
}
