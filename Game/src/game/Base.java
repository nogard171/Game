package game;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Color;

import core.Chunk;
import core.ChunkManager;
import core.Renderer;
import core.TaskManager;
import core.Window;
import utils.FPS;
import core.Object;

public class Base {

	TaskManager taskManager;

	boolean isRunning = true;
	public static Point mousePosition;

	public void start() {
		this.setup();

		while (this.isRunning) {
			this.update();

			this.render();
		}
		this.destroy();

	}

	public void setup() {

		Window.start();
		Window.setup();
		Renderer.load();

		view = new Rectangle(0, 0, Window.width, Window.height);

		chunkManager.setup();

		FPS.setup();
		taskManager = new TaskManager();
		taskManager.start();
	}

	public void update() {
		Window.update();
		// hoveredObjects.clear();

		mousePosition = new Point(Mouse.getX() + view.x, (Window.height - Mouse.getY()) + view.y);

		if (Window.close()) {
			this.isRunning = false;
		}

		if (Window.wasResized) {
			view.width = Window.width;
			view.height = Window.height;
			Window.wasResized = false;
		}

		FPS.updateFPS();

		float speed = FPS.getDelta();

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			view.x -= speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			view.x += speed;

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			view.y -= speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			view.y += speed;

		}
		if (!view.equals(previousView)) {
			viewChanged = true;
			previousView = view;
		}
		chunkManager.update();

		if (view.equals(previousView)) {
			viewChanged = false;
		}
	}

	public static ArrayList<Object> hoveredObjects = new ArrayList<Object>();

	public static Rectangle view = new Rectangle(0, 0, 0, 0);
	public Rectangle previousView = new Rectangle(0, 0, 0, 0);
	public static boolean viewChanged = true;
	ChunkManager chunkManager = new ChunkManager();

	public void render() {
		Window.render();

		GL11.glPushMatrix();
		GL11.glTranslatef(-view.x, -view.y, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Renderer.texture.getTextureID());

		chunkManager.render();
		GL11.glPopMatrix();

		Renderer.renderQuad(new Rectangle(0, 0, 200, 64), new Color(0, 0, 0, 0.5f));
		Renderer.renderText(new Vector2f(0, 0), "FPS: " + FPS.getFPS(), 12, Color.white);

		Renderer.renderText(new Vector2f(0, 16), "Hover Count: " + hoveredObjects.size(), 12, Color.white);

		Renderer.renderText(new Vector2f(0, 32), "Render Count: " + ChunkManager.getRenderCount(), 12, Color.white);

		Renderer.renderText(new Vector2f(0, 48), "Chunk Count: " + ChunkManager.chunksInView.size(), 12, Color.white);

		if (ChunkManager.hover != null) {

			Renderer.renderQuad(new Rectangle(200, 0, 300, 16), new Color(0, 0, 0, 0.5f));
			Renderer.renderText(new Vector2f(200, 0), "index: " + ChunkManager.hover, 12, Color.white);
		}
		/*
		 * int i = 0; for (Object obj : hoveredObjects) { String sprite =
		 * obj.getSprite(); if (!obj.known) { sprite = "unknown"; }
		 * Renderer.renderText(new Vector2f(200, i * 16), "index: " + obj.getIndex() +
		 * "(" + sprite + ")", 12, Color.white); i++; }
		 */

	}

	public void destroy() {
		taskManager.destroy();
		Window.destroy();
	}
}
