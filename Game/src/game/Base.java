package game;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import core.ANode;
import core.APathFinder;
import core.Chunk;
import core.ChunkManager;
import core.Input;
import core.Renderer;
import core.ResourceDatabase;
import core.TextureType;
import core.View;
import core.Window;
import utils.FPS;

public class Base {

	boolean isRunning = true;

	public void start() {
		this.setup();

		while (this.isRunning) {
			this.update();
			this.render();
		}
		this.destroy();

	}

	private void pollHover() {
		int cartX = (Input.getMousePoint().x) + view.x;
		int cartY = (Input.getMousePoint().y) + view.y;
		int isoX = (cartX / 2 + (cartY));
		int isoY = (cartY - cartX / 2);

		int indexX = (int) Math.floor((float) isoX / (float) 32);
		int indexY = (int) Math.floor((float) isoY / (float) 32);

		test = new Point(indexX, indexY);
	}

	ChunkManager chunkMgr;

	public void setup() {

		Window.start();
		Window.setup();

		Input.setup();

		view = new View(0, 0, Window.width, Window.height);

		FPS.setup();

		ResourceDatabase.load();

		TextureType type = TextureType.GRASS;
		System.out.println("Type: " + type.toString());
		chunkMgr = new ChunkManager();
		chunkMgr.setup();

	}

	public void update() {
		Window.update();
		pollHover();
		if (Window.close()) {
			this.isRunning = false;
		}

		if (Window.wasResized) {
			view.w = Window.width;
			view.h = Window.height;
			Window.wasResized = false;
		}

		playerIndex = ChunkManager.getIndexByType(TextureType.CHARACTER);
		FPS.updateFPS();
		int forceX = 0;
		int forceY = 0;

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			forceY = -1;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			forceY = 1;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			forceX = -1;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			forceX = 1;
		}
		if (Input.isMousePressed(0)) {
			ChunkManager.removeType(TextureType.PATH_DURING);
			ChunkManager.removeType(TextureType.PATH_FINISH);
			LinkedList<ANode> path = APathFinder.find(new ANode(playerIndex), new ANode(test));

			System.out.println("Path: " + path);
			if (path != null) {
				System.out.println("Path Size: " + path.size());

				if (path.size() > 0) {
					for (int i = 0; i < path.size() - 1; i++) {
						ANode node = path.get(i);
						if (node != null) {
							ChunkManager.setObjectAtIndex(node.toPoint(), TextureType.PATH_DURING);
						}
					}
					ANode node = path.get(path.size() - 1);
					ChunkManager.setObjectAtIndex(node.toPoint(), TextureType.PATH_FINISH);
				}
				// ChunkManager.move(playerIndex, test);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_F1)) {
			ResourceDatabase.load();
		}
		// view.x = x;
		// view.y = y;
		view.move(forceX, forceY);
		// System.out.println("NEW Index: " + playerIndex);
	}

	Point playerIndex;
	Point test;
	public static View view;

	public void render() {
		Window.render();
		Input.poll();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, ResourceDatabase.texture.getTextureID());
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);

		GL11.glPushMatrix();
		GL11.glTranslatef(-view.x, -view.y, 0);
		chunkMgr.render();
		Renderer.renderGrid(test.x, test.y);
		GL11.glPopMatrix();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

		Renderer.renderQuad(new Rectangle(0, 0, 200, 64), new Color(0, 0, 0, 0.5f));
		Renderer.renderText(new Vector2f(0, 0), "FPS: " + FPS.getFPS(), 12, Color.white);
		Renderer.renderText(new Vector2f(0, 16), "Player Index: " + playerIndex, 12, Color.white);

		float posX = (((test.x - test.y) * 32) - 32);
		float posY = ((test.y + test.x) * 16);
		Renderer.renderText(new Vector2f(0, 32), "Hover: " + test, 12, Color.white);

		TextureType type = ChunkManager.getTypeByIndexWithTiles(test);

		if (type != null) {
			Renderer.renderText(new Vector2f(0, 48), "Hover Type: " + type.toString(), 12, Color.white);
		}

	}

	public void destroy() {
		Window.destroy();
	}
}
