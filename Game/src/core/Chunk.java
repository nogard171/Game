package core;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

public class Chunk {

	int id = -1;
	Point index;
	Rectangle bounds;

	HashMap<Point, Tile> tiles = new HashMap<Point, Tile>();
	HashMap<Point, Object> objects = new HashMap<Point, Object>();
	public static Dimension size = new Dimension(16, 16);

	public Chunk(int x, int y) {
		index = new Point(x, y);
	}

	public Tile getTile(Point index) {
		return tiles.get(index);
	}

	public Object getObject(Point index) {
		return objects.get(index);
	}

	public void setup() {
		float[][] heightMap = Generator.generateHeightMap(size.width, size.height);

		for (int x = 0; x < size.width; x++) {
			for (int y = 0; y < size.height; y++) {

				float chunkPosX = (((index.x - index.y) * 32) * size.width);
				float chunkPosY = (((index.y + index.x) * 16) * size.height);

				float posX = chunkPosX + ((x - y) * 32) - 32;
				float posY = chunkPosY + ((y + x) * 16);

				Tile tile = new Tile(TextureType.GRASS);

				Random r = new Random();
				float t = r.nextFloat();
				if (t < 0.5f) {
					tile.setType(TextureType.GRASS0);
				}

				tile.setPosition(new Vector2f(posX, posY));
				tiles.put(new Point(x, y), tile);

				if (x == 5 && y == 5) {
					Object test = new Object(TextureType.TREE);

					test.setPosition(new Vector2f(posX, posY));
					objects.put(new Point(x, y), test);
				}
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

					Renderer.renderSprite(tile.getType(), (int) tile.getPosition().x, (int) tile.getPosition().y);
				}

				Object obj = objects.get(new Point(x, y));
				if (obj != null) {
					System.out.println("index: " + x + "," + y);
					Renderer.renderSprite(obj.getType(), (int) obj.getPosition().x, (int) obj.getPosition().y);
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
