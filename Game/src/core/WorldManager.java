package core;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import game.Base;
import utils.Renderer;

public class WorldManager {
	public static HashMap<Point, Chunk> chunks = new HashMap<Point, Chunk>();
	public LinkedList<Chunk> renderChunks = new LinkedList<Chunk>();

	public static Player player;

	public Point playerChunk = new Point(0, 0);
	public Point maxChunks = new Point(5, 5);

	public static Point view = new Point(0, 0);
	public boolean viewMoved = true;

	public void setup() {
		Chunk chunk = new Chunk();
		chunk.setup();
		chunk.build();
		chunks.put(new Point(0, 0), chunk);

		player = new Player();
	}

	public void update() {

		if (viewMoved) {

			renderChunks.clear();
			for (int x = playerChunk.x - maxChunks.x; x < playerChunk.x + maxChunks.x; x++) {
				for (int y = playerChunk.y - maxChunks.y; y < playerChunk.y + maxChunks.y; y++) {
					Chunk chunk = chunks.get(new Point(x, y));
					if (chunk != null) {
						chunk.update();
						renderChunks.add(chunk);
					}
				}
			}
			viewMoved = false;
		}
		for (Chunk chunk : renderChunks) {
			chunk.update();
		}

		PlayerController.update();

		// view = new Point( (int) -player.position.x +
		// (Integer.parseInt(Base.settings.getProperty("window.width")) / 2) - 32, (int)
		// -player.position.y +
		// (Integer.parseInt(Base.settings.getProperty("window.height")) / 2) - 8);

		
	}
	

	public void render() {
		/*
		 * for (int x = playerChunk.x - maxChunks.x; x < playerChunk.x + maxChunks.x;
		 * x++) { for (int y = playerChunk.y - maxChunks.y; y < playerChunk.y +
		 * maxChunks.y; y++) { Chunk chunk = chunks.get(new Point(x,y)); if(chunk!=null)
		 * { chunk.render();
		 * 
		 * //render player
		 * 
		 * chunk.renderFringe(); }
		 * 
		 * } }
		 */
		GL11.glPushMatrix();
		GL11.glTranslatef(view.x, view.y, 0);
		for (Chunk chunk : renderChunks) {
			chunk.render();

			// render player
			PlayerController.render();
			chunk.renderFringe();
		}
		GL11.glPopMatrix();
		
		
		
	
	}

	public void destroy() {
		chunks.clear();
	}
}
