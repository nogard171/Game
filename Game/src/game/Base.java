package game;

import java.awt.Point;

import org.lwjgl.opengl.GL11;

import core.Chunk;
import core.Renderer;
import core.Window;

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
	}

	public void update() {
		Window.update();

		if (Window.close()) {
			this.isRunning = false;
		}
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
	}

	public void destroy() {
		Window.destroy();
	}
}
