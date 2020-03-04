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

import game.Data;

public class GLDisplay {

	public void createDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(Integer.parseInt(Data.settings.getProperty("window.width")),
					Integer.parseInt(Data.settings.getProperty("window.height"))));
			Display.create();

			Display.setResizable(true);

			this.setupViewPort();

		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public void setupViewPort() {
		Data.settings.setProperty("window.width", Display.getWidth()+"");
		Data.settings.setProperty("window.height", Display.getHeight()+"");

		glViewport(0, 0, Integer.parseInt(Data.settings.getProperty("window.width")), Integer.parseInt(Data.settings.getProperty("window.height")));
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Integer.parseInt(Data.settings.getProperty("window.width")), Integer.parseInt(Data.settings.getProperty("window.height")), 0, -1, 1);
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
		if(Boolean.parseBoolean(Data.settings.getProperty("window.vsync")))
		{
			Display.sync(Integer.parseInt(Data.settings.getProperty("window.fps")));
		}
	}

	public void render() {
		if (Display.wasResized()) {
			this.setupViewPort();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
	}
}
