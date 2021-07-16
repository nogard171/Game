package core;

import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

public class Chunk {

	int id = -1;
	Point index;

	HashMap<Point, Tile> tiles = new HashMap<Point, Tile>();
	Dimension size = new Dimension(16, 16);

	public Chunk(int x, int y) {
		index = new Point(x, y);
	}

	public void setup() {
		float[][] heightMap = Generator.generateHeightMap(size.width, size.height);

		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {

				float chunkPosX = ((index.x - index.y) * 32) * size.width;
				float chunkPosY = ((index.y + index.x) * 16) * size.height;

				float posX = chunkPosX + ((x - y) * 32);
				float posY = chunkPosY + ((y + x) * 16);

				Tile tile = new Tile(TextureType.GRASS);
				tile.setPosition(new Vector2f(posX, posY));
				float height = heightMap[x][y];
				float height1 = (x + 1 < size.width ? heightMap[x + 1][y] : height);
				float height2 = (x + 1 < size.width && y + 1 < size.height ? heightMap[x + 1][y + 1] : height);
				float height3 = (y + 1 < size.height ? heightMap[x][y + 1] : height);

				float[] heights = { height, height1, height2, height3 };
				tile.setHeights(heights);

				Color c = new Color(255, 255, 255);
				Color c1 = new Color(255, 255, 255);
				Color c2 = new Color(255, 255, 255);
				Color c3 = new Color(255, 255, 255);

				Color[] colors = { c, c1, c2, c3 };
				tile.setColors(colors);
				tiles.put(new Point(x, y), tile);
			}
		}
	}

	public void build() {
		id = GL11.glGenLists(1);
		GL11.glNewList(id, GL11.GL_COMPILE);

		GL11.glBegin(GL11.GL_QUADS);
		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {
				Tile tile = tiles.get(new Point(x, y));
				if (tile != null) {

					Renderer.renderSprite(TextureType.GRASS, (int)tile.getPosition().x, (int)tile.getPosition().y,
							tile.getHeights(), tile.getColors());
				}
			}
		}
		GL11.glEnd();

		GL11.glEndList();
	}

	public void update() {

	}

	public void render() {
		if (id == -1) {
			build();
			System.out.println("Build");
		} else {
			GL11.glCallList(id);
		}
	}
}
