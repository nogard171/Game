package game;

import org.lwjgl.opengl.GL11;

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
	}

	public void update() {
		Window.update();
		if (Window.close()) {
			this.isRunning = false;
		}
	}

	public void render() {
		Window.render();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Renderer.texture.getTextureID());
		GL11.glColor3f(1, 1, 1);
		GL11.glBegin(GL11.GL_QUADS);
		Renderer.renderSprite("grass", 0, 0);
		GL11.glEnd();
	}

	public void destroy() {
		Window.destroy();
	}
}
