package utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import classes.Chunk;
import classes.Index;
import classes.TextureData;
import classes.TextureType;
import classes.Object;
import classes.RawMaterial;
import classes.RawModel;
import data.MaterialData;
import data.ModelData;
import data.WorldData;

public class Renderer {
	public static void renderModel(Chunk self, int x, int z) {

		if (self != null) {
			int carX = (self.index.getX() * 32) * 16;
			int carY = (self.index.getY() * 32) * 16;
			int isoX = carX - carY;
			int isoY = (carY + carX) / 2;

			int selfX = isoX;
			int selfY = isoY;
			Object obj = self.objects[x][z];
			if (obj != null) {
				RawModel raw = ModelData.modelData.get(obj.getModel());
				if (raw != null) {

					RawMaterial mat = MaterialData.materialData.get(obj.getMaterial());
					if (mat != null) {
						for (byte i : raw.indices) {
							Vector2f textureVec = mat.vectors[i];
							GL11.glTexCoord2f(textureVec.x / MaterialData.texture.getImageWidth(),
									textureVec.y / MaterialData.texture.getImageHeight());
							Vector2f vec = raw.vectors[i];
							GL11.glVertex2f(vec.x + selfX + obj.getX(), vec.y + selfY + obj.getY());
						}
					}
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
}
