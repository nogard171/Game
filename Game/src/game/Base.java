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

import engine.Chunk;
import engine.ChunkManager;
import engine.Entity;
import engine.FPS;
import engine.Input;
import engine.Renderer;
import engine.View;
import engine.Window;

public class Base {

	boolean isRunning = true;
	public static Point mousePosition;

	ChunkManager chunkManager;

	public void start() {
		this.setup();

		while (this.isRunning) {
			this.update();
			this.render();
		}
		this.destroy();

	}

	Entity player;

	public void setup() {

		Window.start();
		Window.setup();
		ResourceDatabase.load();
		Input.setup();

		FPS.setup();

		view = new View(0, -200, Window.width, Window.height);

		chunkManager = new ChunkManager();
		chunkManager.setup();

		player = new Entity(64, -64);
	}

	public void update() {
		Window.update();

		mousePosition = new Point(Mouse.getX() + view.x, (Window.height - Mouse.getY()) + view.y);

		if (Window.close()) {
			this.isRunning = false;
		}

		if (Window.wasResized) {
			view.w = Window.width;
			view.h = Window.height;
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
		view.move(forceX, forceY);

		view.finalizeMove();
		chunkManager.update();
	}

	public static View view;

	public void render() {
		Window.render();
		Input.poll();

		Renderer.bindTexture();

		GL11.glPushMatrix();
		GL11.glTranslatef(-view.x, -view.y, 0);

		chunkManager.render();

		Point index = View.getIndexInWorld();

		GL11.glBegin(GL11.GL_QUADS);
		Renderer.renderSprite("character", player.position.x,player.position.y);

		GL11.glEnd();
		Renderer.renderQuad(new Rectangle(index.x * 32, index.y * 32, 32, 32), new Color(0, 0, 0, 0.5f));

		GL11.glPopMatrix();

		Renderer.renderQuad(new Rectangle(0, 0, 200, 64), new Color(0, 0, 0, 0.5f));
		Renderer.renderText(0, 0, "FPS: " + FPS.getFPS(), 12, Color.white);
		Renderer.renderText(0, 16, "Chunks: " + ChunkManager.chunksInView.size(), 12, Color.white);

	}

	public void destroy() {
		chunkManager.destroy();
		ResourceDatabase.clean();
		Window.destroy();
	}
}
