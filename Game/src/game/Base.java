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
import core.Input;
import core.Renderer;
import core.TaskManager;
import core.UserInterface;
import core.View;
import core.Window;
import utils.FPS;
import core.Object;

public class Base {

	TaskManager taskManager;
	UserInterface ui;

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

		chunkManager.setup();

		FPS.setup();
		taskManager = new TaskManager();
		taskManager.start();
		ui = new UserInterface();
		ui.setup();

		viewTest = new View(0, 0, Window.width, Window.height);
	}

	public void update() {
		Window.update();

		mousePosition = new Point(Mouse.getX() + viewTest.x, (Window.height - Mouse.getY()) + viewTest.y);

		if (Window.close()) {
			this.isRunning = false;
		}

		if (Window.wasResized) {
			viewTest.w = Window.width;
			viewTest.h = Window.height;
			Window.wasResized = false;
		}

		FPS.updateFPS();

		float speed = FPS.getDelta();
		int forceX = 0;
		int forceY = 0;
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			forceX = (int) -speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			forceX = (int) speed;

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			forceY = (int) -speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			forceY = (int) speed;
		}
		viewTest.move(forceX, forceY);

		chunkManager.update();

		viewTest.finalizeMove();

		ui.update();
	}

	public static ArrayList<Object> hoveredObjects = new ArrayList<Object>();
	public static View viewTest;
	ChunkManager chunkManager = new ChunkManager();

	public void render() {
		Window.render();
		Input.poll();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Renderer.texture.getTextureID());
		GL11.glPushMatrix();
		GL11.glTranslatef(-viewTest.x, -viewTest.y, 0);

		chunkManager.render();
		GL11.glPopMatrix();
		ui.render();

		Renderer.renderQuad(new Rectangle(0, 0, 200, 64), new Color(0, 0, 0, 0.5f));
		Renderer.renderText(new Vector2f(0, 0), "FPS: " + FPS.getFPS(), 12, Color.white);

		Renderer.renderText(new Vector2f(0, 16), "Hover Count: " + hoveredObjects.size(), 12, Color.white);

		Renderer.renderText(new Vector2f(0, 32), "Render Count: " + ChunkManager.getRenderCount(), 12, Color.white);

		Renderer.renderText(new Vector2f(0, 48), "Chunk Count: " + ChunkManager.chunksInView.size(), 12, Color.white);

		Renderer.renderText(new Vector2f(0, 64), "Layer: " + ChunkManager.layer, 12, Color.white);

		if ((ChunkManager.hover == null ? UserInterface.hover : ChunkManager.hover) != null) {

			Renderer.renderQuad(new Rectangle(200, 0, 300, 16), new Color(0, 0, 0, 0.5f));
			Renderer.renderText(new Vector2f(200, 0),
					"index: " + (ChunkManager.hover == null ? UserInterface.hover : ChunkManager.hover), 12,
					Color.white);
		}


	}

	public void destroy() {
		taskManager.destroy();
		Window.destroy();
	}
}
