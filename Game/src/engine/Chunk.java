package engine;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import java.awt.Rectangle;

public class Chunk {
	public Rectangle chunkBounds;
	private Point index;
	private int displayList;
	private boolean updateList;
	private Point position;

	public HashMap<String, Object> objects = new HashMap<String, Object>();

	public Chunk(Point newIndex) {
		index = newIndex;
	}

	@Override
	public String toString() {
		return (int) index.x + "," + (int) index.y;
	}

	public void load() {

		int posX = (int) ((index.x * 32) * ChunkManager.size.x);
		int posY = (int) ((index.y * 32) * ChunkManager.size.y);

		position = new Point(posX, posY);

		chunkBounds = new Rectangle(position.x, position.y, (int) ((32) * ChunkManager.size.x),
				(int) ((32) * ChunkManager.size.y));

		for (int x = 0; x < ChunkManager.size.x; x++) {
			for (int y = 0; y < ChunkManager.size.y; y++) {

			}
		}

		build();
		updateList = true;

	}

	ArrayList<Integer> updateLayers = new ArrayList<Integer>();

	public void build() {

		displayList = GL11.glGenLists(1);
		GL11.glNewList(displayList, GL11.GL_COMPILE_AND_EXECUTE);
		GL11.glBegin(GL11.GL_QUADS);
		for (int x = 0; x < ChunkManager.size.x; x++) {
			for (int y = 0; y < ChunkManager.size.y; y++) {

				String sprite = "grass";

				if (y > 0) {
					sprite = "unknown";
				}

				Renderer.renderSprite(sprite, (x * 32) + position.x, (y * 32) + position.y);
			}
		}

		GL11.glEnd();
		GL11.glEndList();

	}

	public void update() {
	}

	public void render() {
		if (updateList) {
			build();
			updateList = false;
		} else {

			GL11.glColor3f(1, 1, 1);
			GL11.glCallList(displayList);

			

			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glColor3f(1, 0, 0);
			GL11.glBegin(GL11.GL_QUADS);

			GL11.glVertex2i(chunkBounds.x, chunkBounds.y);
			GL11.glVertex2i(chunkBounds.x + chunkBounds.width, chunkBounds.y);
			GL11.glVertex2i(chunkBounds.x + chunkBounds.width, chunkBounds.y + chunkBounds.height);
			GL11.glVertex2i(chunkBounds.x, chunkBounds.y + chunkBounds.height);

			GL11.glEnd();
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
			GL11.glEnable(GL11.GL_TEXTURE_2D);

		}

	}

}
