package game;

import java.awt.Point;
import java.awt.Rectangle;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;

import core.Chunk;
import core.Renderer;
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

	public void setup() {
		Window.start();
		Window.setup();
		Renderer.load();

		chunk = new Chunk();
		chunk.load();
		
		FPS.setup();
	}

	public void update() {
		Window.update();

		if (Window.close()) {
			this.isRunning = false;
		}
		
		FPS.updateFPS();

		float speed = FPS.getDelta();

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			view.x += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			view.x -= speed;

		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			view.y += speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			view.y -= speed;

		}
		
		int mouseX = Mouse.getX();
		int mouseY = Mouse.getY();
		
		
	}

	Chunk chunk;
	Point view = new Point(100, 100);

	public void render() {
		Window.render();
		GL11.glPushMatrix();
		GL11.glTranslatef(view.x, view.y, 0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Renderer.texture.getTextureID());
		GL11.glColor3f(1, 1, 1);
		chunk.render();

		GL11.glPopMatrix();
		
		Renderer.renderQuad(new Rectangle(0, 0, 200, 32), new Color(0, 0, 0, 0.5f));
		Renderer.renderText(new Vector2f(0, 0), "FPS: " + FPS.getFPS(), 12, Color.white);
	}

	public void destroy() {
		Window.destroy();
	}
}
