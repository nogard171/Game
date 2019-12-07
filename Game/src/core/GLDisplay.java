package core;

import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GLDisplay {
	public int WIDTH = 800;
	public int HEIGHT = 600;

	public void createDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create();

			Display.setResizable(true);

			this.setupViewPort();

		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void setupViewPort() {
		this.WIDTH = Display.getWidth();
		this.HEIGHT = Display.getHeight();

		glViewport(0, 0, WIDTH, HEIGHT);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, HEIGHT, 0, -1, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
	}

	public void destroyDisplay() {
		Display.destroy();
	}

	public void sync() {
		Display.update();
		//Display.sync(120);
	}

	public void render() {
		if (Display.wasResized()) {
			this.setupViewPort();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
}
