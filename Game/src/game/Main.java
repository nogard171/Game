package game;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.SwingUtilities;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import classes.GLObject;
import classes.GLSize;
import classes.GLSpriteData;
import classes.GLType;
import classes.GLView;
import classes.GLActionHandler;
import classes.GLIndex;
import classes.GLMenuItem;
import core.GLChunk;
import core.GLChunkManager;
import core.GLDisplay;
import core.GLQueueManager;
import core.GLUIManager;
import utils.GLDebug;
import utils.GLFPS;
import utils.GLGenerator;
import utils.GLLoader;
import utils.GLTicker;

public class Main extends GLDisplay {

	GLChunkManager manager;
	GLUIManager uiManager;
	GLQueueManager queueManager;
	public static GLView view;

	public void run() {
		try {
			GLLoader.loadSettings("config.properties");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		this.createDisplay();
		GLFPS.setup();

		GLLoader.loadResources();
		
		manager = new GLChunkManager();
		manager.setup();

		uiManager = new GLUIManager();
		uiManager.setup();

		queueManager = new GLQueueManager();
		queueManager.setup();

		view = new GLView();

		try {
			Data.texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream(Data.settings.getProperty("assets.texture")));
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Data.texture.getTextureID());

		GLLoader.loadSprites();

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

		while (!Display.isCloseRequested()) {
			GLFPS.updateFPS();
			this.update();

			GLTicker.update();

			this.render();

			this.sync();
		}
		this.destroy();
	}

	private void destroy() {

		this.destroyDisplay();
	}

	public void update() {
		view.setSize(new GLSize(Display.getWidth(),Display.getHeight()));
		float speed = 0.5f * GLFPS.getDelta();
		manager.update();
		uiManager.update();

		queueManager.update();

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			view.move(new Vector2f(speed, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			view.move(new Vector2f(-speed, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			view.move(new Vector2f(0, speed));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			view.move(new Vector2f(0, -speed));
		}

	}

	@Override
	public void render() {
		super.render();
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		Data.texture.bind();
		GL11.glPushMatrix();
		GL11.glTranslatef((int)Math.ceil( -view.getPosition().x), (int) Math.ceil( -view.getPosition().y), 0);
		manager.render();
		GL11.glPopMatrix();

		uiManager.render();

	}

	public static void main(String[] args) {
		final Main app = new Main();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				app.run();
			}
		});
	}
}