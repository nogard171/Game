package core;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class Chunk {
	private int id = -1;
	public HashMap<Vector3f, String> objects = new HashMap<Vector3f, String>();
	private Vector3f size = new Vector3f(16, 16, 16);

	public void load() {

		for (int y = 0; y < size.y; y++) {
			for (int x = 0; x < size.x; x++) {
				for (int z = 0; z < size.x; z++) {
					String sprite = "grass";
					objects.put(new Vector3f(x, y, z), sprite);
				}
			}
		}
	}

	public void build() {
		id = GL11.glGenLists(1);
		GL11.glNewList(id, GL11.GL_COMPILE);
		GL11.glBegin(GL11.GL_QUADS);
		for (int y = 0; y < size.y; y++) {
			for (int x = 0; x < size.x; x++) {
				for (int z = 0; z < size.x; z++) {
					String sprite = objects.get(new Vector3f(x, y, z));
					if (sprite != null) {
						int posX = 0;
						int posY = 0;
						int posZ = 0;
						Renderer.renderSprite(sprite, posX, posZ);
					}
				}
			}
		}
		GL11.glEnd();
		GL11.glEndList();
	}
}
